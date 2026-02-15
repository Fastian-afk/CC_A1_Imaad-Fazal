/**
 * Token
 * -------------------------
 * Represents a lexical token produced by the scanner, containing the token
 * type, matched lexeme, and its position in the source code (line and column).
 * Provides a formatted string representation used for displaying scanner output
 * in a consistent and readable structure.
 */


package src;

public class Token {
    public TokenType type;
    public String lexeme;
    public int line;
    public int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        // Format required: <KEYWORD, "start", Line: 1, Col: 1>
        return String.format("<%s, \"%s\", Line: %d, Col: %d>", 
            type, lexeme, line, column);
    }
}
