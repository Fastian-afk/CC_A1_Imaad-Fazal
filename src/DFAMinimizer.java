package src;

import java.util.*;

/**
 * BONUS TASK 4: DFA Minimization Algorithm
 * Implements Moore's Algorithm (Partition Refinement)
 */

/**
 * DFAMinimizer
 * -------------------------
 * Implements DFA minimization using Moore's partition refinement algorithm.
 * A sample DFA containing redundant states is constructed, after which states
 * are repeatedly grouped and split based on transition behavior until no further
 * refinement is possible. States with identical transition patterns are merged,
 * producing minimized DFA partitions while preserving language acceptance.
 * The program then prints original states and resulting minimized groups.
 */

public class DFAMinimizer {

    // Represents a State in the DFA
    static class State {
        int id;
        boolean isFinal;
        Map<Character, State> transitions = new HashMap<>();

        public State(int id, boolean isFinal) {
            this.id = id;
            this.isFinal = isFinal;
        }

        @Override
        public String toString() { return "q" + id; }
    }

    public static void main(String[] args) {
        // --- 1. SETUP SAMPLE DFA (e.g., Integer Literal with redundant states) ---
        // Let's create a DFA that accepts (a|b)*abb
        // This usually has 4 states, but we'll make a redundant version with 5 states.
        
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b'));
        
        State q0 = new State(0, false); // Start
        State q1 = new State(1, false);
        State q2 = new State(2, false);
        State q3 = new State(3, true);  // Final
        State q4 = new State(4, true);  // Redundant Final (equivalent to q3)

        // Transitions
        q0.transitions.put('a', q1); q0.transitions.put('b', q2);
        q1.transitions.put('a', q1); q1.transitions.put('b', q3);
        q2.transitions.put('a', q1); q2.transitions.put('b', q2);
        q3.transitions.put('a', q1); q3.transitions.put('b', q2);
        q4.transitions.put('a', q1); q4.transitions.put('b', q2); // q4 acts exactly like q3

        List<State> dfaStates = Arrays.asList(q0, q1, q2, q3, q4);

        System.out.println("--- Original DFA States ---");
        for (State s : dfaStates) {
            System.out.println("State " + s + (s.isFinal ? "* " : " ") + " -> " + formatTransitions(s));
        }

        // --- 2. RUN MINIMIZATION ---
        System.out.println("\n--- Running Minimization Algorithm ---");
        List<Set<State>> partitions = minimize(dfaStates, alphabet);

        // --- 3. OUTPUT RESULTS ---
        System.out.println("Minimized Partitions (Merged States):");
        for (int i = 0; i < partitions.size(); i++) {
            System.out.print("New State M" + i + ": { ");
            for (State s : partitions.get(i)) System.out.print(s + " ");
            System.out.println("}");
        }
    }

    // --- THE ALGORITHM ---
    public static List<Set<State>> minimize(List<State> states, Set<Character> alphabet) {
        // Step 1: Initial Partition -> {Non-Final States}, {Final States}
        Set<State> finalStates = new HashSet<>();
        Set<State> nonFinalStates = new HashSet<>();
        
        for (State s : states) {
            if (s.isFinal) finalStates.add(s);
            else nonFinalStates.add(s);
        }

        List<Set<State>> partitions = new ArrayList<>();
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);
        if (!finalStates.isEmpty()) partitions.add(finalStates);

        boolean changed = true;
        while (changed) {
            changed = false;
            List<Set<State>> newPartitions = new ArrayList<>();

            for (Set<State> group : partitions) {
                if (group.size() <= 1) {
                    newPartitions.add(group);
                    continue;
                }

                // Try to split this group
                Map<String, Set<State>> splitter = new HashMap<>();
                for (State s : group) {
                    String signature = getSignature(s, partitions, alphabet);
                    splitter.putIfAbsent(signature, new HashSet<>());
                    splitter.get(signature).add(s);
                }

                if (splitter.size() > 1) changed = true;
                newPartitions.addAll(splitter.values());
            }
            partitions = newPartitions;
        }
        return partitions;
    }

    // Helper: Generate a "Signature" for a state based on where its transitions go
    private static String getSignature(State s, List<Set<State>> currentPartitions, Set<Character> alphabet) {
        StringBuilder sig = new StringBuilder();
        for (char c : alphabet) {
            State target = s.transitions.get(c);
            int partitionIndex = -1;
            // Find which partition the target state belongs to
            if (target != null) {
                for (int i = 0; i < currentPartitions.size(); i++) {
                    if (currentPartitions.get(i).contains(target)) {
                        partitionIndex = i;
                        break;
                    }
                }
            }
            sig.append(c).append(partitionIndex).append("|");
        }
        return sig.toString();
    }

    private static String formatTransitions(State s) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, State> entry : s.transitions.entrySet()) {
            sb.append(entry.getKey()).append("->").append(entry.getValue()).append(" ");
        }
        return sb.toString();
    }

}
