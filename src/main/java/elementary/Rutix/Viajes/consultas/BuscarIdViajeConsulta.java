package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.consultas.dto.ReservaViewDto;
import elementary.Rutix.Viajes.consultas.dto.ViajeDetalleDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.AccionEnum;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuscarIdViajeConsulta implements Consulta<Long, ViajeDetalleDto> {
    private ModelMapper modelMapper;
    private ViajeRepository repo;
    private final PerfilRutixRepository repoPerfil;

    public BuscarIdViajeConsulta(ModelMapper modelMapper,
                                 ViajeRepository repo,
                                 PerfilRutixRepository repoPerfil
                    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public ViajeDetalleDto execute(Long value) {
        //Obtener mi perfil
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));
        Viaje r =  repo.findById(value).orElseThrow(()-> new NoEncontradoException("Viaje Inexistente: "+ value.toString()));
        List<ReservaViewDto> listaReservasDto = r.getListaReservas().stream().map(reserva-> modelMapper.map(reserva,ReservaViewDto.class)).toList();

        ViajeDetalleDto dto = new ViajeDetalleDto()
                .builder()
                .id(r.getId())
                .ciudadPartida(r.getCiudadPartida())
                .ciudadDestino(r.getCiudadDestino())
                .fechaSalida(r.getFechaSalida())
                .horaSalida(r.getHoraSalida())
                .horaLlegada(r.getHoraLlegada())
                .estado(r.getEstado())
                .valor(r.getValor())
                .porcentajeSenia(r.getPorcentajeSenia())
                .pagaSenia(r.getPagaSenia())
                .conductor(modelMapper.map(r.getConductor(), PerfilRutixResumidoDto.class))
                .vehiculo(modelMapper.map(r.getVehiculo(), VehiculoDto.class))
                .listaReservas(listaReservasDto)
                .asientos(r.getAsientos())
                .accionesDisponibles(this.getListaAcciones(r,p))
                .asientosDisponibles(r.getAsientosDisponibles())
                .asientosReservados(r.getAsientosReservados())
                .build();

        return dto;
    }

    private List<String>getListaAcciones(Viaje v, PerfilRutix p){
        List<String> lista = new ArrayList<>();
        //si EL conductor esta viendo el detalle del viaje
        if (v.getConductor().getId() == p.getId()){
            lista.add(AccionEnum.RESERVA_CONFIRMAR.name());
            lista.add(AccionEnum.RESERVA_RECHAZAR.name());
        }else{
            lista.add(AccionEnum.RESERVA_REGISTRAR.name());
            lista.add(AccionEnum.RESERVA_CANCELAR.name());
            lista.add(AccionEnum.RESERVA_PAGAR.name());
        }
        return lista;
    }



}
