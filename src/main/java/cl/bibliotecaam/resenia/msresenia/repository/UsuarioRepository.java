package cl.bibliotecaam.resenia.msresenia.repository;

import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
