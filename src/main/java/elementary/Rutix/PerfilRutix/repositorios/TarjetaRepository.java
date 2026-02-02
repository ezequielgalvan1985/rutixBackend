package elementary.Rutix.PerfilRutix.repositorios;

import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    @Query(value = "SELECT m FROM Tarjeta m WHERE m.id > :offset ORDER BY m.id DESC")
    List<Tarjeta> findAll(@Param("offset") Long offset, Pageable pageable);

    boolean existsByToken(String token);
    boolean existsByTokenAndIdNot(String token, Long id);
}
