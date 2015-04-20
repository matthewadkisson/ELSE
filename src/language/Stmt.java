package language;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static language.Instruction.scanner;

/**
 *
 * @author morell
 */
public class Stmt extends Instruction {
   /* 
    <stmt> ::= <assignment> | <if> | <while> | <block> | wait | send
    <block> ::=  begin <stmtlist> end
    <stmtlist> ::= <stmt> | <stmt> <stmtlist>
    <assignment> ::= <id> := <exp>
    <if> ::= if <exp> then <stmt> | if <exp> then <stmt> else <stmt>
    
    */

   static Stmt parse() {
      Token t = scanner.getCurrentToken();
      Stmt stmt = null;

      if (t instanceof IfToken) {
         stmt = Stmt_If.parse();
      } else if (t instanceof WhileToken) {
         stmt = Stmt_While.parse();
      } else if (t instanceof PrintToken) {
         stmt = Stmt_Print.parse();
      } else if (t instanceof SendToken) {
         stmt = Stmt_Send.parse();
      } else if (t instanceof WaitToken) {
         stmt = Stmt_Wait.parse();
      } else {  // Must be an assignment statement
         stmt = Stmt_Assign.parse();
      }
      return stmt;
   }
}

class Stmt_Print extends Stmt {

   Exp exp;

   Stmt_Print(Exp exp) {

      this.exp = exp;
   }
   /* <print> ::= print  <exp> */

   static Stmt_Print parse() {
      Token t = scanner.getCurrentToken();
      if (!(t instanceof PrintToken)) {
         System.err.println("Exected 'print'; found " + t);
         System.exit(-1);
      }
      scanner.get(); // Toss the print

      return new Stmt_Print(Exp.parse());
   }

   @Override
   String eval() {
      String r = exp.eval();
      editor.printOut(r);
      System.out.println(r);
      return null;
   }
}

class Stmt_Assign extends Stmt {

   Token id;
   Exp exp;

   Stmt_Assign(Token id, Exp exp) {
      this.id = id;
      this.exp = exp;
   }
   /* <assign> ::= <id> := <exp> */

   static Stmt_Assign parse() {
      Token t = scanner.getCurrentToken();
      if (!(t instanceof IdToken)) {
         System.err.println("Exected id; found " + t);
         System.exit(-1);
      }
      scanner.get(); // Toss the id
      if (!(scanner.getCurrentToken() instanceof AssignToken)) {
         System.err.println("Expecting :=; found " + t);
         System.exit(-1);
      }
      scanner.get(); // Toss the :=      
      return new Stmt_Assign(t, Exp.parse());
   }

   @Override
   String eval() {
      String s = id.getStringValue();
      String r = exp.eval();      
      env.put(s, r);

      return null;
   }
}

class StmtList extends Stmt {
   /* <stmtlist> ::= <stmt> | <stmt> <stmtlist> */

   Stmt stmt;
   StmtList stmtlist;

   StmtList(Stmt s) {
      stmt = s;
      stmtlist = null;
   }

   StmtList(Stmt s, StmtList sl) {
      stmt = s;
      stmtlist = sl;
   }

   static StmtList parse() {
      Stmt stmt;
      StmtList stmtlist;

      stmt = Stmt.parse();
      // Check to see if another stmt follows
      Token t = scanner.getCurrentToken();
      if (t instanceof WhileToken
         || t instanceof IfToken
         || t instanceof PrintToken
         || t instanceof SendToken
         || t instanceof WaitToken
         || t instanceof IdToken) {
         stmtlist = StmtList.parse();
         return new StmtList(stmt, stmtlist);

      } else {

         return new StmtList(stmt, null);
      }
   }

   String eval() {
      stmt.eval();
      if (stmtlist != null) {
         stmtlist.eval();
      }
      return null;
   }
}

class Stmt_If extends Stmt {

   static Stmt_If parse() {
      scanner.get();  // Toss the if 
      Exp condition = Exp.parse();
      scanner.get(); // Toss the then
      Stmt thenStmt = StmtList.parse();
      if (scanner.getCurrentToken() instanceof ElseToken) {
         scanner.get(); // Skip the else
         Stmt elseStmt = StmtList.parse();
         scanner.get(); // Skip the endif
         return new Stmt_If_Then_Else(condition, thenStmt, elseStmt);
      } else {
         scanner.get(); // Skip the endif
         return new Stmt_If_Then(condition, thenStmt);
      }

   }
}

class Stmt_If_Then extends Stmt_If {

   Exp condition;
   Stmt thenStmt;

   Stmt_If_Then(Exp cond, Stmt t) {
      condition = cond;
      thenStmt = t;
   }

   @Override
   String eval() {
      int n = Integer.parseInt(condition.eval());
      if (n > 0) {
         thenStmt.eval();
      }
      return null; // Dummy value, never used
   }

}

class Stmt_If_Then_Else extends Stmt_If {

   Exp condition;
   Stmt thenStmt;
   Stmt elseStmt;

   Stmt_If_Then_Else(Exp cond, Stmt t, Stmt e) {
      condition = cond;
      thenStmt = t;
      elseStmt = e;
   }

   @Override
   String eval() {
      int n = Integer.parseInt(condition.eval());
      if (n > 0) {
         thenStmt.eval();
      } else {
         elseStmt.eval();
      }
      return null; // Dummy value, never used
   }
}

class Stmt_While extends Stmt {

   Exp condition;
   Stmt stmt;

   Stmt_While(Exp cond, Stmt s) {
      condition = cond;
      stmt = s;
   }

   static Stmt_While parse() {
      scanner.get(); // Toss the while
      Exp condition = Exp.parse();
      scanner.get(); // Toss the do
      Stmt stmt = StmtList.parse();
      scanner.get(); // Toss the done
      return new Stmt_While(condition, stmt);

   }

   @Override
   String eval() {
      int c = Integer.parseInt(condition.eval());
      while (c > 0) {
         stmt.eval();
         c = Integer.parseInt(condition.eval());
      }
      return null;

   }
}

class Stmt_Send extends Stmt {

   String receiver;
   String message;
   Exp arg1;
   Exp arg2;

   Stmt_Send() {
      receiver = "";
      message = "0";
   }

   Stmt_Send(Exp ar1, Exp ar2) {
      arg1 = ar1;
      arg2 = ar2;
   }

   static Stmt_Send parse() {
      Exp arg1;
      Exp arg2;

      
      scanner.get();
      arg1 = Exp.parse();
      scanner.get();
      arg2 = Exp.parse();
      Stmt_Send result = new Stmt_Send(arg1, arg2);
      scanner.get();

      return result;
   }

   @Override
   String eval() {
      try {
         receiver = arg1.eval();
         message = arg2.eval();

         Sender sender = new Sender();
         sender.sendMail(receiver, message);
      } catch (Exception e) {
         System.out.println("oooooo nodeSEx");
      }

      return null;
   }
}

class Stmt_Wait extends Stmt {

   String sender;
   String message;
   Exp arg1;   
   Factor arg2;

   Stmt_Wait() {
      sender = "";
      message = "";
   }

   Stmt_Wait(Exp ar1, Factor ar2) {
      arg1 = ar1;
      arg2 = ar2;
   }

   static Stmt_Wait parse() {
      Exp arg1;
      Factor arg2;

      scanner.get();
      arg1 = Exp.parse();
      scanner.get();
      arg2 = FactorId.parse();
      Stmt_Wait result = new Stmt_Wait(arg1, arg2);
      scanner.get();

      return result;
   }

   @Override
   String eval() {
      String msg;
      sender = arg1.eval();
      message = ((FactorId) arg2).getId();
      
      while(mailbox.get(sender) == null);
      /*
      if (mailbox.get(sender) == null) {         
         mailbox.put(sender, new LinkedList<>());
      }
      if (mailbox.get(sender).isEmpty()) {
         LinkedList<String> box = mailbox.get(sender);
         while (box.isEmpty()){
            //editor.printOut("stuck");
         }
      }
         */
      msg = mailbox.get(sender).pop();
      //mailbox.remove(sender);

      env.put(message, msg);

      return null;
   }
}
