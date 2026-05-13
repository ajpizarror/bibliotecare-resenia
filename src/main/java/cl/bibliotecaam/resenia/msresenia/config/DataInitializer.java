package cl.bibliotecaam.resenia.msresenia.config;

import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.model.Usuario;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import cl.bibliotecaam.resenia.msresenia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ReseniaRepository reseniaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args){
        if (reseniaRepository.count() > 0){
            log.info(">>> Data initializer: La BD ya tiene datos, se omite la carga inicial.");
            return;
        }

        log.info(">>> Data initializer: BD vacia detectada, insertando datos de prueba...");

        Usuario paladin = usuarioRepository.save(new Usuario(
                null, 20888888L, "K", "Miguel","Angel","Arguello","Quintana", LocalDate.of(2000,7,13)));

        Usuario espadachinMago = usuarioRepository.save(new Usuario(
                null, 888888L, "8", "Ludwig","Joseph","Wittgenstein","X", LocalDate.of(1800,1,1)));


        Resenia primeraResenia = reseniaRepository.save(
                new Resenia(null, 100L, "Muy buen libro", LocalDate.of(2026,5,5), 1L));

        Resenia segundaResenia = reseniaRepository.save(
                new Resenia(null, 10L, "Pesimo libor", LocalDate.of(2026, 3, 3), 2L));

    }



}
