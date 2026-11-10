package elementary.Rutix.Viajes.repositorios;

import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dominio.Viaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {

    Page<Reserva> findAll(Pageable pageable);

    Page<Reserva> findByPasajeroIdOrderByIdDesc(Long pasajeroId, Pageable pageable);

    // Puede haber mas de una Reserva para el mismo pasajero+viaje (p.ej. una
    // RECHAZADA/CANCELADA vieja y una PENDIENTE nueva tras volver a
    // reservar), asi que esto ya no puede ser un Optional de resultado
    // unico: devuelve todas, ordenadas de la mas nueva a la mas vieja.
    List<Reserva> findByPasajeroIdAndViajeIdOrderByIdDesc(Long pasajeroId, Long viajeId);
}
