package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap; 

public class ManualScanner {
    private String source;
    private int current = 0;
    public int line = 1;      
    private int col = 0;      
    private int start = 0; 
    
    private SymbolTable symbolTable;
    private ErrorHandler errorHandler;
    
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("start", TokenType.START);
        keywords.put("finish", TokenType.FINISH);
        keywords.put("loop", TokenType.LOOP);
        keywords.put("condition", TokenType.CONDITION);
        keywords.put("declare", TokenType.DECLARE);
        keywords.put("output", TokenType.OUTPUT);
        keywords.put("input", TokenType.INPUT);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("return", TokenType.RETURN);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("else", TokenType.ELSE);
        keywords.put("true", TokenType.BOOLEAN);
        keywords.put("false", TokenType.BOOLEAN);
    }

    public ManualScanner(String filePath, SymbolTable st, ErrorHandler eh) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String lineStr;
            while ((lineStr = reader.readLine()) != null) {
                sb.append(lineStr).append("\n");
            }
        }
        this.source = sb.toString();
        this.symbolTable = st;
        this.errorHandler = eh;
    }

    private boolean isAtEnd() { return current >= source.length(); }
    
    private char advance() { 
        col++;
        return source.charAt(current++); 
    }
    
    private char peek() { return isAtEnd() ? '\0' : source.charAt(current); }
    
    private char peekNext() { 
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    public Token nextToken() {
        skipWhitespace();
        if (isAtEnd()) return new Token(TokenType.EOF, "", line, col);

        start = current;
        char c = advance();
        int tokenCol = col; 

        if (Character.isLetter(c)) return processWord(tokenCol);
        
        if (Character.isDigit(c)) return number(tokenCol);
        
        // Handle negative numbers vs operators
        if (c == '-' && Character.isDigit(peek())) return number(tokenCol);
        if (c == '+' && Character.isDigit(peek())) return number(tokenCol);

        if (c == '"') return string(tokenCol);
        if (c == '\'') return character(tokenCol);

        switch (c) {
            case '(': return new Token(TokenType.LPAREN, "(", line, tokenCol);
            case ')': return new Token(TokenType.RPAREN, ")", line, tokenCol);
            case '{': return new Token(TokenType.LBRACE, "{", line, tokenCol);
            case '}': return new Token(TokenType.RBRACE, "}", line, tokenCol);
            case '[': return new Token(TokenType.LBRACKET, "[", line, tokenCol);
            case ']': return new Token(TokenType.RBRACKET, "]", line, tokenCol);
            case ',': return new Token(TokenType.COMMA, ",", line, tokenCol);
            case ';': return new Token(TokenType.SEMICOLON, ";", line, tokenCol);
            case ':': return new Token(TokenType.COLON, ":", line, tokenCol);
            
            case '#':
                if (peek() == '#') { // Single line
                    while (peek() != '\n' && !isAtEnd()) advance();
                    return nextToken(); 
                } else if (peek() == '*') { // BONUS TASK 2: Nested Multi-line Comments 
                    advance(); // consume *
                    int depth = 1;
                    
                    while (!isAtEnd() && depth > 0) {
                        if (peek() == '#' && peekNext() == '*') { 
                             // Found nested start "#*"
                             advance(); advance();
                             depth++;
                        } else if (peek() == '*' && peekNext() == '#') { 
                             // Found nested end "*#"
                             advance(); advance();
                             depth--;
                        } else {
                             // Just normal comment content 
                             if (peek() == '\n') { line++; col = 0; }
                             advance();
                        }
                    }
                    
                    if (depth > 0) {
                        errorHandler.reportError("Unclosed Comment", line, tokenCol, "#*", "Nested comment not closed");
                        return new Token(TokenType.EOF, "", line, col);
                    } // For unclosed comments
                    
                    // Successfully skipped comment, recurse to find next real token
                    return nextToken();
                }
                break; 
            
            case '+': 
                if (peek() == '+') { advance(); return new Token(TokenType.INC_DEC_OP, "++", line, tokenCol); }
                if (peek() == '=') { advance(); return new Token(TokenType.ASSIGNMENT_OP, "+=", line, tokenCol); }
                return new Token(TokenType.ARITHMETIC_OP, "+", line, tokenCol);
            case '-':
                if (peek() == '-') { advance(); return new Token(TokenType.INC_DEC_OP, "--", line, tokenCol); }
                if (peek() == '=') { advance(); return new Token(TokenType.ASSIGNMENT_OP, "-=", line, tokenCol); }
                return new Token(TokenType.ARITHMETIC_OP, "-", line, tokenCol);
            case '*':
                if (peek() == '*') { advance(); return new Token(TokenType.ARITHMETIC_OP, "**", line, tokenCol); }
                if (peek() == '=') { advance(); return new Token(TokenType.ASSIGNMENT_OP, "*=", line, tokenCol); }
                return new Token(TokenType.ARITHMETIC_OP, "*", line, tokenCol);
            case '/':
                if (peek() == '=') { advance(); return new Token(TokenType.ASSIGNMENT_OP, "/=", line, tokenCol); }
                return new Token(TokenType.ARITHMETIC_OP, "/", line, tokenCol);
            case '=':
                if (peek() == '=') { advance(); return new Token(TokenType.RELATIONAL_OP, "==", line, tokenCol); }
                return new Token(TokenType.ASSIGNMENT_OP, "=", line, tokenCol);
            case '!':
                if (peek() == '=') { advance(); return new Token(TokenType.RELATIONAL_OP, "!=", line, tokenCol); }
                return new Token(TokenType.LOGICAL_OP, "!", line, tokenCol);
            case '<':
                if (peek() == '=') { advance(); return new Token(TokenType.RELATIONAL_OP, "<=", line, tokenCol); }
                return new Token(TokenType.RELATIONAL_OP, "<", line, tokenCol);
            case '>':
                if (peek() == '=') { advance(); return new Token(TokenType.RELATIONAL_OP, ">=", line, tokenCol); }
                return new Token(TokenType.RELATIONAL_OP, ">", line, tokenCol);
            case '&':
                if (peek() == '&') { advance(); return new Token(TokenType.LOGICAL_OP, "&&", line, tokenCol); }
                break;
            case '|':
                if (peek() == '|') { advance(); return new Token(TokenType.LOGICAL_OP, "||", line, tokenCol); }
                break;
        }

        errorHandler.reportError("Invalid Character", line, tokenCol, String.valueOf(c), "Unexpected character");
        return nextToken();
    }

    private void skipWhitespace() {
        while (true) {
            char c = peek();
            if (c == ' ' || c == '\r' || c == '\t') {
                advance();
            } else if (c == '\n') {
                line++;
                col = 0; // Reset column counter
                advance();
            } else {
                return;
            }
        }
    }

    private Token processWord(int startCol) {
        while (Character.isLetterOrDigit(peek()) || peek() == '_') advance();
        
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);

        if (type != null) {
            return new Token(type, text, line, startCol);
        }

        if (Character.isUpperCase(text.charAt(0))) {
            if (text.length() > 31) {
                errorHandler.reportError("ID Length", line, startCol, text, "Exceeds 30 characters");
            }
            symbolTable.add(text, "IDENTIFIER", line);
            return new Token(TokenType.IDENTIFIER, text, line, startCol);
        }

        errorHandler.reportError("Invalid Identifier", line, startCol, text, "Identifiers must start with Uppercase");
        return new Token(TokenType.IDENTIFIER, text, line, startCol);
    }

    private Token number(int startCol) {
        boolean isFloat = false;
        if (peek() == '-' || peek() == '+') advance(); 
        
        while (Character.isDigit(peek())) advance();

        if (peek() == '.') {
            isFloat = true;
            advance();
            while (Character.isDigit(peek())) advance();
        }

        if (peek() == 'e' || peek() == 'E') {
            isFloat = true;
            advance();
            if (peek() == '+' || peek() == '-') advance();
            while (Character.isDigit(peek())) advance();
        }

        String text = source.substring(start, current);
        return new Token(isFloat ? TokenType.FLOAT : TokenType.INTEGER, text, line, startCol);
    }

    private Token string(int startCol) {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') { line++; col=0; }
            
            if (peek() == '\\') {
                advance(); // consume the backslash
                
                // BONUS TASK 3: Unicode Escape Support (\uXXXX)
                if (peek() == 'u') {
                    advance(); // consume 'u'
                    // Consume exactly 4 hex digits
                    for(int i=0; i<4; i++) {
                        if (!isAtEnd()) advance();
                    }
                } else {
                    // Consume standard escape char (n, t, r, ", \)
                    advance(); 
                }
            } else {
                advance(); // consume normal char
            }
        }

        if (isAtEnd()) {
            errorHandler.reportError("String Error", line, startCol, "...", "Unterminated string");
            return new Token(TokenType.EOF, "", line, col);
        }

        advance(); // The closing "
        String text = source.substring(start, current);
        return new Token(TokenType.STRING, text, line, startCol);
    }

    private Token character(int startCol) {
        if (peek() == '\\') advance(); 
        advance(); 
        if (peek() == '\'') {
            advance(); 
            return new Token(TokenType.CHAR, source.substring(start, current), line, startCol);
        } else {
             errorHandler.reportError("Char Error", line, startCol, "...", "Malformed character");
             return nextToken();
        }
    }

    public static void main(String[] args) {
        try {
            // Default to test1 if no argument is provided
            String filePath = (args.length > 0) ? args[0] : "tests/test1.lang";
            
            System.out.println("Reading file: " + filePath);
            
            SymbolTable st = new SymbolTable();
            ErrorHandler eh = new ErrorHandler();
            ManualScanner scanner = new ManualScanner(filePath, st, eh);

            System.out.println("--- Manual Scanner Output ---");
            
            // Statistics Variables
            int totalTokens = 0;
            Map<TokenType, Integer> tokenCounts = new TreeMap<>(); // TreeMap keeps keys sorted
            
            Token token = scanner.nextToken();
            while (token.type != TokenType.EOF) {
                System.out.println(token);
                
                // Update Stats
                totalTokens++;
                tokenCounts.put(token.type, tokenCounts.getOrDefault(token.type, 0) + 1);
                
                token = scanner.nextToken();
            }
            
            // --- STATISTICS DISPLAY (Requirement 4.2 D) ---
            System.out.println("\n--- Scanner Statistics ---");
            System.out.println("Total Tokens: " + totalTokens);
            System.out.println("Lines Processed: " + scanner.line);
            System.out.println("Token Counts by Type:");
            for (Map.Entry<TokenType, Integer> entry : tokenCounts.entrySet()) {
                System.out.println("  " + String.format("%-15s", entry.getKey()) + ": " + entry.getValue());
            }

            // Print Symbol Table
            st.printTable();
            
            // Print Errors
            if (eh.hasErrors()) {
                System.err.println("\n--- Errors Found ---");
            }

        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
