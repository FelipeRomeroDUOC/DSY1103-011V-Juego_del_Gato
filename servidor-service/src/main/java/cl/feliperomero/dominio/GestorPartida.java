package cl.feliperomero.dominio;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class GestorPartida {
    
    private TableroGato tableroPartida = new TableroGato();
    private TableroGato.TipoFicha fichaActual = TableroGato.TipoFicha.X;

    private boolean verificarTurno(TableroGato.TipoFicha fichaRecibida){
        if (fichaRecibida == fichaActual){
            return true;
        }
        return false;
    }

    private TableroGato.TipoFicha cambiarTurno(TableroGato.TipoFicha ficha){
        if (ficha == TableroGato.TipoFicha.X){
            ficha = TableroGato.TipoFicha.O;
        }
        else{
            ficha = TableroGato.TipoFicha.X;
        }
        return ficha;
    }
    
    public void recibirJugada(String casilla, TableroGato.TipoFicha ficha){

        if (verificarTurno(ficha)){

            boolean jugadaExitosa = tableroPartida.ponerFicha(casilla, ficha);

            if (jugadaExitosa){

                TableroGato.TipoFicha ganador = tableroPartida.verificarGanador();

                if (ganador != TableroGato.TipoFicha.VACIA){
                System.out.println("¡El juego terminó! Ganador: " + ganador);
                }
                else if (tableroPartida.estaLleno()) {
                System.out.println("¡Empate! Tablero lleno.");
                }
                else{
                    this.fichaActual = cambiarTurno(this.fichaActual);
                }
            }
        }

        
    }
}
