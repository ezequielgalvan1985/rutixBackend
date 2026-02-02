package elementary.Rutix.Viajes.consultas.reservas;

import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListadoReservasConsulta implements Consulta<ConsultaListadoRequestDto, List<ReservaDto>> {

    private ModelMapper modelMapper;
    private ReservaRepository repo;

    public ListadoReservasConsulta(ModelMapper modelMapper, ReservaRepository repo) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public List<ReservaDto> execute(ConsultaListadoRequestDto value) {
        Pageable page = PageRequest.of(0, value.getLimit());
        List<Reserva> resultset = this.repo.findAll(value.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, ReservaDto.class))
                .collect(Collectors.toList());
    }
}
