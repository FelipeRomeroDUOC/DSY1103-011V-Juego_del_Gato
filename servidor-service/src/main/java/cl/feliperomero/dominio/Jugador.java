package cl.feliperomero.dominio;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class Jugador {
    
    private final String nombre;
    private final TableroGato.TipoFicha ficha;

}
