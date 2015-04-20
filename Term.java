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
public class Term extends Instruction {

    String eval() {
        return null;
    }
    /*
     <term> :: = <factor> | <factor> <multend> 
     */

    static Term parse() {
        Factor factor;
        factor = Factor.parse();
        MultEnd multend;
        Term result;

        // Now check to see if there is a multiply or a divide operator next
        Token t = scanner.getCurrentToken();
        if (t instanceof DivideOpToken || t instanceof MultOpToken) {
       
            multend = (MultEnd) MultEnd.parse();
            result = new TermMultEnd(factor, multend);
        } else {
            result = new TermFactor(factor);
        }
        return result;
    }

}

class TermFactor extends Term {

    Factor factor;

    public TermFactor(Factor factor) {
        this.factor = factor;
    }
    @Override
    String eval () {
       return factor.eval();
    }
}
class TermMultEnd extends Term {

   Factor f;
   MultEnd me;

   public TermMultEnd(Factor f, MultEnd me) {
      this.f = f;
      this.me = me;
   }
   
   @Override
   String eval () {
      int left = Integer.parseInt(f.eval());
      
      return me.eval(left);
   }

}

