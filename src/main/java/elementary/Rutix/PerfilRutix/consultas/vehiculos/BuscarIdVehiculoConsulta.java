package elementary.Rutix.PerfilRutix.consultas.vehiculos;

import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.VehiculoRepository;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdVehiculoConsulta implements Consulta<Long, VehiculoDto> {
    @Autowired
    private VehiculoRepository repo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public VehiculoDto execute(Long input) {
        Vehiculo entity =  repo.findById(input).orElseThrow(()-> new NoEncontradoException("Vehiculo inexistente: "+ input.toString()));
        return modelMapper.map(entity, VehiculoDto.class);
    }
}
