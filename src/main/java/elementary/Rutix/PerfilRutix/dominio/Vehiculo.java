package elementary.Rutix.PerfilRutix.dominio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehiculos")
public class Vehiculo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 50)
    private String marca;

    @Column(length = 100)
    private String modelo;

    @Column(length = 10)
    private String patente;

    @Column(length = 4)
    private String anio;

    @Column(nullable = false)
    private Boolean tieneAire;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne()
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilRutix perfil;

    private Integer asientos;

}
