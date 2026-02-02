package elementary.Rutix.Viajes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ValoracionDto {
    private long id;
    private LocalDateTime fechaAlta;
    private long usuarioId;
    private int valoracion;
    private String comentario;
    private ViajeDto viaje;
}
