package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ListadoViajeConsulta implements Consulta<ConsultaListadoRequestDto, List<ViajeDto>> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;


    public ListadoViajeConsulta(ModelMapper modelMapper, ViajeRepository repo
    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;

    }

    @Override
    public List<ViajeDto> execute(ConsultaListadoRequestDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<Viaje> resultset = this.repo.findAll(input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, ViajeDto.class))
                .collect(Collectors.toList());
    }
}
