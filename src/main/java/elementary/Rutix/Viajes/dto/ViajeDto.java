package elementary.Rutix.Viajes.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.validators.interfaces.ValidFechaSalidaViaje;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
import jakarta.validation.constraints.*;
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
public class ViajeDto {
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
    private PerfilRutixDto conductor;

    private VehiculoDto vehiculo;

    private List<ReservaDto> listaReservas = new ArrayList<ReservaDto>();

    @Max(value=30,message="Cantidad maxima permitida 30")
    private Integer lugaresTotales;


    private boolean pagaSenia;

    @Max(value=30,message="Cantidad maxima permitida 100")
    @Min(value=1, message = "Cantidad minia permitida 1")
    private Integer porcentajeSenia;

    private BigDecimal valor;

    private EstadoViajeEnum estado;

    private Integer asientos;

    private Integer asientosDisponibles;

    private Integer asientosReservados;


}
