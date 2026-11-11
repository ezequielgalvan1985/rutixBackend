package elementary.Rutix.Viajes.IntegracionPagoExterno;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import elementary.Rutix.Viajes.IntegracionPagoExterno.config.MercadoPagoProperties;
import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.common.Enum.MetodoPagoEnum;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MercadoLibreCobroStrategy implements GeneradorCobroStrategy{

    private static final Logger log = LoggerFactory.getLogger(MercadoLibreCobroStrategy.class);

    private final MercadoPagoProperties properties;

    public MercadoLibreCobroStrategy(MercadoPagoProperties properties) {
        this.properties = properties;
    }

    @Override
    public MetodoPagoEnum getMetodoPago() {
        return MetodoPagoEnum.MERCADOPAGO;
    }

    @Override
    public PagoDto generar(PagoDto pagoDto) {
        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Reserva Rutix #" + pagoDto.getReservaId())
                    .quantity(1)
                    .currencyId("ARS")
                    .unitPrice(pagoDto.getImporte())
                    .build();

            PreferenceRequest.PreferenceRequestBuilder requestBuilder = PreferenceRequest.builder()
                    .items(java.util.List.of(item))
                    // permite relacionar el pago/webhook con esta reserva sin
                    // depender de haber guardado antes el Pago en la base.
                    .externalReference(String.valueOf(pagoDto.getReservaId()));

            // back_urls / notification_url son opcionales para crear la
            // preferencia: todavia no estan configuradas (falta el tunel de
            // ngrok), asi que se agregan solo si ya estan seteadas. Sin
            // back_urls el pasajero paga igual, solo que Mercado Pago no lo
            // redirige de vuelta a la app automaticamente al terminar.
            MercadoPagoProperties.BackUrls backUrls = properties.getBackUrls();
            boolean backUrlsCompletas = backUrls != null
                    && isPresente(backUrls.getSuccess())
                    && isPresente(backUrls.getPending())
                    && isPresente(backUrls.getFailure());

            if (backUrlsCompletas) {
                requestBuilder.backUrls(PreferenceBackUrlsRequest.builder()
                        .success(backUrls.getSuccess())
                        .pending(backUrls.getPending())
                        .failure(backUrls.getFailure())
                        .build());
                requestBuilder.autoReturn("approved");
            }

            if (isPresente(properties.getNotificationUrl())) {
                requestBuilder.notificationUrl(properties.getNotificationUrl());
            }

            log.info("Creando preferencia de Mercado Pago para reserva {} (backUrlsCompletas={}, notificationUrl={})",
                    pagoDto.getReservaId(), backUrlsCompletas, properties.getNotificationUrl());

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(requestBuilder.build());

            log.info("Preferencia creada: id={}, initPoint={}, sandboxInitPoint={}",
                    preference.getId(), preference.getInitPoint(), preference.getSandboxInitPoint());

            // Con un access token de prueba (TEST-...) hay que abrir el
            // checkout de SANDBOX, no el de produccion: con credenciales de
            // test, init_point (produccion) restringe/filtra medios de pago
            // (tarjetas de debito, Rapipago, etc.), mientras que
            // sandbox_init_point habilita el flujo completo de prueba con
            // las tarjetas ficticias de Mercado Pago.
            String checkoutUrl = esTokenDePrueba()
                    ? preference.getSandboxInitPoint()
                    : preference.getInitPoint();
            pagoDto.setCheckoutUrl(checkoutUrl);
            return pagoDto;

        } catch (MPApiException e) {
            log.error("Mercado Pago rechazo la solicitud ({}): {}", e.getStatusCode(), e.getApiResponse() != null ? e.getApiResponse().getContent() : null, e);
            throw new ReglaNegocioException("Mercado Pago rechazo la solicitud: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            log.error("No se pudo generar el pago con Mercado Pago", e);
            throw new ReglaNegocioException("No se pudo generar el pago con Mercado Pago: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error inesperado generando el cobro de Mercado Pago", e);
            throw e;
        }
    }

    private boolean isPresente(String valor) {
        return valor != null && !valor.isBlank();
    }

    private boolean esTokenDePrueba() {
        String token = properties.getAccessToken();
        return token != null && token.startsWith("TEST-");
    }
}
