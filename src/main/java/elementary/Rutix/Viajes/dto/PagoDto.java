package elementary.Rutix.Viajes.dto;

import elementary.Rutix.common.Enum.EstadoPagoEnum;
import elementary.Rutix.common.Enum.MetodoPagoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagoDto {

    private long id;

    @NotNull(message = "ReservaID es obligatorio")
    private Long reservaId;

    @NotNull(message = "Importe es Obligatorio")
    private BigDecimal importe;

    @NotNull(message="Forma de pago Obligatorio")
    @Enumerated(EnumType.STRING)
    private MetodoPagoEnum forma;

}
