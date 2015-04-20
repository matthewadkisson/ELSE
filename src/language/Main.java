 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author morell
 */
public class Main {
   /* Build the tree for 3*14/8 using the grammar
    <term> :: = <factor> | <factor> <multend> 
    <multend> ::= <multop> <factor> | <multop> <factor> <multend> 
    <factor> :: = ( <exp> ) | <no> 
    <multop> ::= * | /
   
    T
    /  \
    /    \                         where: 
    /      \
    /        \                            T = Term
    /          \                           ME = MultEnd
    |           ME                          MO = MultOp
    |         / | \                         F = Factor
    |        /  |  \
    |       /   |   \
    |      /    |     \
    |     /     |       \
    |    |      |        ME
    |    |      |      /    \
    |    |      |     |      |
    F    |      F     |      F
    |    |      |     |      |
    No   MO    No     MO     No
    |    |      |     |      |
    3    *     14     /      5
    */

//    public static void buildEvalTree(String[] args) {
//        MultOp mo;
//        Factor f;
//        MultEnd me;
//        Term t;
//
//        f = new FactorNo(new No(5));
//        mo = new MultOpDivide();
//        me = new MultEnd_MultOp_Factor(mo, f);
//        mo = new MultOpMultiply();
//        f = new FactorNo(new No(14));
//        me = new MultEnd_MultOp_Factor_MultEnd(mo, f, me);
//        f = new FactorNo(new No(3));
//        t = new TermMultEnd(f, me);
//
//        System.out.println(f.eval());
//        System.out.println(t.eval());
//        String source = "3*14/8";
//        GenericScanner scanner = new GenericScanner(new Buffer(source));
//        GenericScanner.addLegalOperator("*", new MultOpToken());
//        GenericScanner.addLegalOperator("/", new DivideOpToken());
//        GenericScanner.addLegalOperator("-", new MinusOpToken());
//
//        GenericScanner.get();
//        Term term = Term.parse();
//        int eval = term.eval();
//        System.out.println(eval);
//    }
   static void testScanner(Scanner scanner) {
      // Setup a buffer 

      Token t = scanner.get();
      while (!(t instanceof BadToken) && !(t instanceof EofToken)) {
         System.out.println(t.getStringValue());
         t = scanner.get();
      }
      if (!(t instanceof EofToken)) {
         System.out.println("Error should have been at end of file");
      }

   }

   static void init(Scanner scanner) {
      scanner.init("0123456789", new NumberToken());
      scanner.init("abcdefghijklmnopqrstuvwxyz"
         + "ABCEDFGHIJKLMNOPQSTUVWXYZ", new IdToken());
      scanner.init("+", new PlusOpToken());
      scanner.init("-", new MinusOpToken());
      scanner.init("*", new MultOpToken());
      scanner.init("/", new DivideOpToken());
      scanner.init("(", new LeftParenToken());
      scanner.init(")", new RightParenToken());
      scanner.init(":", new AssignToken());
      scanner.init("\"", new StringToken());
      scanner.init(",", new CommaToken());
      Instruction.setScanner(scanner);
   }

   public static void main(String[] args) throws RemoteException, AlreadyBoundException {
      System.setProperty("java.rmi.server.hostname", "localhost");
      //setup mailbox
      Map<String, LinkedList<String>> mailbox = new HashMap<>();
      LinkedList ll = new LinkedList<>();
      ll.add("0");
      mailbox.put("toasty", ll);
      Instruction.setMailbox(mailbox);
      
      //setup listener
      Listener svr = new Listener();
      svr.setMailbox(mailbox);
      svr.startRegistry();      
      svr.registerObject("Listener", svr);

      
      Scanner scanner = new Scanner();
      init(scanner);
      // Initialize scanner to process various tokens
      // Set the scanner to be globally available to all parsers
      /*
       scanner.setSource(
         "print -3*---4 "
       + "print (5*(7*8+7)) "
       + "print 3*4; "
       + "p := 2 " 
       //+ "print \"tit\" "
       + "x := \"horse titties\" "
       + "print x "
       + "print p "      
       + "send(\"localhost\" , 56) "
       + "b := 3 "
       + "c := a+b "
       + "if -c then "
       + "   print (a+b)+c "
       + "   print c "
       + "else "
       + "   print a*b "
       + "   print c "
       + "endif "
       + "b := b*b "
       + "while b do "
       + "   print b "
       + "   b := b -1 "
       + "done "
       );
       */
      
      //scanner.setSource(" "
      //   + "wait(\"localhost\", joe) "
      //   + "print joe "
      //   + "done "
      //);
      
      
      scanner.setSource(" "
         + "send(\"localhost\", 22) "
         + "wait(\"localhost\", a) "
         + "print a "
         + "done "
      );
      

      // Load the first token into the scanner
      scanner.get();

      // Initialize the run-time environment to have some entries
      Environment env = new Environment();      
      env.put("qw", "chickenTenders");
      Instruction.setEnvironment(env);

      // Parse the program 
      Stmt instr = StmtList.parse();

      // Execute the program
      instr.eval();
      
      System.exit(0);
   }
}
