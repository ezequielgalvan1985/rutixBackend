package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerfilRutixResumidoDto {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private Integer valoracion;
    private String direccion;
    private String telefono;
    private String email;
    private String ciudad;
}
