package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;

public class ConfirmarReservaDeViajeComando implements Comando<ActualizarReservaDeViajeDto, Void> {
    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;
    private ReservaRepository repoReserva;

    @Override
    public Void execute(ActualizarReservaDeViajeDto value) {
        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));
        v.confirmarReserva(v.getId());
        return null;
    }
}
