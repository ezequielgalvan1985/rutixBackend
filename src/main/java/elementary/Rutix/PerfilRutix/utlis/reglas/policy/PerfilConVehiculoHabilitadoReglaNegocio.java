package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PerfilConVehiculoHabilitadoReglaNegocio implements ReglaNegocio<Long, Boolean> {
    private final PerfilRutixRepository repoPerfil;

    public PerfilConVehiculoHabilitadoReglaNegocio(
            PerfilRutixRepository repoPerfil,
            ModelMapper mapper
    ) {
        this.repoPerfil = repoPerfil;
    }

    @Override
    public Boolean aplicar(Long value) {
        if (value == null || value <= 0) throw new ReglaNegocioException("Perfil ID inválido");
        PerfilRutix p = repoPerfil.findById(value).orElseThrow(()-> new ReglaNegocioException("No existe Perfil"));
        //if (p.getVehiculo()== null) throw new ReglaNegocioException("Perfil No tiene Vehiculo Cargado");
        //if(!p.getVehiculo().getActivo()) throw new ReglaNegocioException("Vehiculo no habilitado");
        return true;
    }

}
