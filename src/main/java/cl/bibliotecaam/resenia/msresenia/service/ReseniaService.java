package cl.bibliotecaam.resenia.msresenia.service;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
                resenia.getIdUsuario()
        );
    }

    public Optional<ReseniaResponseDTO> obtenerPorId(Long id){
        return reseniaRepository.findById(id).map(this::mapToDTO);
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

    public List<ReseniaResponseDTO> listarPorUsuario(Long id){
        return reseniaRepository.findByIdUsuario(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Resenia guardar(Resenia resenia){
        return reseniaRepository.save(resenia);
    }

    public void eliminarPorId(Long id){
        reseniaRepository.deleteById(id);
    }

    public Optional<ReseniaResponseDTO> actualizar(Long id, ReseniaRequestDTO doto){
        return reseniaRepository.findById(id).map(existente -> {
            existente.setPuntaje(doto.getPuntaje());
            existente.setComentario(doto.getComentario());
            existente.setFechaRese(doto.getFechaRese());
            existente.setIdUsuario(doto.getIdUsuario());
            return mapToDTO(existente);
        });
    }
}
