import java.rmi.*;

public interface EDOInterface extends Remote{
    public byte[] descargarArchivo(String nombre) throws RemoteException;
    public String[] listaNombresArchivos() throws RemoteException;
}
