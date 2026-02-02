package elementary.Rutix.PerfilRutix.consultas.vehiculos;

import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.repositorios.VehiculoRepository;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ListadoVehiculoConsulta implements Consulta<ConsultaListadoRequestDto, List<VehiculoDto>> {
    @Autowired
    private VehiculoRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<VehiculoDto> execute(ConsultaListadoRequestDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<Vehiculo> resultset = this.repo.findAll(input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, VehiculoDto.class))
                .collect(Collectors.toList());
    }
}
