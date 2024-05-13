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
    private long uuid;

    public DirectorioPeers(String direccion, String id){
        mapIdIp = new HashMap<>();
        mapIpId = new HashMap<>();

        this.id = id;
        this.uuid = Long.valueOf(id);

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

    public String obtenerIpPeer(String id){
        return mapIdIp.get(id);
    }

    public String obtenerIdPeer(String ip){
        return mapIpId.get(ip);
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
        ArrayList<Long> lista = new ArrayList<>();

        for (String id: listaIds){
            lista.add( Long.valueOf( id ) );
        }

        Collections.sort(lista);

        System.out.println("mi id"+id);
        System.out.println("otro id segun la lista : "+lista);
        if (lista.get(0) == uuid){
            return true;
        }

        return false;
    }

    public void eliminarPeer(String ip){
        String id = mapIpId.get(ip);

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
