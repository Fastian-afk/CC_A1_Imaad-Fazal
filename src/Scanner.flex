package src;
import src.TokenType;
import src.Token;

%%

%class Yylex
%public
%unicode
%line
%column
%type Token

%{
  private Token token(TokenType type) {
      return new Token(type, yytext(), yyline + 1, yycolumn + 1);
  }
  private Token token(TokenType type, String text) {
      return new Token(type, text, yyline + 1, yycolumn + 1);
  }
%}

/* Regex Patterns from Assignment */
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier     = [A-Z][a-zA-Z0-9_]{0,30}
Integer        = [+-]?[0-9]+
Float          = [+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?
String         = \"([^\\\"]|\\.)*\"
Char           = \'([^\\\']|\\.)\'

%%

<YYINITIAL> {
  /* Keywords */
  "start"        { return token(TokenType.START); }
  "finish"       { return token(TokenType.FINISH); }
  "loop"         { return token(TokenType.LOOP); }
  "condition"    { return token(TokenType.CONDITION); }
  "declare"      { return token(TokenType.DECLARE); }
  "output"       { return token(TokenType.OUTPUT); }
  "input"        { return token(TokenType.INPUT); }
  "function"     { return token(TokenType.FUNCTION); }
  "return"       { return token(TokenType.RETURN); }
  "break"        { return token(TokenType.BREAK); }
  "continue"     { return token(TokenType.CONTINUE); }
  "else"         { return token(TokenType.ELSE); }
  
  /* Booleans */
  "true"         { return token(TokenType.BOOLEAN); }
  "false"        { return token(TokenType.BOOLEAN); }

  /* Literals */
  {Identifier}   { return token(TokenType.IDENTIFIER); }
  {Integer}      { return token(TokenType.INTEGER); }
  {Float}        { return token(TokenType.FLOAT); }
  {String}       { return token(TokenType.STRING); }
  {Char}         { return token(TokenType.CHAR); }

  /* Operators */
  "**"           { return token(TokenType.ARITHMETIC_OP); }
  "=="           { return token(TokenType.RELATIONAL_OP); }
  "!="           { return token(TokenType.RELATIONAL_OP); }
  "<="           { return token(TokenType.RELATIONAL_OP); }
  ">="           { return token(TokenType.RELATIONAL_OP); }
  "&&"           { return token(TokenType.LOGICAL_OP); }
  "||"           { return token(TokenType.LOGICAL_OP); }
  "++"           { return token(TokenType.INC_DEC_OP); }
  "--"           { return token(TokenType.INC_DEC_OP); }
  "+="           { return token(TokenType.ASSIGNMENT_OP); }
  "-="           { return token(TokenType.ASSIGNMENT_OP); }
  "*="           { return token(TokenType.ASSIGNMENT_OP); }
  "/="           { return token(TokenType.ASSIGNMENT_OP); }

  "+"            { return token(TokenType.ARITHMETIC_OP); }
  "-"            { return token(TokenType.ARITHMETIC_OP); }
  "*"            { return token(TokenType.ARITHMETIC_OP); }
  "/"            { return token(TokenType.ARITHMETIC_OP); }
  "%"            { return token(TokenType.ARITHMETIC_OP); }
  "="            { return token(TokenType.ASSIGNMENT_OP); }
  "<"            { return token(TokenType.RELATIONAL_OP); }
  ">"            { return token(TokenType.RELATIONAL_OP); }
  "!"            { return token(TokenType.LOGICAL_OP); }

  /* Punctuators */
  "("            { return token(TokenType.LPAREN); }
  ")"            { return token(TokenType.RPAREN); }
  "{"            { return token(TokenType.LBRACE); }
  "}"            { return token(TokenType.RBRACE); }
  "["            { return token(TokenType.LBRACKET); }
  "]"            { return token(TokenType.RBRACKET); }
  ","            { return token(TokenType.COMMA); }
  ";"            { return token(TokenType.SEMICOLON); }
  ":"            { return token(TokenType.COLON); }

  /* Comments */
  "##" [^\r\n]* { /* Ignore Single Line */ }
  "#*" ~"*#"     { /* Ignore Multi Line */ }

  /* Whitespace */
  {WhiteSpace}   { /* Ignore */ }

  /* Error Fallback */
  .              { System.err.println("Illegal character: " + yytext()); return token(TokenType.ERROR); }
}