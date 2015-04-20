
package language;

/**
 *
 * @author morell
 */
public class Scanner {

    Token[] start;
    Buffer buffer;
    Token t;   // Most recently read token

    Scanner(Buffer b) {
        start = new Token[128];
        for (int i = 0; i < 128; i++) {
            start[i] = new BadToken();
            start[i].setBuffer(b);
        }
        buffer = b;
    }
    Scanner () {
        buffer = new Buffer();
        start = new Token[128];
        for (int i = 0; i < 128; i++) {
            start[i] = new BadToken();
            start[i].setBuffer(buffer);
        }
    }

    void setSource(String string) {
        buffer.setSource(string);
    }
    
  
    void init(String chars, Token t) {
        for (int i = 0; i < chars.length(); i++) {
            start[chars.charAt(i)] = t;
            t.setBuffer(buffer); // ensure every token uses the same buffer
        }
    }

    Token getCurrentToken() {
        return t;
    }

    Token get() {
        buffer.skipBlanks();
        if (buffer.eof()) {
            t = new EofToken();
            return t;
        } else {
            char ch = buffer.peek();
            t = start[ch].get();
            while (!buffer.eof() && t instanceof BadToken) {
                //System.err.println("Invalid character '"+ ch +"' found.  Skipping it" );
                buffer.advance();
                buffer.skipBlanks();
                
                ch = buffer.peek();
                t = start[ch].get();
            }
            if (buffer.eof()) {
                
                return t;
            }
            return t;
        }
    }    
}
