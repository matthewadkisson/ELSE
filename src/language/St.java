/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

/**
 *
 * @author Andrew
 */
public class St extends Instruction {

    String str;

    St(String s) {
        str = s;
    }

    @Override
    String eval() {
        return str;
    }

    static St parse() {
        Token t = scanner.getCurrentToken();        
       
        if (t instanceof StringToken) {
            St s = new St(((StringToken) t).getStringValue());          
            scanner.get(); // Prefetch the next token
            return s;
        }
        return null;

    }
}
