package elementary.Rutix.Ubicaciones.repositorios;

import elementary.Rutix.Ubicaciones.dominio.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByIdGeoref(String idGeoref);
    List<Provincia> findByPaisId(Long paisId);
}
