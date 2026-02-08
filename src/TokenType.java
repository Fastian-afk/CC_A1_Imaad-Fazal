package src;

public enum TokenType {
    // Keywords
    START, FINISH, LOOP, CONDITION, DECLARE, OUTPUT, INPUT, 
    FUNCTION, RETURN, BREAK, CONTINUE, ELSE,
    
    // Literals
    IDENTIFIER,     // Starts with Uppercase
    INTEGER,        // No decimal
    FLOAT,          // Decimal or Scientific
    STRING,         // "..."
    CHAR,           // '.'
    BOOLEAN,        // true/false
    
    // Operators
    ARITHMETIC_OP,  // +, -, *, /, %, **
    RELATIONAL_OP,  // ==, !=, <=, >=, <, >
    LOGICAL_OP,     // &&, ||, !
    ASSIGNMENT_OP,  // =, +=, -=, *=, /=
    INC_DEC_OP,     // ++, --
    
    // Punctuators
    LPAREN, RPAREN, // ( )
    LBRACE, RBRACE, // { }
    LBRACKET, RBRACKET, // [ ]
    COMMA, SEMICOLON, COLON,
    
    // Special
    EOF,            // End of File
    ERROR           // For ErrorHandler
}