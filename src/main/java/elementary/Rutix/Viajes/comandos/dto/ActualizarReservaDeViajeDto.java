package elementary.Rutix.Viajes.comandos.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarReservaDeViajeDto {
    private Long id;
    private Long viajeId;
    @Max(value=30, message="cantidad de lugares maximo permitido 30")
    private Integer asientos;

    private Integer puntaje;

}
