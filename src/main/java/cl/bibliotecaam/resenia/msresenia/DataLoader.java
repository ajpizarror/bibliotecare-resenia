package cl.bibliotecaam.resenia.msresenia;

import cl.bibliotecaam.resenia.msresenia.model.Resenia;
import cl.bibliotecaam.resenia.msresenia.repository.ReseniaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private ReseniaRepository reseniaRepository;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            Resenia resenia = new Resenia();
            resenia.setPuntaje((long) faker.number().numberBetween(0,100));
            resenia.setComentario(faker.lorem().sentence());
            resenia.setFechaRese((faker.timeAndDate().birthday()));
            resenia.setIdUsuario((long)faker.number().numberBetween(1,3));
            resenia.setIdLibro((long)faker.number().numberBetween(1,3));

            reseniaRepository.save(resenia);
        }
    }
}
