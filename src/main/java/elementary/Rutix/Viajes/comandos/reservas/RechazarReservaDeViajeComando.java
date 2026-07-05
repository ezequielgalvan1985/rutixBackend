package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarEstadoReservaDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RechazarReservaDeViajeComando implements Comando<ActualizarEstadoReservaDto, Void> {
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;

    public RechazarReservaDeViajeComando(ViajeRepository repoViaje, PerfilRutixRepository repoPerfil) {
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public Void execute(ActualizarEstadoReservaDto value) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));
        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));

        //agregar validacion para que solo el conductor o el dueño de la reserva pueda rechazar
        Reserva r = v.getListaReservas().stream()
                .filter(x -> x.getId().equals(value.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe la reserva"));
        if (v.getConductor().getId()!= p.getId() && r.getPasajero().getId()!= p.getId() ) throw new ReglaNegocioException("Solo puede Rechazar la Reserva el Conductor o el Pasajero del Viaje");

        v.rechazarReserva(value.getId());

        this.repoViaje.save(v);

        return null;
    }
}
