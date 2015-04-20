package language;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.LinkedList;
import java.util.Map;

public class Listener extends UnicastRemoteObject
   implements MailInterface {

   Map<String, LinkedList<String>> mailbox;
   private final int SERVERPORT = 1105;//port the server runs on
   private Registry registry;

   void setMailbox(Map<String, LinkedList<String>> m) {
      mailbox = m;
   }

   public Registry getRegistry() {
      return registry;
   }

   public void startRegistry() {
      try {
         registry = java.rmi.registry.LocateRegistry.createRegistry(SERVERPORT);
      } catch (Exception e) {
         System.out.println(e);
      }
   }

   public void registerObject(String name, Remote remoteObj) {
      try {
         registry.rebind(name, remoteObj);
      } catch (Exception e) {
         System.out.println(e);
      }
   }

   public Listener() throws RemoteException {
      super();
   }

   @Override
   public String Message(String receiver, String S) {
      if (mailbox.containsKey(receiver)) {
         LinkedList<String> box;
         box = mailbox.get(receiver);
         box.add(S);
      } else {
         LinkedList<String> box = new LinkedList<>();
         box.add(S);
         mailbox.put(receiver, box);
      }
      return null;
   }

}
