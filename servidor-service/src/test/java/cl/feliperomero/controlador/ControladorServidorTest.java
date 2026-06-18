package cl.feliperomero.controlador;

import cl.feliperomero.dto.UnirseRequestDto;
import cl.feliperomero.servicio.GestorPartida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControladorServidorTest {

    private GestorPartida gestorPartida;
    private ControladorServidor controlador;

    @BeforeEach
    void setUp() {
        gestorPartida = mock(GestorPartida.class);
        controlador = new ControladorServidor(gestorPartida);
    }

    @Test
    void deberiaPermitirUnirseConDatosValidos() {
        // Given: el gestor acepta al jugador
        when(gestorPartida.unirJugador("Felipe", 15)).thenReturn(true);

        UnirseRequestDto request = new UnirseRequestDto("Felipe", 15);

        // When
        ResponseEntity<?> respuesta = controlador.unirJugador(request);

        // Then
        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals("Unido exitosamente.", respuesta.getBody());
    }

    @Test
    void deberiaRechazarSiPartidaEstaLlena() {
        // Given: el gestor rechaza al jugador (partida llena)
        when(gestorPartida.unirJugador("Felipe", null)).thenReturn(false);

        UnirseRequestDto request = new UnirseRequestDto("Felipe", null);

        // When
        ResponseEntity<?> respuesta = controlador.unirJugador(request);

        // Then
        assertEquals(400, respuesta.getStatusCode().value());
        assertEquals("Partida llena o en curso.", respuesta.getBody());
    }
}
