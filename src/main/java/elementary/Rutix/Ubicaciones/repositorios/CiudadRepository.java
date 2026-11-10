package elementary.Rutix.Ubicaciones.repositorios;

import elementary.Rutix.Ubicaciones.dominio.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Optional<Ciudad> findByIdGeoref(String idGeoref);

    // Autocomplete: prioriza coincidencias que empiezan con el texto buscado sobre
    // las que solo lo contienen en el medio. JOIN FETCH de provincia+pais para
    // armar "Ciudad, Provincia, Pais" sin N+1 (son pocos resultados, pero total
    // sentido evitarlo ya que esta query se llama en cada tecla que se tipea).
    @Query("""
        SELECT c FROM Ciudad c
        JOIN FETCH c.provincia p
        JOIN FETCH p.pais
        WHERE UPPER(c.nombre) LIKE UPPER(CONCAT(:texto, '%'))
           OR UPPER(c.nombre) LIKE UPPER(CONCAT('%', :texto, '%'))
        ORDER BY
           CASE WHEN UPPER(c.nombre) LIKE UPPER(CONCAT(:texto, '%')) THEN 0 ELSE 1 END,
           c.nombre ASC
    """)
    List<Ciudad> buscarPorTexto(@Param("texto") String texto);
}
