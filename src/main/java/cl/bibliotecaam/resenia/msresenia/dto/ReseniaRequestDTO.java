package cl.bibliotecaam.resenia.msresenia.dto;

import cl.bibliotecaam.resenia.msresenia.model.Usuario;
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
    @NotNull(message = "El puntaje es obligatorio.")
    private Long puntaje;
    @NotBlank(message = "El comentario es obligatorio.")
    private String comentario;
    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate fechaRese;
    @NotNull(message = "La resenia tuvo que haber sido hecha por un usuario.")
    private Long idUsuario;


}
