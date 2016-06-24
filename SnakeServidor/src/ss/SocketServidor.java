package ss;

import java.io.*;
import java.net.*;

/**
 * En esta clase se definen los metodos y funciones necesarios para la conexión con 
 * el cliente. Envio y interpretación de los mensajes recibidos.
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class SocketServidor extends Thread {
    int codigoCliente;
    boolean parar=true;
    static final int Puerto=2000;
    Socket skCliente;
    InputStream auxin;
    DataInputStream flujo_entrada;
    OutputStream auxout;
    static DataOutputStream flujo_salida;
    Modelo modelo;
    
    /**
    * Constructor del Socket. Crea una entidad de modelo y los correspondiente flujos
    * para la correcta transmisión de datos. Además de enviar y recibir, interpreta 
    * los mensajes del cliente según su tipo.
    */
    public SocketServidor(Socket sCliente,  int codCliente) throws IOException {
        this.modelo = new Modelo();
        this.skCliente = sCliente;
        this.codigoCliente=codCliente;
        auxin = skCliente.getInputStream();
        flujo_entrada= new DataInputStream( auxin );
        auxout = skCliente.getOutputStream();
        flujo_salida= new DataOutputStream( auxout );
    }
    
    SocketServidor(){}  //Constructor vacio
    
    /**
    * Realiza el envio de los datos deseados al cliente
    */
    static void enviarServidor (String mensaje) throws IOException {
        flujo_salida.writeUTF(mensaje);
        flujo_salida.flush();
    }
    
    /**
    * Cierra los flujos de transmisión de datos con el cliente
    */
    void cerrarConexion() throws IOException{ 
        flujo_salida.close();
        auxout.close();
        flujo_entrada.close();
        auxin.close();
        skCliente.close();
    }
    
    /**
    * [Thread SOCKET SERVIDOR]. Recibe los datos del cliente de forma ininterrumpida.
    * Además, es aquí donde se interpretan los mensajes segú su tipo.
    */
    @Override
    public void run (){
        try{
            String mensaje="";     
            while (parar){
                mensaje=flujo_entrada.readUTF();
                String[] men = ((String) mensaje).split(";");
                if (men[0].equals("REINICIAR")){
                    modelo.inicio();
//new SocketServidor(this.skCliente,this.codigoCliente).start();
                }else if(men[0].equals("PARAR")){ //Mensaje de finalizar sesión
                    Modelo.parar=false;
                    cerrarConexion();
                    break;
                }else if (men[0].equals("LISTO")){  //Mensaje de establecimiento de la conexión + dimensiones del tablero
                    Modelo.parar=true;
                    System.out.println("3: [Envío de las dimensiones del tablero]");
                    flujo_salida.writeUTF(Integer.toString(modelo.ANCHO)+";"+Integer.toString(modelo.ALTO));
                    modelo.inicio();
                }else if (men[0].equals("DIR")){    //Mensaje de cambio de dirección
                    if (men[1].equals("ARRIBA")){
                        if (!Modelo.direccion.equals("ABAJO"))
                            this.modelo.setDireccion("ARRIBA");
                    }else if (men[1].equals("ABAJO")){
                        if (!Modelo.direccion.equals("ARRIBA"))
                            this.modelo.setDireccion("ABAJO"); 
                    }else if (men[1].equals("DERECHA")){
                        if (!Modelo.direccion.equals("IZQUIERDA"))
                            this.modelo.setDireccion("DERECHA");
                    }else if (men[1].equals("IZQUIERDA")){
                        if (!Modelo.direccion.equals("DERECHA"))
                            this.modelo.setDireccion("IZQUIERDA");
                    }
                }
            }
            System.out.println("4: [Se ha cerrado la conexión con el cliente " + this.codigoCliente+ "]"); 
        }catch( Exception e ) {
            System.out.println( e.getMessage());
        }
    }
               
}