import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
    public static void main(String[] args) throws Exception {
        try{
            InetAddress inetAddress = InetAddress.getLocalHost();
            String direccion = "/"+InetAddress.getLocalHost().toString().split("/")[1];

            Registry registry = LocateRegistry.createRegistry(1099);
            RMIInterface fi = new RMIImpl();
            Naming.rebind("/"+direccion+"/RMIImpl", fi);

            System.out.println("direccion naming: "+direccion);
            System.out.println("No error"); 
    
            VentanaPrincipal ventanita = new VentanaPrincipal();

        }catch(Exception e){
            System.out.println("Error App: "+e.getCause());
            e.printStackTrace();
        }
        
           
    }
}
