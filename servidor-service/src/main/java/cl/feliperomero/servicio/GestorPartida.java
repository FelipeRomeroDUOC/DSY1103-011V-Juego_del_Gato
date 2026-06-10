package cl.feliperomero.servicio;

import cl.feliperomero.dominio.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Service
public class GestorPartida {
    
    private TableroGato tableroPartida = new TableroGato();
    private TableroGato.TipoFicha fichaActual = TableroGato.TipoFicha.X;

    private LocalDateTime tiempoCreacion;
    private LocalDateTime tiempoInicio;
    private LocalDateTime tiempoUltimaJugada;
    private Integer segundosPorTurno;

    private List<Jugador> jugadores = new ArrayList<>();

    public boolean unirJugador(Jugador nuevoJugador, Integer limiteTiempo){
        if (jugadores.isEmpty()){
            jugadores.add(nuevoJugador);
            this.tiempoCreacion = LocalDateTime.now();
            this.segundosPorTurno = limiteTiempo;
            return true;
        }
        if(jugadores.size() == 1){
            jugadores.add(nuevoJugador);
            if (this.segundosPorTurno == null){
                iniciarPartida(TipoPartida.SIN_TIEMPO);
            } else {
                iniciarPartida(TipoPartida.CON_TIEMPO);
            }
            return true;
        }
        return false;
    }

    public enum TipoPartida{
        CON_TIEMPO, SIN_TIEMPO
    }
    private TipoPartida tipoPartidaActual = TipoPartida.SIN_TIEMPO;

    public enum EstadoPartida{
        ESPERANDO, EN_PROGRESO, TERMINADA
    }
    private EstadoPartida estadoActual = EstadoPartida.ESPERANDO;

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

    public void terminarPartida(ResultadoPartida resultado){
        this.resultadoPartida = resultado;
        this.estadoActual = EstadoPartida.TERMINADA;
    }

    public void tiempoAgotado(){
        if (tipoPartidaActual == TipoPartida.CON_TIEMPO){
            this.terminarPartida(ResultadoPartida.TIMEOUT);
        }
    }

    public void revisarYAplicarTimeout(){
        if (this.tipoPartidaActual == TipoPartida.CON_TIEMPO) {
            
            if (obtenerTiempoRestante() == 0) {
                tiempoAgotado();
                return;
            }
        }
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

        this.revisarYAplicarTimeout();
        if (this.estadoActual == EstadoPartida.TERMINADA){
            return;
        }

        if (verificarTurno(ficha)){

            boolean jugadaExitosa = tableroPartida.ponerFicha(casilla, ficha);

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
            }
        }
    }
}
