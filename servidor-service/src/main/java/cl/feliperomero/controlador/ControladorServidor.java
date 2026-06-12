package cl.feliperomero.controlador;

import cl.feliperomero.dominio.Jugador;
import cl.feliperomero.dominio.TableroGato;
import cl.feliperomero.servicio.GestorPartida;
import cl.feliperomero.dto.JugadaRequestDto;
import cl.feliperomero.dto.UnirseRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/partida")
public class ControladorServidor {

    private final GestorPartida gestorPartida;

    public ControladorServidor(GestorPartida gestorPartida) {
        this.gestorPartida = gestorPartida;
    }

    // Aquí podrás definir tus endpoints más adelante, por ejemplo:
    // @PostMapping("/unirse")
    // @PostMapping("/jugada")
    // @GetMapping("/estado")
    @PostMapping("/unirse")
    public ResponseEntity<?> unirJugador(@Valid @RequestBody UnirseRequestDto request){

        boolean pudoUnirse = gestorPartida.unirJugador(request.nombre(), request.segundosPorTurno());
        
        if(pudoUnirse){
            return ResponseEntity.ok("Unido exitosamente.");
        }else{
            return ResponseEntity.badRequest().body("Partida llena o en curso.");
        }
    }

    @PostMapping("/jugada")
    public ResponseEntity<?> hacerJugada(@Valid @RequestBody JugadaRequestDto request){

        TableroGato.Casilla casillaEnum = TableroGato.Casilla.valueOf("C" + request.casilla());
        
        boolean jugadaExitosa = gestorPartida.recibirJugada(request.nombre(), casillaEnum);

        if (jugadaExitosa) {
            return ResponseEntity.ok("Jugada exitosa.");
        }else{
            return ResponseEntity.badRequest().body("Jugada invalida.");
        }
        

    }

}
