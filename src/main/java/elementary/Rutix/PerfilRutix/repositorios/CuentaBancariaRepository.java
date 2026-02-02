package elementary.Rutix.PerfilRutix.repositorios;

import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria,Long> {
    @Query(value = "SELECT m FROM CuentaBancaria m WHERE m.id > :offset ORDER BY m.id DESC")
    List<CuentaBancaria> findAll(@Param("offset") Long offset, Pageable pageable);

    @Query(value="select case when count(1) > 0 then true else false end from CuentaBancaria m where m.cbu = :cbu and m.id <> :id")
    Boolean existsByCbu(@Param("cbu") String cbu, @Param("id") Long id);

    boolean existsByCbuAndIdNot(String cbu, Long id);
}
