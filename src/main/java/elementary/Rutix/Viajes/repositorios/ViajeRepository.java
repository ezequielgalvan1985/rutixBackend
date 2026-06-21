package elementary.Rutix.Viajes.repositorios;

import elementary.Rutix.Viajes.dominio.Viaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje,Long> {
    @Query(value = "SELECT m FROM Viaje m WHERE m.id > :offset ORDER BY m.id DESC")
    List<Viaje> findAll(@Param("offset") Long offset, Pageable pageable);

    @Query("""
       SELECT m FROM Viaje m
       WHERE ((:fechaSalida IS NULL AND m.fechaSalida = CURRENT_DATE) OR m.fechaSalida = :fechaSalida)
       AND ((:ciudadPartida IS NULL OR TRIM(:ciudadPartida)='') OR m.ciudadPartida = :ciudadPartida)
       AND ((:ciudadDestino IS NULL OR TRIM(:ciudadDestino)='') OR  m.ciudadDestino = :ciudadDestino)
       """)
    Page<Viaje> buscarViajes(
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("ciudadPartida") String ciudadPartida,
            @Param("ciudadDestino") String ciudadDestino,
            Pageable pageable);

    Page<Viaje> findByConductorIdOrderByIdDesc(Long conductorId, Pageable pageable);
}
