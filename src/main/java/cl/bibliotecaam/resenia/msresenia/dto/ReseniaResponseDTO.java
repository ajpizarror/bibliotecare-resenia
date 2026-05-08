package cl.bibliotecaam.resenia.msresenia.dto;

import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseniaResponseDTO {
    private Long id_resenia;
    private Long puntaje;
    private String comentario;
    private LocalDate fechaRese;
    private Long idUsuario;

}
