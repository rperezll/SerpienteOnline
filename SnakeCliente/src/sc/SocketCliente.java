package sc;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * En esta clase se aportan los métodos y funciones necesarias para desempeñar las funciones de envio
 * y recibimiento de datos entre el cliente y el servidor.
 * Además, en esta clase se encuentra el ejecutador del cliente.
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class SocketCliente extends Thread{

    private String miMensaje="CONTINUAR";
    String mensajeInicial;
    final String HOST = "localhost";
    final int Puerto = 2000;
    static DataOutputStream flujo_salida;
    DataInputStream flujo_entrada;
    OutputStream auxout;
    InputStream auxin;
    Socket sCliente;
    String respuesta;
    static boolean fin=false;
    static boolean parar=true;
    static ControlCliente cc;
    ControladorP controlador = new ControladorP(); 
    static SocketCliente ac;
    
    /**
    *Constructor Socket Cliente. Construye un socket con la IP y el puerto de escucha
    * del servidor. También se crean los flujos de entrada y salida de datos.
    */
    public SocketCliente() {
        try {
            sCliente = new Socket(HOST, Puerto);
            auxout = sCliente.getOutputStream();
            flujo_salida = new DataOutputStream(auxout);
            auxin = sCliente.getInputStream();
            flujo_entrada = new DataInputStream(auxin);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
    * Actualiza el valor de nuestra orden al mensaje.
    */
    void setMiMensaje(String orden) {
        this.miMensaje = orden;
    }
    
    /**
    * Establecer conexión + recibir dimensiones del tablero.
    */
    public String conexion() {
        String respuestaInicial="";
        try {
            mensajeInicial = "LISTO";
            flujo_salida.writeUTF(mensajeInicial);
            flujo_salida.flush();
            respuestaInicial = flujo_entrada.readUTF();    //Duerme hasta que recibe la respuesta 
            System.out.println("1: [Establecida conexión con el servidor]");
            cc.setInicio(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return respuestaInicial;
    }

    /**
    * Envia el mensaje deseado al servidor.
    */
    public static void enviar(String mensaje) {
        try {
            flujo_salida.writeUTF(mensaje);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
    * Cierra los flujos de entra y salida de datos. Cerrar conexión
    */
    public void cerrarConexion() {
        try {
            System.out.println("Fin del juego...");
            System.out.println("3: [Se ha cerrado la conexión con el servidor...]");
            flujo_salida.close();
            auxout.close();
            flujo_entrada.close();
            auxout.close();
            sCliente.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
    * Ejecutador del cliente.
    * Se crea el socket cliente y se establece conexión. Se reciben los parámetros altura y anchura del servidor.
    * Se construye la ventana de juego y marcador. 
    * Se establece el patrón observer con la clase ControlCliente a la cual la observan tanto la Ventana de juego
    * como la Ventana marcador.
    */
    public static void main(String[] args) {
        ac = new SocketCliente();
        cc = new ControlCliente();
        String mensaje2 = ac.conexion();
        String[] coordenadas = mensaje2.split(";");
        System.out.println("2: [Construcción del tablero de juego]");
        VentanaJuego v = new VentanaJuego(Integer.parseInt(coordenadas[0]), Integer.parseInt(coordenadas[0]));
        VentanaMarcador vm= new VentanaMarcador();
        vm.setVisible(true);
        v.setVisible(true);
        ac.start();
        cc.addObserver(v);
        cc.addObserver(vm);
    }
    
    /**
    * [Thread SOCKET CLIENTE]. Recibe los datos del servidor de forma ininterrumpida.
    * Además, es aquí donde los datos se pasan a la clase de control ControlCliente()
    */
    @Override
    public void run (){
        try{
            while (parar){
                respuesta=flujo_entrada.readUTF();
                String[] mensajes = ((String) respuesta).split(";");
                System.out.println(Arrays.toString(mensajes));
                if (mensajes[0].equals("MOV")){ //Mover
                    cc.control(Integer.parseInt(mensajes[1]),Integer.parseInt(mensajes[2]),Integer.parseInt(mensajes[3]),Integer.parseInt(mensajes[4]),Integer.parseInt(mensajes[5]),Integer.parseInt(mensajes[6]),Integer.parseInt(mensajes[7]));
                }else if(mensajes[0].equals("CHOQUE")){ //Con los límites
                    cc.setChoque(true); 
                    enviar("PARAR");
                    break;
                }else if(mensajes[0].equals("AUMENTO")){    //Comer tesoro
                    cc.eventos("AUMENTO");
                    
                }
            }            
        } catch( Exception e ) {
            System.out.println( e.getMessage());
        }
    }
}
