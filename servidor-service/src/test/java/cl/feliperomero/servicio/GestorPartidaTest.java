package cl.feliperomero.servicio;

import cl.feliperomero.dominio.*;
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

    @Test
    void deberiaPermitirUnirseMaximoDosJugadores() {
        // Given (Dado)
        Jugador j1 = new Jugador("P1", TableroGato.TipoFicha.X);
        Jugador j2 = new Jugador("P2", TableroGato.TipoFicha.O);
        Jugador j3 = new Jugador("P3", TableroGato.TipoFicha.X);

        // When (Cuando)
        boolean entroJ1 = gestor.unirJugador(j1, null);
        GestorPartida.EstadoPartida estadoDespuesJ1 = gestor.getEstadoActual();
        
        boolean entroJ2 = gestor.unirJugador(j2, null);
        GestorPartida.EstadoPartida estadoDespuesJ2 = gestor.getEstadoActual();

        boolean entroJ3 = gestor.unirJugador(j3, null);

        // Then (Entonces)
        assertTrue(entroJ1);
        assertEquals(GestorPartida.EstadoPartida.ESPERANDO, estadoDespuesJ1);

        assertTrue(entroJ2);
        assertEquals(GestorPartida.EstadoPartida.EN_PROGRESO, estadoDespuesJ2);

        assertFalse(entroJ3); // El tercero es rechazado
    }

    @Test
    void deberiaDeclararTimeoutSiJugadaSeTardaDemasiado() throws InterruptedException {
        // Given (Dado)
        Jugador j1 = new Jugador("P1", TableroGato.TipoFicha.X);
        Jugador j2 = new Jugador("P2", TableroGato.TipoFicha.O);
        
        gestor.unirJugador(j1, 1); // 1 segundo por turno (CON_TIEMPO)
        gestor.unirJugador(j2, null);
        
        // When (Cuando)
        Thread.sleep(2100); // Esperamos 2.1 segundos (para que Duration.getSeconds() retorne 2)
        gestor.recibirJugada("1", TableroGato.TipoFicha.X);
        
        // Then (Entonces)
        assertEquals(GestorPartida.ResultadoPartida.TIMEOUT, gestor.getResultadoPartida());
        assertEquals(GestorPartida.EstadoPartida.TERMINADA, gestor.getEstadoActual());
        assertEquals(TableroGato.TipoFicha.VACIA, gestor.getTableroPartida().getTablero().get("1"));
    }

    @Test
    void deberiaGuardarElResultadoAlTerminar() {
        // Given (Dado)
        Jugador j1 = new Jugador("P1", TableroGato.TipoFicha.X);
        Jugador j2 = new Jugador("P2", TableroGato.TipoFicha.O);
        gestor.unirJugador(j1, null); // SIN_TIEMPO
        gestor.unirJugador(j2, null);

        // When (Cuando)
        gestor.recibirJugada("1", TableroGato.TipoFicha.X);
        gestor.recibirJugada("4", TableroGato.TipoFicha.O);
        gestor.recibirJugada("2", TableroGato.TipoFicha.X);
        gestor.recibirJugada("5", TableroGato.TipoFicha.O);
        gestor.recibirJugada("3", TableroGato.TipoFicha.X); // X hace 3 en línea (1, 2, 3)

        // Then (Entonces)
        assertEquals(GestorPartida.ResultadoPartida.X, gestor.getResultadoPartida());
        assertEquals(GestorPartida.EstadoPartida.TERMINADA, gestor.getEstadoActual());
    }

    @Test
    void deberiaAplicarTimeoutPasivamenteSiNadieJuega() throws InterruptedException {
        // Given (Dado)
        Jugador j1 = new Jugador("P1", TableroGato.TipoFicha.X);
        Jugador j2 = new Jugador("P2", TableroGato.TipoFicha.O);
        
        gestor.unirJugador(j1, 1); // 1 segundo por turno (CON_TIEMPO)
        gestor.unirJugador(j2, null);
        
        // When (Cuando)
        Thread.sleep(2100); // Esperamos a que pase el tiempo
        // Llamamos directamente al guardia (Simulando un cliente que pregunta por el estado)
        gestor.revisarYAplicarTimeout(); 
        
        // Then (Entonces)
        // La partida debería cerrarse sin que nadie haya mandado una jugada
        assertEquals(GestorPartida.ResultadoPartida.TIMEOUT, gestor.getResultadoPartida());
        assertEquals(GestorPartida.EstadoPartida.TERMINADA, gestor.getEstadoActual());
    }

    @Test
    void deberiaCalcularTiempoRestanteCorrectamente() throws InterruptedException {
        // Given (Dado)
        Jugador j1 = new Jugador("P1", TableroGato.TipoFicha.X);
        Jugador j2 = new Jugador("P2", TableroGato.TipoFicha.O);
        
        // Iniciamos partida con 2 segundos por turno
        gestor.unirJugador(j1, 2); 
        gestor.unirJugador(j2, null);
        
        // When & Then (Cuando y Entonces)
        
        // Esperamos ~1 segundo (1000ms)
        Thread.sleep(1100); 
        Integer tiempoRestante = gestor.obtenerTiempoRestante();
        
        // Debería quedar 1 segundo restante (2 - 1 = 1)
        assertEquals(1, tiempoRestante);
        
        // Esperamos otros 1.5 segundos (ya habrán pasado ~2.6s en total)
        Thread.sleep(1500);
        Integer tiempoAgotado = gestor.obtenerTiempoRestante();
        
        // Debería estancarse en 0 y no devolver números negativos
        assertEquals(0, tiempoAgotado);
    }
}
