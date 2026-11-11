package elementary.Rutix.Viajes.IntegracionPagoExterno.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

// Nombrado distinto a com.mercadopago.MercadoPagoConfig (la clase del SDK)
// para no pisarse con ella.
@Component
public class MercadoPagoSdkInitializer {

    private final MercadoPagoProperties properties;

    public MercadoPagoSdkInitializer(MercadoPagoProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // El SDK de Mercado Pago guarda el access token en un campo
        // estatico global (asi esta disenado el sdk-java oficial), por eso
        // esto se configura una sola vez al arrancar la app.
        com.mercadopago.MercadoPagoConfig.setAccessToken(properties.getAccessToken());
    }
}
