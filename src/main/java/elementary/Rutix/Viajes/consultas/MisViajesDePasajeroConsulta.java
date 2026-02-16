package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.dto.PageResponseDto;
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

@Service
public class MisViajesDePasajeroConsulta implements Consulta<ConsultaListadoRequestDto, PageResponseDto<ViajeResumidoDto>> {

    private ReservaRepository repo;
    private ModelMapper modelMapper;
    private PerfilRutixRepository repoPerfil;


    public MisViajesDePasajeroConsulta(ReservaRepository r,
                                       ModelMapper modelMapper,
                                       PerfilRutixRepository repoPerfil){
        this.repo = r;
        this.modelMapper = modelMapper;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public PageResponseDto<ViajeResumidoDto> execute(ConsultaListadoRequestDto value) {

        Pageable pageable = PageRequest.of(
                value.getOffset().intValue(),   // page
                value.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );
        //Obtener mi perfil
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));


        Page<Reserva> result = this.repo.findByPasajeroIdOrderByIdDesc(p.getId(), pageable);
        return PageResponseDto.<ViajeResumidoDto>builder()
                .data(result.map(this::toDto).getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .total(result.getTotalElements())
                .hasNext(result.hasNext())
                .build();

    }


    private ViajeResumidoDto toDto(Reserva r) {

        ViajeResumidoDto dto = new ViajeResumidoDto()
                .builder()
                .id(r.getId())
                .ciudadPartida(r.getViaje().getCiudadPartida())
                .ciudadDestino(r.getViaje().getCiudadDestino())
                .fechaSalida(r.getViaje().getFechaSalida())
                .horaSalida(r.getViaje().getHoraSalida())
                .horaLlegada(r.getViaje().getHoraLlegada())
                .estado(r.getViaje().getEstado())
                .valor(r.getViaje().getValor())
                .porcentajeSenia(r.getViaje().getPorcentajeSenia())
                .pagaSenia(r.getViaje().getPagaSenia())
                .conductor(modelMapper.map(r.getViaje().getConductor(), PerfilRutixResumidoDto.class))
                .vehiculo(modelMapper.map(r.getViaje().getVehiculo(), VehiculoDto.class))
                .asientos(r.getAsientos())
                .asientosDisponibles(r.getViaje().getAsientosDisponibles())
                .asientosReservados(r.getViaje().getAsientosReservados())
                .build();
        return dto;
    }


}
