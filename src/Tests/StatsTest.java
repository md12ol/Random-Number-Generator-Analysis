package Tests;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import Generators.RandomNumberGenerator;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Implements a statistical test for testing the randomness of a random number generator.  Given a
 * generator the test is executed and the results of the test are returned.  This test generates
 * 1000 lists of random numbers from the generator of length numSamples/1000.  This allows us to use
 * the central limit theorem as well as the mean test described within the included "Beautiful
 * Testing Chapter 10.pdf" which allows the user to compare the mean and variance between various
 * random number generators.
 */
public class StatsTest implements Test {

  private Writer out;
  private int min;
  private int lessThan;
  private int numSamples;
  private RandomNumberGenerator generator;
  private double intMean;
  private double intVariance;
  private double confidenceInterval;
  private int numTests;
  private double expectedMean;

  /**
   * Complete a stats test on gen using samples number of samples who's value is in the range [min,
   * lessThan) for the int test.  The results are printed to output.
   *
   * @param output where to place the test results
   * @param min minimum value for int test (inclusive)
   * @param lessThan maximum value for int test (exclusive)
   * @param samples number of samples for the test
   * @param gen random number generator being tested
   */
  private StatsTest(Writer output, int min, int lessThan, int samples, RandomNumberGenerator gen) {
    if (samples < 1) {
      throw new IllegalArgumentException("The number of samples cannot be less than 1.");
    }
    out = output;
    this.min = min;
    this.lessThan = lessThan;
    numSamples = samples;
    generator = gen;
    expectedMean = (lessThan - 1.0 - min) / 2.0;
    Test.println("Stats Test Results for " + gen.getClass().getName(), out);
    Test.println("Number of samples: " + numSamples, out);
    Test.println("", out);

    if (lessThan - min > 2) { // More than 2 possible values
      intTest();
      printIntResults();
    } else {
      Test.println("Integer test not run as number of possible values was less than 3", out);
      Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    }
  } // StatsTest

  /**
   * Complete a stats test on gen and outputs the results to output.
   *
   * @param output where to place the test results
   * @param gen random number generator being tested
   */
  public StatsTest(Writer output, RandomNumberGenerator gen) {
    this(output, 0, 100, 1000000, gen);
  } // StatsTest

  /**
   * Facilitates running the Stats randomness test on gen once where minimized output will be sent
   * to output if print is set to true.
   *
   * @param output where to place the results
   * @param gen random number generator being tested
   * @param print whether or not output should print
   * @throws IllegalArgumentException if runs is less than 1
   */
  public StatsTest(Writer output, RandomNumberGenerator gen, boolean print) throws
      IllegalArgumentException {
    out = output;
    min = 0;
    lessThan = 100;
    numSamples = 1000000;
    generator = gen;
    expectedMean = (lessThan - 1.0 - min) / 2.0;
    intTest();
    if (print) {
      Test.println(intMean + "\t" + intVariance + "\t" + success(), out);
    }
  } // StatsTest

  /**
   * Calculates the 95% confidence interval of the mean of the samples and stores it in
   * confidenceInterval.
   */
  private void calcConfidenceInterval() {
    confidenceInterval = 1.960 * sqrt(intVariance) / sqrt(numTests);
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
  } // intMean

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
  } // doubleMean

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
  } // doubleVariance

  /**
   * Returns whether or not the expectedMean fell within the 95% confidence interval of the sample
   * mean.
   *
   * @return success or not
   */
  public boolean success() {
    return intMean - confidenceInterval <= expectedMean
        && intMean + confidenceInterval >= expectedMean;
  }

  @Override
  public void boolTest() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("This operation is not available for the Stats Test.");
  } // boolTest

  @Override
  public void intTest() {
    numTests = 1000;
    ArrayList<Double> means = new ArrayList<>();
    ArrayList<Integer> intList;
    for (int test = 0; test < numTests; test++) {
      intList = generator.intList(min, lessThan, numSamples / numTests);
      means.add(intMean(intList));
    }
    intMean = doubleMean(means);
    intVariance = doubleVariance(means);
    calcConfidenceInterval();
  } // intTest

  @Override
  public void printBoolResults() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("This operation is not available for the Stats Test.");
  } // printBoolResults

  @Override
  public void printIntResults() {
    Test.println("Integer Results", out);
    Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    Test.println("Number of Sample Means Calculated: " + numTests, out);
    Test.println("Number of Samples Used to Calculate Each Mean: "
        + numSamples / numTests, out);
    Test.println("Mean of Means: " + intMean, out);
    Test.println("Variance of Means: " + intVariance, out);
    Test.println("95% Confidence Interval: " + (intMean - confidenceInterval) + " - " +
        (intMean + confidenceInterval), out);
    Test.println("Expected mean of " + expectedMean +
        " was within 95% confidence interval: " + success(), out);
    Test.println("", out);
  } // printIntResults
}