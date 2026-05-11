package cl.bibliotecaam.resenia.msresenia.controller;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import cl.bibliotecaam.resenia.msresenia.service.ReseniaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bibliotecaam/resenias")
@RequiredArgsConstructor
public class ReseniaController {
    private final ReseniaService reseniaService;

    @GetMapping
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerTodas(){
        return ResponseEntity.ok(reseniaService.listarTodas());
    }

    @GetMapping("/puntaje/{puntaje}")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorPuntaje(@PathVariable Long puntaje){
        return ResponseEntity.ok((reseniaService.listarPorPuntaje(puntaje)));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorFecha(@PathVariable LocalDate fecha){
        return ResponseEntity.ok(reseniaService.listarPorFecha(fecha));
    }
    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorIdUsuario(@PathVariable Long id){
        return ResponseEntity.ok(reseniaService.listarPorUsuario(id));
    }

    @PostMapping
    public ResponseEntity<Resenia> guardar(@Valid @RequestBody Resenia resenia){
        Resenia nuevaResenia = reseniaService.guardar((resenia));
        return ResponseEntity.status(201).body(reseniaService.guardar(nuevaResenia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReseniaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReseniaRequestDTO doto){
        return reseniaService.actualizar(id, doto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if (reseniaService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        reseniaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
