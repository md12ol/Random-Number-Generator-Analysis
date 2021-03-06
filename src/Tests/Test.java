package Tests;

import java.io.IOException;
import java.io.Writer;

/**
 * Outlines common methods that all tests need to implement and provides utility methods that are
 * needed by some of the tests.
 */
public interface Test {

  /**
   * Prints toPrint to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void print(String toPrint, Writer out) {
    try {
      out.write(toPrint);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error writing to file");
    }
    System.out.print(toPrint);
  } // print

  /**
   * Prints toPrint to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void print(int toPrint, Writer out) {
    print(String.valueOf(toPrint), out);
  } // print

  /**
   * Prints toPrint to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void print(double toPrint, Writer out) {
    print(String.valueOf(toPrint), out);
  } // print

  /**
   * Prints toPrint and a new line to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void println(String toPrint, Writer out) {
    print(toPrint, out);
    print("\n", out);
  } // println

  /**
   * Prints toPrint and a new line to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void println(int toPrint, Writer out) {
    print(toPrint, out);
    print("\n", out);
  } // println

  /**
   * Prints toPrint and a new line to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  static void println(double toPrint, Writer out) {
    print(String.valueOf(toPrint), out);
    print("\n", out);
  } // println

  /**
   * Conducts a randomness test on a list of booleans generated from a random number generator.
   */
  void boolTest();

  /**
   * Conducts a randomness test on a list of integers generated from a random number generator.
   */
  void intTest();

  /**
   * Prints the results from the randomness test applied to booleans generated via a random number
   * generator.
   */
  void printBoolResults();

  /**
   * Prints the results from the randomness test applied to integers generated via a random number
   * generator.
   */
  void printIntResults();
}
