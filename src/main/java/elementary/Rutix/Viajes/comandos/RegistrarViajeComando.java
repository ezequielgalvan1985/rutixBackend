package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConCuentaHabilitadaReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilConVehiculoHabilitadoReglaNegocio;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.dto.RegistrarViajeRequestComandoDto;
import elementary.Rutix.Viajes.dto.ViajeResumidoDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class RegistrarViajeComando implements Comando<RegistrarViajeRequestComandoDto, ViajeResumidoDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repo;
    private PerfilRutixRepository repoPerfil;
    //Reglas de negocio
    private PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio;
    private PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio;

    public RegistrarViajeComando(ModelMapper modelMapper,
                                 ViajeRepository repo,
                                 PerfilRutixRepository repoPerfil,
                                 PerfilConVehiculoHabilitadoReglaNegocio perfilConVehiculoHabilitadoReglaNegocio,
                                 PerfilConCuentaHabilitadaReglaNegocio perfilConCuentaHabilitadaReglaNegocio) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoPerfil = repoPerfil;
        this.perfilConVehiculoHabilitadoReglaNegocio = perfilConVehiculoHabilitadoReglaNegocio;
        this.perfilConCuentaHabilitadaReglaNegocio = perfilConCuentaHabilitadaReglaNegocio;
    }

    @Override
    public ViajeResumidoDto execute(RegistrarViajeRequestComandoDto input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix p = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        //MAPEO
        Viaje v = new Viaje();
        v.setFechaSalida(input.getFechaSalida());
        v.setHoraSalida(input.getHoraSalida());
        v.setHoraLlegada(input.getHoraLlegada());
        v.setCiudadPartida(input.getCiudadPartida());
        v.setCiudadDestino(input.getCiudadDestino());
        v.setConductor(p);
        Vehiculo vx = p.getListaVehiculos().stream()
                .filter(ve -> ve.getId().equals(input.getVehiculoId()))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException("El vehículo no pertenece al perfil"));

        v.setVehiculo(vx);
        v.setAsientos(input.getAsientos());
        v.setPagaSenia(input.getPagaSenia());
        v.setPorcentajeSenia(input.getPorcentajeSenia());
        v.setValor(input.getValor());
        v.setEstado(EstadoViajeEnum.valueOf("CREADO"));
        v.setAsientosDisponibles(v.getAsientos());
        v.setAsientosReservados(0);
        Viaje viaje = this.repo.save(v);

        //Respuesta
        return   modelMapper.map(viaje, ViajeResumidoDto.class);
    }

}
