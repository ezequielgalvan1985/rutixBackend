package elementary.Rutix.Viajes.dto;

import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.EstadoViajeEnum;
import elementary.Rutix.common.validators.interfaces.ValidFechaSalidaViaje;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViajeResumidoDto {

    private Long id;

    @ValidFechaSalidaViaje
    private LocalDate fechaSalida;

    @NotNull
    private LocalTime horaSalida;

    @NotNull
    private LocalTime horaLlegada;

    @Size(max=100, message = "Lugar de salida supera la cantidad maxima de caracteres permitida 100")
    private String ciudadPartida;

    @Size(max=100, message = "Destino supera la cantidad maxima de caracteres permitida 100")
    private String ciudadDestino;

    @NotNull
    private PerfilRutixResumidoDto conductor;

    private VehiculoDto vehiculo;

    @Max(value=30,message="Cantidad maxima permitida 30")
    private Integer lugaresTotales;


    private boolean pagaSenia;

    @Max(value=30,message="Cantidad maxima permitida 100")
    @Min(value=1, message = "Cantidad minia permitida 1")
    private Integer porcentajeSenia;

    private BigDecimal valor;

    private EstadoViajeEnum estado;

}
