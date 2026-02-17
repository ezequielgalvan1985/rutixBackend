package elementary.Rutix.Viajes.dto;

import elementary.Rutix.common.Enum.EstadoPagoEnum;
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

    private LocalDateTime fechaAlta;

    @NotNull(message = "Importe es Obligatorio")
    private BigDecimal importe;

    @NotNull(message = "Estado es obligatorio")
    private EstadoPagoEnum estado;

}
