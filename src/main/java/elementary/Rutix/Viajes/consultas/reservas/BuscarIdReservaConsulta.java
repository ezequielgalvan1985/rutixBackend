package elementary.Rutix.Viajes.consultas.reservas;

import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Consulta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BuscarIdReservaConsulta implements Consulta<Long, ReservaDto> {
    private ModelMapper modelMapper;
    private ReservaRepository repo;

    public BuscarIdReservaConsulta(ModelMapper modelMapper, ReservaRepository repo) {
        this.modelMapper = modelMapper;
        this.repo = repo;
    }

    @Override
    public ReservaDto execute(Long value) {
        Reserva r = repo.findById(value).orElseThrow(()->new ReglaNegocioException("Reserva Inexistente"));
        return modelMapper.map(r,ReservaDto.class);
    }
}
