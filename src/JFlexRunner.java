/**
 * JFlexRunner
 * -------------------------
 * Serves as the entry point for executing the JFlex-generated lexical scanner.
 * It reads a source file, continuously invokes the scanner to obtain tokens,
 * and prints each token until the end-of-file is reached. The program also
 * handles file reading and scanner-related errors to ensure controlled execution
 * during lexical analysis testing.
 */


package src;

import java.io.FileReader;
import java.io.IOException;

public class JFlexRunner {
    public static void main(String[] args) {
        try {
            // Make sure this points to your actual test file
            String testFile = "tests/test1.lang";
            
            System.out.println("Reading from: " + testFile);
            Yylex scanner = new Yylex(new FileReader(testFile));
            Token token;
            
            System.out.println("--- JFlex Scanner Output ---");
            // Loop until End Of File (EOF)
            while ((token = scanner.yylex()) != null && token.type != TokenType.EOF) {
                System.out.println(token);
            }
            // Print the final EOF token for completeness
            if (token != null) {
                System.out.println(token);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Error e) {
            // Catch JFlex specific errors
            System.err.println("Scanner Error: " + e.getMessage());
        }
    }
}
