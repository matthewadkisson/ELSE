package language;

/**
 * The Token class associates strings with their source (filename, line, pos)
 * and symbol, and classifies them into one of the following:
 * <ul>
 * <li>&lt;id> -- a char followed by one or more chars, digits, or
 * underscores</li>
 * <li>&lt;no -- a typical floating point of integer literal </li>
 * <li>&lt;string -- a quote (single or double) followed by zero or more
 * intervening chars followed by a matching quote (single or double; special
 * characters must be escaped by a backslash. These include:
 * <ul>
 * <li>a backslash </li>
 * <li>a double quote (if the opening quote is a double quote)</li>
 * <li>a single quote (if the opening quote is a single quote)</li>
 * <li>a newline</li>
 * </ul>
 * </li>
 * <li>&lt;char> </li>
 * <li>&lt;operator</li>
 * </ul>
 *
 * @author Larry Morell <morell@cs.atu.edu>
 */
public class Token {

   //-----------------  Member variables -----------------//
   protected String str;           // The string extracted from the buffer
   protected Buffer buffer;
   protected String fileName;      // the name of the file from whence this token was extracted
   protected int lineNo;           // the line number in the source file
   protected int charPos;          // the character position in the source file

   //--------------------- Constructors: none -----------------//
   // Setters
   public void setBuffer(Buffer b) {
      buffer = b;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public void setLineNo(int lineNo) {
      this.lineNo = lineNo;
   }

   public void setCharPos(int charPos) {
      this.charPos = charPos;
   }

   public int getCharPos() {
      return charPos;
   }

   public String getFileName() {
      return fileName;
   }

   public int getLineNo() {
      return lineNo;
   }

   public String getStringValue() {
      return str;
   }

   public void setStringValue(String v) {
      str = v;
   }

   public String toString() {
      return str;
   }

   Token get() {
      buffer.get(); // Toss the operator
      return this;
   }
}

class BadToken extends Token {

   BadToken() {
      str = "";
   }

   BadToken(String chars) {
      str = chars;
   }
}

class EofToken extends Token {

   EofToken() {
      str = "eof";
   }
}

class IdToken extends Token {

   IdToken() {
      str = "";
   }

   IdToken(String v) {
      setStringValue(v);
   }
   
   final static IfToken ifToken = new IfToken();
   final static WhileToken whileToken = new WhileToken();
   final static ThenToken thenToken = new ThenToken();
   final static ElseToken elseToken = new ElseToken();
   final static DoToken doToken = new DoToken();
   final static DoneToken doneToken = new DoneToken();
   final static PrintToken printToken = new PrintToken();
   final static EndifToken endifToken = new EndifToken();
   final static SendToken sendToken = new SendToken();
   final static WaitToken waitToken = new WaitToken();

   @Override
   public Token get() {
      String id = buffer.getId();
      Token result = null;
      if (id.equals("if")) {
         result = ifToken;
      } else if (id.equals("then")) {
         result = thenToken;
      } else if (id.equals("else")) {
         result = elseToken;
      } else if (id.equals("endif")) {
         result = endifToken;
      } else if (id.equals("do")) {
         result = doToken;
      } else if (id.equals("done")) {
         result = doneToken;
      } else if (id.equals("while")) {
         result = whileToken;
      } else if (id.equals("print")) {
         result = printToken;
      } else if (id.equals("send")) {         
         buffer.advance();   //gobble up right paren 
         result = sendToken; 
      } else if (id.equals("wait")) {         
         buffer.advance();   //gobble up right paren
         result = waitToken;
      } else {
         result = new IdToken(id);
      }
      result.setBuffer(buffer);
      return result;
   }

   public String toString() {
      return str;
   }

   IdToken newInstance() {
      return new IdToken("");
   }
}

class DoToken extends Token {

   DoToken() {
      str = "do";
   }
}

class DoneToken extends Token {

   DoneToken() {
      str = "done";
   }
}

class EndifToken extends Token {

   EndifToken() {
      str = "end";
   }
}

class ThenToken extends Token {

   ThenToken() {
      str = "then";
   }
}

class ElseToken extends Token {

   ElseToken() {
      str = "else";
   }
}

class IfToken extends Token {

   IfToken() {
      str = "if";
   }
}

class WhileToken extends Token {

   WhileToken() {
      str = "while";
   }
}

class SendToken extends Token {   

   SendToken() {
      str = "Send";
   }
}

class WaitToken extends Token {  

   WaitToken() {
      str = "Wait";
   }   
}

class PrintToken extends Token {

   PrintToken() {
      str = "print";
   }
}

class LeftParenToken extends Token {

   public LeftParenToken() {
      str = "(";
   }
}

class MinusOpToken extends Token {

   public MinusOpToken() {
      str = "-";
   }
}

class PlusOpToken extends Token {

   public PlusOpToken() {
      str = "+";
   }
}

class DivideOpToken extends Token {

   public DivideOpToken() {
      str = "/";
   }
}

class MultOpToken extends Token {

   public MultOpToken() {
      str = "*";
   }
}

class CommaToken extends Token {

   public CommaToken() {
      str = ",";
   }
}

class AssignToken extends Token {

   static AssignToken at = new AssignToken();

   AssignToken() {
      str = ":=";
   }

   @Override
   Token get() {
      char ch = buffer.peek(1);
      if (ch == '=') {
         buffer.advance();
         buffer.advance();
         return at;
      } else { // Note to future self: This will need to be modified
         // if any other operator starting with : is added 
         System.err.println("Looking for :=; found just :");
         System.err.println("Skipping the :");
         buffer.advance();
         return null;
      }
   }
}

class NumberToken extends Token {

   Number number;

   public NumberToken() {
      str = "0";
      number = 0;
   }

   public NumberToken(String value) {
      this.str = value;
      if (value.contains(".")) {
         this.number = Double.parseDouble(value);
      } else {
         this.number = Integer.parseInt(value);
      }
   }

   @Override
   public Token get() {
      String n = buffer.getNumber();
      NumberToken no = new NumberToken(n);
      no.setBuffer(buffer);
      return no;
   }

   public Number getNumber() {
      return number;
   }
}

class RightParenToken extends Token {

   public RightParenToken() {
      str = ")";
   }

}

class StringToken extends Token { // Not used ... yet
   
   StringToken(){
      str = "";
   }

   StringToken(String s) {
      str = s;
   }
   
   @Override
   public Token get() {
      String s ="";
      char c;
      
      buffer.advance();      
      while((c = buffer.get()) != '"'){
         s = s + c;
      }      
      
      StringToken result = new StringToken(s);
      return result;
      
   }
   
   
}
