package elementary.Rutix.PerfilRutix.dto;

import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
@Data
public class PerfilRutixDto {
    private Long id;

    private String nombre;

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

    @Valid
    private List<TarjetaDto> listaTarjetas; //debito o credito

    @Valid
    private List<CuentaBancariaDto> listaCuentas = new ArrayList<CuentaBancariaDto>();; //debito o credito

    private List<VehiculoDto> listaVehiculos = new ArrayList<VehiculoDto>();; //debito o credito

}
