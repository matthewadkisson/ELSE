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
public class MultEnd extends Instruction {

    public MultEnd() {
    }

    String eval() {
        return null;
    }

    String eval(int left) {
        return null;
    }
    /* <multend> ::= <multop> <factor> | <multop> <factor> <multend> */

    static MultEnd parse() {
        MultOp multop = MultOp.parse();      
        Factor factor = Factor.parse();
        Token t = scanner.getCurrentToken();
        MultEnd multend;
        if (t instanceof DivideOpToken || t instanceof MultOpToken) {
            multend = parse();
            multend = new MultEnd_MultOp_Factor_MultEnd(multop, factor, multend);
        } else {
            multend = new MultEnd_MultOp_Factor(multop, factor);
        }
        return multend;
    }
}
 class MultEnd_MultOp_Factor extends MultEnd {
   MultOp mo;
   Factor f;

   public MultEnd_MultOp_Factor(MultOp mo, Factor f) {
      this.mo = mo;
      this.f = f;
   }
   
   String eval (int left) {
      char operator = mo.get();
      int right = Integer.parseInt(f.eval());
      return mo.eval(left,right);
   }
   
   
}
class MultEnd_MultOp_Factor_MultEnd extends MultEnd {

    MultOp multop;
    Factor factor;
    MultEnd me;

    public MultEnd_MultOp_Factor_MultEnd(MultOp multop, Factor factor, MultEnd multend) {
        this.multop = multop;
        this.factor = factor;
        this.me = multend;
    }

    String eval(int left) {
        char operator = multop.get();
        int right = Integer.parseInt(factor.eval());
        int result;
        if (operator == '*') {
            result = left * right;
        } else {
            result = left / right;
        }
        return me.eval(result);
    }

}
