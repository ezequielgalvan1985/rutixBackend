package elementary.Rutix.Viajes.comandos.pagos;

import elementary.Rutix.Viajes.comandos.dto.EliminarPagoDeReservaComandoDto;
import elementary.Rutix.Viajes.comandos.dto.EliminarReservaDeViajeComandoDto;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminarPagoDeReservaComando implements Comando<EliminarPagoDeReservaComandoDto, Void> {
    @Autowired
    private ReservaRepository repo;

    @Override
    public Void execute(EliminarPagoDeReservaComandoDto value) {
        Reserva r = this.repo.findById(value.getReservaId()).orElseThrow(()->new ReglaNegocioException("NO existe reserva"));
        r.eliminarPago(value.getId());
        return null;
    }
}
