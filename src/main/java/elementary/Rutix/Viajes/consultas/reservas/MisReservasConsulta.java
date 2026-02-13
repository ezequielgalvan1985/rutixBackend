package elementary.Rutix.Viajes.consultas.reservas;

import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dto.ListadoByPasajeroIdRequestDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MisReservasConsulta implements Consulta<ListadoByPasajeroIdRequestDto, Page<ReservaDto>> {

    private ReservaRepository repo;
    private ModelMapper modelMapper;

    public MisReservasConsulta(ReservaRepository r, ModelMapper modelMapper){
        this.repo = r;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ReservaDto> execute(ListadoByPasajeroIdRequestDto value) {
        Pageable pageable = PageRequest.of(
                value.getOffset().intValue(),   // page
                value.getLimit(),               // size
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Reserva> result = this.repo.findByPasajeroIdOrderByIdDesc(value.getPasajeroId(), pageable);
        return result.map(this::toDto);

    }

    private ReservaDto toDto(Reserva r) {
        return this.modelMapper.map(r,ReservaDto.class);
    }

}
