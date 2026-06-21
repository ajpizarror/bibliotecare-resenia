package cl.bibliotecaam.resenia.msresenia;

import cl.bibliotecaam.resenia.msresenia.assembler.ReseniaModelAssembler;
import cl.bibliotecaam.resenia.msresenia.controller.ReseniaController;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.service.ReseniaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReseniaController.class)
@ActiveProfiles("test")
@Import(ReseniaModelAssembler.class)
@DisplayName("Tests Unitarios - ReseniaController")
class ReseniaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private ReseniaService reseniaService;

    @Test
    @DisplayName("GIVEN: Existen reseñas WHEN: GET /api/bibliotecaam/resenia THEN: Retorna 200 OK y la lista")
    void shouldReturnTodasLasResenias() throws Exception {
        ReseniaResponseDTO res1 = new ReseniaResponseDTO(1L, 85L, "Excelente libro", LocalDate.now(), 1L, 10L);
        ReseniaResponseDTO res2 = new ReseniaResponseDTO(2L, 40L, "Regular", LocalDate.now(), 2L, 20L);

        List<ReseniaResponseDTO> lista = Arrays.asList(res1, res2);

        Mockito.when(reseniaService.listarTodas()).thenReturn(lista);

        mockMvc.perform(get("/api/bibliotecaam/resenia")
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reseniaResponseDTOList.length()").value(2))
                .andExpect(jsonPath("$._embedded.reseniaResponseDTOList[0].id_resenia").value(1L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/bibliotecaam/resenia/{id} THEN: Retorna 200 OK y el DTO")
    void shouldReturnReseniaById() throws Exception {
        Long id = 1L;
        ReseniaResponseDTO mockResponse = new ReseniaResponseDTO(id, 90L, "Muy buen libro", LocalDate.now(), 1L, 10L);

        Mockito.when(reseniaService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/bibliotecaam/resenia/{id}", id)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_resenia").value(id))
                .andExpect(jsonPath("$.comentario").value("Muy buen libro"))
                .andExpect(jsonPath("$.puntaje").value(90L));
    }

    @Test
    @DisplayName("GIVEN: ID inexistente WHEN: GET /api/bibliotecaam/resenia/{id} THEN: Retorna 404 Not Found")
    void shouldReturnNotFoundWhenReseniaDoesNotExist() throws Exception {
        Long id = 99L;
        Mockito.when(reseniaService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bibliotecaam/resenia/{id}", id)
                        .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN: Request válido WHEN: POST /api/bibliotecaam/resenia THEN: Retorna 201 Created")
    void shouldCreateResenia() throws Exception {
        ReseniaRequestDTO request = new ReseniaRequestDTO(85L, "Increíble", LocalDate.of(2026, 6, 21), 1L, 10L);
        ReseniaResponseDTO mockResponse = new ReseniaResponseDTO(1L, 85L, "Increíble", LocalDate.of(2026, 6, 21), 1L, 10L);

        Mockito.when(reseniaService.guardar(any(ReseniaRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/bibliotecaam/resenia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_resenia").value(1L))
                .andExpect(jsonPath("$.comentario").value("Increíble"))
                .andExpect(jsonPath("$.fechaRese").value("2026-06-21"));
    }

    @Test
    @DisplayName("GIVEN: Request inválido (Puntaje > 100) WHEN: POST /api/bibliotecaam/resenia THEN: Retorna 400 Bad Request")
    void shouldReturnBadRequestWhenPuntajeIsInvalid() throws Exception {
        // Puntaje de 105 excede el máximo de @Max(100)
        ReseniaRequestDTO request = new ReseniaRequestDTO(105L, "Comentario válido", LocalDate.now(), 1L, 10L);

        mockMvc.perform(post("/api/bibliotecaam/resenia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GIVEN: ID y Request válido WHEN: PUT /api/bibliotecaam/resenia/{id} THEN: Retorna 200 OK")
    void shouldUpdateResenia() throws Exception {
        Long id = 1L;
        ReseniaRequestDTO request = new ReseniaRequestDTO(70L, "Editado", LocalDate.now(), 1L, 10L);
        ReseniaResponseDTO mockResponse = new ReseniaResponseDTO(id, 70L, "Editado", LocalDate.now(), 1L, 10L);

        Mockito.when(reseniaService.actualizar(eq(id), any(ReseniaRequestDTO.class))).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(put("/api/bibliotecaam/resenia/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Editado"))
                .andExpect(jsonPath("$.puntaje").value(70L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: DELETE /api/bibliotecaam/resenia/{id} THEN: Retorna 204 No Content")
    void shouldDeleteResenia() throws Exception {
        Long id = 1L;
        ReseniaResponseDTO mockResponse = new ReseniaResponseDTO(id, 50L, "Borrar", LocalDate.now(), 1L, 10L);

        Mockito.when(reseniaService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));
        Mockito.doNothing().when(reseniaService).eliminarPorId(id);

        mockMvc.perform(delete("/api/bibliotecaam/resenia/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GIVEN: ID inexistente WHEN: DELETE /api/bibliotecaam/resenia/{id} THEN: Retorna 404 Not Found")
    void shouldReturnNotFoundWhenDeletingNonExistentResenia() throws Exception {
        Long id = 99L;
        Mockito.when(reseniaService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bibliotecaam/resenia/{id}", id))
                .andExpect(status().isNotFound());
    }
}