import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.*;;
public class DirectorioPeers {

    private HashMap<String, String> mapIdIp;
    private HashMap<String, String> mapIpId;

    private String direccionPropia;
    private String id;
    private UUID uuid;


    public DirectorioPeers(String direccion, String id){
        mapIdIp = new HashMap<>();
        mapIpId = new HashMap<>();

        this.id = id;
        this.uuid = UUID.fromString( this.id );

        this.direccionPropia = direccion;

        mapIdIp.put(id, direccionPropia);
        mapIpId.put(direccionPropia, id);

        System.out.println("keyset"+mapIpId.entrySet());
        for (Map.Entry<String, String> entrada : mapIpId.entrySet()){
            System.out.println("key: "+entrada.getKey());
            System.out.println("value: "+entrada.getValue());

        }
    }

    public void agregarNuevoPeer(String ip, String id){
        if(!existePeer(id)){
            this.mapIdIp.put(id, ip);
            this.mapIpId.put(ip, id);
        }

        System.out.println("El peer ya estaba registrado: id="+id+", ip="+ip);

    }

    public void agregarNuevosPeers(HashMap<String, String> mapIdIp){
        if(this.obtenerNumeroPeers() != 0){
            return;
        }

        this.mapIdIp.putAll(mapIdIp);
    }

    public String obtenerIpPeer(String id){
        return mapIdIp.get(id);
    }

    public String obtenerIdPeer(String ip){
        return mapIpId.get(ip);
    }  
    
    public ArrayList<Map.Entry<String, String>> obtenerPeersIpId(){
        ArrayList<Map.Entry<String, String>> listaPeers = new ArrayList<Map.Entry<String, String>>();

        for (Map.Entry<String, String> entry : mapIpId.entrySet()){
            listaPeers.add(entry);
        }

        System.out.println("lista peers:"+listaPeers);

        return listaPeers;
    }

    public boolean existePeer(String id){
        return ( mapIdIp.containsKey( id ) );
    }

    public int obtenerNumeroPeers(){
        return mapIdIp.size();
    }

    public String obtenerDireccionPropia(){
        return this.direccionPropia;
    }

    public boolean tengoIdSuperior(){
        ArrayList<String> listaIds = new ArrayList<String>(mapIdIp.keySet());

        listaIds.remove( this.id );

        for (String id : listaIds){
            UUID uudiEnLista = UUID.fromString( id );

            if ( uuid.compareTo( uudiEnLista ) <= 0){
                return false;
            }
        }

        return true;
    }

    public void eliminarPeer(String ip){
        String id = mapIdIp.get(ip);

        mapIpId.remove(ip);
        mapIdIp.remove(id);
    }

    public boolean tengoContactos(){
        return (mapIpId.size() != 0);
    }

    public HashMap<String, String> obtenerMapIpId(){
        return this.mapIpId;
    }
}
