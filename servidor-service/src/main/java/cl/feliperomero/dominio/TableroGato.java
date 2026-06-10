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

     public enum Casilla{
        C1("1"),
        C2("2"),
        C3("3"),
        C4("4"),
        C5("5"),
        C6("6"),
        C7("7"),
        C8("8"),
        C9("9");
        
        private final String idStr;

        Casilla(String idStr){
            this.idStr = idStr;
        }

        public String getIdStr(){
            return idStr;
        }
    }

    private Map<Casilla, TipoFicha> tablero;

    private enum CombinacionesGanadoras{
        FILA_SUPERIOR(Casilla.C1, Casilla.C2, Casilla.C3),
        FILA_CENTRAL(Casilla.C4, Casilla.C5, Casilla.C6),
        FILA_INFERIOR(Casilla.C7, Casilla.C8, Casilla.C9),
        COLUMNA_IZQUIERDA(Casilla.C1, Casilla.C4, Casilla.C7),
        COLUMNA_CENTRAL(Casilla.C2, Casilla.C5, Casilla.C8),
        COLUMNA_DERECHA(Casilla.C3, Casilla.C6, Casilla.C9),
        DIAGONAL_PRINCIPAL(Casilla.C1, Casilla.C5, Casilla.C9),
        DIAGONAL_SECUNDARIA(Casilla.C3, Casilla.C5, Casilla.C7);

        private final Casilla c1;
        private final Casilla c2;
        private final Casilla c3;

        CombinacionesGanadoras(Casilla c1, Casilla c2, Casilla c3){
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
        }

        public Casilla getC1(){
            return c1;
        }
        public Casilla getC2(){
            return c2;
        }
        public Casilla getC3(){
            return c3;
        }
    };


    private void crearTablero(){
        this.tablero = new HashMap<>();

        this.tablero.put(Casilla.C1, TipoFicha.VACIA);
        this.tablero.put(Casilla.C2, TipoFicha.VACIA);
        this.tablero.put(Casilla.C3, TipoFicha.VACIA);
        this.tablero.put(Casilla.C4, TipoFicha.VACIA);
        this.tablero.put(Casilla.C5, TipoFicha.VACIA);
        this.tablero.put(Casilla.C6, TipoFicha.VACIA);
        this.tablero.put(Casilla.C7, TipoFicha.VACIA);
        this.tablero.put(Casilla.C8, TipoFicha.VACIA);
        this.tablero.put(Casilla.C9, TipoFicha.VACIA);
    }

    public TableroGato() {
        this.crearTablero();
    }

    public boolean ponerFicha(Casilla casilla, TipoFicha ficha){
        if(this.tablero.get(casilla) == TipoFicha.VACIA){
            this.tablero.put(casilla, ficha);
            return true;
        }
        return false;
    }

    public TipoFicha verificarGanador(){
        for(CombinacionesGanadoras combinacion : CombinacionesGanadoras.values()){
            Casilla primeraCasilla = combinacion.getC1();
            Casilla segundaCasilla = combinacion.getC2();
            Casilla terceraCasilla = combinacion.getC3();

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
               " " + formatearFicha(Casilla.C1) + " | " + formatearFicha(Casilla.C2) + " | " + formatearFicha(Casilla.C3) + " \n" +
               "---+---+---\n" +
               " " + formatearFicha(Casilla.C4) + " | " + formatearFicha(Casilla.C5) + " | " + formatearFicha(Casilla.C6) + " \n" +
               "---+---+---\n" +
               " " + formatearFicha(Casilla.C7) + " | " + formatearFicha(Casilla.C8) + " | " + formatearFicha(Casilla.C9) + " \n";
    }

    private String formatearFicha(Casilla casilla) {
        TipoFicha ficha = this.tablero.get(casilla);
        if (ficha == TipoFicha.VACIA) {
            return " ";
        }
        return ficha.name();
    }
}
