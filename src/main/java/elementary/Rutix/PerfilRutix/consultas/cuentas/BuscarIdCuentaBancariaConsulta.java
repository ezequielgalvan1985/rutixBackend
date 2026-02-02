package elementary.Rutix.PerfilRutix.consultas.cuentas;

import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.common.excepciones.NoEncontradoException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdCuentaBancariaConsulta implements Consulta<Long, CuentaBancariaDto> {

    private final CuentaBancariaRepository repo;
    private final ModelMapper modelMapper;

    public BuscarIdCuentaBancariaConsulta(
            CuentaBancariaRepository repo,
            ModelMapper modelMapper
    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CuentaBancariaDto execute(Long input) {
        CuentaBancaria entity =  repo.findById(input).orElseThrow(()-> new NoEncontradoException("no se encontro el id: "+ input.toString()));
        return modelMapper.map(entity, CuentaBancariaDto.class);
    }
}
