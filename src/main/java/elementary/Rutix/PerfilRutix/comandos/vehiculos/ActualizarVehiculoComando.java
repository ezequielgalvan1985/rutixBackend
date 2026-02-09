package elementary.Rutix.PerfilRutix.comandos.vehiculos;

import elementary.Rutix.PerfilRutix.comandos.dto.ActualizarVehiculoComandoDto;
import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.repositorios.VehiculoRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ActualizarVehiculoComando implements Comando<ActualizarVehiculoComandoDto, PerfilRutixDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PerfilRutixRepository repoPerfil;

    @Override
    public PerfilRutixDto execute(ActualizarVehiculoComandoDto input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long perfilId = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findById(perfilId).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        p.actualizarVehiculo(input);
        this.repoPerfil.save(p);

        return  modelMapper.map(p, PerfilRutixDto.class);

    }
}
