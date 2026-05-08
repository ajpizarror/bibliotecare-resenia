package cl.bibliotecaam.resenia.msresenia.controller;

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
    public ResponseEntity<List<ReseniaResponseDTO>> obtenerPorUsuario(@RequestBody Usuario usuario){
        return ResponseEntity.ok(reseniaService.listarPorUsuario(usuario));
    }
}
