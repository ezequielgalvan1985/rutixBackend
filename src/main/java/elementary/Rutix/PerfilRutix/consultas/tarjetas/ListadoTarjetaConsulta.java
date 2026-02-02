package elementary.Rutix.PerfilRutix.consultas.tarjetas;

import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.TokenUnicoReglaNegocio;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListadoTarjetaConsulta implements Consulta<ConsultaListadoRequestDto, List<TarjetaDto>> {

    private final TarjetaRepository repo;
    private final ModelMapper modelMapper;

    public ListadoTarjetaConsulta(
            TarjetaRepository repo,
            ModelMapper modelMapper,
            TokenUnicoReglaNegocio tokenUnicoReglaNegocio

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;

    }
    @Override
    public List<TarjetaDto> execute(ConsultaListadoRequestDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<Tarjeta> resultset = this.repo.findAll(input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, TarjetaDto.class))
                .collect(Collectors.toList());
    }
}
