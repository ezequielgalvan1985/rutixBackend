package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TarjetaIdExistenteReglaNegocio implements ReglaNegocio<TarjetaDto, Boolean> {
    private final TarjetaRepository repo;
    private final ModelMapper mapper;

    public TarjetaIdExistenteReglaNegocio(
            TarjetaRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }
    @Override
    public Boolean aplicar(TarjetaDto dto) {
        if (dto == null ) throw new ReglaNegocioException("Tarjeta Invalida");
        if (dto.getId() == null ) throw new ReglaNegocioException("Tarjeta ID Invalido");
        if (dto.getId() <= 0  ) throw new ReglaNegocioException("Tarjeta ID no puede ser Cero");
        if (!this.repo.existsById(dto.getId())) throw new ReglaNegocioException("Tarjeta ID inexistente");
        return true;
    }
}
