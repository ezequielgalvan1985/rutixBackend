package elementary.Rutix.Viajes.consultas.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViajeDetalleDto {
    private Long id;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private String ciudadPartida;
    private String ciudadDestino;
    private PerfilRutixResumidoDto conductor;
    private VehiculoDto vehiculo;
    private List<ReservaViewDto> listaReservas = new ArrayList<ReservaViewDto>();
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private EstadoViajeEnum estado;

    private Integer asientos;
    private Integer asientosDisponibles;
    private Integer asientosReservados;
    private Boolean pagaSenia;
    private Integer porcentajeSenia;
    private LocalDateTime fechaAlta;
    private List<String> accionesDisponibles = new ArrayList<>();

}
