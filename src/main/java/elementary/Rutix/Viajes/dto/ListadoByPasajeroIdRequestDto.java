package elementary.Rutix.Viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListadoByPasajeroIdRequestDto {
    private Long pasajeroId;
    private Long offset;
    private Integer limit;

}
