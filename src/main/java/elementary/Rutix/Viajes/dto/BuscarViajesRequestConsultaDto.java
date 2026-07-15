package elementary.Rutix.Viajes.dto;

import elementary.Rutix.common.Enum.EstadoViajeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuscarViajesRequestConsultaDto {
    private LocalDate fechaSalida;
    private String ciudadPartida;
    private String ciudadDestino;

    @Enumerated(EnumType.STRING)
    private EstadoViajeEnum estado;

    private Long offset;
    private Integer limit;
}
