package cl.bibliotecaam.resenia.msresenia.service;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReseniaService {
    private final ReseniaRepository reseniaRepository;

    private ReseniaResponseDTO mapToDTO(Resenia resenia){
        return new ReseniaResponseDTO(
                resenia.getIdResenia(),
                resenia.getPuntaje(),
                resenia.getComentario(),
                resenia.getFechaRese(),
                resenia.getUsuario().getId()
        );
    }

    public List<ReseniaResponseDTO> listarTodas(){
        return reseniaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReseniaResponseDTO> listarPorPuntaje(Long puntaje){
        return reseniaRepository.findByPuntaje(puntaje)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReseniaResponseDTO> listarPorFecha(LocalDate fechaRese){
        return reseniaRepository.findByFechaRese(fechaRese)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ReseniaResponseDTO> listarPorUsuario(Usuario usuario){
        return reseniaRepository.findByUsuario(usuario)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
