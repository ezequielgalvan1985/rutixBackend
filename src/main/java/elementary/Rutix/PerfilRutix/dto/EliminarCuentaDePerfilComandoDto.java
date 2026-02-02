package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EliminarCuentaDePerfilComandoDto {
    @NotNull
    private Long id;
    
    @NotNull
    private Long perfilId;
}

