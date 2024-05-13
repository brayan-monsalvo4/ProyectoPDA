import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class EDOImplementacion extends UnicastRemoteObject implements EDOInterface {
    String name;
    
    public EDOImplementacion(String s)throws RemoteException{
        super();
        name = s;
    }

    @Override
    public byte[] descargarArchivo(String nombreArchivo) throws RemoteException {
        String rutaDescarga = "./downloads";
        String rutaCarga = "./uploads";
        File carpeta = new File(rutaDescarga);

        if(!carpeta.exists()){
            carpeta.mkdirs();
        }

        if(!(new File(rutaCarga + "/" + nombreArchivo)).exists()){
            System.out.println("Error, archivo no existe");
        }
        
        try{
            String rutaArchivo = rutaCarga + "/" + nombreArchivo;
            File archivo = new File(rutaArchivo);
            byte buffer[] = new byte[(int) archivo.length()];

            BufferedInputStream input = new BufferedInputStream(new FileInputStream(nombreArchivo));

            input.read(buffer,0,buffer.length);
            input.close();

            return(buffer);
        }catch(Exception e){
            System.out.println("Error EDOImplementacion descargarArchivo: "+e.getMessage());
            e.printStackTrace();
            return(null);
        }
    }

    @Override
    public String[] listaNombresArchivos() throws RemoteException {
        File archivos = new File("./uploads");
        ArrayList<String> nombres = new ArrayList<>();

        for (File archivo : archivos.listFiles()){
            if (archivo.isFile()){
                nombres.add( archivo.getName() );
            }
        }

        return (String[]) nombres.toArray();
    }

}