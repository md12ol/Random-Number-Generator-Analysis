import Generators.BlumBlumShub;
import Generators.CompoundInversiveGenerator;
import Generators.InversiveCongruentialGenerator;
import Generators.JavaGenerator;
import Generators.LinearCongruentialGenerator;
import Tests.ChiSquaredTest;
import Tests.MeanTest;
import Tests.SpreadTest;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

/**
 * Runs a statistical analysis on multiple pseudo-random number generators.  Displays output in
 * Output.txt.  The statistical information for the report is stored in the files
 *
 * Note: the Random class is one of the pseudo-random number generators being tested in this
 * project.  With that being said there is a need for random numbers in order to implement the other
 * random number generators.  Though, the Random class is only used to create parameters for the
 * other random number generators being tested.  And considering the limitations on the parameters
 * the randomness of these algorithms should not be impacted by the use of the Random class for
 * creating the parameters.
 *
 * This code follows, as closely as possible, Google's Java Style Guide.
 */
public class Main {

  private static final long RANDOM_SEED = 125;
  private static final Random RAND_GEN = new Random(RANDOM_SEED);

  public static void main(String[] args) {
    displayResults(); // Run this to see some cool stuff from the generators
//    gatherDataForReport(); // Run this to generate data for the report
  } // main

  /**
   * Runs all the generators against all the tests and places results in Output.txt and on the
   * console.
   */
  private static void displayResults() {
    // Prepare output
    Writer out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Output.txt"),
          "utf-8"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error creating Writer");
    }
    assert out != null;
    print("Michael Dubé\nCOSC 4P03 Final Project\n\n", out);

    // Generators
    JavaGenerator javaGen = new JavaGenerator(RAND_GEN.nextInt(1000));
    BlumBlumShub blumGen = new BlumBlumShub(RAND_GEN, 5);
    LinearCongruentialGenerator linearGen = new LinearCongruentialGenerator(RAND_GEN);
    InversiveCongruentialGenerator congruentialGen = new InversiveCongruentialGenerator(RAND_GEN,
        11, 12);
    int[] bVals = {3, 5, 7};
    int[] seeds = {4, 8, 6};
    CompoundInversiveGenerator compoundGen = new CompoundInversiveGenerator(RAND_GEN, 3,
        bVals, seeds);

    // Tests
    new SpreadTest(out, javaGen);
    new SpreadTest(out, blumGen);
    new SpreadTest(out, linearGen);
    new SpreadTest(out, congruentialGen);
    new SpreadTest(out, compoundGen);
    new MeanTest(out, javaGen);
    new MeanTest(out, blumGen);
    new MeanTest(out, linearGen);
    new MeanTest(out, congruentialGen);
    new MeanTest(out, compoundGen);
    new ChiSquaredTest(out, javaGen);
    new ChiSquaredTest(out, blumGen);
    new ChiSquaredTest(out, linearGen);
    new ChiSquaredTest(out, congruentialGen);
    new ChiSquaredTest(out, compoundGen);

    // Close output
    try {
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error closing writer");
    }
  }

  /**
   * Completes a statistical report for all generators on the Mean and Chi-Squared test using 30
   * runs.  Results are placed in five files: blumResults.txt, compoundResults.txt,
   * congruentialResults.txt, javaResults.txt and linearResults.txt.
   */
  private static void gatherDataForReport() {
    JavaGenerator javaGen;
    BlumBlumShub blumGen;
    LinearCongruentialGenerator linearGen;
    InversiveCongruentialGenerator congruentialGen;
    int[] bVals;
    int[] seeds;
    CompoundInversiveGenerator compoundGen;
    int javaPasses = 0;
    int blumPasses = 0;
    int linearPasses = 0;
    int congruentialPasses = 0;
    int compoundPasses = 0;

    // Open writers
    Writer javaResults = null;
    Writer blumResults = null;
    Writer linearResults = null;
    Writer congruentialResults = null;
    Writer compoundResults = null;
    try {
      javaResults = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("javaResults.txt"), "utf-8"));
      blumResults = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("blumResults.txt"), "utf-8"));
      linearResults = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("linearResults.txt"), "utf-8"));
      congruentialResults = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("congruentialResults.txt"), "utf-8"));
      compoundResults = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("compoundResults.txt"), "utf-8"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error creating Writer");
    }
    assert javaResults != null;
    assert blumResults != null;
    assert linearResults != null;
    assert congruentialResults != null;
    assert compoundResults != null;

    // Mean Tests
    println("Mean\t\tVariance\t\tPassed", javaResults);
    println("Mean\t\tVariance\t\tPassed", blumResults);
    println("Mean\t\tVariance\t\tPassed", linearResults);
    println("Mean\t\tVariance\t\tPassed", congruentialResults);
    println("Mean\t\tVariance\t\tPassed", compoundResults);
    MeanTest test;
    for (int run = 0; run < 30; run++) {
      javaGen = new JavaGenerator(RAND_GEN.nextInt(1000));
      blumGen = new BlumBlumShub(RAND_GEN, 5);
      linearGen = new LinearCongruentialGenerator(RAND_GEN);
      congruentialGen = new InversiveCongruentialGenerator(RAND_GEN,
          11, 12);
      bVals = new int[]{3, 5, 7};
      seeds = new int[]{4, 8, 6};
      compoundGen = new CompoundInversiveGenerator(RAND_GEN, 3, bVals, seeds);
      test = new MeanTest(javaResults, javaGen, true);
      if (test.success()) {
        javaPasses++;
      }
      test = new MeanTest(blumResults, blumGen, true);
      if (test.success()) {
        blumPasses++;
      }
      test = new MeanTest(linearResults, linearGen, true);
      if (test.success()) {
        linearPasses++;
      }
      test = new MeanTest(congruentialResults, congruentialGen, true);
      if (test.success()) {
        congruentialPasses++;
      }
      test = new MeanTest(compoundResults, compoundGen, true);
      if (test.success()) {
        compoundPasses++;
      }
    }
    println("Total successes: " + javaPasses, javaResults);
    println("Total successes: " + blumPasses, blumResults);
    println("Total successes: " + linearPasses, linearResults);
    println("Total successes: " + congruentialPasses, congruentialResults);
    println("Total successes: " + compoundPasses, compoundResults);
    println("\n", javaResults);
    println("\n", blumResults);
    println("\n", linearResults);
    println("\n", congruentialResults);
    println("\n", compoundResults);

    // Chi-Squared Tests
    println("boolChi\t\tboolPasses\t\tintChi\t\tintPasses", javaResults);
    println("boolChi\t\tboolPasses\t\tintChi\t\tintPasses", blumResults);
    println("boolChi\t\tboolPasses\t\tintChi\t\tintPasses", linearResults);
    println("boolChi\t\tboolPasses\t\tintChi\t\tintPasses", congruentialResults);
    println("boolChi\t\tboolPasses\t\tintChi\t\tintPasses", compoundResults);
    for (int run = 0; run < 30; run++) {
      javaGen = new JavaGenerator(RAND_GEN.nextInt(1000));
      blumGen = new BlumBlumShub(RAND_GEN, 5);
      linearGen = new LinearCongruentialGenerator(RAND_GEN);
      congruentialGen = new InversiveCongruentialGenerator(RAND_GEN,
          11, 12);
      bVals = new int[]{3, 5, 7};
      seeds = new int[]{4, 8, 6};
      compoundGen = new CompoundInversiveGenerator(RAND_GEN, 3, bVals, seeds);
      new ChiSquaredTest(javaResults, javaGen, true);
      new ChiSquaredTest(blumResults, blumGen, true);
      new ChiSquaredTest(linearResults, linearGen, true);
      new ChiSquaredTest(congruentialResults, congruentialGen, true);
      new ChiSquaredTest(compoundResults, compoundGen, true);
    }

    // Close writers
    try {
      javaResults.close();
      blumResults.close();
      linearResults.close();
      congruentialResults.close();
      compoundResults.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error closing writer");
    }
  } // gatherDataForReport

  /**
   * Prints toPrint to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  private static void print(String toPrint, Writer out) {
    try {
      out.write(toPrint);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error writing to file");
    }
    System.out.print(toPrint);
  } // print

  /**
   * Print toPrint and a new line to console and to out.
   *
   * @param toPrint what is to be printed
   * @param out where toPrint is to be printed
   */
  private static void println(String toPrint, Writer out) {
    print(toPrint, out);
    print("\n", out);
  } // printLn
}