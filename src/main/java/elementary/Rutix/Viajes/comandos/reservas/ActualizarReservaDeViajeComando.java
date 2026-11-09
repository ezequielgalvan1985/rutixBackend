package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ActualizarReservaDeViajeComando implements Comando<ActualizarReservaDeViajeDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;
    private ReservaRepository repoReserva;

    public ActualizarReservaDeViajeComando(ModelMapper modelMapper,
                                            ViajeRepository repoViaje,
                                            PerfilRutixRepository repoPerfil,
                                            ReservaRepository repoReserva) {
        this.modelMapper = modelMapper;
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
        this.repoReserva = repoReserva;
    }

    @Override
    public ReservaDto execute(ActualizarReservaDeViajeDto value) {

        //Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));
        Reserva r = repoReserva.findById(value.getId()).orElseThrow(()-> new ReglaNegocioException("Reserva Id inexistente"));

        //restriccion: solo el pasajero que genero la reserva puede actualizarla, y solo mientras esta PENDIENTE
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix usuarioLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        if (!usuarioLogueado.getId().equals(r.getPasajero().getId()))
            throw new ReglaNegocioException("Solo el pasajero que generó la reserva puede actualizarla");

        if (r.getEstado() != EstadoReserva.PENDIENTE)
            throw new ReglaNegocioException("La reserva solo puede actualizarse mientras se encuentra PENDIENTE");

        Viaje v = r.getViaje();
        int asientosAnteriores = r.getAsientos();
        int diferenciaAsientos = value.getAsientos() - asientosAnteriores;

        if (diferenciaAsientos > v.getAsientosDisponibles())
            throw new ReglaNegocioException("La cantidad de asientos no puede superar los asientos disponibles del viaje");

        r.setAsientos(value.getAsientos());
        r.setEstado(value.getEstado());
        r.setPuntaje(value.getPuntaje());
        r.setFechaActualizacion(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        repoReserva.save(r);

        v.setAsientosReservados(v.getAsientosReservados() + diferenciaAsientos);
        v.setAsientosDisponibles(v.getAsientos() - v.getAsientosReservados());
        repoViaje.save(v);

        return null;
    }
}
