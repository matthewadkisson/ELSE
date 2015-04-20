/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author morell
 */
public class Instruction {

    static Scanner scanner;  // Lazy
    static Environment env;  // Lazy
    static Map<String, LinkedList<String>> mailbox;
    static Editor editor;
    
    static void setEditor(Editor e){
       editor = e;
    }
    
    static void setMailbox(Map<String, LinkedList<String>> m){
       mailbox = m;
    }

    static void setScanner(Scanner s) {
        scanner = s;
    }

    static void setEnvironment(Environment e) {
        env = e;
    }

    static Instruction parse() {
        return new Instruction();
    }

    String eval() {
        return null;
    }
    
    
}
