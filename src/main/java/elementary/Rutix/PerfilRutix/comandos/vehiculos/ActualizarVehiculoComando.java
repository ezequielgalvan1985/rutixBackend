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
import org.springframework.stereotype.Service;

@Service
public class ActualizarVehiculoComando implements Comando<ActualizarVehiculoComandoDto, PerfilRutixDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PerfilRutixRepository repoPerfil;

    @Override
    public PerfilRutixDto execute(ActualizarVehiculoComandoDto input) {

        PerfilRutix p =this.repoPerfil.findById(input.getPerfilId()).orElseThrow(()-> new ReglaNegocioException("Perfil No encontrado"));
        p.actualizarVehiculo(input);
        this.repoPerfil.save(p);

        return  modelMapper.map(p, PerfilRutixDto.class);

    }
}
