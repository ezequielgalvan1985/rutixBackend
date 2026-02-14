package elementary.Rutix.Viajes.comandos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarReservaDto {
    private Long viajeId;
    private Integer asientos;

}
