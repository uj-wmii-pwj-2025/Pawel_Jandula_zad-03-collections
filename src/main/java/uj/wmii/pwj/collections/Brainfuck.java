package uj.wmii.pwj.collections;

import java.io.InputStream;
import java.io.PrintStream;

public interface Brainfuck {

  void execute();

  
  static Brainfuck createInstance(String program) {
    return createInstance(program, System.out, System.in, 1024);
  }

  /**
   * Creates a new instance of Brainfuck interpreter with given parameters.
   * @param program brainfuck program to interpret
   * @param out output stream to be used by interpreter implementation
   * @param in input stream to be used by interpreter implementation
   * @param stackSize maximum stack size, that is allowed for this interpreter
   * @return new instance of the interpreter
   * @throws IllegalArgumentException if: program is null or empty, OR out is null, OR in is null, OR stackSize is below 1.
   */
  static Brainfuck createInstance(String program, PrintStream out, InputStream in, int stackSize) {
    if (program == null || program.isEmpty()) { 
      throw new IllegalArgumentException("Program cannot be null or empty");
    }
    if (out == null) {
      throw new IllegalArgumentException("Output stream cannot be null");
    }
    if (in == null) {
      throw new IllegalArgumentException("Input stream cannot be null");
    }
    if (stackSize < 1) {
      throw new IllegalArgumentException("Stack size must be at least 1");
    }
    return new BrainfuckCode(program, out, in, stackSize);
  }
}
class BrainfuckCode implements Brainfuck {
  private final String program;
  private final PrintStream out;
  private final InputStream in;
  private final int stackSize;


  public BrainfuckCode(String program, PrintStream out, InputStream in, int stackSize) {
    this.program = program;
    this.out = out;
    this.in = in;
    this.stackSize = stackSize;

  }

  @Override
  public void execute() {
    int pointer = 0;
    int[] memory = new int[stackSize]; 
    char[] instructions = program.toCharArray();
    for (int i = 0; i < instructions.length; i++) {
      char instruction = instructions[i];
      switch (instruction) {
        case '>':
          pointer++; 
          break;
        case '<':
          pointer--; 
          break;
        case '+':
          memory[pointer]++;
          break;
        case '-':
          memory[pointer]--;
          break;
        case '.':
          out.print((char) (memory[pointer]));
          break;
        case ',':
          try {
            memory[pointer] = (byte) in.read();
          } catch (Exception e) {
            throw new IllegalArgumentException("Invalid input.");
          }
          break;
        case '[':
          if (memory[pointer] == 0) {
            int loop = 1;
            while (loop > 0) {
              i++;
              if (i >= instructions.length) {
                throw new IllegalArgumentException("Invalid program.");
              }
              if (instructions[i] == '[') loop++;
              if (instructions[i] == ']') loop--;
            }
          }
          break;
        case ']':
          if (memory[pointer] != 0) {
            int loop = 1;
            while (loop > 0) {
              i--;
              if (i < 0) {
                throw new IllegalArgumentException("Invalid program.");
                }
              if (instructions[i] == ']') loop++;
              if (instructions[i] == '[') loop--;
            }
          }
          break;
        default:
          break;
      }

    }
  }
}