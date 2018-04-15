package Tests;

import Generators.RandomNumberGenerator;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Implements a naive test for testing the randomness of a random number generator.  Given a list of
 * values created by a random number generator the percentage distribution of all potential values
 * is returned.
 */
public class NaiveTest implements Test {

  private Writer out;
  private int min;
  private int lessThan;

  /**
   * Complete a naive randomness test on gen using samples number of samples who's value is between
   * min (inclusive) and lessThan (exclusive) for the int test.  The results are printed to output.
   *
   * @param output where to place the test results
   * @param min minimum value for int test (inclusive)
   * @param lessThan maximum value for int test (exclusive)
   * @param samples number of samples for the test
   * @param gen random number generator being tested
   */
  public NaiveTest(Writer output, int min, int lessThan, int samples, RandomNumberGenerator gen) {
    out = output;
    this.min = min;
    this.lessThan = lessThan;
    Test.println("Naive Test Results for " + gen.getClass().getName(), out);
    Test.println("Number of samples: " + samples, out);
    Test.println("", out);

    ArrayList<Boolean> boolList = gen.booleanList(samples);
    double[] boolResults = boolTest(boolList);
    printBoolResults(boolResults);

    if (lessThan - min > 2) { // More than 2 possible values
      ArrayList<Integer> intList = gen.intList(min, lessThan, samples);
      double[] intResults = intTest(intList);
      printIntResults(intResults);
    } else {
      Test.println("Integer test not run as possible values was less than 3", out);
      Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
      Test.println("", out);
    }
  } // NaiveTest

  /**
   * Complete a naive randomness test on gen and outputs the results to output.
   *
   * @param output where to place the test results
   * @param gen random number generator being tested
   */
  public NaiveTest(Writer output, RandomNumberGenerator gen) {
    this(output, 0, 10, 10000, gen);
  } // NaiveTest

  @Override
  public double[] intTest(ArrayList<Integer> nums) {
    int[] count = new int[lessThan - min];
    double[] percents = new double[lessThan - min];
    for (Integer num : nums) {
      count[num - min]++; // num - min will range 0 (inclusive) to (lessThan - min - 1) (inclusive)
    }
    for (int i = 0; i < count.length; i++) {
      percents[i] = (double) count[i] / nums.size();
    }
    return percents;
  } // intTest

  @Override
  public double[] boolTest(ArrayList<Boolean> bools) {
    int[] count = new int[2];
    double[] percents = new double[2];
    for (Boolean bool : bools) {
      if (bool) {
        count[1]++;
      } else {
        count[0]++;
      }
    }
    for (int i = 0; i < count.length; i++) {
      percents[i] = (double) count[i] / bools.size();
    }
    return percents;
  } // boolTest


  @Override
  public void printIntResults(double[] results) {
    Test.println("Integer Results", out);
    Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    Test.println("Value\tPercent", out);
    for (int i = min; i < lessThan; i++) {
      Test.print(i, out);
      Test.print("\t", out);
      // i - min will range 0 (inclusive) to (lessThan - min - 1) (inclusive)
      Test.println(100 * results[i - min], out);
    }
    Test.println("", out);
  } // printIntResults

  @Override
  public void printBoolResults(double[] results) {
    Test.println("Boolean Results", out);
    Test.println("Value\tPercent", out);
    Test.print("False", out);
    Test.print("\t", out);
    Test.println(100 * results[0], out);
    Test.print("True", out);
    Test.print("\t", out);
    Test.println(100 * results[1], out);
    Test.println("", out);
  } // printBoolResults
}