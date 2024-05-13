import java.rmi.*;
import java.util.ArrayList;

public interface RMIInterface extends Remote{
    public ArrayList<String> listaArchivos() throws Exception ;
    public byte[] descargarArchivo(String nombrearchivo) throws Exception;
}

