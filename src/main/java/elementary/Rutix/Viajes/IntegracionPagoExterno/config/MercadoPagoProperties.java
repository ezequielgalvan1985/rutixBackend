package elementary.Rutix.Viajes.IntegracionPagoExterno.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mercadopago")
public class MercadoPagoProperties {

    private String accessToken;
    private String notificationUrl;
    private BackUrls backUrls = new BackUrls();

    @Getter
    @Setter
    public static class BackUrls {
        private String success;
        private String pending;
        private String failure;
    }
}
