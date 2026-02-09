# Compiler Construction - Assignment 01
**Topic:** Lexical Analyzer (Scanner) Implementation  
**Language:** Java (Manual & JFlex)  
**Input File Extension:** `.lang`

## 👥 Team Members
* **Imaad Fazal** - [23I-0656]
* **Muhammad Immad** - [23I-0026]

## 🛠️ Compilation & Execution
Follow these steps to compile and run the project from the root directory.

### 1. Compile Source Code
```bash
javac src/*.java

```

### 2. Run Manual Scanner

To run the manually implemented scanner on a specific test file:

```bash
java src.ManualScanner tests/test1.lang

```

### 3. Run JFlex Scanner

To run the JFlex-generated scanner (verifies output against manual scanner):

```bash
java src.JFlexRunner

```

---

## Language Specifications

**Name:** CustomLang

**Extension:** `.lang`

### 1. Keywords & Meanings

All keywords are **case-sensitive** and must be lowercase.

| Keyword | Meaning |
| --- | --- |
| `start` | Marks the beginning of the program. |
| `finish` | Marks the end of the program. |
| `declare` | Used to define a new variable. |
| `loop` | Initiates a while-loop structure. |
| `condition` | Initiates an if-statement structure. |
| `else` | Marks the alternative block in a conditional structure. |
| `output` | Prints data to the console. |
| `input` | Reads input from the user. |
| `function` | Defines a new function. |
| `return` | Returns a value from a function. |
| `break` | Exits the current loop immediately. |
| `continue` | Skips to the next iteration of the loop. |

### 2. Identifier Rules

* **Regex:** `[A-Z][a-z0-9]{0,30}`
* **Rule:** Must start with an **Uppercase Letter** (A-Z). Can contain lowercase letters and digits.
* **Length:** Maximum 31 characters.
* **Examples:**
* Valid: `Count`, `TotalSum`, `X`, `Var1`
* Invalid: `count` (starts with lowercase), `1var` (starts with digit), `total_sum` (contains underscore).



### 3. Literals

| Type | Format | Examples |
| --- | --- | --- |
| **Integer** | Optional sign, sequence of digits | `10`, `+50`, `-123`, `0` |
| **Float** | Decimal or Scientific Notation | `3.14`, `-0.01`, `1.5e10`, `2.4E-5` |
| **String** | Enclosed in double quotes | `"Hello"`, `"Value: "` |
| **Char** | Single char in single quotes | `'A'`, `'\n'`, `'.'` |
| **Boolean** | Exact keywords | `true`, `false` |

### 4. Operators & Precedence

Operators are listed from **Highest** to **Lowest** precedence.

1. **Exponentiation:** `**`
2. **Multiplicative:** `*`, `/`, `%`
3. **Additive:** `+`, `-`
4. **Relational:** `==`, `!=`, `<`, `>`, `<=`, `>=`
5. **Logical:** `!`, `&&`, `||`
6. **Assignment:** `=`, `+=`, `-=`, `*=`, `/=`

### 5. Comment Syntax

* **Single-line:** Starts with `##`. Everything after is ignored until the new line.
```text
## This is a comment
declare X = 10; ## Comment at end of line

```


* **Multi-line:** Starts with `#*` and ends with `*#`. Can span multiple lines.
```text
#* This is a multi-line comment.
   It ignores everything inside.
*#

```



---

## Sample Programs

### Sample 1: Basic Arithmetic (`test1.lang`)

```text
start
    declare Count = 10;
    declare Price = 45.50;
    
    ## Loop until count is 0
    loop (Count > 0) {
        output "Value is: " + Count;
        Count--;
    }
finish

```

### Sample 2: Complex Expressions (`test2.lang`)

```text
start
    declare Result = 0;
    
    ## Mathematical Precedence
    Result = 10 + 5 * 2 - 20 / 4; 
    
    ## Scientific Notation
    declare BigNum = 1.5e10;
    
    condition (Result >= 10 && Result <= 100) {
        output "Range OK";
    }
finish

```

### Sample 3: String & Char Handling (`test3.lang`)

```text
start
    declare Msg = "Hello, World!";
    declare Escape = "Line1\nLine2\tTabbed";
    declare Char1 = 'A';
    
    output Msg;
finish
