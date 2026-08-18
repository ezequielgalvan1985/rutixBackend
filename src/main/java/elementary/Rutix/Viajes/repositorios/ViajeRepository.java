package elementary.Rutix.Viajes.repositorios;

import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
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
    WHERE (
        (:fechaSalida IS NULL AND m.fechaSalida >= CURRENT_DATE)
        OR m.fechaSalida = :fechaSalida
    )
    AND ((:ciudadPartida IS NULL or :ciudadPartida = '')OR m.ciudadPartida = :ciudadPartida )
    AND ((:ciudadDestino IS NULL or :ciudadDestino = '') OR m.ciudadDestino = :ciudadDestino)
    AND (:estado IS NULL OR m.estado = :estado)
""")
    Page<Viaje> buscarViajes(
            @Param("fechaSalida") LocalDate fechaSalida,
            @Param("ciudadPartida") String ciudadPartida,
            @Param("ciudadDestino") String ciudadDestino,
            @Param("estado") EstadoViajeEnum estado,
            Pageable pageable);

    Page<Viaje> findByConductorIdOrderByIdDesc(Long conductorId, Pageable pageable);
}
