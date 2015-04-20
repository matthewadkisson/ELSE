package language;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;

//
// PowerService Interface
//
// Interface for a RMI service that calculates powers
//
public interface MailInterface extends java.rmi.Remote {
   public String Message(String receiver, String s)
      throws RemoteException;
}
