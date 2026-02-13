package elementary.Rutix.Viajes.dto;

import elementary.Rutix.common.EstadoReserva;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaDto {
    private Long id;

    private Long pasajeroId;

    private Long viajeId;

    private Long pagoId;


    @Max(value=30, message="cantidad de lugares maximo permitido 30")
    private Integer cantidadLugares;


    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    private LocalDateTime fechaAlta;

    private LocalDateTime fechaPuntaje;

    private Integer puntaje;
}
