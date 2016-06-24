package ss;

import static ss.SocketServidor.Puerto;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Ejecutador del servidor. Creación de un socket individual para cada conexión. Se asigna a cada
 * nuevo usuario un código de identificación único.
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class EjecutadorServidor {
    public static void main( String[] arg ) {
        try {
   
            ServerSocket skServidor = new ServerSocket(Puerto);
            Socket sCliente;
            int numcli=1;
            System.out.println("1: [Servidor listo] Esperando a un nuevo cliente...");
            while (true){
                sCliente = skServidor.accept();
                System.out.println("2: [Conexión establecida, el cliente "+ numcli + " está jugando]"); 
                new SocketServidor(sCliente, numcli).start();
                numcli++;   
            }
        } catch( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }
}
