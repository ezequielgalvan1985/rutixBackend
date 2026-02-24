package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarEstadoReservaDto;
import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ConfirmarReservaDeViajeComando implements Comando<ActualizarEstadoReservaDto, Void> {
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;

    public ConfirmarReservaDeViajeComando(ViajeRepository repoViaje, PerfilRutixRepository repoPerfil) {
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public Void execute(ActualizarEstadoReservaDto value) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));
        if (v.getConductor().getId().equals(p.getId()) ==false) throw new ReglaNegocioException("Solo puede Confirmar la Reserva el Conductor del Viaje");
        v.confirmarReserva(value.getId());
        this.repoViaje.save(v);

        return null;
    }
}
