package elementary.Rutix.PerfilRutix.consultas.tarjetas;

import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdTarjetaConsulta implements Consulta<Long, TarjetaDto > {

    private final TarjetaRepository repo;
    private final ModelMapper modelMapper;

    public BuscarIdTarjetaConsulta(
            TarjetaRepository repo,
            ModelMapper modelMapper

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;

    }


    @Override
    public TarjetaDto execute(Long input) {
        Tarjeta entity =  repo.findById(input).orElseThrow(()-> new ReglaNegocioException("Tarjeta ID Inexistente: "+ input.toString()));
        return modelMapper.map(entity, TarjetaDto.class);
    }
}
