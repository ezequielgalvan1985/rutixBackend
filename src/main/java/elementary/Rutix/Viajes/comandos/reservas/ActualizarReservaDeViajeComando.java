package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

@Service
public class ActualizarReservaDeViajeComando implements Comando<ReservaDto, ReservaDto> {

    @Override
    public ReservaDto execute(ReservaDto value) {
        return null;
    }
}
