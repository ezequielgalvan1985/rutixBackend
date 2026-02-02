package elementary.Rutix.Viajes.repositorios;

import elementary.Rutix.Viajes.dominio.Pago;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PagoRepository extends JpaRepository<Pago,Long> {
    @Query(value = "SELECT m FROM Viaje m WHERE m.id > :offset ORDER BY m.id DESC")
    List<Pago> findAll(@Param("offset") Long offset, Pageable pageable);

}
