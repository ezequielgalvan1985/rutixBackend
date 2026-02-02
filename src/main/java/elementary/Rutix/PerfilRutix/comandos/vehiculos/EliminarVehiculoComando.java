package elementary.Rutix.PerfilRutix.comandos.vehiculos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.EliminarVehiculoDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminarVehiculoComando implements Comando<EliminarVehiculoDePerfilComandoDto,Void> {

    @Autowired
    private PerfilRutixRepository repo;

    @Override
    public Void execute(EliminarVehiculoDePerfilComandoDto input) {
        PerfilRutix p = repo.findById(input.getPerfilId()).orElseThrow(()->new ReglaNegocioException("Perfil Inexistente"));
        p.eliminarVehiculo(input.getId());
        repo.save(p);
        return null;
    }

}
