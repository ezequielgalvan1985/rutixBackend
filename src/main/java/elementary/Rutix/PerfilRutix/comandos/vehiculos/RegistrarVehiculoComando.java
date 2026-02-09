package elementary.Rutix.PerfilRutix.comandos.vehiculos;

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
public class RegistrarVehiculoComando implements Comando<VehiculoDto, PerfilRutixDto> {

    @Autowired
    private PerfilRutixRepository repoPerfil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PerfilRutixDto execute(VehiculoDto input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long perfilId = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findById(perfilId).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        Vehiculo v = modelMapper.map(input, Vehiculo.class);
        v.setPerfil(p);
        p.agregarVehiculo(v);
        return  modelMapper.map(repoPerfil.save(p), PerfilRutixDto.class);

    }

}
