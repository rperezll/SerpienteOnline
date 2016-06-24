package ss;

import java.io.IOException;
import java.util.Random;

 /**
 * En esta clase se definen los metodos y funciones necesarios para el calculo de coordenadas,
 * tesoros, elementos bombas, eventos de choque y puntuación de la partida
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class Modelo{
    static String direccion;
    //Variables de dimension del tablero.
    final int ANCHO=60;                          
    final int ALTO=60;                                      
    //Variable de la velocidad de nuestro hilo.
    final int VEL = 9;                                    
    //Random utilizado en tesoro() e inicio().
    Random r,r2;                           
    //Variable utilizada para parar el hilo.
    static boolean parar;                
    //Coordenadas de la cabeza de nuestra serpiente.   
    int x;                              
    int y;
    //Coordenadas del tesoro.
    int xTesoro; int xTesoroOld;  int xBomba; int xBombaOld;                     
    int yTesoro; int yTesoroOld;  int yBomba; int yBombaOld;
    //Variables puntuacion
    int puntuacion;
    String orden;
    int aumento=150;
    
    //Constructor vacio.
     public Modelo(){    
    }
     
    void setDireccion(String d){
        direccion=d; 
    }
    
    /**
     * Método que establece la posición principal de la serpiente, el primer tesoro
     * y la primera bomba. Además, inicia los hilos.
     */
    public void inicio() throws IOException{
        x = ANCHO/2; y = ALTO/2; direccion = "SV"; puntuacion=0;
        r=new Random();
        r2=new Random();
        new BombasPosicion().start();
        new ActualizaTablero().start();
        tesoro(); //Nada más ejecutar nuestro modelo, generamos el primer tesoro.
        //generaBomba(); //Nada más ejecutar nuestro modelo, generamos el primer tesoro.
        SocketServidor.enviarServidor("MOV"+";"+x+";"+y+";"+xTesoro+";"+yTesoro+";"+0+";"+10+";"+10);
    }
    
    /**
     * Método que genera un nuevo tesoro.
     */
    public void tesoro() {
        boolean ok=true;
        xTesoroOld=xTesoro;
        xTesoroOld=yTesoro;
        while (ok){
            xTesoro=r.nextInt(ANCHO);
            yTesoro=r.nextInt(ALTO);
            if ( !(xTesoro==0 && yTesoro==0) && !(xTesoro==x && yTesoro==y) && 
                    !(xTesoro==xTesoroOld && yTesoro==yTesoroOld) && 
                    (xTesoro!= ANCHO-1 && yTesoro!= ALTO-1) && (xTesoro!=0 && yTesoro!=0) )
                ok=false;
        }
    }
    
    /**
     * Método que genera un nuevo tesoro.
     */
    public void generaBomba() {
        boolean ok=true;
        xBombaOld=xBomba;
        yBombaOld=yBomba;
        while (ok){
            xBomba=r.nextInt(ANCHO);
            yBomba=r.nextInt(ALTO);
            if ( !(yBomba==0 && xBomba==0) && !(xBomba==x && yBomba==y) && !(xBomba==xTesoro && yBomba==yTesoro) && 
                    !(xBomba==xBombaOld && yBomba==yBombaOld) && 
                    (xBomba!= ANCHO-1 && yBomba!= ALTO-1) && (xBomba!=0 && yBomba!=0) )
                ok=false;
        }
    }
    
    /**
    * [Método principal de la clase] Calcula nuevas coordenadas dependiendo de la dirección,
    * aumenta la puntuación y controla el choque de la serpiente en el tablero.
     */
    void actualizaPosicion() throws IOException{
//        if (this.x==xTesoro && y==yTesoro){
//            tesoro();
//            puntuacion++;
//            orden="AUMENTO";
//            //SocketServidor.enviarServidor(orden/*+";"+x+";"+y+";"+xTesoro+";"+yTesoro+";"+puntuacion+";"+xBomba+";"+yBomba*/);
//        }
        if (direccion.equals("ARRIBA") ) {
            orden="MOV"; x--;  
            if (x<1){    //Control de choque
                orden="CHOQUE";
            }else if (this.x==xTesoro && y==yTesoro){
                //orden="AUMENTO";
                tesoro();
                puntuacion++;
            }
        } else if (direccion.equals("ABAJO")) {
            orden="MOV"; x++;
            if (x> ANCHO-2){ //Control de choque
                orden="CHOQUE";
            }else if (this.x==xTesoro && y==yTesoro){
                //orden="AUMENTO";
                tesoro();
                puntuacion++;
            }
        } else if (direccion.equals("DERECHA")) {
            orden="MOV"; y++;
            if (y> ALTO-2){ //Control de choque
                orden="CHOQUE";
            }else if (this.x==xTesoro && y==yTesoro){
                ///orden="AUMENTO";
                tesoro();
                puntuacion++;
            } 
        } else if (direccion.equals("IZQUIERDA")) {
            orden="MOV"; y--;
            if (y<1){ //Control de choque
                orden="CHOQUE";
            }else if (this.x==xTesoro && y==yTesoro){
                //orden="AUMENTO";
                tesoro();
                puntuacion++;
            }
        }
        SocketServidor.enviarServidor(orden+";"+x+";"+y+";"+xTesoro+";"+yTesoro+";"+puntuacion+";"+xBomba+";"+yBomba);
        System.out.println(orden+";"+x+";"+y+";"+xTesoro+";"+yTesoro+";"+puntuacion+";"+xBomba+";"+yBomba);
    }
    
    /**
     * [Thread ACTUALIZA POSICION MODELO] Subclase de modelo. (Actualiza las nuevas coordenadas de tesoros, 
     * posición de la serpiente y demás en cada iteración)
     */
    class ActualizaTablero extends Thread {  
        @Override
        public void run() {                             
            while (parar) {                             
                try{                                                    
                    Thread.sleep(VEL * 8);          
                } catch (InterruptedException ex){
                    System.err.println("Error en Thread MODELO ActualizaTablero");
                }
                try{   
                    actualizaPosicion();  
                } catch (IOException ex) {
                    System.err.println(/*"Error en ActualizaTablero"*/);
                }      
            }     
        }
    }
    
    /**
     * [Thread BOMBA MODELO]. Genera bombas cada cierto tiempo en posiciones aleatorias.
     */
    class BombasPosicion extends Thread {  
        @Override
        public void run() {                             
            while (parar) {                             
                try{                                                    
                    Thread.sleep(VEL * 100);          
                } catch (InterruptedException ex){
                    System.err.println("Error en Thread MODELO BombasPosicion");
                }                                                  
                generaBomba();               
            }     
        }
    }
    
}