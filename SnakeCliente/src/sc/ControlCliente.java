package sc;

import java.applet.AudioClip;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;

/**
 * En esta clase se aportan los métodos, funciones variables necesarias para la captación de 
 * la información dada por el servidor y el posterior dibujado en el tablero.
 * Esta clase es observada por la VentanaJuego y la VentanaMarcador.
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class ControlCliente extends Observable{
    int xPos,yPos,xTes,yTes,xTesOld,xBOld,yBOld,yTesOld,xB,yB,puntuacion=0,anteriorX,anteriorY;
    boolean mover=false,choque=false,inicio=false,aumento=false,bomba=false,cuerpoChoque=false,choqueBomba;
    ArrayList<Point>bombas=new ArrayList<Point>();
    String tipoEvento;
    
    public int getxPos() {
        return this.xPos;
    }
    public void setxPos(int xPos) {
        this.xPos = xPos;
        notificaCambios();
    }
    public int getyPos() {
        return this.yPos;
    }
    void setInicio(boolean inicio){
        this.inicio=inicio;
    }
    int getXB(){
        return this.xB;
    }
    int getYB(){
        return this.yB;
    }
    
    /**
    * Para la escucha del cliente y cierra la conexion con el servidor.
    */
    public void parar (){
        SocketCliente.enviar("PARAR");
        SocketCliente.parar=false;
        SocketCliente.ac.cerrarConexion();
    }
    
    public void control(int x, int y, int xT, int yT, int pun, int xB, int yB){
        
        if (puntuacion!=pun){
            aumento=true;
            tipoEvento="AUMENTO";
            notificaCambios();
        }if (xTes!=xT && yTes!=yT){
            xTesOld=xTes;
            yTesOld=yTes;
        }if(xB!=xBOld && yB!=yBOld){
            xBOld=xB;
            yBOld=yB;
            bombas.add(new Point(xB,yB));
        }
        xPos = x; yPos = y; xTes=xT; yTes=yT; puntuacion=pun;
        for (int i=0;i<bombas.size();i++){
            if (x==(int)bombas.get(i).getX() && y==(int)bombas.get(i).getY()){
                bomba=true;
                notificaCambios();
                parar();
            }
        }
        mover=false;
        notificaCambios();
        mover=true;
        notificaCambios();
        anteriorX=xPos;anteriorY=yPos;
    }
    
    void notificarChoque (){
        SocketCliente.parar=false;
        SocketCliente.enviar("FIN");
        SocketCliente.ac.cerrarConexion();
    }
    public void reiniciar(){
        SocketCliente.enviar("REINICIAR");
    }
    public void setChoque(boolean valor){
        this.choque=valor;
        notificaCambios();
    }
    public void eventos(String tipo){
        this.tipoEvento=tipo;
        notificaCambios();
        this.tipoEvento="";
    }
    public void setChoqueCola(boolean valor){
        this.cuerpoChoque=valor;
        notificaCambios();
    }
    
    public void setChoqueBomba(boolean valor){
        this.bomba=valor;
        notificaCambios();
    }
    
    public void setyPos(int yPos) {
        this.yPos = yPos;
        notificaCambios();
    }
    
    public int getxTes() {
        return xTes;
    }
    
    public void setxTes(int xTes) {
        this.xTes = xTes;
        notificaCambios();
    }
    
    public int getyTes() {
        return this.yTes;
    }
    public void setyTes(int yTes) {
        this.yTes = yTes;
        notificaCambios();
    }
    
    public boolean getMover() {
        return this.mover;
    }
    
    public void setMover(boolean mover) {
        this.mover = mover;
        notificaCambios();
    }
    
    public void notificaCambios() {
        this.setChanged();
        this.notifyObservers();
    }
    
}