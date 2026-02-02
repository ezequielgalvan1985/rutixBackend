package elementary.Rutix.PerfilRutix.comandos;

import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CuentaBancariaExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.VehiculoExistenteReglaNegocio;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarPerfilRutixComando implements Comando<PerfilRutixDto,PerfilRutixDto> {

    private final PerfilRutixRepository repo;
    private final ModelMapper modelMapper;
    private final VehiculoExistenteReglaNegocio vehiculoExistenteRegla;
    private final CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio;

    public RegistrarPerfilRutixComando(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            VehiculoExistenteReglaNegocio vehiculoExistenteRegla,
            CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio
            ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.vehiculoExistenteRegla = vehiculoExistenteRegla;
        this.cuentaBancariaExistenteReglaNegocio = cuentaBancariaExistenteReglaNegocio;
    }


    @Override
    public PerfilRutixDto execute(PerfilRutixDto input) {



        //Mappeo
        PerfilRutix r =this.repo.save(modelMapper.map(input, PerfilRutix.class));

        //Guardar
        return  modelMapper.map(r, PerfilRutixDto.class);
    }
}
