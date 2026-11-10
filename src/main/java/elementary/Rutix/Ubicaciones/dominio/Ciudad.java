package elementary.Rutix.Ubicaciones.dominio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ciudades")
public class Ciudad {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 150, nullable = false)
    private String nombre;

    // Id oficial de Georef, mismo motivo que en Provincia.idGeoref
    @Column(length = 20)
    private String idGeoref;

    private Double latitud;
    private Double longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;
}
