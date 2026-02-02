package elementary.Rutix.Viajes.comandos.reservas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarReservaEnViajeComando implements Comando<ReservaDto, ViajeDto> {

    private ModelMapper modelMapper;
    private ViajeRepository repoViaje;
    private PerfilExistenteReglaNegocio perfilExistenteReglaNegocio;
    public RegistrarReservaEnViajeComando(ModelMapper modelMapper,
                                          ViajeRepository repoViaje,
                                          PerfilExistenteReglaNegocio perfilExistenteReglaNegocio) {
        this.modelMapper = modelMapper;
        this.repoViaje = repoViaje;
        this.perfilExistenteReglaNegocio = perfilExistenteReglaNegocio;
    }

    @Override
    public ViajeDto execute(ReservaDto value) {
        Viaje v = this.repoViaje.findById(value.getViajeId()).orElseThrow(()->new ReglaNegocioException("Viaje Inexistente"));
        if (v.getLugaresDisponibles() < value.getCantidadLugares() ) throw new ReglaNegocioException("No hay lugares disponibles");
        PerfilRutix pasajero = perfilExistenteReglaNegocio.aplicar(value.getPasajeroId());

        Reserva r = Reserva.builder()
                .pasajero(pasajero)
                .viaje(v)
                .cantidadLugares(value.getCantidadLugares())
                .estado(value.getEstado())
                .build();

        v.registrarReserva(r);
        repoViaje.save(v);
        return this.modelMapper.map(v, ViajeDto.class);
    }
}
