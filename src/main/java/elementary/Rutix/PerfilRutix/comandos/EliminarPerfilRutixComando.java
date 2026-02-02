package elementary.Rutix.PerfilRutix.comandos;

import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EliminarPerfilRutixComando implements Comando<Long, Void> {

    @Autowired
    private PerfilRutixRepository repo;


    @Override
    public Void execute(Long input) {
        repo.deleteById(input);
        return null;
    }

}
