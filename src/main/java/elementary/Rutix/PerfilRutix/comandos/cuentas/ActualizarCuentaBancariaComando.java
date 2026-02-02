package elementary.Rutix.PerfilRutix.comandos.cuentas;

import elementary.Rutix.PerfilRutix.dto.CbuUnicoReglaNegocioDto;
import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CbuUnicoReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CuentaBancariaExistenteReglaNegocio;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ActualizarCuentaBancariaComando implements Comando<CuentaBancariaDto, CuentaBancariaDto> {

    private final CuentaBancariaRepository repo;
    private final ModelMapper modelMapper;
    private final CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio;
    private final CbuUnicoReglaNegocio cbuUnicoReglaNegocio;

    public ActualizarCuentaBancariaComando(CuentaBancariaRepository repo,
                                           ModelMapper modelMapper,
                                           CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio,
                                           CbuUnicoReglaNegocio cbuUnicoReglaNegocio
    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.cuentaBancariaExistenteReglaNegocio = cuentaBancariaExistenteReglaNegocio;
        this.cbuUnicoReglaNegocio = cbuUnicoReglaNegocio;

    }



    @Override
    public CuentaBancariaDto execute(CuentaBancariaDto input) {

        this.cbuUnicoReglaNegocio.aplicar(new CbuUnicoReglaNegocioDto(input.getId(),input.getCbu()));

        //Validaciones
        cuentaBancariaExistenteReglaNegocio.aplicar(input.getId());

        //Mappeo
        CuentaBancaria r =this.repo.save(modelMapper.map(input, CuentaBancaria.class));

        //Guardar
        return  modelMapper.map(r, CuentaBancariaDto.class);

    }

}
