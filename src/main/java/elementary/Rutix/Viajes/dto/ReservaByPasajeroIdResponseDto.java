package elementary.Rutix.Viajes.dto;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.EstadoReserva;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter

public class ReservaByPasajeroIdResponseDto {
    private Long reservaId;
    private EstadoReserva estado;

    private PerfilRutixResumidoDto conductor;
    private VehiculoDto vehiculo;

    private ViajeResumidoDto viaje;

}
