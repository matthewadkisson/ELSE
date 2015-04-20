package language;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Sender extends UnicastRemoteObject implements MailInterface {

   private Registry registry2;   
   private final int PORT = 1104;

   @Override
   public String Message(String receiver, String S) throws RemoteException {     
      return null;
   }
 
   public Sender() throws RemoteException {
      super();
   }

   public void sendMail(String HOST, String msg) {
      try {         
         registry2 = LocateRegistry.getRegistry(HOST, PORT);
         MailInterface service = (MailInterface) registry2.lookup("Listener");
         service.Message(HOST, msg);

      } catch (Exception e) {
         System.out.println("Trying to connect...");
         sendMail(HOST, msg);          
         ///  horrifically abusing the stack
         ///.....sorry stack
      }
   }
   
}
