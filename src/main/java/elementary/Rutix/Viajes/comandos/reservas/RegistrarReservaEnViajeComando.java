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
        if (v.getAsientosDisponibles() < value.getAsientos()) throw new ReglaNegocioException("No hay Asientos disponibles");

        Reserva r = Reserva.builder()
                .pasajero(p)
                .viaje(v)
                .asientos(value.getAsientos())
                .estado(EstadoReserva.PENDIENTE)
                .build();
        v.setAsientosReservados(v.getAsientosReservados()+value.getAsientos());
        v.setAsientosDisponibles(v.getAsientos()-v.getAsientosReservados());
        v.registrarReserva(r);
        repoViaje.save(v);
        return this.modelMapper.map(r, ReservaDto.class);
    }
}
