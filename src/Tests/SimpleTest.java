package Tests;

import static java.lang.Math.pow;

import Generators.RandomNumberGenerator;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Implements a simple test for testing the randomness of a random number generator.  Given a
 * generator the test is executed and the results of the test are returned.  The percentage
 * distribution observed for all possible values along with a statistical analysis of
 * the results, described below, is returned to the user.
 */
public class SimpleTest implements Test {

  private Writer out;
  private int min;
  private int lessThan;
  private int numSamples;
  private RandomNumberGenerator generator;
  private double[] intResults;
  private double[] boolResults;

  /**
   * Complete a simple randomness test on gen using samples number of samples who's value is between
   * min (inclusive) and lessThan (exclusive) for the int test.  The results are printed to output.
   *
   * @param output where to place the test results
   * @param min minimum value for int test (inclusive)
   * @param lessThan maximum value for int test (exclusive)
   * @param samples number of samples for the test
   * @param gen random number generator being tested
   */
  private SimpleTest(Writer output, int min, int lessThan, int samples, RandomNumberGenerator gen) {
    out = output;
    this.min = min;
    this.lessThan = lessThan;
    numSamples = samples;
    generator = gen;
    Test.println("Simple Test Results for " + gen.getClass().getName(), out);
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
  } // SimpleTest

  /**
   * Complete a simple randomness test on gen and outputs the results to output.
   *
   * @param output where to place the test results
   * @param gen random number generator being tested
   */
  public SimpleTest(Writer output, RandomNumberGenerator gen) {
    this(output, 0, 100, 1000000, gen);
  } // SimpleTest

  /**
   * Conducts a statistical analysis on the provided generator.  This analysis generates 1000 lists
   * of random numbers from the generator of length numSamples/1000.  This allows us to use the
   * central limit theorem as well as the mean test described within "Beautiful Testing Chapter
   * 10.pdf" which allows the user to compare the mean and variance between various random number
   * generators.
   */
  private void printStats() {
    int numTests = 1000;
    ArrayList<Double> means = new ArrayList<Double>();
    ArrayList<Integer> intList;
    for (int test = 0; test < numTests; test++) {
      intList = generator.intList(min, lessThan, numSamples / numTests);
      means.add(intMean(intList));
    }
    double mean = doubleMean(means);
    double variance = doubleVariance(means);
    Test.println("Statistical Results for Integer Test", out);
    Test.println("Number of Sample Means Calculated: " + numTests, out);
    Test.println("Number of Samples Used to Calculate Each Mean: "
        + numSamples / numTests, out);
    Test.println("Mean of Means: " + mean, out);
    Test.println("Variance of Means: " + variance, out);
  }

  /**
   * Computes the mean of all the doubles within nums.
   *
   * @param nums list of doubles
   * @return mean of list of doubles
   */
  private double intMean(ArrayList<Integer> nums) {
    double sum = 0.0;
    for (int num : nums) {
      sum += num;
    }
    return sum / nums.size();
  }

  /**
   * Computes the mean of all the doubles within nums.
   *
   * @param nums list of doubles
   * @return mean of list of doubles
   */
  private double doubleMean(ArrayList<Double> nums) {
    double sum = 0.0;
    for (double num : nums) {
      sum += num;
    }
    return sum / nums.size();
  }

  /**
   * Returns the variance of the doubles in nums.
   *
   * @param nums list of doubles
   * @return variance of nums
   */
  private double doubleVariance(ArrayList<Double> nums) {
    double mean = doubleMean(nums);
    double sum = 0.0;
    for (double num : nums) {
      sum += pow((num - mean), 2);
    }
    return sum / (nums.size() - 1);
  }

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
    Test.println("---------------", out);
    printStats();
    Test.println("", out);
  } // printIntResults

  @Override
  public void printBoolResults() {
    Test.println("Boolean Results", out);
    Test.println("Value\tPercent", out);
    Test.print("False", out);
    Test.print("\t", out);
    Test.println(100 * boolResults[0], out);
    Test.print("True", out);
    Test.print("\t", out);
    Test.println(100 * boolResults[1], out);
    Test.println("", out);
  } // printBoolResults
}