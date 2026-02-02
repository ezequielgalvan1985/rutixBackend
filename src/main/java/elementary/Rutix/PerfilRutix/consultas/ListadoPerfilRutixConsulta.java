package elementary.Rutix.PerfilRutix.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
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
public class ListadoPerfilRutixConsulta implements Consulta<ConsultaListadoRequestDto, List<PerfilRutixDto>> {

    @Autowired
    private PerfilRutixRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PerfilRutixDto> execute(ConsultaListadoRequestDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<PerfilRutix> resultset = this.repo.findAll(input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, PerfilRutixDto.class))
                .collect(Collectors.toList());
    }
}
