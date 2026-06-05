package cl.bibliotecaam.resenia.msresenia.controller;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.service.ReseniaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecaam/resenias")
@RequiredArgsConstructor
@Tag(name = "Resenias", description = "Operaciones asociadas a resenias.")
public class ReseniaController {
    private final ReseniaService reseniaService;

    @GetMapping
    @Operation(summary = "Obtener todas las resenias", description = "Obtiene una lista de todas las resenias.")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerTodas(){
        return ResponseEntity.ok(reseniaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener resenias por id", description = "Obtiene una resenia acorde a un id.")
    public ResponseEntity<Optional<ReseniaResponseDTO>> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(reseniaService.obtenerPorId(id));
    }

    @GetMapping("/puntaje/{puntaje}")
    @Operation(summary = "Obtener resenias por puntaje", description = "Obtiene una resenia acorde a un puntaje.")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorPuntaje(@PathVariable Long puntaje){
        return ResponseEntity.ok((reseniaService.listarPorPuntaje(puntaje)));
    }

    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Obtener resenias por fecha", description = "Obtiene una resenia acorde a una fecha.")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorFecha(@PathVariable LocalDate fecha){
        return ResponseEntity.ok(reseniaService.listarPorFecha(fecha));
    }
    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener resenias por usuario", description = "Obtiene una resenia acorde a un usuario.")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorIdUsuario(@PathVariable Long id){
        return ResponseEntity.ok(reseniaService.listarPorUsuario(id));
    }

    @PostMapping
    @Operation(summary = "Guardar una resenia", description = "Guarda una resenia acorde a lo ingresado.")
    public ResponseEntity<ReseniaResponseDTO> guardar(@Valid @RequestBody ReseniaRequestDTO doto){
        return ResponseEntity.status(201).body(reseniaService.guardar(doto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar resenia", description = "Actualiza una resenia acorde a una id.")
    public ResponseEntity<ReseniaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReseniaRequestDTO doto){
        return reseniaService.actualizar(id, doto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar resenia", description = "Elimina una resenia acorde a una id.")
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if (reseniaService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        reseniaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
