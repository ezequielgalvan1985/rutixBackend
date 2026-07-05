package elementary.Rutix.Viajes.dto;

import elementary.Rutix.common.Enum.EstadoReserva;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReservaDto {
    private Long id;

    private Long pasajeroId;

    private Long viajeId;


    private List<PagoDto> listaPagos = new ArrayList<PagoDto>();



    @Max(value=30, message="cantidad de lugares maximo permitido 30")
    private Integer asientos;


    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    private LocalDateTime fechaAlta;

    private LocalDateTime fechaPuntaje;

    private Integer puntaje;

    private BigDecimal valorTotalReserva;

}
