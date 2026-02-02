package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.Viajes.comandos.dto.EliminarReservaDeViajeComandoDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

@Service
public class EliminarReservaDeViajeComando implements Comando<EliminarReservaDeViajeComandoDto, Void> {

    private ViajeRepository repoViaje;

    public EliminarReservaDeViajeComando(ViajeRepository repoViaje) {
        this.repoViaje = repoViaje;
    }

    @Override
    public Void execute(EliminarReservaDeViajeComandoDto value) {
        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));
        v.eliminarReserva(value.getId());
        this.repoViaje.save(v);
        return null;
    }
}
