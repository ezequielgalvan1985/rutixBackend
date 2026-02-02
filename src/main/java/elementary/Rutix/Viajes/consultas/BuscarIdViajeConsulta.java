package elementary.Rutix.Viajes.consultas;

import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdViajeConsulta implements Consulta<Long, ViajeDto> {
    private ModelMapper modelMapper;
    private ViajeRepository repo;


    public BuscarIdViajeConsulta(ModelMapper modelMapper, ViajeRepository repo
                    ) {
        this.modelMapper = modelMapper;
        this.repo = repo;

    }

    @Override
    public ViajeDto execute(Long value) {
        Viaje entity =  repo.findById(value).orElseThrow(()-> new NoEncontradoException("Viaje Inexistente: "+ value.toString()));
        return this.modelMapper.map(entity, ViajeDto.class);
    }
}
