package elementary.Rutix.Viajes.comandos;

import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

@Service
public class EliminarViajeComando implements Comando<Long,Void> {

    private ViajeRepository repo;

    public EliminarViajeComando(ViajeRepository repo
    ) {
        this.repo = repo;
    }
    @Override
    public Void execute(Long value) {
        this.repo.deleteById(value);
        return null;
    }
}
