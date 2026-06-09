package cl.feliperomero.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Map;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class TableroGato {
    public enum TipoFicha {
        X, O, VACIA
    }

    private Map<String, TipoFicha> tablero;

    public final String[][] combinacionesGanadoras = {
        {"1", "2", "3"}, // Fila superior
        {"4", "5", "6"}, // Fila central
        {"7", "8", "9"}, // Fila inferior
        {"1", "4", "7"}, // Columna izquierda
        {"2", "5", "8"}, // Columna central
        {"3", "6", "9"}, // Columna derecha
        {"1", "5", "9"}, // Diagonal principal
        {"3", "5", "7"}  // Diagonal secundaria
    };

    public TableroGato() {
        this.crearTablero();
    }

    private void crearTablero(){
        this.tablero = new HashMap<>();

        this.tablero.put("1", TipoFicha.VACIA);
        this.tablero.put("2", TipoFicha.VACIA);
        this.tablero.put("3", TipoFicha.VACIA);
        this.tablero.put("4", TipoFicha.VACIA);
        this.tablero.put("5", TipoFicha.VACIA);
        this.tablero.put("6", TipoFicha.VACIA);
        this.tablero.put("7", TipoFicha.VACIA);
        this.tablero.put("8", TipoFicha.VACIA);
        this.tablero.put("9", TipoFicha.VACIA);
    }

    public boolean ponerFicha(String casilla, TipoFicha ficha){
        if(this.tablero.get(casilla) == TipoFicha.VACIA){
            this.tablero.put(casilla, ficha);
            return true;
        }
        return false;
    }

    public TipoFicha verificarGanador(){
        for(String[] combinacion: combinacionesGanadoras){
            String primeraCasilla = combinacion[0];
            String segundaCasilla = combinacion[1];
            String terceraCasilla = combinacion[2];

            if(this.tablero.get(primeraCasilla) != TipoFicha.VACIA &&
            this.tablero.get(primeraCasilla) == 
            this.tablero.get(segundaCasilla) &&
            this.tablero.get(segundaCasilla) == 
            this.tablero.get(terceraCasilla)){
                return this.tablero.get(primeraCasilla);
            }            
        }
        return TipoFicha.VACIA;
    }

    public boolean estaLleno(){
        for(TipoFicha ficha: tablero.values()){
            if(ficha == TipoFicha.VACIA){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "\n" +
               " " + formatearFicha("1") + " | " + formatearFicha("2") + " | " + formatearFicha("3") + " \n" +
               "---+---+---\n" +
               " " + formatearFicha("4") + " | " + formatearFicha("5") + " | " + formatearFicha("6") + " \n" +
               "---+---+---\n" +
               " " + formatearFicha("7") + " | " + formatearFicha("8") + " | " + formatearFicha("9") + " \n";
    }

    private String formatearFicha(String casilla) {
        TipoFicha ficha = this.tablero.get(casilla);
        if (ficha == TipoFicha.VACIA) {
            return " ";
        }
        return ficha.name();
    }
}
