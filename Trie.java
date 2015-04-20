/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author morell
 */

class TrieNode {

    TrieNode[] next;
    String legalCharacters;
    boolean end;
    private Token token;

    TrieNode() {
        next = null;  // Only alloc when characters are provided
        end = false;
        legalCharacters = "";
        token = null;
    }

    public Token getToken() {
        return token;
    }

    void defineLegalChars(String legal) {
        legalCharacters = legal;
        if (next == null) {    // initialize the array to no successors
            next = new TrieNode[128];
            for (int i = 0; i < 128; i++) {
                next[i] = null;
            }
        }
        for (int i = 0; i < legal.length(); i++) {  // create self loop 
            next[legal.charAt(i)] = this;           // for each legal char
        }
    }

    /**
     *
     * @param str the string to be added to the trie as a token
     */
    void add(String str, Token t) {
        if (next == null) { // First addition; alloc and init next
            next = new TrieNode[128];
            for (int i = 0; i < 128; i++) {
                next[i] = null;
            }
        }
        // First see if there are any self loops at this node.  If there are,
        // they have to be replaced with links to new nodes, otherwise we will
        // mis-identify words as being special tokens because there is a loop
        // in the middle of them, e.g.  abc[a-z]*def.  Alternately, could leave
        // the loop in and just disallow any word from being an acceptable token
        // after traversing the loop even one time.  Thus, I comment out what
        // follows:
//        if (next[0] == this) {
//            for (int i = 0; i < 128; i++) {
//                next[i] = new TrieNode();
//            }
//        }
        // Now do whatever extension
        char ch = str.charAt(0);
        TrieNode tn = next[ch];
        if (tn == null || tn == this) {
            tn = new TrieNode();
            tn.defineLegalChars(legalCharacters);
        }
        if (str.length() > 1) {
            tn.add(str.substring(1), t);
        } else {
            tn.end = true; // Hey, the child must be the end of the token
            tn.token = t;  // Aha, all this work to use Strategy Pattern!
            System.out.println("Added " + ch + " with token " + t);
        }
        next[ch] = tn;

    }

    TrieNode advance(char ch) {
        TrieNode tn = next[ch];
        return tn;
    }

}

public class Trie {

    TrieNode root;
    String legalCharacters;
    Buffer buffer;
    Token token;   // The default token

    Trie(Buffer b, String legalCharacters, Token token) {
        root = new TrieNode();
        buffer = b;
        root.defineLegalChars(legalCharacters);
        this.legalCharacters = legalCharacters;
        this.token = token;
    }

    void defineLegalCharacters(String legalCharacters) {
        this.legalCharacters = legalCharacters;
    }

    void insert(String str, Token t) {
        System.out.println("Inserting " + str + " and token " + t);
        root.add(str, t);
    }

    Token get() {
        Token t = null;
        char ch;
        boolean useDefault = false;
        buffer.skipBlanks();
        TrieNode tn = root;
        TrieNode previous = root;
        StringBuilder value = new StringBuilder("");

        if (!buffer.eof()) {
            ch = buffer.get();
            do {
                previous = tn;
                tn = tn.advance(ch);
                if (tn == previous) // self-loop
                {
                    useDefault = true;
                }
                if (tn != null) {
                    System.out.println("Got " + ch + ", token is " + tn.getToken());
                    value.append(ch);
                    ch = buffer.get();
                } else {
                    System.out.println("Encountered delimiter '" + ch + "'");
                }

            } while (!buffer.eof() && tn != null);  // more input and more trie
            tn = previous;// Get back onto the trie
            if (tn != root) {
                buffer.backup();
            }

            // useDefault ==> A loop was traversed
            // tn.getToken() == null ==> no token posted where the string stopped
            //                       ==> must use the default for the trie as well
            if (useDefault || (t = tn.getToken()) == null) { // default value
                try {
                    t = token.getClass().newInstance(); // how about a factory!
                    System.out.println("1. setting token to '" + value + "'");
                    t.setStringValue(value.toString());
                    return t;
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(Trie.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Found the special token " + t);
                return t;
            }
        }
        return t;
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer("     lawrence\nlarry  */- >lan language82 laproscopic17");
        Trie trieId = new Trie(buffer, "abcdefghijklmnopqrastuvwxyz", new IdToken(""));
        Trie trieNumber = new Trie(buffer, "0123456789", new NumberToken("0"));
        Trie trieOperator = new Trie(buffer, "", new BadToken(""));
        // Insert keywords 
        trieId.insert("larry", new IdToken("larry"));
        trieId.insert("lawrence", new IdToken("lawrence"));
        trieId.insert("language", new IdToken("language"));
        trieId.insert("lan", new IdToken("lan"));
        trieOperator.insert("*", new MultOpToken());
        trieOperator.insert("/", new DivideOpToken());
        trieOperator.insert("-", new MinusOpToken());

        Token token;
        
        
        // Now process the input buffer token by token
//        while ((token= t.get()) != null) {
        buffer.skipBlanks();
        while (!buffer.eof()) {

            char ch = buffer.peek();
            if (Character.isLetter(ch)) {
                token = trieId.get();
            } else if (Character.isDigit(ch)) {
                token = trieNumber.get();
            } else { // Hopefully an` operator
                token = trieOperator.get();
            }
            System.out.println(token);

            buffer.skipBlanks();
        }

    }
}
