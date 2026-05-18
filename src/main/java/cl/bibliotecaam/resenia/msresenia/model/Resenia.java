package cl.bibliotecaam.resenia.msresenia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "resenia")
public class Resenia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResenia;

    @Column(nullable = false, length = 3)
    private Long puntaje;

    @Column(nullable = false, length = 500)
    private String comentario;

    @Column(nullable = false)
    private LocalDate fechaRese;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private Long idLibro;
}
