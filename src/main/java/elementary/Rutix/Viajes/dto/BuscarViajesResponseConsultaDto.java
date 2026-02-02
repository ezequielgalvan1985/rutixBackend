package elementary.Rutix.Viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuscarViajesResponseConsultaDto {
    private LocalDate fechaSalida;
    private String ciudadPartida;
    private String ciudadDestino;
    private Long offset;
    private Integer limit;
}
