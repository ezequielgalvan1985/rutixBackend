package elementary.Rutix.Viajes.comandos.pagos;

import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarPagoDeReservaComando implements Comando<PagoDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ReservaRepository repo;
    public RegistrarPagoDeReservaComando(ModelMapper modelMapper,
                                         ReservaRepository repo
                                          ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public ReservaDto execute(PagoDto value) {
        Reserva r = this.repo.findById(value.getReservaId()).orElseThrow(()->new ReglaNegocioException("NO existe reserva"));
        Pago p = modelMapper.map(value,Pago.class);
        r.registrarPago(p);
        r = repo.save(r);
        return modelMapper.map(r, ReservaDto.class);

    }
}
