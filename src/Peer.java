import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.apache.commons.*;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class Peer {
    private PeerChat peerChat;
    private PeerProtocolos peerProtocolos;
    private DirectorioPeers directorio;
    private String id;

    private Thread chat;
    private Thread protocolos;

    public Peer(String host){
        try{
            long marcaTiempo = getServerTime();

            this.id = generateUUID(marcaTiempo).toString();
            
            String direccion = "/"+InetAddress.getLocalHost().toString().split("/")[1];
            
            this.directorio = new DirectorioPeers(direccion, this.id);
            this.peerChat = new PeerChat(host=host, 10000, id=this.id, this.directorio);
            this.peerProtocolos = new PeerProtocolos(host=host, 8080, id = this.id, this.directorio);
            
        }catch(Exception e){
            System.out.println("error Peer(), "+e.getMessage());
        }

    }

    public void iniciarPeers(){
        chat = new Thread( this.peerChat );
        protocolos = new Thread( this.peerProtocolos );

        chat.start();
        protocolos.start();
    }

    public void detenerPeers(){
        peerChat.cerrarSocket();
        peerProtocolos.cerrarSocket();
    }

    public PeerChat obtenerPeerChat(){
        return this.peerChat;
    }

    public PeerProtocolos obtenerPeerProtocolos(){
        return this.peerProtocolos;
    }

    public DirectorioPeers obtenerDirectorio(){
        return this.directorio;
    }

    private long getServerTime() {
        NTPUDPClient client = new NTPUDPClient();
        try {
            client.open();
            InetAddress host = InetAddress.getByName("pool.ntp.org"); // Servidor NTP público
            TimeInfo timeInfo = client.getTime(host);
            return timeInfo.getMessage().getTransmitTimeStamp().getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis(); // En caso de error, utilizar el tiempo local
        } finally {
            client.close();
        }
    }

    private UUID generateUUID(long timestamp) {
        byte[] timestampBytes = ByteBuffer.allocate(Long.BYTES).putLong(timestamp).array();
        return UUID.nameUUIDFromBytes(timestampBytes);
    }
}
