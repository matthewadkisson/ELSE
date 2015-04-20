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

public class Exp extends Instruction {

    
    /*
     <exp> :: = <term> | <term> <addend> 
     */

    static Exp parse() {
        Term term;
        term = Term.parse();
        AddEnd addend;
        Exp result;

        // Now check to see if there is a multiply or a divide operator next
        Token t = scanner.getCurrentToken();
        if (t instanceof PlusOpToken || t instanceof MinusOpToken) {
            addend =  AddEnd.parse();
            result = new ExpTermAddEnd(term, addend);
        } else {
            result = new ExpTerm(term);
        }
        return result;
    }

}

class ExpTerm extends Exp {

    Term term;

    public ExpTerm(Term term) {
        this.term = term;
    }
    String eval () {
       return term.eval();
    }
}
class ExpTermAddEnd extends Exp {

   Term term;
   AddEnd addend;

   public ExpTermAddEnd(Term t, AddEnd ae) {
      this.term = t;
      this.addend = ae;
   }
   
   String eval () {
      int left = Integer.parseInt(term.eval());
      
      return addend.eval(left);
   }

}


