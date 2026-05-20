package cl.bibliotecaam.resenia.msresenia.service;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
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
    private final WebClient webClientUsuario;

    private final WebClient webClientLibro;

    private ReseniaResponseDTO mapToDTO(Resenia resenia){
        return new ReseniaResponseDTO(
                resenia.getIdResenia(),
                resenia.getPuntaje(),
                resenia.getComentario(),
                resenia.getFechaRese(),
                resenia.getIdUsuario(),
                resenia.getIdLibro()
        );
    }

    private void validarUsuario(Long idUsuario){
        try{
            webClientUsuario.get()
                    .uri("/api/bibliotecaam/usuario/{id}", idUsuario)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Usuario {} validado correctamente (WebClient)",idUsuario);
        } catch (WebClientResponseException.NotFound e){
            throw new RuntimeException(
                    "El usuario con id "+ idUsuario +" no existe en Usuario");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Usuario: " + e.getMessage());
        }

    }

    private void validarLibro(Long idLibro){
        try{
            webClientLibro.get()
                    .uri("/api/bibliotecaam/libro/{id}", idLibro)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Libro {} validado correctamente (WebClient)",idLibro);
        } catch (WebClientResponseException.NotFound e){
            throw new RuntimeException(
                    "El libro con id "+ idLibro+" no existe en la Base de Datos de Libro");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Libro: " + e.getMessage());
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

    public ReseniaResponseDTO guardar(ReseniaRequestDTO doto){
        validarUsuario(doto.getIdUsuario());
        validarLibro(doto.getIdLibro());
        Resenia resenia = new Resenia(
                null,
                doto.getPuntaje(),
                doto.getComentario(),
                doto.getFechaRese(),
                doto.getIdUsuario(),
                doto.getIdLibro()
        );
        return mapToDTO(reseniaRepository.save(resenia));
    }

    public void eliminarPorId(Long id){
        reseniaRepository.deleteById(id);
    }

    public Optional<ReseniaResponseDTO> actualizar(Long id, ReseniaRequestDTO doto){
        return reseniaRepository.findById(id).map(existente -> {
            validarUsuario(doto.getIdUsuario());
            validarLibro(doto.getIdLibro());
            existente.setPuntaje(doto.getPuntaje());
            existente.setComentario(doto.getComentario());
            existente.setFechaRese(doto.getFechaRese());
            existente.setIdUsuario(doto.getIdUsuario());
            existente.setIdLibro(doto.getIdLibro());
            return mapToDTO(reseniaRepository.save(existente));
        });
    }
}
