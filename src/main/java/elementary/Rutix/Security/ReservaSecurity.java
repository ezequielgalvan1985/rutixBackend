package elementary.Rutix.Security;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Autorizacion por recurso para Reserva: el pasajero dueno de ESA reserva
 * puntual, no "es pasajero" como atributo general del perfil.
 * Usado desde @PreAuthorize en ReservaController; cierra H17 del backlog
 * (EliminarReservaDeViajeComando no validaba dueno).
 */
@Component("reservaSecurity")
public class ReservaSecurity {

    private final ReservaRepository repoReserva;
    private final PerfilRutixRepository repoPerfil;

    public ReservaSecurity(ReservaRepository repoReserva, PerfilRutixRepository repoPerfil) {
        this.repoReserva = repoReserva;
        this.repoPerfil = repoPerfil;
    }

    public boolean esPasajeroDueno(Long reservaId, Authentication auth) {
        if (reservaId == null || auth == null) return false;

        Long uid = Long.parseLong(auth.getName());
        PerfilRutix perfilAutenticado = repoPerfil.findByUsuarioId(uid).orElse(null);
        if (perfilAutenticado == null) return false;

        Reserva reserva = repoReserva.findById(reservaId).orElse(null);
        if (reserva == null) return false;

        return reserva.getPasajero().getId().equals(perfilAutenticado.getId());
    }
}
