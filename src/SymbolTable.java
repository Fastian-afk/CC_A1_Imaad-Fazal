package src;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private static class SymbolInfo {
        String type;
        int firstLine;
        int frequency;

        SymbolInfo(String type, int line) {
            this.type = type;
            this.firstLine = line;
            this.frequency = 1;
        }
    }

    private Map<String, SymbolInfo> table = new HashMap<>();

    public void add(String lexeme, String type, int line) {
        if (table.containsKey(lexeme)) {
            table.get(lexeme).frequency++;
        } else {
            table.put(lexeme, new SymbolInfo(type, line));
        }
    }

    public void printTable() {
        System.out.println("\n--- Symbol Table ---");
        System.out.printf("%-20s %-15s %-10s %-10s%n", "Name", "Type", "First Line", "Frequency");
        for (Map.Entry<String, SymbolInfo> entry : table.entrySet()) {
            SymbolInfo info = entry.getValue();
            System.out.printf("%-20s %-15s %-10d %-10d%n", 
                entry.getKey(), info.type, info.firstLine, info.frequency);
        }
    }
}