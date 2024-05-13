import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.net.InetAddress;

public class RMIImpl extends UnicastRemoteObject implements RMIInterface{
    public RMIImpl() throws RemoteException{

    }

    @Override
    public ArrayList<String> listaArchivos() throws Exception {
        ArrayList<String> listaArchivos = new ArrayList<String>();

        for (File archivo : (new File("./uploads")).listFiles()){
            if (!archivo.isFile()){
                continue;
            }

            String direccion = "/"+InetAddress.getLocalHost().toString().split("/")[1] + "@" + archivo.getName();

            listaArchivos.add(direccion);
        }

        return listaArchivos;
    }

    @Override
    public byte[] descargarArchivo(String nombrearchivo) throws Exception {
      try {
         File file = new File("./uploads/"+nombrearchivo);
         byte buffer[] = new byte[(int)file.length()];

         BufferedInputStream input = new BufferedInputStream(new FileInputStream( "./uploads/"+nombrearchivo ));
         input.read(buffer,0,buffer.length);
         input.close();
         return(buffer);
      } catch(Exception e){
         throw e;
      }
    }


}
