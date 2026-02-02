package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehiculoDto {

    private Long id;

    private Long perfilId;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede superar los 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 100, message = "El modelo no puede superar los 100 caracteres")
    private String modelo;

    @NotBlank(message = "La patente es obligatoria")
    @Size(max = 10, message = "La patente no puede superar los 10 caracteres")
    private String patente;

    @Pattern(
            regexp = "\\d{4}",
            message = "El año debe tener 4 dígitos"
    )
    private String anio;
    @NotNull
    private Boolean tieneAire;
    @NotNull
    private Boolean activo;

}
