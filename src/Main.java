import Generators.BlumBlumShub;
import Generators.CompoundInversiveGenerator;
import Generators.InversiveCongruentialGenerator;
import Generators.JavaGenerator;
import Tests.ChiSquaredTest;
import Tests.SimpleTest;
import Tests.SpectralTest;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

/**
 * Runs a statistical analysis on multiple pseudo-random number generators.  Displays output in
 * Output.txt.
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
    BlumBlumShub blumGen = new BlumBlumShub(RAND_GEN, 5);
    InversiveCongruentialGenerator congruentialGen = new InversiveCongruentialGenerator(RAND_GEN,
        11, 12);
    int[] bVals = {3, 5, 7};
    int[] seeds = {4, 8, 6};
    CompoundInversiveGenerator compoundGen = new CompoundInversiveGenerator(RAND_GEN, 3,
        bVals, seeds);
    JavaGenerator javaGen = new JavaGenerator(RANDOM_SEED);

    // Tests
    ChiSquaredTest chaiTest = new ChiSquaredTest();
    SpectralTest spectralTest = new SpectralTest();
    SimpleTest simpleTest = new SimpleTest(out, 0, 100, 100000, blumGen);
    simpleTest = new SimpleTest(out, 0, 100, 100000, congruentialGen);
    simpleTest = new SimpleTest(out, 0, 100, 100000, compoundGen);

    // Close output
    try {
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error closing writer");
    }
  } // main

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
}