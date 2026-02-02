package elementary.Rutix.PerfilRutix.dto;

import elementary.Rutix.common.MarcaTarjeta;
import elementary.Rutix.common.TipoTarjeta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TarjetaDto {
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarjeta tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarcaTarjeta marca;

    @Size(min= 4, max= 4, message = "Debe ingresar 4 Digitos")
    @Pattern(regexp = "\\d+", message = "Solo admite números")
    private String ultimos4Digitos;

    @NotBlank(message = "Titular es Obligatorio")
    @Size(max = 100, message = "El Titular no puede superar los 100 caracteres")
    private String titular;

    @Min(value=1, message = "Mes debe estar entre 1 y 12")
    @Max(value=12, message = "Mes debe estar entre 1 y 12")
    @NotNull
    private Integer mesVencimiento;

    @Min(value=2016, message = "Año debe ser mayor a 2016")
    @Max(value=2100, message = "Año debe ser menor a 2100")
    @NotNull
    private Integer anioVencimiento;

    @NotNull
    @NotBlank
    private String token;

    @NotNull
    private Boolean activa = true;

    private LocalDateTime fechaAlta;

    private Long perfilId;

}
