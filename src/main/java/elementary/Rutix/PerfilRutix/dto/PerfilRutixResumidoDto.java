package elementary.Rutix.PerfilRutix.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class PerfilRutixResumidoDto {
    private Long id;
    private Long usuarioId;
    private Integer valoracion;
    private String direccion;
    private String telefono;
    private String email;
    private String ciudad;
}
