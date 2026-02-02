package elementary.Rutix.PerfilRutix.consultas;


import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdPerfilRutixConsulta implements Consulta<Long, PerfilRutixDto> {
    @Autowired
    private PerfilRutixRepository repo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PerfilRutixDto execute(Long input) {
        PerfilRutix entity =  repo.findById(input).orElseThrow(()-> new NoEncontradoException("no se encontro el id: "+ input.toString()));
        return modelMapper.map(entity, PerfilRutixDto.class);
    }
}
