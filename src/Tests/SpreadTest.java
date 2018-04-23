package Tests;

import Generators.RandomNumberGenerator;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Implements a spread test for testing the randomness of a random number generator.  Given a
 * generator the test is executed and the results of the test are returned.  The percentage
 * distribution observed for all each of the potential values with the results being returned to the
 * user.  This test was not taken from an academic source and instead acted as a perliminary check
 * to determine if I was implementing the random number generators correctly.
 */
public class SpreadTest implements Test {

  private Writer out;
  private int min;
  private int lessThan;
  private int numSamples;
  private RandomNumberGenerator generator;
  private double[] intResults;
  private double[] boolResults;

  /**
   * Completes a spread randomness test on gen using samples number of samples who's value is in the
   * range [min, lessThan).  The results are printed to output.
   *
   * @param output where to place the test results
   * @param min minimum value for int test (inclusive)
   * @param lessThan maximum value for int test (exclusive)
   * @param samples number of samples for the test
   * @param gen random number generator being tested
   * @throws IllegalArgumentException if samples is less than 1
   */
  public SpreadTest(Writer output, int min, int lessThan, int samples, RandomNumberGenerator gen)
      throws IllegalArgumentException {
    if (samples < 1) {
      throw new IllegalArgumentException("The number of samples cannot be less than 1.");
    }
    out = output;
    this.min = min;
    this.lessThan = lessThan;
    numSamples = samples;
    generator = gen;
    Test.println("Spread Test Results for " + gen.getClass().getName(), out);
    Test.println("Number of samples: " + numSamples, out);
    Test.println("", out);

    boolTest();
    printBoolResults();

    if (lessThan - min > 2) { // More than 2 possible values
      intTest();
      printIntResults();
    } else {
      Test.println("Integer test not run as number of possible values was less than 3", out);
      Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    }
  } // SpreadTest

  /**
   * Complete the spread randomness test on gen and outputs the results to output.
   *
   * @param output where to place the test results
   * @param gen random number generator being tested
   */
  public SpreadTest(Writer output, RandomNumberGenerator gen) {
    this(output, 0, 100, 1000000, gen);
  } // SpreadTest

  @Override
  public void boolTest() {
    ArrayList<Boolean> boolList = generator.booleanList(numSamples);
    int[] count = new int[2];
    boolResults = new double[2];
    for (Boolean bool : boolList) {
      if (bool) {
        count[1]++;
      } else {
        count[0]++;
      }
    }
    for (int i = 0; i < count.length; i++) {
      boolResults[i] = (double) count[i] / numSamples;
    }
  } // boolTest

  @Override
  public void intTest() {
    ArrayList<Integer> intList = generator.intList(min, lessThan, numSamples);
    int[] count = new int[lessThan - min];
    intResults = new double[lessThan - min];
    for (Integer num : intList) {
      count[num - min]++; // num - min will range [0, lessThan - min - 1)
    }
    for (int i = 0; i < count.length; i++) {
      intResults[i] = (double) count[i] / numSamples;
    }
  } // intTest

  @Override
  public void printBoolResults() {
    Test.println("Value\tPercent", out);
    Test.print("False", out);
    Test.print("\t", out);
    Test.println(100 * boolResults[0], out);
    Test.print("True", out);
    Test.print("\t", out);
    Test.println(100 * boolResults[1], out);
    Test.println("", out);
  } // printBoolResults

  @Override
  public void printIntResults() {
    Test.println("Integer Results", out);
    Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    Test.println("Value\tPercent", out);
    for (int i = min; i < lessThan; i++) {
      Test.print(i, out);
      Test.print("\t", out);
      // i - min will range 0 (inclusive) to (lessThan - min - 1) (inclusive)
      Test.println(100 * intResults[i - min], out);
    }
    Test.println("", out);
  } // printIntResults
}
