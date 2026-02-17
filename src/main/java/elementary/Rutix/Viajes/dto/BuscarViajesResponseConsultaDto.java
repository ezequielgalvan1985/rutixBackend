package elementary.Rutix.Viajes.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuscarViajesResponseConsultaDto {
    private Long id;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private String ciudadPartida;
    private String ciudadDestino;
    private Integer lugaresTotales;
    private boolean pagaSenia;
    private Integer porcentajeSenia;
    private BigDecimal valor;
    private EstadoViajeEnum estado;
    private PerfilRutixResumidoDto conductor;

}
