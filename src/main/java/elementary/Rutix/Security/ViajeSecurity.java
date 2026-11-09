package elementary.Rutix.Security;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Autorizacion por recurso para Viaje: resuelve si el PerfilRutix autenticado
 * es el conductor DEL VIAJE PUNTUAL que se quiere escribir, no si "es conductor"
 * en general (un mismo perfil puede ser conductor de un viaje y pasajero de otro).
 * Usado desde @PreAuthorize en ViajeController; cierra H15/H16 del backlog
 * (ActualizarViajeComando y EliminarViajeComando no validaban dueno).
 */
@Component("viajeSecurity")
public class ViajeSecurity {

    private final ViajeRepository repoViaje;
    private final PerfilRutixRepository repoPerfil;

    public ViajeSecurity(ViajeRepository repoViaje, PerfilRutixRepository repoPerfil) {
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
    }

    public boolean esConductor(Long viajeId, Authentication auth) {
        if (viajeId == null || auth == null) return false;

        Long uid = Long.parseLong(auth.getName());
        PerfilRutix perfilAutenticado = repoPerfil.findByUsuarioId(uid).orElse(null);
        if (perfilAutenticado == null) return false;

        Viaje viaje = repoViaje.findById(viajeId).orElse(null);
        if (viaje == null) return false;

        return viaje.getConductor().getId().equals(perfilAutenticado.getId());
    }
}
