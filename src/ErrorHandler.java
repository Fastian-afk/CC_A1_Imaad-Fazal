/**
 * ErrorHandler
 * -------------------------
 * Manages lexical or parsing errors encountered during program execution.
 * It records formatted error messages including error type, location,
 * offending lexeme, and explanation, while also printing them to the
 * standard error stream. Provides a utility method to check whether any
 * errors have been reported during processing.
 */


package src;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private List<String> errors = new ArrayList<>();

    public void reportError(String type, int line, int column, String lexeme, String reason) {
        String errorMsg = String.format("Error: [%s] at %d:%d - Lexeme: '%s' -> %s", 
            type, line, column, lexeme, reason);
        errors.add(errorMsg);
        System.err.println(errorMsg);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
