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
public class MultOp extends Instruction {

    String eval(int l, int r) {
        return null;
    }

    static MultOp parse() {
        Token t = scanner.getCurrentToken();
        MultOp multop = null;
        
        if (t instanceof DivideOpToken) {
            multop = new MultOpDivide();
        } else if (t instanceof MultOpToken) {
            multop = new MultOpMultiply();
        }
        else {
            System.err.println ("Expecting multiply or divide; found "+t);
            System.exit(-1);
        }
        scanner.get(); // Skip to next token
        return multop;
    }

    char get() {
        return 'c';
    }
}

class MultOpDivide extends MultOp {

    char operator;

    MultOpDivide() {
        operator = '/';
    }

    char get() {
        return operator;
    }

    @Override
    String eval(int left, int right) {
        return Integer.toString(left / right);
    }

}

class MultOpMultiply extends MultOp {

    char operator;

    MultOpMultiply() {
        operator = '*';
    }

    char get() {
        return operator;
    }

    @Override
    String eval(int left, int right) {
        return Integer.toString(left * right);
    }
}
