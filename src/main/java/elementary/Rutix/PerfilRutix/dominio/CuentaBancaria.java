package elementary.Rutix.PerfilRutix.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaBancaria {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(length = 50)
    private String nombreTitular;

    @Pattern(regexp = "\\d{22}")
    @Column(length = 22, unique = true)
    private String cbu;

    @Column(length = 100)
    private String alias;

    @Column(length = 50)
    private String numero;

    @Column(length = 50)
    private String nombreEntidad;

    private Integer codigoEntidad;

    @Column(updatable = false)
    private LocalDateTime fechaAlta;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne()
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilRutix perfil;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }
}
