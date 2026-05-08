package cl.bibliotecaam.resenia.msresenia.repository;

import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReseniaRepository extends JpaRepository<Resenia, Long> {
    List<Resenia> findByPuntaje(Long puntaje);
    List<Resenia> findByFechaRese(LocalDate fecha);
    List<Resenia> findByUsuario(Usuario usuario);
}
