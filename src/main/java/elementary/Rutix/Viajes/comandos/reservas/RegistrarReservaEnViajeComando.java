package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.comandos.dto.RegistrarReservaDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RegistrarReservaEnViajeComando implements Comando<RegistrarReservaDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilRutixRepository repoPerfil;
    public RegistrarReservaEnViajeComando(ModelMapper modelMapper,
                                          ViajeRepository repoViaje,
                                          PerfilRutixRepository repoPerfil
                                          ) {
        this.modelMapper = modelMapper;
        this.repoViaje = repoViaje;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public ReservaDto execute(RegistrarReservaDto value) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());

        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()->new ReglaNegocioException("Viaje Inexistente"));

        if (v.getAsientosDisponibles() < value.getAsientos())
            throw new ReglaNegocioException("Cantidad Ingresada supera los Asientos Disponibles");

        //agregar restriccion, pasajero no puede ser conductor
        if(v.getConductor().getId().equals(p.getId()))
            throw  new ReglaNegocioException("Conductor no puede reservar un asiento");
        //buscar si tiene una reserva PENDIENTE este usuario (una reserva ya
        //rechazada/cancelada/confirmada/pagada es un estado cerrado: una nueva
        //reserva del mismo pasajero tiene que arrancar una fila nueva en
        //PENDIENTE, no reabrir/sumarle asientos a la vieja -- eso fue lo que
        //causaba que al rechazar una reserva de 3 asientos y volver a reservar
        //1 asiento, la reserva rechazada quedara en 4 asientos sin volver a
        //PENDIENTE).

        //si existe reserva PENDIENTE se debera modificar la Reserva agregando asientos cantidad de asientos
        Reserva reserva = v.getListaReservas().stream()
                .filter(r -> r.getPasajero().getId().equals(p.getId()) && r.getEstado() == EstadoReserva.PENDIENTE)
                .findFirst()
                .orElse(null);
        if (reserva!=null){

            reserva.setAsientos(reserva.getAsientos() + value.getAsientos());
        }else{
            reserva = Reserva.builder()
                    .pasajero(p)
                    .viaje(v)
                    .asientos(value.getAsientos())
                    .estado(EstadoReserva.PENDIENTE)
                    .build();
            v.registrarReserva(reserva);
        }

        v.setAsientosReservados(v.getAsientosReservados()+value.getAsientos());
        v.setAsientosDisponibles(v.getAsientos()-v.getAsientosReservados());

        repoViaje.save(v);
        return this.modelMapper.map(reserva, ReservaDto.class);
    }
}
