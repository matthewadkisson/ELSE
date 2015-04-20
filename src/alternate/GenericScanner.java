/*
 *  Copyright (C) 2010 Larry Morell <morell@cs.atu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package language;

import language.Buffer;
import language.EofToken;
import language.IdToken;

import language.NumberToken;
import language.StringToken;
import language.Token;
import java.util.HashMap;

/**
 *
 * @author Larry Morell (lmorell@atu.edu)
 */
public class GenericScanner {

    // ---------------- Static variables ------------------//
    static HashMap<String, Token> keywords = new HashMap<>();
//    static NumberScanner numberScanner = null;
    static Token token;      // The most recent token returned by get()

    // ---------------- Static initialization -------------//
    // ---------------- Static methods --------------------//
    // ---------------- Instance variables ----------------//
    static Buffer buffer;             // The input source

    public GenericScanner(Buffer source) {
        buffer = source;

    }

    // ---------------- Getters/Setters -------------------//
    public static void addLegalOperator(String operator, Token token) {
        keywords.put(operator, token);
    }

    public static void setKeywords(HashMap<String, Token> keywords) {
        GenericScanner.keywords = keywords;
    }

    public static void addKeyword(String keyword, Token token) {
        keywords.put(keyword, token);
    }

//    public static void setNumberScanner(NumberScanner numberScanner) {
//        GenericScanner.numberScanner = numberScanner;
//    }

    public static Token getCurrentToken() {
        return token;
    }

    // ---------------- Other member functions ------------//
    public static Token get() {
        // Strategy is to skip whitespace, branch on the encountered character,
        // invoke the appropriate buffer routine, convert if necessary
        // NOTE: the variable ch throughout this code denotes the character
        // that will be read by the next call to get()
        Token t;
        String value;

        buffer.skipBlanks(); // align after ws
        int lineNo = buffer.lineNumber();
        int charPos = buffer.getPos();

        if (buffer.eof()) { // set up an EOF symbol
            t = new EofToken();
        } else {
            // for each of these form a token from it
            char ch = buffer.peek();

            if (Character.isLetter(ch) || ch == '_') {
                value = buffer.getId();
                t = keywords.get(value);

                if (t == null) {

                    t = new IdToken(value);
                }
            } else if (Character.isDigit(ch)) {
                value = buffer.getNumber(); // get the string version of the number

                t = new NumberToken(value);
            } else if (ch == '"' || ch == '\'') {  // it is a literal string
                char terminator = ch;
                StringBuilder val = new StringBuilder();
                // val.append(terminator);
                buffer.advance();
                ch = buffer.peek(); // get the first char after the quote

                while (!buffer.eof() && ch != terminator) {

                    if (ch == '\\') {
                        buffer.advance();
                        ch = buffer.peek();
                        if (ch == 'n') {
                            ch = '\n';
                        } else if (ch == 't') {
                            ch = '\t';
                        } else if (ch == 'r') {
                            ch = '\r';
                        }
                        // if the character is anything else, including a '\'
                        // then just keep it
                    }
                    val.append(ch);
                    buffer.advance();
                    ch = buffer.peek();
                }
                if (ch != terminator) {
                    System.err.println("Double quotes are not balanced.");
                    System.err.println("There was no matching  quote("
                        + terminator + ") "
                        + "for the quote found on or before line "
                        + lineNo + '.');
                    System.err.println("The mistake could well be much earlier in the algorithm.");
                    System.err.println("Check all pairs of quotes to find the mistake.");

                    System.exit(1);
                }
                // val.append(ch); // Append the "
                ch = buffer.get();

                t = new StringToken(val.toString());
            } else if (ch == '#') { // single line comment
                buffer.getToEoln();
                t = get(); // note the recursion
            } else if (ch == '/') { // Maybe a comment                                  
                ch = buffer.peek(1); // peek at the next character
                if (ch == '/') {  //  a //-comment found
                    buffer.getToEoln();
                    t = get();  // Note the recursion
                } else if (ch == '*') {  // sm: handle multi-line /* comments */
                    boolean mlComment = true;
                    ch = buffer.get();
                    ch = buffer.get();
                    while (!buffer.eof() && mlComment) {
                        if (ch == '*') {
                            ch = buffer.get();
                            if (ch == '/') {
                                mlComment = false;   // Muntha error
                                ch = buffer.get();
                            }
                        } else {
                            ch = buffer.get();
                        }
                    }
                    if (mlComment) {
                        System.err.println("'/*' comment not closed with '*/'");

                        t = new Token();  // not needed?
                    } else {
                        t = get();  // recursive call ... go get the token after the comment
                    }
                } else {
                    buffer.setPos(charPos);// backup, if needed
                    value = buffer.getOperator();
                    t = keywords.get(value);

                    if (t == null) {
                        System.err.println("The symbol '" + value + " is not a valid operator");
                        t = new Token();
                    }
                }
            } else { // not a digit, letter, quote, or comment, must be an operator
                value = buffer.getOperator();
                t = keywords.get(value);

                if (t == null) {
                    System.err.println("The symbol '" + value + " is not a valid operator");

                    t = new Token();
                }

            }
        }
        token = t; // save the token for calls to current()
        token.setFileName(buffer.getFileName());
        token.setLineNo(buffer.lineNumber());
        token.setCharPos(charPos);
        return token;

    }

    boolean eof() {
        return buffer.eof();
    }

    // Test the scanner
    public static void main(String[] args) {
        // First set up the grammar and some simple rules

        Token t;
        String[] testInput = new String[]{
            "do done if while pig", // various keywords
            "+ - += =+ /- -/ ", // various operators
            "a b c move def g", // ids
            "17 17.3 49.8 0 12345", // numbers
            "// This is a comment\n1 2 3", // single-line comment
            "#  this is a comment\na b c", // single-line comment
            "/* multi\nline\n  comment*/ with other ", // multi-line comment
            "'a string'",
            "'a string\n across several lines '",
            "\"a string\"",
            "\"a string \n across \n several lines\"",};
        testInput = new String[]{
            "+ - += =+ /- -/ ", // various operators

            "/* multi\nline\n  comment*/ with other ", // multi-line comment
        };
        for (String source : testInput) {
            System.out.println("Source =" + source);

            GenericScanner scanner = new GenericScanner(new Buffer(source));

            GenericScanner.addKeyword("do", new IdToken("do"));
            GenericScanner.addKeyword("done", new IdToken("done"));
            GenericScanner.addKeyword("if", new IdToken("if"));
            GenericScanner.addKeyword("while", new IdToken("while"));
            do {
                t = GenericScanner.get();
                System.out.println(t.getStringValue());
            } while (!scanner.eof());
        }
    }

    public Token current() {
        return this.token;
    }
}
