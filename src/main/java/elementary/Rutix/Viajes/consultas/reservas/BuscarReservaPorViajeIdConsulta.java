package elementary.Rutix.Viajes.consultas.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Consulta;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BuscarReservaPorViajeIdConsulta implements Consulta<Long, ReservaDto> {
    private ReservaRepository repo;
    private ModelMapper modelMapper;
    private PerfilRutixRepository repoPerfil;

    public BuscarReservaPorViajeIdConsulta(ReservaRepository r,
                                           ModelMapper modelMapper,
                                           PerfilRutixRepository repoPerfil) {
        this.repo = r;
        this.modelMapper = modelMapper;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public ReservaDto execute(Long value) {

        //Obtener mi perfil
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix perfilPasajeroId = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        // Puede existir mas de una Reserva de este pasajero para el mismo
        // viaje (p.ej. una RECHAZADA/CANCELADA vieja y una PENDIENTE nueva
        // tras volver a reservar). "La reserva actual" es la mas reciente.
        List<Reserva> reservas = repo.findByPasajeroIdAndViajeIdOrderByIdDesc(perfilPasajeroId.getId(), value);
        if (reservas.isEmpty()) {
            throw new ReglaNegocioException("Usuario Logueado no tiene Reserva en el Viaje");
        }
        Reserva r = reservas.get(0);
        return modelMapper.map(r,ReservaDto.class);
    }
}
