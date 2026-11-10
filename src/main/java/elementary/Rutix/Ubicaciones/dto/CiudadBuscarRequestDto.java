package elementary.Rutix.Ubicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CiudadBuscarRequestDto {
    private String texto;
    private Integer limit;
}
