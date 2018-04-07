import Generators.BlumBlumShub;
import Generators.CompoundInversiveGenerator;
import Generators.InversiveCongruentialGenerator;
import Generators.JavaGenerator;
import Tests.ChiSquaredTest;
import Tests.SpectralTest;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Runs a statistical analysis on multiple pseudo-random number generators.  Displays output in
 * Output.txt.
 */
public class Main {

  public static void main(String[] args) {
    PrintWriter out = null;
    try {
      out = new PrintWriter("Output.txt", "UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("Error creating output file");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      System.out.println("Error using UTF-8 encoding");
    }
    print("Michael Dubé\nCOSC 4P03 Final Project\n", out);

    BlumBlumShub blumGen = new BlumBlumShub();
    InversiveCongruentialGenerator congruentialGen = new InversiveCongruentialGenerator();
    CompoundInversiveGenerator compoundGen = new CompoundInversiveGenerator();
    JavaGenerator javaGen = new JavaGenerator();

    ChiSquaredTest chaiTest = new ChiSquaredTest();
    SpectralTest spectralTest = new SpectralTest();
  } // main

  private static void print(String toPrint, PrintWriter out) {
    out.print(toPrint);
    System.out.print(toPrint);
  } // print

  private static void print(int toPrint, PrintWriter out) {
    out.print(toPrint);
    System.out.print(toPrint);
  } // print

  private static void print(double toPrint, PrintWriter out) {
    out.print(toPrint);
    System.out.print(toPrint);
  } // print
}