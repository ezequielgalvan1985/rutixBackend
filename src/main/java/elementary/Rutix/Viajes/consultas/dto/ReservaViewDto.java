package elementary.Rutix.Viajes.consultas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.common.Enum.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaViewDto {
    private Long id;
    private EstadoReserva estado;
    private PerfilRutixResumidoDto pasajero;
    private Integer asientos;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaAlta;

}
