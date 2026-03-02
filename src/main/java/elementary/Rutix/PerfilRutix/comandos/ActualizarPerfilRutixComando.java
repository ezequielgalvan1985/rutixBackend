package elementary.Rutix.PerfilRutix.comandos;

import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CuentaBancariaExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.VehiculoExistenteReglaNegocio;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ActualizarPerfilRutixComando implements Comando<PerfilRutixDto,PerfilRutixDto> {


    private final PerfilRutixRepository repo;
    private final ModelMapper modelMapper;
    private final VehiculoExistenteReglaNegocio vehiculoExistenteRegla;
    private final PerfilExistenteReglaNegocio perfilExistenteRegla;
    private final CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio;

    public ActualizarPerfilRutixComando(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            VehiculoExistenteReglaNegocio vehiculoExistenteRegla,
            PerfilExistenteReglaNegocio perfilExistenteRegla,
            CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.vehiculoExistenteRegla = vehiculoExistenteRegla;
        this.perfilExistenteRegla = perfilExistenteRegla;
        this.cuentaBancariaExistenteReglaNegocio = cuentaBancariaExistenteReglaNegocio;

    }
    @Override
    public PerfilRutixDto execute(PerfilRutixDto input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        input.setUsuarioId(Long.valueOf(auth.getName()));
        PerfilRutix p = repo.findByUsuarioId(input.getUsuarioId()).orElseThrow(()-> new ReglaNegocioException("NO existe perfil para el Usuario"));

        //Mappeo
        PerfilRutix r =this.repo.save(modelMapper.map(input, PerfilRutix.class));

        //Guardar
        return  modelMapper.map(r, PerfilRutixDto.class);
    }
}
