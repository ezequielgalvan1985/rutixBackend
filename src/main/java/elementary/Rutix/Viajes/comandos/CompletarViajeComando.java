package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CompletarViajeComando implements Comando<Long, Void> {

    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;

    public CompletarViajeComando(ViajeRepository repoViaje, PerfilRutixRepository repoPerfil) {
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public Void execute(Long value) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Viaje v = this.repoViaje.findById(value).orElseThrow(() -> new ReglaNegocioException("Viaje Inexistente"));

        if (v.getConductor().getId().equals(p.getId()) == false)
            throw new ReglaNegocioException("Solo puede Completar el Viaje el Conductor del Viaje");

        v.completar();
        this.repoViaje.save(v);

        return null;
    }
}
