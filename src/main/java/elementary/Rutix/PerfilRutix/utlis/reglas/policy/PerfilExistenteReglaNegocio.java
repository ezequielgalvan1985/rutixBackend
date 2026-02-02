package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PerfilExistenteReglaNegocio implements ReglaNegocio<Long, PerfilRutix> {

    private final PerfilRutixRepository repo;
    private final ModelMapper mapper;

    public PerfilExistenteReglaNegocio(
            PerfilRutixRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public PerfilRutix aplicar(Long value) {
        if (value == null ) throw new ReglaNegocioException("Perfil Rutix inválido");

        if (value ==null) throw new ReglaNegocioException("ID Perfil no puede ser nulo");
        if (value <= 0 )throw new ReglaNegocioException("ID Perfil debe ser mayor a cero");

        PerfilRutix entity = repo.findById(value)
                .orElseThrow(() ->
                        new ReglaNegocioException("El Perfil ID no existe")
                );

        return entity;
    }
}
