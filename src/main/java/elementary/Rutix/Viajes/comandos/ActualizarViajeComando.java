package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConCuentaHabilitadaReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConVehiculoHabilitadoReglaNegocio;
import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.Ubicaciones.dominio.Ciudad;
import elementary.Rutix.Ubicaciones.repositorios.CiudadRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ActualizarViajeComando implements Comando<ViajeDto, ViajeDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;
    private CiudadRepository repoCiudad;

    //Reglas de negocio
    private PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio;
    private PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio;

    public ActualizarViajeComando(ModelMapper modelMapper, ViajeRepository repo,
                                  CiudadRepository repoCiudad,
                                  PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio,
                                  PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoCiudad = repoCiudad;
        this.perfilConVehiculoHabilitadoReglaNegocio = perfilConVehiculoHabilitadoReglaNegocio;
        this.perfilConCuentaHabilitadaReglaNegocio = perfilConCuentaHabilitadaReglaNegocio;
    }

    @Override
    public ViajeDto execute(ViajeDto input) {
        //validaciones
        this.perfilConCuentaHabilitadaReglaNegocio.aplicar(input.getConductor().getId());
        this.perfilConVehiculoHabilitadoReglaNegocio.aplicar(input.getConductor().getId());
        Viaje v = modelMapper.map(input, Viaje.class);

        // Las ciudades se resuelven a mano: ModelMapper no puede convertir el
        // ciudadPartidaId/ciudadDestinoId (Long) del dto en la entidad Ciudad.
        Ciudad ciudadPartida = repoCiudad.findById(input.getCiudadPartidaId())
                .orElseThrow(() -> new ReglaNegocioException("La ciudad de partida no existe"));
        Ciudad ciudadDestino = repoCiudad.findById(input.getCiudadDestinoId())
                .orElseThrow(() -> new ReglaNegocioException("La ciudad de destino no existe"));
        v.setCiudadPartida(ciudadPartida);
        v.setCiudadDestino(ciudadDestino);

        //MAPEO
        Viaje viaje =this.repo.save(v);

        //Respuesta
        return   modelMapper.map(viaje,ViajeDto.class);
    }
}
