package cl.bibliotecaam.resenia.msresenia.controller;

import cl.bibliotecaam.resenia.msresenia.assembler.ReseniaModelAssembler;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.service.ReseniaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/bibliotecaam/resenias")
@RequiredArgsConstructor
@Tag(name = "Resenias", description = "Operaciones asociadas a resenias.")
public class ReseniaController {
    private final ReseniaService reseniaService;

    @Autowired
    private ReseniaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las resenias", description = "Obtiene una lista de todas las resenias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReseniaResponseDTO>>> obtenerTodas(){
        List<EntityModel<ReseniaResponseDTO>> asistencias = reseniaService.listarTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(asistencias,
                linkTo(methodOn(ReseniaController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener resenias por id", description = "Obtiene una resenia acorde a un id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<EntityModel<ReseniaResponseDTO>> obtenerPorId(@PathVariable Long id){
        return reseniaService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/puntaje/{puntaje}")
    @Operation(summary = "Obtener resenias por puntaje", description = "Obtiene una resenia acorde a un puntaje.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReseniaResponseDTO>>> obtenerPorPuntaje(@PathVariable Long puntaje){
        List<EntityModel<ReseniaResponseDTO>> resenias = reseniaService.listarPorUsuario(puntaje).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resenias,
                linkTo(methodOn(ReseniaController.class).obtenerPorPuntaje(puntaje)).withSelfRel()));
    }

    @GetMapping("/fecha/{fecha}")
    @Operation(summary = "Obtener resenias por fecha", description = "Obtiene una resenia acorde a una fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReseniaResponseDTO>>> obtenerPorFecha(@PathVariable LocalDate fecha){
        List<EntityModel<ReseniaResponseDTO>> resenias = reseniaService.listarPorFecha(fecha).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resenias,
                linkTo(methodOn(ReseniaController.class).obtenerPorFecha(fecha)).withSelfRel()
        ));
    }
    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener resenias por usuario", description = "Obtiene una resenia acorde a un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Resenia no encontrada")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReseniaResponseDTO>>> obtenerPorUsuario(@PathVariable Long id){
        List<EntityModel<ReseniaResponseDTO>> resenias = reseniaService.listarPorUsuario(id).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resenias,
                linkTo(methodOn(ReseniaController.class).obtenerPorUsuario(id)).withSelfRel()
        ));
    }

    @PostMapping
    @Operation(summary = "Guardar una resenia", description = "Guarda una resenia acorde a lo ingresado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa."),
            @ApiResponse(responseCode = "400", description = "Error al ingresar parametros. Revise si ingreso todos los parametros solicitados."),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para hacer el cambio.")
    })
    public ResponseEntity<EntityModel<ReseniaResponseDTO>> guardar(@Valid @RequestBody ReseniaRequestDTO doto){
        ReseniaResponseDTO nuevaResenia = reseniaService.guardar(doto);
        return ResponseEntity.status(201).body(assembler.toModel(nuevaResenia));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar resenia", description = "Actualiza una resenia acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resenia actualizada",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Resenia.class))),
            @ApiResponse(responseCode = "404", description = "El id de la resenia no existe.")
    })
    public ResponseEntity<EntityModel<ReseniaResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ReseniaRequestDTO doto){
        return reseniaService.actualizar(id, doto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar resenia", description = "Elimina una resenia acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "¡Resenia eliminada con exito!"),
            @ApiResponse(responseCode = "404",description = "ERROR: ¡El id de la resenia ingresada no existe!")
    })
    public ResponseEntity<Void> eliminar (@PathVariable Long id){
        if (reseniaService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        reseniaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
