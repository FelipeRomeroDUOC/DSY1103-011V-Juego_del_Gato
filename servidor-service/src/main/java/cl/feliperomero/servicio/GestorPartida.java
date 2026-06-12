package cl.feliperomero.servicio;

import cl.feliperomero.dominio.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Service
public class GestorPartida {
    
    private TableroGato tableroPartida = new TableroGato();
    private TableroGato.TipoFicha fichaActual = TableroGato.TipoFicha.X;

    private LocalDateTime tiempoCreacion;
    private LocalDateTime tiempoInicio;
    private LocalDateTime tiempoUltimaJugada;
    private Integer segundosPorTurno;

    private List<Jugador> jugadores = new ArrayList<>();

    public enum TipoPartida{
        CON_TIEMPO, SIN_TIEMPO
    }
    private TipoPartida tipoPartidaActual = TipoPartida.SIN_TIEMPO;

    public enum EstadoPartida{
        ESPERANDO, EN_PROGRESO, TERMINADA
    }
    private EstadoPartida estadoActual = EstadoPartida.ESPERANDO;

    public boolean unirJugador(String nombreJugador, Integer limiteTiempo){
        if (this.estadoActual == EstadoPartida.ESPERANDO) {
            if (jugadores.isEmpty()){
                Jugador nuevoJugador = new Jugador(nombreJugador, TableroGato.TipoFicha.X);
                jugadores.add(nuevoJugador);
                this.tiempoCreacion = LocalDateTime.now();
                this.segundosPorTurno = limiteTiempo;
                return true;
            }
            if(jugadores.size() == 1){
                Jugador nuevoJugador = new Jugador(nombreJugador, TableroGato.TipoFicha.O);
                jugadores.add(nuevoJugador);
                if (this.segundosPorTurno == null){
                    iniciarPartida(TipoPartida.SIN_TIEMPO);
                } else {
                    iniciarPartida(TipoPartida.CON_TIEMPO);
                }
                return true;
            }
        }
        return false;
    }


    public enum ResultadoPartida{
        X, O, EMPATE, TIMEOUT
    }

    private ResultadoPartida resultadoPartida = null;

    public void iniciarPartida(TipoPartida tipo){
        this.tipoPartidaActual = tipo;
        this.estadoActual = EstadoPartida.EN_PROGRESO;
        this.tiempoInicio = LocalDateTime.now();
        this.tiempoUltimaJugada = LocalDateTime.now();
    }

    public ResultadoPartida terminarPartida(ResultadoPartida resultado){
        if (this.estadoActual == EstadoPartida.EN_PROGRESO ) {
            this.resultadoPartida = resultado;
            this.estadoActual = EstadoPartida.TERMINADA;
            return this.resultadoPartida;
        }
        return null;
    }

    public ResultadoPartida tiempoAgotado(){
        if (this.estadoActual == EstadoPartida.EN_PROGRESO) {
            this.resultadoPartida = ResultadoPartida.TIMEOUT;
            this.estadoActual = EstadoPartida.TERMINADA;
            return this.resultadoPartida;
        }
        return null;
    }

    public boolean revisarYAplicarTimeout(){
        if (this.tipoPartidaActual == TipoPartida.CON_TIEMPO) {
            if (obtenerTiempoRestante() == 0) {
                tiempoAgotado();
                return true;
            }
            return false;
        }
        return false;
    }

    private Integer tiempoRestante = 0;
    public Integer obtenerTiempoRestante(){
        if (this.tipoPartidaActual == TipoPartida.CON_TIEMPO) {
            Duration diferencia = Duration.between(this.tiempoUltimaJugada, LocalDateTime.now());
            this.tiempoRestante = this.segundosPorTurno - (int) diferencia.getSeconds();
            if(this.tiempoRestante < 0){
                this.tiempoRestante = 0;
            }
        }
        return this.tiempoRestante;
    }

    private boolean verificarTurno(String nombre){
        for(Jugador jugador : jugadores){
            if(nombre.equals(jugador.getNombre())){
                if (this.fichaActual == jugador.getFicha()){
                    return true;
                }
                return false;
            }
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
    
    public boolean recibirJugada(String nombre, TableroGato.Casilla casilla){

        this.revisarYAplicarTimeout();
        
        if (this.estadoActual != EstadoPartida.EN_PROGRESO){
            return false;
        }

        if (verificarTurno(nombre)){

            boolean jugadaExitosa = tableroPartida.ponerFicha(casilla, this.fichaActual);

            if (jugadaExitosa){

                TableroGato.TipoFicha ganador = tableroPartida.verificarGanador();

                if (ganador != TableroGato.TipoFicha.VACIA){
                    if (ganador == TableroGato.TipoFicha.X) {
                        this.terminarPartida(ResultadoPartida.X);
                    } else {
                        this.terminarPartida(ResultadoPartida.O);
                    }
                }
                
                else if (tableroPartida.estaLleno()) {
                    this.terminarPartida(ResultadoPartida.EMPATE);
                }
                else{
                    this.fichaActual = cambiarTurno(this.fichaActual);
                    this.tiempoUltimaJugada = LocalDateTime.now();
                }
                return true;
            }
        }
        return false;
    }
}
