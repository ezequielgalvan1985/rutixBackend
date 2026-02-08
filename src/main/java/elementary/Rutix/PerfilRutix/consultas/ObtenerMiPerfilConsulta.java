package elementary.Rutix.PerfilRutix.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ObtenerMiPerfilConsulta implements Consulta<Void, PerfilRutixDto> {
    private PerfilRutixRepository repo;
    private ModelMapper modelMapper;

    public ObtenerMiPerfilConsulta(PerfilRutixRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    @Override
    public PerfilRutixDto execute(Void v) {

        //obtener el id a partir del jwt
        Long id = 1l;
        PerfilRutix entity =  repo.findById(id).orElseThrow(()-> new NoEncontradoException("no se encontro el id: "+ id.toString()));
        return modelMapper.map(entity, PerfilRutixDto.class);

    }
}
