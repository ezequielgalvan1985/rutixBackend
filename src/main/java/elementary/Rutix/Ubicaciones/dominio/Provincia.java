package elementary.Rutix.Ubicaciones.dominio;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "provincias")
public class Provincia {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre;

    // Id oficial de Georef (apis.datos.gob.ar/georef), para poder re-sincronizar o
    // ampliar el seed despues sin generar duplicados. Nulo en filas cargadas a mano.
    @Column(length = 20)
    private String idGeoref;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;

    @OneToMany(mappedBy = "provincia", fetch = FetchType.LAZY)
    private List<Ciudad> ciudades = new ArrayList<>();
}
