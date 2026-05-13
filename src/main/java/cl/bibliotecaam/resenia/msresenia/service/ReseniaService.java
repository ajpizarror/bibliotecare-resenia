package cl.bibliotecaam.resenia.msresenia.service;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReseniaService {
    private final ReseniaRepository reseniaRepository;
    private final WebClient webClient;

    private ReseniaResponseDTO mapToDTO(Resenia resenia){
        return new ReseniaResponseDTO(
                resenia.getIdResenia(),
                resenia.getPuntaje(),
                resenia.getComentario(),
                resenia.getFechaRese(),
                resenia.getIdUsuario()
        );
    }

    private void validarUsuario(Long usuarioId){
        try{
            webClient.get()
                    .uri("/api/bibliotecaam/usuarios/id/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Usuario {usuarioId} validado correctamente (WebClient)");
        } catch (WebClientResponseException.NotFound e){
            throw new RuntimeException(
                    "El usuario con id" + usuarioId + "no existe en Usuario");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Usuario: " + e.getMessage());
        }

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
