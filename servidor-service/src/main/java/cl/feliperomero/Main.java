package cl.feliperomero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta anotación es la magia. Le dice a Spring "esta es la raíz de mi aplicación, escanea desde aquí hacia abajo".
@SpringBootApplication 
public class Main {

    public static void main(String[] args) {
        // En lugar de instanciar cosas nosotros, le pasamos el control a Spring
        SpringApplication.run(Main.class, args);
    }
}
