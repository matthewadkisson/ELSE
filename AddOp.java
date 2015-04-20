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
public class AddOp extends Instruction {

   String eval(int l, int r) {
      return null;
   }

   static AddOp parse() {
      Token t = scanner.getCurrentToken();
      AddOp addop = null;
      if (t instanceof PlusOpToken) {
         addop = new AddOpPlus();
      } else if (t instanceof MinusOpToken) {
         addop = new AddOpMinus();
      } else {
         System.err.println("Expecting an addition opearator; found " + t);
         System.exit(-1);
      }
      scanner.get(); // Skip to next token
      return addop;

   }

   char get() {
      return 'c';
   }
}

class AddOpPlus extends AddOp {

   char operator;

   AddOpPlus() {
      operator = '+';
   }

   @Override
   char get() {
      return operator;
   }

   @Override
   String eval(int left, int right) {
      int i = left + right;
      return Integer.toString(i);
   }

}

class AddOpMinus extends AddOp {

   char operator;

   AddOpMinus() {
      operator = '-';
   }

   @Override
   char get() {
      return operator;
   }

   @Override
   String eval(int left, int right) {
      return Integer.toString(left - right);
   }
}
