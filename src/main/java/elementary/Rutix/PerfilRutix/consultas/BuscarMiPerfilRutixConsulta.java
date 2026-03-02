package elementary.Rutix.PerfilRutix.consultas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CuentaBancariaExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.VehiculoExistenteReglaNegocio;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BuscarMiPerfilRutixConsulta implements Consulta<Void, PerfilRutixDto> {

    private final PerfilRutixRepository repo;
    private final ModelMapper modelMapper;

    public BuscarMiPerfilRutixConsulta(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            VehiculoExistenteReglaNegocio vehiculoExistenteRegla,
            CuentaBancariaExistenteReglaNegocio cuentaBancariaExistenteReglaNegocio
    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;

    }


    @Override
    public PerfilRutixDto execute(Void v) {
        //busca perfil para usuario logueado
        // si no existe lo crea
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PerfilRutixDto dto = new PerfilRutixDto();
        dto.setUsuarioId(Long.valueOf(auth.getName()));
        PerfilRutix p = repo.findByUsuarioId(dto.getUsuarioId()).orElse(null);

        if (p==null){
            p = modelMapper.map(dto, PerfilRutix.class);
        }
        //Mappeo
        PerfilRutix perfilUpd =this.repo.save(p);

        //Guardar
        return  modelMapper.map(perfilUpd, PerfilRutixDto.class);

    }
}
