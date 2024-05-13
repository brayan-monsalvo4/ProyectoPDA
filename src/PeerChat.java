import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

import javax.swing.JTextArea;
import javax.swing.JToggleButton;

public class PeerChat implements Runnable {
    private MulticastSocket socket;
    private InetAddress host;
    private int port;

    public int length_bytes = 1024;

    private JTextArea areaChat;
    private JTextArea areaLog;
    private JToggleButton toggleAlgoritmoConsenso;
    private JToggleButton toggleAlgoritmoBully;

    public PeerChat(String host, int port, String id, DirectorioPeers directorio){
        try{
            this.socket = new MulticastSocket(port);
            this.host = InetAddress.getByName(host);
            this.port = port;
            this.socket.joinGroup(this.host);

        } catch(Exception e){
            System.out.println("Excepcion: "+e.getMessage());
        }
    }

    public void enviarMensaje(String text){
        try{
            byte[] mensaje = text.getBytes();

            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, host, port);

            this.socket.send(   paquete );

        }catch(Exception e){
            System.out.println("Error en enviarMensaje: "+e.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            socket.setTimeToLive(255);
            while(true){
                
                byte[] buffer = new byte[ length_bytes ];
                DatagramPacket respuesta = new DatagramPacket(buffer, length_bytes, host, port);
                socket.receive( respuesta );
                
                
                String datos = new String(respuesta.getData(), 0, respuesta.getLength());
                
                this.areaChat.append(datos.trim()+"\n");
            }
            
        } catch(Exception e){
            System.out.println("Error run(): "+e.getMessage());
        }
    }

    public void setAreaChat(JTextArea area){
        this.areaChat = area;
    }

    public void cerrarSocket(){
        try {
			this.socket.leaveGroup(this.host);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.socket.close();
    }

}
