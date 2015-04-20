/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

/**
 *
 * @author morell
 */
public class Id extends Instruction {

    String str;

    Id() {
        this.str = "";
    }

    Id(String id) {
        this.str = id;
    }

    String eval() {
        return env.get(str);
    }

    
}

