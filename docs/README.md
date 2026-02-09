# Compiler Construction - Assignment 01
**Topic:** Lexical Analyzer Implementation  
**Language:** Custom Language (.lang)

## Team Members
* **Member 1:** Imaad Fazal (23I-0656)
* **Member 2:** Muhammad Immad (23I-0026)

## How to Run
1.  **Compile:**
    ```bash
    javac src/*.java
    ```
2.  **Run Manual Scanner:**
    ```bash
    java src.ManualScanner tests/test1.lang
    ```
3.  **Run JFlex Scanner:**
    ```bash
    java src.JFlexRunner
    ```

## Language Specifications
* **File Extension:** `.lang`
* **Keywords:** `start`, `finish`, `loop`, `condition`, `declare`, `output`, `input`, `function`, `return`, `break`, `continue`, `else`
* **Boolean:** `true`, `false`

### Rules
1.  **Identifiers:** Must start with an Uppercase letter (A-Z). Example: `Count`, `Total_Sum`.
2.  **Comments:**
    * Single-line: `## Comment`
    * Multi-line: `#* Comment *#`
3.  **Literals:**
    * Integers: `10`, `-5`
    * Floats: `3.14`, `1.5e10`
    * Strings: `"Hello"`
