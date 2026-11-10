package elementary.Rutix.Viajes.comandos.pagos;

import elementary.Rutix.Viajes.comandos.dto.DevolucionPagoDeReservaComandoDto;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

/**
 * Devuelve el dinero de un pago ya registrado (PENDIENTE o CONFIRMADO) sobre
 * una reserva, marcandolo como DEVUELTO. Hoy es una transicion de estado pura:
 * no hay integracion real con la pasarela de pago (GeneradorCobroStrategy.generar
 * tampoco la tiene, ver backlog tecnico), asi que la devolucion efectiva del
 * dinero en el medio de pago queda pendiente como trabajo futuro.
 *
 * Se usa tanto desde CancelarViajeComando (cascada automatica cuando el viaje
 * se cancela con reservas ya pagadas) como de forma independiente via el
 * endpoint PUT /v1/reservas/devolver-pago, restringido a ADMIN.
 */
@Service
public class DevolucionPagoDeReservaComando implements Comando<DevolucionPagoDeReservaComandoDto, Void> {

    private final ReservaRepository repoReserva;

    public DevolucionPagoDeReservaComando(ReservaRepository repoReserva) {
        this.repoReserva = repoReserva;
    }

    @Override
    public Void execute(DevolucionPagoDeReservaComandoDto value) {
        Reserva r = this.repoReserva.findById(value.getReservaId())
                .orElseThrow(() -> new ReglaNegocioException("No existe reserva"));

        Pago p = r.getListaPagos().stream()
                .filter(x -> x.getId() == value.getId())
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException("No existe el pago"));

        p.devolver();
        this.repoReserva.save(r);

        return null;
    }
}
