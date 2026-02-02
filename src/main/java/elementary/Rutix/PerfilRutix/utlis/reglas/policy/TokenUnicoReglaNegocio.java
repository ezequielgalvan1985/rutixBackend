package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TokenUnicoReglaNegocio implements ReglaNegocio<TarjetaDto,Boolean> {
    private final TarjetaRepository repo;
    private final ModelMapper mapper;

    public TokenUnicoReglaNegocio(
            TarjetaRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Boolean aplicar(TarjetaDto dto) {
        if (dto == null ) throw new ReglaNegocioException("Tarjeta Invalida");
        if (this.repo.existsByTokenAndIdNot(dto.getToken(), dto.getId())) throw new ReglaNegocioException("Token Tarjeta debe ser Unico");
        return true;
    }
}
