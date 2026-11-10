package elementary.Rutix.Viajes.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Ubicaciones.dto.CiudadAutocompleteDto;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
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

    private CiudadAutocompleteDto ciudadPartida;

    private CiudadAutocompleteDto ciudadDestino;

    @NotNull
    private PerfilRutixResumidoDto conductor;

    private VehiculoDto vehiculo;

    @Max(value=30,message="Cantidad maxima permitida 30")
    private Integer asientos;

    private Integer asientosDisponibles;

    private Integer asientosReservados;

    private boolean pagaSenia;

    @Max(value=30,message="Cantidad maxima permitida 100")
    @Min(value=1, message = "Cantidad minia permitida 1")
    private Integer porcentajeSenia;

    private BigDecimal valor;

    private EstadoViajeEnum estado;

    private List<String> accionesDisponibles = new ArrayList<>();

}
