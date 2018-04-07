import Generators.BlumBlumShub;
import Generators.CompoundInversiveGenerator;
import Generators.InversiveCongruentialGenerator;
import Generators.JavaGenerator;
import Tests.ChiSquaredTest;
import Tests.SpectralTest;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Runs a statistical analysis on multiple pseudo-random number generators.  Displays output in
 * Output.txt.
 */
public class Main {

  public static void main(String[] args) {
    Writer out = null;
    try {
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Output.txt"),
          "utf-8"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error creating Writer");
    }
    print("Michael Dubé\nCOSC 4P03 Final Project\n", out);

    BlumBlumShub blumGen = new BlumBlumShub();
    InversiveCongruentialGenerator congruentialGen = new InversiveCongruentialGenerator();
    CompoundInversiveGenerator compoundGen = new CompoundInversiveGenerator();
    JavaGenerator javaGen = new JavaGenerator();

    ChiSquaredTest chaiTest = new ChiSquaredTest();
    SpectralTest spectralTest = new SpectralTest();
  } // main

  private static void print(String toPrint, Writer out) {
    try {
      out.write(toPrint);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error writing to file");
    }
    System.out.print(toPrint);
  } // print

  private static void print(int toPrint, Writer out) {
    print(String.valueOf(toPrint), out);
  } // print

  private static void print(double toPrint, Writer out) {
    print(String.valueOf(toPrint), out);
  } // print
}