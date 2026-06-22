package cl.bibliotecaam.resenia.msresenia;

import cl.bibliotecaam.resenia.msresenia.dto.ReseniaRequestDTO;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import cl.bibliotecaam.resenia.msresenia.service.ReseniaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(classes = ReseniaService.class)
@ActiveProfiles("test")
@DisplayName("Tests Unitarios - ReseniaService")
class ReseniaServiceTest {

    @Autowired
    private ReseniaService reseniaService;

    @MockitoBean
    private ReseniaRepository reseniaRepository;

    @MockitoBean(name = "webClientUsuario")
    private WebClient webClientUsuario;

    @MockitoBean(name = "webClientLibro")
    private WebClient webClientLibro;

    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    void setUp() {
        requestHeadersUriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientSuccess(WebClient webClientMock, String uri, Object id) {
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri(eq(uri), eq(id))).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("OK"));
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientException(WebClient webClientMock, String uri, Object id, Throwable exception) {
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri(eq(uri), eq(id))).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.error(exception));
    }

    @Test
    @DisplayName("GIVEN: Existe reseña WHEN: obtenerPorId THEN: Retorna el DTO correspondiente")
    void shouldReturnReseniaById() {
        Long id = 1L;
        Resenia resenia = new Resenia(id, 80L, "Buen libro", LocalDate.now(), 2L, 3L);
        Mockito.when(reseniaRepository.findById(id)).thenReturn(Optional.of(resenia));

        Optional<ReseniaResponseDTO> resultado = reseniaService.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId_resenia());
        assertEquals("Buen libro", resultado.get().getComentario());
    }

    @Test
    @DisplayName("GIVEN: Existen reseñas WHEN: listarTodas THEN: Retorna la lista completa de DTOs")
    void shouldReturnAllResenias() {
        List<Resenia> mockList = Arrays.asList(
                new Resenia(1L, 90L, "Excelente", LocalDate.now(), 1L, 10L),
                new Resenia(2L, 50L, "Regular", LocalDate.now(), 2L, 20L)
        );
        Mockito.when(reseniaRepository.findAll()).thenReturn(mockList);

        List<ReseniaResponseDTO> resultado = reseniaService.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId_resenia());
    }

    @Test
    @DisplayName("GIVEN: Puntaje existente WHEN: listarPorPuntaje THEN: Retorna las reseñas filtradas")
    void shouldReturnReseniasByPuntaje() {
        Long puntaje = 100L;
        List<Resenia> mockList = Arrays.asList(new Resenia(1L, puntaje, "Perfecto", LocalDate.now(), 1L, 10L));
        Mockito.when(reseniaRepository.findByPuntaje(puntaje)).thenReturn(mockList);

        List<ReseniaResponseDTO> resultado = reseniaService.listarPorPuntaje(puntaje);

        assertEquals(1, resultado.size());
        assertEquals(puntaje, resultado.get(0).getPuntaje());
    }

    @Test
    @DisplayName("GIVEN: Fecha válida WHEN: listarPorFecha THEN: Retorna las reseñas de ese día")
    void shouldReturnReseniasByFecha() {
        LocalDate fecha = LocalDate.now();
        List<Resenia> mockList = Arrays.asList(new Resenia(1L, 70L, "Interesante", fecha, 1L, 10L));
        Mockito.when(reseniaRepository.findByFechaRese(fecha)).thenReturn(mockList);

        List<ReseniaResponseDTO> resultado = reseniaService.listarPorFecha(fecha);

        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFechaRese());
    }

    @Test
    @DisplayName("GIVEN: ID de usuario WHEN: listarPorUsuario THEN: Retorna las reseñas del usuario")
    void shouldReturnReseniasByUsuario() {
        Long idUsuario = 5L;
        List<Resenia> mockList = Arrays.asList(new Resenia(1L, 85L, "Genial", LocalDate.now(), idUsuario, 10L));
        Mockito.when(reseniaRepository.findByIdUsuario(idUsuario)).thenReturn(mockList);

        List<ReseniaResponseDTO> resultado = reseniaService.listarPorUsuario(idUsuario);

        assertEquals(1, resultado.size());
        assertEquals(idUsuario, resultado.get(0).getIdUsuario());
    }

    @Test
    @DisplayName("GIVEN: Request válido WHEN: guardar THEN: Valida remotamente y guarda la reseña")
    void shouldSaveReseniaSuccessfully() {
        ReseniaRequestDTO request = new ReseniaRequestDTO(85L, "Genial", LocalDate.now(), 1L, 10L);
        Resenia reseniaGuardada = new Resenia(100L, 85L, "Genial", LocalDate.now(), 1L, 10L);

        mockWebClientSuccess(webClientUsuario, "/api/bibliotecaam/usuario/{id}", 1L);
        mockWebClientSuccess(webClientLibro, "/api/bibliotecaam/libro/{id}", 10L);
        Mockito.when(reseniaRepository.save(any(Resenia.class))).thenReturn(reseniaGuardada);

        ReseniaResponseDTO resultado = reseniaService.guardar(request);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId_resenia());
    }

    @Test
    @DisplayName("GIVEN: Usuario inexistente WHEN: guardar THEN: Lanza RuntimeException")
    void shouldThrowExceptionWhenUsuarioNotFound() {
        ReseniaRequestDTO request = new ReseniaRequestDTO(85L, "Genial", LocalDate.now(), 99L, 10L);

        WebClientResponseException notFoundException = Mockito.mock(WebClientResponseException.NotFound.class);
        mockWebClientException(webClientUsuario, "/api/bibliotecaam/usuario/{id}", 99L, notFoundException);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reseniaService.guardar(request));
        assertTrue(exception.getMessage().contains("El usuario con id 99 no existe"));
        Mockito.verify(reseniaRepository, Mockito.never()).save(any(Resenia.class));
    }

    @Test
    @DisplayName("GIVEN: ID y Request válido WHEN: actualizar THEN: Modifica la reseña existente")
    void shouldUpdateReseniaSuccessfully() {
        Long id = 1L;
        Resenia existente = new Resenia(id, 50L, "Viejo", LocalDate.now(), 1L, 10L);
        ReseniaRequestDTO request = new ReseniaRequestDTO(95L, "Nuevo", LocalDate.now(), 1L, 10L);
        Resenia modificado = new Resenia(id, 95L, "Nuevo", LocalDate.now(), 1L, 10L);

        Mockito.when(reseniaRepository.findById(id)).thenReturn(Optional.of(existente));
        mockWebClientSuccess(webClientUsuario, "/api/bibliotecaam/usuario/{id}", 1L);
        mockWebClientSuccess(webClientLibro, "/api/bibliotecaam/libro/{id}", 10L);
        Mockito.when(reseniaRepository.save(any(Resenia.class))).thenReturn(modificado);

        Optional<ReseniaResponseDTO> resultado = reseniaService.actualizar(id, request);

        assertTrue(resultado.isPresent());
        assertEquals("Nuevo", resultado.get().getComentario());
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: eliminarPorId THEN: Borra el registro")
    void shouldDeleteResenia() {
        Long id = 1L;
        Mockito.doNothing().when(reseniaRepository).deleteById(id);

        assertDoesNotThrow(() -> reseniaService.eliminarPorId(id));
        Mockito.verify(reseniaRepository, Mockito.times(1)).deleteById(id);
    }
}