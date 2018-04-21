package Tests;

import static java.lang.Math.pow;

import Generators.RandomNumberGenerator;
import java.io.Writer;
import java.util.ArrayList;

public class ChiSquaredTest implements Test {

  private RandomNumberGenerator generator;
  private double intResults;
  private double boolResults;
  private Writer out;
  private int min;
  private int lessThan;
  private int numSamples;

  // TODO: JavaDoc

  private ChiSquaredTest(Writer output, int min, int lessThan, int samples, RandomNumberGenerator
      gen) {
    out = output;
    this.min = min;
    this.lessThan = lessThan;
    numSamples = samples;
    generator = gen;
    Test.println("Chi-Squared Test Results for " + gen.getClass().getName(), out);
    Test.println("Number of samples: " + numSamples, out);

    boolTest();
    printBoolResults();

    if (lessThan - min > 2) { // More than 2 possible values
      intTest();
      printIntResults();
    } else {
      Test.println("Integer test not run as number of possible values was less than 3", out);
      Test.println("Min: " + min + "\tMax: " + (lessThan - 1), out);
    }
    Test.println("", out);
  } // ChiSquaredTest

  public ChiSquaredTest(Writer output, RandomNumberGenerator gen) {
    this(output, 0, 100, 1000000, gen);
  }

  private double calcChiSquared(int[] results) {
    int numCells = results.length;
    int expected = numSamples / numCells;
    double chiSum = 0.0;
    for (int r : results) {
      chiSum += pow(r - expected, 2) / expected;
    }
    return chiSum;
  }

  @Override
  public void printIntResults() {
    boolean test1 = intResults < 77.046; // a = 95%
    boolean test2 = intResults < 81.449; // a = 90%
    boolean test3 = intResults < 89.181; // a = 75%
    boolean test4 = intResults < 98.334; // a = 50%
    boolean test5 = intResults < 108.09; // a = 25%
    Test.println("Integer Chi-Squared Value: " + intResults, out);
    Test.println("Passes Chi-Squared test with 95% confidence interval: " + test1, out);
    Test.println("Passes Chi-Squared test with 90% confidence interval: " + test2, out);
    Test.println("Passes Chi-Squared test with 75% confidence interval: " + test3, out);
    Test.println("Passes Chi-Squared test with 50% confidence interval: " + test4, out);
    Test.println("Passes Chi-Squared test with 25% confidence interval: " + test5, out);
  }

  @Override
  public void printBoolResults() {
    boolean test1 = boolResults < 0.00393; // a = 95%
    boolean test2 = boolResults < 0.0158; // a = 90%
    boolean test3 = boolResults < 0.102; // a = 75%
    boolean test4 = boolResults < 0.455; // a = 50%
    boolean test5 = boolResults < 1.323; // a = 25%
    Test.println("Boolean Chi-Squared Value: " + boolResults, out);
    Test.println("Passes Chi-Squared test with 95% confidence interval: " + test1, out);
    Test.println("Passes Chi-Squared test with 90% confidence interval: " + test2, out);
    Test.println("Passes Chi-Squared test with 75% confidence interval: " + test3, out);
    Test.println("Passes Chi-Squared test with 50% confidence interval: " + test4, out);
    Test.println("Passes Chi-Squared test with 25% confidence interval: " + test5, out);
  }

  @Override
  public void intTest() {
    ArrayList<Integer> intList = generator.intList(min, lessThan, numSamples);
    int[] count = new int[lessThan - min];
    for (int num : intList) {
      count[num - min]++;
    }
    intResults = calcChiSquared(count);
  }

  @Override
  public void boolTest() {
    ArrayList<Boolean> boolList = generator.booleanList(numSamples);
    int[] count = new int[2];
    for (Boolean bool : boolList) {
      if (bool) {
        count[1]++;
      } else {
        count[0]++;
      }
    }
    boolResults = calcChiSquared(count);
  }
}
