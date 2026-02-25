package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.BuscarViajesRequestConsultaDto;
import elementary.Rutix.Viajes.dto.BuscarViajesResponseConsultaDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.AccionEnum;
import elementary.Rutix.common.dto.PageResponseDto;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BuscarViajesConsulta implements Consulta<BuscarViajesRequestConsultaDto, PageResponseDto<ViajeResumidoDto>> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;
    private final PerfilRutixRepository repoPerfil;
    private PerfilRutix perfilLogueado;

    public BuscarViajesConsulta(ModelMapper modelMapper, ViajeRepository repo, PerfilRutixRepository repoPerfil
    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoPerfil = repoPerfil;


    }

    @Override
    public PageResponseDto<ViajeResumidoDto> execute(BuscarViajesRequestConsultaDto input) {
        Pageable pageable = PageRequest.of(
                input.getOffset().intValue(),   // page
                input.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        this.perfilLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaSalida(), input.getCiudadPartida(), input.getCiudadDestino(),  pageable);

        return PageResponseDto.<ViajeResumidoDto>builder()
                .data(result.map(this::toDto).getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .total(result.getTotalElements())
                .hasNext(result.hasNext())
                .build();
    }

    private ViajeResumidoDto toDto(Viaje r) {

        ViajeResumidoDto dto = new ViajeResumidoDto()
                .builder()
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
                .asientosReservados(r.getAsientosReservados())
                .asientosDisponibles(r.getAsientosDisponibles())
                .asientos(r.getAsientos())
                .accionesDisponibles(this.getListaAcciones(r,this.perfilLogueado))
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
