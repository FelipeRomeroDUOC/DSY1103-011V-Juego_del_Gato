package cl.feliperomero.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableroGatoTest {

    private TableroGato tablero;

    @BeforeEach
    void iniciarTablero() {
        tablero = new TableroGato();
    }

    @Test
    void deberiaCrearTableroVacio() {
        // Given (Dado)
        // El tablero ya fue inicializado por @BeforeEach

        // When (Cuando)
        boolean estaLleno = tablero.estaLleno();
        TableroGato.TipoFicha fichaEnCasilla1 = tablero.getTablero().get("1");

        // Then (Entonces)
        assertFalse(estaLleno);
        assertEquals(TableroGato.TipoFicha.VACIA, fichaEnCasilla1);
    }

    @Test
    void deberiaPonerFichaExitosamente() {
        // Given (Dado)
        String casilla = "5";
        TableroGato.TipoFicha ficha = TableroGato.TipoFicha.X;

        // When (Cuando)
        boolean resultado = tablero.ponerFicha(casilla, ficha);
        TableroGato.TipoFicha fichaEnTablero = tablero.getTablero().get(casilla);

        // Then (Entonces)
        assertTrue(resultado);
        assertEquals(TableroGato.TipoFicha.X, fichaEnTablero);
    }

    @Test
    void noDeberiaPonerFichaEnCasillaOcupada() {
        // Given (Dado)
        String casilla = "1";
        tablero.ponerFicha(casilla, TableroGato.TipoFicha.X); // Ocupamos la casilla

        // When (Cuando)
        // Intentamos sobreescribirla con O
        boolean resultado = tablero.ponerFicha(casilla, TableroGato.TipoFicha.O);
        TableroGato.TipoFicha fichaEnTablero = tablero.getTablero().get(casilla);

        // Then (Entonces)
        assertFalse(resultado);
        assertEquals(TableroGato.TipoFicha.X, fichaEnTablero); // Sigue siendo X
    }

    @Test
    void deberiaDetectarVictoriaHorizontal() {
        // Given (Dado)
        tablero.ponerFicha("4", TableroGato.TipoFicha.X);
        tablero.ponerFicha("5", TableroGato.TipoFicha.X);
        tablero.ponerFicha("6", TableroGato.TipoFicha.X);

        // When (Cuando)
        TableroGato.TipoFicha ganador = tablero.verificarGanador();

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.X, ganador);
    }

    @Test
    void deberiaDetectarVictoriaDiagonal() {
        // Given (Dado)
        tablero.ponerFicha("3", TableroGato.TipoFicha.O);
        tablero.ponerFicha("5", TableroGato.TipoFicha.O);
        tablero.ponerFicha("7", TableroGato.TipoFicha.O);

        // When (Cuando)
        TableroGato.TipoFicha ganador = tablero.verificarGanador();

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.O, ganador);
    }

    @Test
    void deberiaRetornarVaciaSiNadieHaGanado() {
        // Given (Dado)
        tablero.ponerFicha("1", TableroGato.TipoFicha.X);
        tablero.ponerFicha("2", TableroGato.TipoFicha.O);
        
        // When (Cuando)
        TableroGato.TipoFicha ganador = tablero.verificarGanador();

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.VACIA, ganador);
    }

    @Test
    void deberiaDetectarEmpateCuandoEstaLleno() {
        // Given (Dado)
        tablero.ponerFicha("1", TableroGato.TipoFicha.X);
        tablero.ponerFicha("2", TableroGato.TipoFicha.O);
        tablero.ponerFicha("3", TableroGato.TipoFicha.X);
        tablero.ponerFicha("4", TableroGato.TipoFicha.X);
        tablero.ponerFicha("5", TableroGato.TipoFicha.O);
        tablero.ponerFicha("6", TableroGato.TipoFicha.O);
        tablero.ponerFicha("7", TableroGato.TipoFicha.O);
        tablero.ponerFicha("8", TableroGato.TipoFicha.X);
        tablero.ponerFicha("9", TableroGato.TipoFicha.X);

        // When (Cuando)
        boolean estaLleno = tablero.estaLleno();
        TableroGato.TipoFicha ganador = tablero.verificarGanador();

        // Then (Entonces)
        assertTrue(estaLleno);
        assertEquals(TableroGato.TipoFicha.VACIA, ganador);
    }
}
