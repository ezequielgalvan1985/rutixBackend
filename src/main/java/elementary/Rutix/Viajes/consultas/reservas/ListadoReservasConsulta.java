package elementary.Rutix.Viajes.consultas.reservas;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixResumidoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
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
public class ListadoReservasConsulta implements Consulta<ConsultaListadoRequestDto, PageResponseDto<ReservaDto>> {

    private ModelMapper modelMapper;
    private ReservaRepository repo;

    public ListadoReservasConsulta(ModelMapper modelMapper, ReservaRepository repo) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public PageResponseDto<ReservaDto> execute(ConsultaListadoRequestDto value) {
        Pageable pageable = PageRequest.of(
                value.getOffset().intValue(),   // page
                value.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );
        Page<Reserva> result = this.repo.findAll(pageable);
        return PageResponseDto.<ReservaDto>builder()
                .data(result.map(this::toDto).getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .total(result.getTotalElements())
                .hasNext(result.hasNext())
                .build();
    }

    private ReservaDto toDto(Reserva r) {
        return modelMapper.map(r, ReservaDto.class);
    }



}
