package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.Viajes.comandos.dto.DevolucionPagoDeReservaComandoDto;
import elementary.Rutix.Viajes.comandos.pagos.DevolucionPagoDeReservaComando;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.Enum.EstadoPagoEnum;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

/**
 * Cancela un viaje. La autorizacion (solo el Conductor del viaje o un ADMIN)
 * se resuelve de forma declarativa en ViajeController via @PreAuthorize +
 * ViajeSecurity.esConductor, mismo patron que delete() (cierra H15/H16).
 *
 * Al cancelar, Viaje.cancelar() cancela en cascada todas sus reservas activas
 * (PENDIENTE/CONFIRMADA/PAGADA). Si alguna reserva ya tenia un pago registrado
 * (PENDIENTE o CONFIRMADO), se dispara su devolucion via DevolucionPagoDeReservaComando.
 */
@Service
public class CancelarViajeComando implements Comando<Long, Void> {

    private final ViajeRepository repoViaje;
    private final DevolucionPagoDeReservaComando devolucionPago;

    public CancelarViajeComando(ViajeRepository repoViaje, DevolucionPagoDeReservaComando devolucionPago) {
        this.repoViaje = repoViaje;
        this.devolucionPago = devolucionPago;
    }

    @Override
    public Void execute(Long value) {
        Viaje v = this.repoViaje.findById(value)
                .orElseThrow(() -> new ReglaNegocioException("Viaje Inexistente"));

        v.cancelar();
        this.repoViaje.save(v);

        for (Reserva r : v.getListaReservas()) {
            for (Pago p : r.getListaPagos()) {
                if (p.getEstado() == EstadoPagoEnum.PENDIENTE || p.getEstado() == EstadoPagoEnum.CONFIRMADO) {
                    this.devolucionPago.execute(new DevolucionPagoDeReservaComandoDto(p.getId(), r.getId()));
                }
            }
        }

        return null;
    }
}
