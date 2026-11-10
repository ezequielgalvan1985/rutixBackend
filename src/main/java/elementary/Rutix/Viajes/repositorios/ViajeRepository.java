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

    // Filtro de ciudad por FK (antes era comparacion de texto libre, fragil
    // ante mayusculas/acentos distintos). m.ciudadPartida.id navega a la
    // columna ciudad_partida_id sin necesidad de hacer join explicito.
    @Query("""
    SELECT m FROM Viaje m
    WHERE (
        (:fechaDesde IS NULL AND :fechaHasta IS NULL AND m.fechaSalida >= CURRENT_DATE)
        OR (:fechaDesde IS NOT NULL AND :fechaHasta IS NOT NULL AND m.fechaSalida BETWEEN :fechaDesde AND :fechaHasta)
        OR (:fechaDesde IS NOT NULL AND :fechaHasta IS NULL AND m.fechaSalida >= :fechaDesde)
        OR (:fechaDesde IS NULL AND :fechaHasta IS NOT NULL AND m.fechaSalida <= :fechaHasta)
    )
    AND (:ciudadPartidaId IS NULL OR m.ciudadPartida.id = :ciudadPartidaId)
    AND (:ciudadDestinoId IS NULL OR m.ciudadDestino.id = :ciudadDestinoId)
    AND (:estado IS NULL OR m.estado = :estado)
""")
    Page<Viaje> buscarViajes(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("ciudadPartidaId") Long ciudadPartidaId,
            @Param("ciudadDestinoId") Long ciudadDestinoId,
            @Param("estado") EstadoViajeEnum estado,
            Pageable pageable);

    Page<Viaje> findByConductorIdOrderByIdDesc(Long conductorId, Pageable pageable);
}
