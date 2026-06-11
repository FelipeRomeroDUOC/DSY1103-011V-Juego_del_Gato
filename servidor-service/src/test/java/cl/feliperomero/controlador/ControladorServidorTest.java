package cl.feliperomero.controlador;

import cl.feliperomero.servicio.GestorPartida;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControladorServidor.class)
class ControladorServidorTest {

    // MockMvc es una herramienta genial que "simula" ser Postman o un navegador
    @Autowired
    private MockMvc mockMvc;

    // Con @MockBean le decimos a Spring que cree un GestorPartida falso solo para este test.
    // Como estamos probando el Controlador (la capa web), no nos interesa la lógica del juego real aquí.
    @MockitoBean
    private GestorPartida gestorPartida;

    @Test
    void deberiaPermitirUnirseConDatosValidos() throws Exception {
        // Given: Simulamos que el Gestor falso aceptará al jugador (retorna true)
        when(gestorPartida.unirJugador(anyString(), eq(15))).thenReturn(true);

        // Simulamos un JSON perfectamente válido enviado por el cliente
        String jsonRequest = """
                {
                    "nombre": "Felipe",
                    "segundosPorTurno": 15
                }
                """;

        // When & Then: Hacemos el POST y verificamos que responda HTTP 200 (OK)
        mockMvc.perform(post("/api/partida/unirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Unido exitosamente."));
    }

    @Test
    void deberiaRechazarSiNombreEstaVacio() throws Exception {
        // Simulamos un JSON inválido (nombre en blanco)
        String jsonRequest = """
                {
                    "nombre": "",
                    "segundosPorTurno": 15
                }
                """;

        // Debería responder HTTP 400 Bad Request gracias a tu anotación @NotBlank
        mockMvc.perform(post("/api/partida/unirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void deberiaRechazarSiTiempoEsMenorAlMinimo() throws Exception {
        // Simulamos un JSON inválido (tiempo menor a 10)
        String jsonRequest = """
                {
                    "nombre": "Felipe",
                    "segundosPorTurno": 5
                }
                """;

        // Debería responder HTTP 400 Bad Request gracias a tu anotación @Min(10)
        mockMvc.perform(post("/api/partida/unirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void deberiaRechazarSiPartidaEstaLlena() throws Exception {
        // Given: Simulamos que el Gestor falso rechaza al jugador (ej: la partida ya está llena)
        when(gestorPartida.unirJugador(anyString(), isNull())).thenReturn(false);

        // JSON válido (sin enviar tiempo límite, es decir, null)
        String jsonRequest = """
                {
                    "nombre": "Felipe"
                }
                """;

        // When & Then: Verificamos que el controlador traduzca el 'false' a un error 400 Bad Request
        mockMvc.perform(post("/api/partida/unirse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Partida llena o en curso."));
    }
}
