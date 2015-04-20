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
public class No extends Instruction {

    Number n;

    No(Number number) {
        n = number;
    }

    String eval() {
        return Integer.toString((int) n);
    }

    static No parse() {
        Token t = scanner.getCurrentToken();
        boolean negative = false;
        if (t instanceof MinusOpToken) {
            negative = true;
            t = scanner.get();
        }
        if (t instanceof NumberToken) {
            Number no = ((NumberToken) t).getNumber();
            int n = no.intValue();
            if (negative) n = -n;
            scanner.get(); // Prefetch the next token
            return new No(n);
        }
        return null;

    }
}
