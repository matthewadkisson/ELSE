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
public class AddEnd extends Instruction {

    public AddEnd() {
    }

    @Override
    String eval() {
        return null;
    }

    String eval(int left) {
        return null;
    }
    /* <addend> ::= <addop> <term> | <addop> <term> <addend> */

    static AddEnd parse() {
        AddOp addop = AddOp.parse();
        Term term = Term.parse();
        AddEnd addend;
        Token t = scanner.getCurrentToken();
        if (t instanceof PlusOpToken || t instanceof MinusOpToken) {
            addend = AddEnd.parse();
            addend = new AddEnd_AddOp_Term_AddEnd(addop, term, addend);
        } else {
            addend = new AddEnd_AddOp_Term(addop, term);
        }
        return addend;
    }
}

class AddEnd_AddOp_Term extends AddEnd {

    AddOp addop;
    Term term;

    public AddEnd_AddOp_Term(AddOp addop, Term term) {
        this.addop = addop;
        this.term = term;
    }

    @Override
    String eval(int left) {
        char operator = addop.get();
        int right = Integer.parseInt(term.eval());
        return addop.eval(left, right);
    }

}

class AddEnd_AddOp_Term_AddEnd extends AddEnd {

    AddOp addop;
    Term term;
    AddEnd addend;

    public AddEnd_AddOp_Term_AddEnd(AddOp ao, Term t, AddEnd ae) {
        this.addop = ao;
        this.term = t;
        this.addend = ae;
    }

    @Override
    String eval(int left) {
        char operator = addop.get();
        int right = Integer.parseInt(term.eval());
        int result;
        if (operator == '+') {
            result = left + right;
        } else {
            result = left - right;
        }
        return addend.eval(result);
    }

}
