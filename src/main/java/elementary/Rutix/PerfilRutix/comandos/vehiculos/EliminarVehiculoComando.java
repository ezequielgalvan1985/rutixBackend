package elementary.Rutix.PerfilRutix.comandos.vehiculos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.EliminarVehiculoDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EliminarVehiculoComando implements Comando<EliminarVehiculoDePerfilComandoDto,Void> {

    @Autowired
    private PerfilRutixRepository repo;

    @Override
    public Void execute(EliminarVehiculoDePerfilComandoDto input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long perfilId = Long.parseLong(auth.getName());
        PerfilRutix p = repo.findById(perfilId).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        p.eliminarVehiculo(input.getId());
        repo.save(p);
        return null;
    }

}
