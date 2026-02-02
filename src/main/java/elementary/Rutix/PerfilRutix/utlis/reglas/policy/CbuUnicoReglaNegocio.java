package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dto.CbuUnicoReglaNegocioDto;
import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CbuUnicoReglaNegocio implements ReglaNegocio<CbuUnicoReglaNegocioDto,Boolean> {
    private final CuentaBancariaRepository repo;
    private final ModelMapper mapper;

    public CbuUnicoReglaNegocio(
            CuentaBancariaRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Boolean aplicar(CbuUnicoReglaNegocioDto dto) {
        if (dto == null ) throw new ReglaNegocioException("Faltan Datos de Cuenta Bancaria");
        Long id = 0l;
        if(dto.getId() != null)
            id = dto.getId();
        if (this.repo.existsByCbuAndIdNot(dto.getCbu(),id)) throw new ReglaNegocioException("CBU ya fue Registrado");

        return true;
    }
}
