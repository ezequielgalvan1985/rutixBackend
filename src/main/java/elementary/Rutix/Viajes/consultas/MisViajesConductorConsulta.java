package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
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
public class MisViajesConductorConsulta implements Consulta<ConsultaListadoRequestDto, PageResponseDto<ViajeResumidoDto>> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;
    private PerfilRutixRepository repoPerfil;


    public MisViajesConductorConsulta(ModelMapper modelMapper,
                                      ViajeRepository repo,
                                      PerfilRutixRepository repoPerfil
    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
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

        Page<Viaje> result = this.repo.findByConductorIdOrderByIdDesc(p.getId(), pageable);

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
                .asientos(r.getAsientos())
                .asientosDisponibles(r.getAsientosDisponibles())
                .asientosReservados(r.getAsientosReservados())
                .build();
        return dto;
    }


}
