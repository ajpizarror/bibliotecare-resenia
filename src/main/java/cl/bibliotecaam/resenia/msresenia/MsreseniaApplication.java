package cl.bibliotecaam.resenia.msresenia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsreseniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsreseniaApplication.class, args);
	}

}
