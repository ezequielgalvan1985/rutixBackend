package elementary.Rutix.Viajes.comandos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EliminarPagoDeReservaComandoDto {
    @NotNull(message = "Pago ID es obligatorio")
    private Long id;

    @NotNull(message = "Reserva ID es obligatorio")
    private Long reservaId;
}
