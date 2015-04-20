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
public class Factor extends Instruction {

    Factor() {
    }

    String eval() {
        return null;
    }

    // <Factor> ::= <No> | ( <Exp> ) | <Var> | string
    static Factor parse() {
        Token t = scanner.getCurrentToken();
        Factor factor = null;
        boolean isNegative = false;
        while (t instanceof MinusOpToken) {
            isNegative = !isNegative;
            scanner.get();
            t = scanner.getCurrentToken();
        }
        if (t instanceof LeftParenToken) {
            scanner.get();  // Toss the lparen
            Exp exp = Exp.parse(); // Change to Exp when that is implemented
            t = scanner.getCurrentToken();
            if (!(t instanceof RightParenToken)) {
                System.err.println("Expected ')', found" + t);
                System.exit(1);
            } else {
                scanner.get();  // Toss the rparen
                factor = new FactorExp(exp);
            }
        } else if (t instanceof NumberToken) {
            No no = No.parse();
            factor = new FactorNo(no);
        } else if (t instanceof IdToken) {
            factor = new FactorId((IdToken) t);
            scanner.get();  // Advance to next token
        } else if (t instanceof StringToken){
           St st = St.parse();
           factor = new FactorString(st);
        }
        
        if (isNegative)
            factor = new Negative(factor);
        return factor;
    }
}

class Negative extends Factor{ // decorator
    Factor factor;
    Negative (Factor factor) {
        this.factor = factor;
    }
    String eval(){
        return Integer.toString(- Integer.parseInt(factor.eval()));
    }
}
class FactorExp extends Factor {

    Exp exp;
    Term term;

    public FactorExp(Exp e) {
        exp = e;
    }

    String eval() {
        return exp.eval();
    }

}

class FactorNo extends Factor {

    No n;

    FactorNo(No number) {
        n = number;
    }

    String eval() {
        return n.eval();
    }
}

class FactorId extends Factor {

    IdToken id;

    FactorId(IdToken id) {
        this.id = id;
    }

    @Override
    String eval() {
        return env.get(id.getStringValue());
    }
    
    public String getId(){
       return id.toString();
    }

}

class FactorString extends Factor {

    St s;

    FactorString(St str) {
        this.s = str;
    }

    @Override
    String eval() {
        return s.eval();
    }

}
