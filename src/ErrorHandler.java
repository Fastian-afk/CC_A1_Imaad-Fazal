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