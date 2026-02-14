package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ActualizarReservaDeViajeComando implements Comando<ActualizarReservaDeViajeDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;

    @Override
    public ReservaDto execute(ActualizarReservaDeViajeDto value) {

        return null;
    }
}
