package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;

@Data
public class CuentaBancariaDto {

    private Long id;

    @NotNull(message = "Perfil ID es obligatorio")
    private Long perfilId;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 50, message = "El nombre del titular no puede superar los 50 caracteres")
    private String nombreTitular;

    @NotBlank(message = "El CBU es obligatorio")
    @Size(min = 22, max = 22, message = "El CBU debe tener exactamente 22 dígitos")
    @Pattern(regexp = "\\d+", message = "El CBU debe contener solo números")
    @Unique
    private String cbu;

    @Size(max = 100, message = "El alias no puede superar los 100 caracteres")
    private String alias;

    @Size(max = 50, message = "El número de cuenta no puede superar los 50 caracteres")
    private String numero;

    @NotBlank(message = "El nombre de la entidad es obligatorio")
    @Size(max = 50, message = "El nombre de la entidad no puede superar los 50 caracteres")
    private String nombreEntidad;

    @NotNull(message = "El código de entidad es obligatorio")
    private Integer codigoEntidad;

    @NotNull
    private Boolean activo;

}
