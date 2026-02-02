package elementary.Rutix.PerfilRutix.dominio;

import elementary.Rutix.common.MarcaTarjeta;
import elementary.Rutix.common.TipoTarjeta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarjetas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarjeta {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarjeta tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarcaTarjeta marca;

    @Column(name = "ultimos_4", length = 4, nullable = false)
    private String ultimos4Digitos;

    @Column(length = 100, nullable = false)
    private String titular;

    @Column(name = "vencimiento_mes", nullable = false)
    private Integer mesVencimiento;

    @Column(name = "vencimiento_anio", nullable = false)
    private Integer anioVencimiento;

    // Token devuelto por MercadoPago / Stripe / etc
    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Boolean activa = true;

    @ManyToOne()
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilRutix perfil;

    @Column(name = "fecha_alta", updatable = false)
    private LocalDateTime fechaAlta;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now();
    }
}

