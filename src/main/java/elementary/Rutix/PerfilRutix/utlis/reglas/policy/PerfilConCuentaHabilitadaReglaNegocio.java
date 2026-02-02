package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PerfilConCuentaHabilitadaReglaNegocio implements ReglaNegocio<Long, PerfilRutixDto> {

    private final PerfilRutixRepository repoPerfil;
    private final ModelMapper mapper;

    public PerfilConCuentaHabilitadaReglaNegocio(
            PerfilRutixRepository repoPerfil,
            ModelMapper mapper
    ) {
        this.repoPerfil = repoPerfil;
        this.mapper = mapper;
    }
    @Override
    public PerfilRutixDto aplicar(Long value) {
        if (value == null || value <= 0) throw new ReglaNegocioException("Perfil ID inválido");
        PerfilRutix p = repoPerfil.findById(value).orElseThrow(() -> new ReglaNegocioException("No existe Perfil"));

        return  mapper.map(p, PerfilRutixDto.class);
    }
}
