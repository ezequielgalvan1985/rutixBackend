package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConCuentaHabilitadaReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConVehiculoHabilitadoReglaNegocio;
import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ActualizarViajeComando implements Comando<ViajeDto, ViajeDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;

    //Reglas de negocio
    private PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio;
    private PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio;

    public ActualizarViajeComando(ModelMapper modelMapper, ViajeRepository repo,
                                  PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio,
                                  PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.perfilConVehiculoHabilitadoReglaNegocio = perfilConVehiculoHabilitadoReglaNegocio;
        this.perfilConCuentaHabilitadaReglaNegocio = perfilConCuentaHabilitadaReglaNegocio;
    }

    @Override
    public ViajeDto execute(ViajeDto input) {
        //validaciones
        this.perfilConCuentaHabilitadaReglaNegocio.aplicar(input.getPerfilCreador().getId());
        this.perfilConVehiculoHabilitadoReglaNegocio.aplicar(input.getPerfilCreador().getId());
        Viaje v = modelMapper.map(input, Viaje.class);

        //if (input.getLugaresReservados() > input.getLugaresReservados()) throw new ReglaNegocioException("Lugares Reservados supera la cantidad de lugares totales");
        //MAPEO
        Viaje viaje =this.repo.save(v);

        //Respuesta
        return   modelMapper.map(viaje,ViajeDto.class);
    }
}
