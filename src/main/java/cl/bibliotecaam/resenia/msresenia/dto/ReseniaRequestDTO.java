package cl.bibliotecaam.resenia.msresenia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaRequestDTO {

    @Min(value = 0, message = "El minimo puntaje que se puede ingresar es de 0")
    @Max(value = 100, message = "El maximo puntaje que se le puede entregar como puntaje es 100")
    @NotNull(message = "El puntaje es obligatorio.")
    private Long puntaje;

    @NotBlank(message = "El comentario es obligatorio.")
    private String comentario;

    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate fechaRese;

    @NotNull(message = "La resenia tuvo que haber sido hecha por un usuario, el id del usuario es indispensable.")
    private Long idUsuario;

    @NotNull(message = "El id del libro es necesario: ¿Como vas a saber si no a que libro le hizo la reseña?")
    private Long idLibro;
}
