package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ActualizarReservaDeViajeComando implements Comando<ActualizarReservaDeViajeDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;
    private ReservaRepository repoReserva;

    @Override
    public ReservaDto execute(ActualizarReservaDeViajeDto value) {

        //agregar restriccion
        //solo puede actualizar, pasajero de la reserva, conductor o admin
        //Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()-> new ReglaNegocioException("Viaje Inexistente"));
        Reserva r = repoReserva.findById(value.getId()).orElseThrow(()-> new ReglaNegocioException("Reserva Id inexistente"));
        r.setAsientos(value.getAsientos());
        r.setEstado(value.getEstado());
        r.setPuntaje(value.getPuntaje());
        r.setFechaActualizacion(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")));
        repoReserva.save(r);
        return null;
    }
}
