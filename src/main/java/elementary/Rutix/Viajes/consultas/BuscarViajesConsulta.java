package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.BuscarViajesRequestConsultaDto;
import elementary.Rutix.Viajes.dto.BuscarViajesResponseConsultaDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BuscarViajesConsulta implements Consulta<BuscarViajesRequestConsultaDto, List<BuscarViajesResponseConsultaDto>> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;

    public BuscarViajesConsulta(ModelMapper modelMapper, ViajeRepository repo
    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public List<BuscarViajesResponseConsultaDto> execute(BuscarViajesRequestConsultaDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<Viaje> resultset = this.repo.buscarViajes(input.getFechaSalida(), input.getCiudadPartida(), input.getCiudadDestino(), input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, BuscarViajesResponseConsultaDto.class))
                .collect(Collectors.toList());
    }
}
