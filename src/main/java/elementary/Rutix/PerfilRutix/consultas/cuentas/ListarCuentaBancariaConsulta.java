package elementary.Rutix.PerfilRutix.consultas.cuentas;

import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ListarCuentaBancariaConsulta implements Consulta<ConsultaListadoRequestDto, List<CuentaBancariaDto>> {
    @Autowired
    private CuentaBancariaRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CuentaBancariaDto> execute(ConsultaListadoRequestDto input) {
        Pageable page = PageRequest.of(0, input.getLimit());
        List<CuentaBancaria> resultset = this.repo.findAll(input.getOffset(), page);
        return resultset
                .stream()
                .map(model->modelMapper.map(model, CuentaBancariaDto.class))
                .collect(Collectors.toList());
    }

}
