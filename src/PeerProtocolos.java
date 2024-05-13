import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PeerProtocolos implements Runnable {
    private MulticastSocket socket;
    private InetAddress host;
    private int port;
    public String id;

    public int length_bytes = 1024;

    private JTextArea areaLog;

    public static final String SCAN_PEERS = "SCAN_PEERS";
    public static final String PEER_HERE = "PEER_HERE";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String COORDINATOR_ALIVE = "COORDINATOR_ALIVE?";

    private DirectorioPeers directorio;

    public PeerProtocolos(String host, int port, String id, DirectorioPeers directorio){
        try{
            this.socket = new MulticastSocket(port);
            this.host = InetAddress.getByName(host);
            this.port = port;
            this.socket.joinGroup(this.host);
            this.id = id;

            ArrayList<Map.Entry<String, String>> lista = new ArrayList<>();
            Map.Entry<String, String> codigo = new AbstractMap.SimpleEntry<>("CODE", PeerProtocolos.SCAN_PEERS);
            Map.Entry<String, String> idEnviar = new AbstractMap.SimpleEntry<>("ID", this.id);
            
            lista.add(codigo);
            lista.add(idEnviar);

            this.enviarMensaje( lista.toString() );

            this.directorio = directorio;

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String mensaje){
        try{
            String usuario = System.getProperty("user.name");
            String mnsj = usuario + "#" + mensaje;
            byte[] datos = mnsj.getBytes();

            DatagramPacket paquete = new DatagramPacket(datos, datos.length, host, port);

            this.socket.send(paquete);
        }catch(Exception e){
            System.out.println("Peer protocolos: ");
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        try{
            //socket.setTimeToLive(255);
            while(true){
                byte[] buffer = new byte[ length_bytes ];
                DatagramPacket respuesta = new DatagramPacket(buffer, length_bytes, host, port);
                socket.receive( respuesta );
                
                String datos = new String(respuesta.getData(), 0, respuesta.getLength());
                String direccion = String.valueOf(respuesta.getAddress());
                String[] linkedString = datos.split("#");

                HashMap<String, String> mensaje = stringArrayListToDictionary(linkedString[1]);

                procesarMensaje(mensaje, direccion);
                
                this.areaLog.append(datos.trim()+"\n");
                
                Thread.sleep(3000);
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setAreaLog(JTextArea area){
        this.areaLog = area;
    }
    
    public void cerrarSocket(){
        try {
            ArrayList<Map.Entry<String, String>> lista = new ArrayList<>();
            Map.Entry<String, String> codigo = new AbstractMap.SimpleEntry<>("CODE", PeerProtocolos.DISCONNECT);
            lista.add(codigo);
            this.enviarMensaje( lista.toString() );

			this.socket.leaveGroup(this.host);
		} catch (IOException e) {
            System.out.println("PeerProcolos: error en cerrarSocket() = "+e.getMessage());
			e.printStackTrace();
		}

        this.socket.close();
    }

    public void procesarMensaje(HashMap<String, String> mensaje, String direccion){
        switch(mensaje.get("CODE")){
            case PeerProtocolos.SCAN_PEERS:
                this.codigoScanPeers(mensaje, direccion);
                break;
            
            case PeerProtocolos.PEER_HERE:
                this.codigoPeerHere(mensaje, direccion);
                break;

            case PeerProtocolos.DISCONNECT:
                this.codigoDisconnect(mensaje, direccion);
                break;

            case PeerProtocolos.COORDINATOR_ALIVE:
                this.codigoCoordinatorAlive(mensaje, direccion);
                break;
        }

    }

    public void codigoCoordinatorAlive(HashMap<String, String> mensaje, String direccion){
        if(directorio.tengoIdSuperior()){
            System.out.println("tengo ID superior (soy mas viejo en la red)");
        }
    }

    public void codigoDisconnect(HashMap<String, String> mensaje, String direccion){
        directorio.eliminarPeer(direccion);
        System.out.println("disconnect() peers registrados: "+(directorio.obtenerMapIpId().toString()));

    }

    public void codigoPeerHere(HashMap<String, String> mensaje, String direccion){
        if(!direccion.equals( directorio.obtenerDireccionPropia() )){
            directorio.agregarNuevoPeer(direccion, mensaje.get("ID"));
        }        
        

        System.out.println("codigoPeerHere() peers registrados: "+(directorio.obtenerMapIpId().toString()));
    }

    public void codigoScanPeers(HashMap<String, String> mensaje, String direccion){
        if(direccion.equals( directorio.obtenerDireccionPropia() )){
            return;
        }

        directorio.agregarNuevoPeer( direccion, mensaje.get("ID") );
        ArrayList<Map.Entry<String, String>> lista = new ArrayList<>();
        Map.Entry<String, String> codigo = new AbstractMap.SimpleEntry<>("CODE", PeerProtocolos.PEER_HERE);
        Map.Entry<String, String> idEnviar = new AbstractMap.SimpleEntry<>("ID", this.id);
        
        lista.add(codigo);
        lista.add(idEnviar);

        this.enviarMensaje( lista.toString() );
    }

    public HashMap<String, String> stringArrayListToDictionary(String str) {
        System.out.println("string que recibe: "+str);
        String[] partes = str.replaceAll("[\\[\\]]", "").split(",\\s*");

        HashMap<String, String> dictionary = new HashMap<>();

        for (String par : partes){
            String[] res = par.split("=");
            String clave = res[0];
            String valor = res[1];

            dictionary.put(clave, valor);
        }
        return dictionary;
    }

}
