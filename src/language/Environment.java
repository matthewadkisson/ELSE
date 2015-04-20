/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.util.HashMap;

public class Environment {
    //private HashMap<String,Integer> env;
    private HashMap<String,String> senv;
    
    Environment () {
      //  env = new HashMap<String, Integer>();
        senv = new HashMap<>();
    }    
    
    String get(String id) {        
        
       // if (env.get(id) == null && senv.get(id) == null) {
       //     System.err.println("Variable '" + id +"' does not exist ... exiting");
       //     System.exit(-1);
       // }
       // if(env.containsKey(id)){
       //    return env.get(id).toString();
       // }
        if(senv.containsKey(id)){
           return senv.get(id);
        } else{
           System.err.println("Variable '" + id +"' does not exist ... exiting");
           System.exit(-1);
        }
        
        return null;
    }  
    
   // void put (String id, int val) {
   //     Integer i = val;
   //     env.put(id,i);
   //     senv.remove(id);
   // }
    void put (String id, String val) {
        String s = val;
        senv.put(id,s);
        //env.remove(id);
    }
}
