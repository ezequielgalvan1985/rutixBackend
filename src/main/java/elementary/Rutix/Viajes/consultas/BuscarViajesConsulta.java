package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.BuscarViajesRequestConsultaDto;
import elementary.Rutix.Viajes.dto.BuscarViajesResponseConsultaDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.dto.PageResponseDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BuscarViajesConsulta implements Consulta<BuscarViajesRequestConsultaDto, PageResponseDto<ViajeResumidoDto>> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;

    public BuscarViajesConsulta(ModelMapper modelMapper, ViajeRepository repo
    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public PageResponseDto<ViajeResumidoDto> execute(BuscarViajesRequestConsultaDto input) {
        Pageable pageable = PageRequest.of(
                input.getOffset().intValue(),   // page
                input.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Viaje> result = this.repo.buscarViajes(input.getFechaSalida(), input.getCiudadPartida(), input.getCiudadDestino(), input.getOffset(), pageable);
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
                .build();
        return dto;
    }

}
