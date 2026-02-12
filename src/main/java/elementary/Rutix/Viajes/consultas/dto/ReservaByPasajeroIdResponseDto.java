package elementary.Rutix.Viajes.consultas.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.common.EstadoReserva;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservaByPasajeroIdResponseDto {
    private Long reservaId;
    private EstadoReserva estado;

    private PerfilRutixResumidoDto conductor;
    private VehiculoDto vehiculo;

    private ViajeResumidoDto viaje;

}
