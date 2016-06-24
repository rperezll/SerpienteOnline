package sc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * En esta clase se recogen las pulsaciones de las teclas correspondoentes a las direcciones.
 * @author: ROBERTO PÉREZ LLANOS
 * @version: 19/06/2016
 */

public class ControladorP implements KeyListener { 
    @Override
        public void keyTyped(KeyEvent ke) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public void keyPressed(KeyEvent control)  {
            int c = control.getKeyCode();
            switch (c) {
                case 38://arriba 
                    SocketCliente.enviar("DIR;ARRIBA");
                break;
                case 40://abajo
                    SocketCliente.enviar("DIR;ABAJO");
                break;
                case 37://izquierda
                    SocketCliente.enviar("DIR;IZQUIERDA");
                break;
                case 39://derecha
                    SocketCliente.enviar("DIR;DERECHA");
                break;
                case 88: //Parar 'X'
                    SocketCliente.enviar("FIN");
                break;
            }
        }
        @Override
        public void keyReleased(KeyEvent ke) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
}