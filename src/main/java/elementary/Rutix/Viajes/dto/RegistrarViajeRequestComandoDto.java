package elementary.Rutix.Viajes.dto;

import com.google.errorprone.annotations.NoAllocation;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.common.EstadoViajeEnum;
import elementary.Rutix.common.validators.interfaces.ValidFechaSalidaViaje;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarViajeRequestComandoDto {

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

    private Long vehiculoId;

    @Max(value=30,message="Cantidad maxima permitida 30")
    private Integer asientos;

    private Boolean pagaSenia;

    @Max(value=30,message="Cantidad maxima permitida 100")
    @Min(value=1, message = "Cantidad minia permitida 1")
    private Integer porcentajeSenia;

    private BigDecimal valor;

    private EstadoViajeEnum estado;

}
