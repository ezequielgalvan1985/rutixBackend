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

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {

    Page<Reserva> findAll(Pageable pageable);

    Page<Reserva> findByPasajeroIdOrderByIdDesc(Long pasajeroId, Pageable pageable);
}
