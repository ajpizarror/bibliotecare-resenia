package cl.bibliotecaam.resenia.msresenia.service;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import cl.bibliotecaam.resenia.msresenia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReseniaService {
    private final ReseniaRepository reseniaRepository;
    private final UsuarioRepository usuarioRepository;

    private ReseniaResponseDTO mapToDTO(Resenia resenia){
        return new ReseniaResponseDTO(
                resenia.getIdResenia(),
                resenia.getPuntaje(),
                resenia.getFechaRese(),
                resenia.getComentario()
                resenia.getUsuario().getNumrunUsu()
        );
    }
}
