package cl.feliperomero.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestorPartidaTest {

    private GestorPartida gestor;

    @BeforeEach
    void iniciarGestor() {
        gestor = new GestorPartida();
    }

    @Test
    void deberiaEmpezarConTurnoX() {
        // Given (Dado)
        // El gestor inicializado en @BeforeEach

        // When (Cuando)
        TableroGato.TipoFicha turnoInicial = gestor.getFichaActual();

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.X, turnoInicial);
    }

    @Test
    void deberiaCambiarTurnoDespuesDeJugadaValida() {
        // Given (Dado)
        String casilla = "1";
        TableroGato.TipoFicha fichaJugada = TableroGato.TipoFicha.X;

        // When (Cuando)
        gestor.recibirJugada(casilla, fichaJugada);
        TableroGato.TipoFicha turnoSiguiente = gestor.getFichaActual();
        TableroGato.TipoFicha estadoCasilla = gestor.getTableroPartida().getTablero().get(casilla);

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.X, estadoCasilla); // La ficha se puso exitosamente
        assertEquals(TableroGato.TipoFicha.O, turnoSiguiente); // El turno cambió a O
    }

    @Test
    void noDeberiaPermitirJugarFueraDeTurno() {
        // Given (Dado)
        // El turno inicial es de X, pero intentaremos mandar una jugada de O
        String casilla = "1";
        TableroGato.TipoFicha fichaIncorrecta = TableroGato.TipoFicha.O; 

        // When (Cuando)
        gestor.recibirJugada(casilla, fichaIncorrecta);
        TableroGato.TipoFicha turnoActual = gestor.getFichaActual();
        TableroGato.TipoFicha estadoCasilla = gestor.getTableroPartida().getTablero().get(casilla);

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.VACIA, estadoCasilla); // La jugada fue rechazada, sigue vacía
        assertEquals(TableroGato.TipoFicha.X, turnoActual); // El turno sigue siendo de X (no se perdió el turno)
    }

    @Test
    void noDeberiaCambiarTurnoSiCasillaEstaOcupada() {
        // Given (Dado)
        gestor.recibirJugada("1", TableroGato.TipoFicha.X); // X juega en la 1 (El turno pasa a O)

        // When (Cuando)
        // O intenta robar la casilla 1 que ya está ocupada
        gestor.recibirJugada("1", TableroGato.TipoFicha.O);
        
        TableroGato.TipoFicha turnoActual = gestor.getFichaActual();
        TableroGato.TipoFicha estadoCasilla = gestor.getTableroPartida().getTablero().get("1");

        // Then (Entonces)
        assertEquals(TableroGato.TipoFicha.X, estadoCasilla); // Sigue estando la X original intacta
        assertEquals(TableroGato.TipoFicha.O, turnoActual); // El turno sigue siendo de O (tiene que reintentar en otra casilla)
    }
}
