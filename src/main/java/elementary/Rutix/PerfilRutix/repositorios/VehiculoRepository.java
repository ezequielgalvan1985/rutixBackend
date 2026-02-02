package elementary.Rutix.PerfilRutix.repositorios;

import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    @Query(value = "SELECT m FROM Vehiculo m WHERE m.id > :offset ORDER BY m.id DESC")
    List<Vehiculo> findAll(@Param("offset") Long offset, Pageable pageable);

}
