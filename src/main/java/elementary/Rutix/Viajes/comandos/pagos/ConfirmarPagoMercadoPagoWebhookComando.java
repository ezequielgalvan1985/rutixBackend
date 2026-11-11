package elementary.Rutix.Viajes.comandos.pagos;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.Enum.EstadoPagoEnum;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.Enum.MetodoPagoEnum;
import elementary.Rutix.common.interfaces.Comando;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Lo dispara el webhook de Mercado Pago (ver ReservaController.webhookMercadoPago).
 * Nunca confia en lo que manda la notificacion en si misma (es solo un
 * "aviso" de que algo paso con un pago, cualquiera puede mandarnos un POST
 * fingiendo ser Mercado Pago): vuelve a consultar el pago real contra la
 * API de Mercado Pago con el access token propio, y recien con esa
 * respuesta decide si confirmar la reserva.
 *
 * No tira excepciones ante datos invalidos o pagos no aprobados todavia:
 * Mercado Pago solo necesita un 200 de vuelta, asi que cualquier problema
 * se loguea y se sigue.
 */
@Service
public class ConfirmarPagoMercadoPagoWebhookComando implements Comando<Long, Void> {

    private static final Logger log = LoggerFactory.getLogger(ConfirmarPagoMercadoPagoWebhookComando.class);

    private final ReservaRepository repo;

    public ConfirmarPagoMercadoPagoWebhookComando(ReservaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Void execute(Long paymentId) {
        Payment payment;
        try {
            payment = new PaymentClient().get(paymentId);
        } catch (MPApiException e) {
            log.warn("Mercado Pago rechazo la consulta del pago {}: {}", paymentId, e.getApiResponse().getContent());
            return null;
        } catch (MPException e) {
            log.warn("No se pudo consultar el pago {} en Mercado Pago: {}", paymentId, e.getMessage());
            return null;
        }

        if (payment.getExternalReference() == null) {
            log.warn("Pago {} de Mercado Pago sin external_reference, no se puede asociar a una reserva", paymentId);
            return null;
        }

        Long reservaId;
        try {
            reservaId = Long.parseLong(payment.getExternalReference());
        } catch (NumberFormatException e) {
            log.warn("external_reference '{}' del pago {} no es un id de reserva valido", payment.getExternalReference(), paymentId);
            return null;
        }

        Reserva reserva = repo.findById(reservaId).orElse(null);
        if (reserva == null) {
            log.warn("Pago {} de Mercado Pago referencia una reserva inexistente: {}", paymentId, reservaId);
            return null;
        }

        if (reserva.getEstado() == EstadoReserva.PAGADA) {
            log.info("Pago {} de la reserva {}: ya estaba PAGADA, webhook duplicado, no se hace nada", paymentId, reservaId);
            return null;
        }

        if (!"approved".equals(payment.getStatus())) {
            log.info("Pago {} de la reserva {} todavia no esta aprobado (status={})", paymentId, reservaId, payment.getStatus());
            return null;
        }

        // el ultimo Pago PENDIENTE en MERCADOPAGO de esta reserva es el que
        // generó este checkout (RegistrarPagoDeReservaComando crea uno nuevo
        // por cada intento de pago).
        Pago pago = reserva.getListaPagos().stream()
                .filter(p -> p.getForma() == MetodoPagoEnum.MERCADOPAGO && p.getEstado() == EstadoPagoEnum.PENDIENTE)
                .reduce((primero, ultimo) -> ultimo)
                .orElse(null);

        if (pago != null) {
            pago.setEstado(EstadoPagoEnum.CONFIRMADO);
            pago.setExternalId(String.valueOf(paymentId));
        } else {
            log.warn("Reserva {} confirmada por Mercado Pago pero no se encontro el Pago PENDIENTE a actualizar", reservaId);
        }

        reserva.confirmarPago();
        repo.save(reserva);

        log.info("Reserva {} PAGADA via Mercado Pago (payment {})", reservaId, paymentId);
        return null;
    }
}
