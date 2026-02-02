package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CuentaBancariaExistenteReglaNegocio implements ReglaNegocio<Long,CuentaBancariaDto> {
    private final CuentaBancariaRepository repo;
    private final ModelMapper mapper;

    public CuentaBancariaExistenteReglaNegocio(
            CuentaBancariaRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public CuentaBancariaDto aplicar(Long id) {
        if (id <=0 ) throw new ReglaNegocioException("Cuenta Bancaria inválida");

        CuentaBancaria entity = repo.findById(id)
                .orElseThrow(() ->
                        new ReglaNegocioException("Cuenta Bancaria Inexistente")
                );

        return mapper.map(entity, CuentaBancariaDto.class);
    }
}
