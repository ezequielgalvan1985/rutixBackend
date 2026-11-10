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
@Table(name = "paises")
public class Pais {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String nombre;

    // Codigo ISO 3166-1 alpha-2 (ej: "AR"), por si en el futuro se suman paises limitrofes
    @Column(length = 2)
    private String codigoIso;

    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    private List<Provincia> provincias = new ArrayList<>();
}
