package Generators;

import static java.lang.Math.pow;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implements Inversive Congruential random number generator.  I will outline the basics below. For
 * a quick overview see 'https://en.wikipedia.org/wiki/Inversive_congruential_generator.' For a
 * detailed explanation see the included 'Inversive Congruential Generators.pdf.'
 *
 * <pre>
 *   Inversive Congruential Generator Overview:
 *   p is a prime number greater than or equal to 5
 *   a, b are positive integers such that gcd(a, b) = 1
 *   n >= 0
 *   (x_n)^(-1) is the modular multiplicative inverse of x_n: x_n * (x_n)^(-1) ≡ 1 (mod p)
 *   x_n+1 = { a * (x_n)^(-1) + b (mod p)    x_n >= 1}
 *           { b                             x_n == 0}
 *   x_0 is chosen such that it is non-negative and less than p
 *   x_n can have range [0, p) with period length p when parameters are chosen correctly
 * </pre>
 */
public class InversiveCongruentialGenerator implements RandomNumberGenerator {

  private int p;
  private int prevVal;
  private int a;
  private int b;

  /**
   * Creates instance of Inversive Congruential random number generator with the provided values for
   * the generator's parameters.  A prime number is determined randomly and the value of a is
   * determined using an algorithm defined within "Compound Inversive Congruential Generator Design
   * Algorithm.pdf" and implemented below.  This will only initialize a generator with maximal
   * period.
   *
   * @param rand random number generator for random creation of parameters
   * @param bVal value of b
   * @param seed value of x_0
   */
  public InversiveCongruentialGenerator(Random rand, int bVal, int seed) {
    if (bVal < 1) {
      throw new IllegalArgumentException("The value for b must be a positive integer");
    }
    if (seed < 0) {
      throw new IllegalArgumentException("Seed must be non-negative");
    }
    // Find prime
    int prime;
    int count = 0;
    do {
      prime = RandomNumberGenerator.getPrime(rand, 4);
      count++;
    } while (!findAndSetA(prime, bVal, seed) && count < 1000);
    // Find a
    if (count < 1000) {
      p = prime;
      b = bVal;
      prevVal = seed;
    } else {
      throw new IllegalArgumentException("This combination of b value and seed has not resulted "
          + "in an Inversive Congruential Generator with maximal period after trying 1000 primes.");
    }
  }

  /**
   * Creates instance of Inversive Congruential random number generator with the provided values for
   * the generator's parameters.  No maximal period is guaranteed.
   *
   * @param prime value of p
   * @param aVal value of a
   * @param bVal value of b
   * @param seed value of x_0
   */
  private InversiveCongruentialGenerator(int prime, int aVal, int bVal, int seed) {
    this.p = prime;
    this.a = aVal;
    this.b = bVal;
    prevVal = seed;
  }

  /**
   * Creates instance of Inversive Congruential random number generator with the provided values for
   * the generator's parameters.  The value of a is determined using an algorithm defined within
   * "Compound Inversive Congruential Generator Design Algorithm.pdf" and implemented below.  This
   * will only initialize a generator with maximal period.
   *
   * @param prime value of p
   * @param bVal value of b
   * @param seed value of x_0
   */
  public InversiveCongruentialGenerator(int prime, int bVal, int seed) {
    if (bVal < 1) {
      throw new IllegalArgumentException("The value for b must be a positive integer");
    }
    if (seed < 0 || seed >= prime) {
      throw new IllegalArgumentException("Seed must be non-negative and less than the prime "
          + "provided.");
    }
    if (findAndSetA(prime, bVal, seed)) {
      p = prime;
      b = bVal;
      prevVal = seed;
    } else {
      throw new IllegalArgumentException("This combination of prime, b value and seed does not "
          + "create an Inversive Congruential Generator with maximal period.");
    }
  }

  /**
   * Implements an algorithm from "Compound Inversive Congruential Generator Design Algorithm .pdf"
   * which is used to find values for a which result in a inversive congruential generator of
   * maximal period.  If no value of a is possible the algorithm returns false.  If a value of a is
   * found then a is set to this value and true is returned.
   *
   * @param prime potential value of p
   * @param bVal potential value of b
   * @param seed potential value of x_0
   * @return whether or not a value of a was found
   */
  public boolean findAndSetA(int prime, int bVal, int seed) {
    int potA;
    for (int c = 1; c < prime; c++) {
      if (notSquares(prime, c)) {
        if (sufficientPeriod(prime, c)) {
          potA = (int) pow(bVal, 2) / (c + 2);
          if (gcd(potA, bVal) == 1) {
            if (generatorPeriod(prime, potA, bVal, seed) == prime) {
              a = potA;
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Returns true if c + 2 and c^2 - 4 are not squares.  If they are not squares, then then the
   * function f(x) = x^2 - cx + 1 is irreducibale over the field F_p.  This is a necessary condition
   * for creating an Inversive Congruential Generator of maximal period.  In order to retrun true
   * the following must be satisfied: (c + 2) mod prime != c^2 and (c^2 - 4) mod prime != c^2.  More
   * information is available at "Compound Inversive Congruential Generator Design Algorithm.pdf"
   *
   * @param prime potential value of p
   * @param c value being tested
   * @return true if c + 2 and c^2 - 4 are not squares
   */
  public boolean notSquares(int prime, int c) {
    int v1 = (c + 2) % (prime);
    int v2 = (int) ((pow(c, 2) - 4) % (prime));
    return v1 != pow(c, 2) && v2 != pow(c, 2);
  }

  /**
   * Checks if the values of prime and c retult in a sequence of period prime + 1 or not.  The
   * sequence being checked is defined as y_0 = 0, y_1 = 1, y_n+2 = c * y_n+1 - y_n.  If this
   * sequence has length prime + 1 then f(x) = x^2 - cx + 1 is an IMP polynomial which is a
   * necessary condition for creating a period of maximal length.  More information is available at
   * "Compound Inversive Congruential Generator Design Algorithm.pdf"
   *
   * @param prime potential p value
   * @param c value being tested
   * @return true if sequence length is prime + 1
   */
  public boolean sufficientPeriod(int prime, int c) {
    int y0 = 0;
    int y1 = 1;
    int cur;
    int prev1 = y1;
    int prev2 = y0;
    for (int n = 2; n < 2 * prime; n++) {
      cur = c * prev1 - prev2;
      if (cur < 0) { // Ensure positive mod
        cur += prime;
      }
      cur = cur % prime;
      prev2 = prev1;
      prev1 = cur;
      if (cur == y0 && n == prime + 1) { // Check true condition
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the greatest common divisor of one and two.
   *
   * @param one first int
   * @param two second int
   * @return greatest common divisor of one and two
   */
  private int gcd(int one, int two) {
    BigInteger bigOne = new BigInteger(String.valueOf(one));
    BigInteger bigTwo = new BigInteger(String.valueOf(two));
    BigInteger gcdBig = bigOne.gcd(bigOne);
    return gcdBig.intValue();
  }

  /**
   * Creates an Inversive Congruential Generator with the proposed values and then returns the
   * length of the period of said generator.  This is used for testing potential generators for
   * maximal period.
   *
   * @param p proposed p value
   * @param a proposed a value
   * @param b proposed b value
   * @param seed proposed x_0 value
   * @return length of period created using above parameters
   */
  private int generatorPeriod(int p, int a, int b, int seed) {
    InversiveCongruentialGenerator test = new InversiveCongruentialGenerator(p, a, b, seed);
    return test.getPeriod();
  }

  /**
   * Returns the period of the values returned by the Inversive Random Number Generator.
   *
   * @return period of generator
   */
  public int getPeriod() {
    int start = nextInt();
    int count = 1;
    while (nextInt() != start) {
      count++;
    }
    return count;
  }

  /**
   * Returns value of p.
   *
   * @return p
   */
  public int getPrime() {
    return p;
  }

  @Override
  public boolean nextBoolean() {
    int temp = nextInt();
    while (temp == p - 1) { // Ensures that even and odd equally likely
      temp = nextInt();
    }
    return nextInt() % 2 == 1;
  } // nextBoolean

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    int difference = lessThan - min;
    int quotient = p / difference;
    // Ensures that each value in [min, lessThan) is equally likely to be chosen by grabbing
    // output from the random number generator until a number < quotient * difference is returned.
    // This will ensure that each value has the same probability of being chosen from prevVal %
    // difference.
    do {
      if (prevVal == 0) {
        prevVal = b;
      } else {
        BigInteger bigP = new BigInteger(String.valueOf(p));
        BigInteger bigPrev = new BigInteger(String.valueOf(prevVal));
        BigInteger bigMultInv = bigPrev.modInverse(bigP);
        int multInv = bigMultInv.intValue();
        prevVal = (int) ((long) (a * multInv + b) % p);
      }
    } while (prevVal >= (quotient * difference));
    return min + (prevVal % (difference));
  } // nextInt

  @Override
  public int nextInt() {
    return nextInt(0, p);
  } // nextInt

  @Override
  public ArrayList<Boolean> booleanList(int length) throws IllegalArgumentException {
    RandomNumberGenerator.testSize(length);
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(nextBoolean());
    }
    return list;
  } // booleanList

  @Override
  public ArrayList<Integer> intList(int min, int lessThan, int length)
      throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    RandomNumberGenerator.testSize(length);
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(nextInt(min, lessThan));
    }
    return list;
  } // intList

  @Override
  public ArrayList<Integer> intList(int length) throws IllegalArgumentException {
    return intList(0, p, length);
  } // intList
}
