package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class PerfilRutixActualizarDto {
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @Min(value = 1, message = "La valoración mínima es 1")
    @Max(value = 10, message = "La valoración máxima es 10")
    private Integer valoracion;

    @Size(max = 50, message ="la direccion no puede superar los 50 caracteres")
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Size(max = 20, message ="la ciudad no puede superar los 20 caracteres")
    private String telefono;

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    @Size(max = 50, message ="la ciudad no puede superar los 50 caracteres")
    private String email;

    @Size(max = 50, message ="la ciudad no puede superar los 50 caracteres")
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;


}
