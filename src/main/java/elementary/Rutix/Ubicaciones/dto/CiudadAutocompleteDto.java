package elementary.Rutix.Ubicaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiudadAutocompleteDto {
    private Long id;
    private String ciudad;
    private String provincia;
    private String pais;
}
