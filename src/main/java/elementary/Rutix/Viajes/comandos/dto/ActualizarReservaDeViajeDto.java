package elementary.Rutix.Viajes.comandos.dto;

import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.common.EstadoReserva;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarReservaDeViajeDto {
    private Long id;
    private Long viajeId;

    @Max(value=30, message="cantidad de lugares maximo permitido 30")
    private Integer cantidadLugares;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    private Integer puntaje;

}
