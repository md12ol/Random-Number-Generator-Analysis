package Generators;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implements Linear Congruential random number generator. I will outline the basics below. For a
 * quick overview see "https://en.wikipedia.org/wiki/Linear_congruential_generator." For a detailed
 * explanation see the included "Random Number Generator.pdf."
 *
 * <pre>
 *   Linear Congruential Generator Overview:
 *   The following restrictions on parameters will ensure a generator of maximal period
 *   x_0 is a positive integer
 *   a and b are integers
 *   m is a positive integer greater than x_0, a and b
 *   b and m have gcd(b, m) = 1
 *   For all p, prime factors of m, a ≡ 1 (mod p)
 *   If 4 is a factor of m then a ≡ 1 (mod 4)
 *   n >= 0
 *   x_n+1 = a * x_n + b (mod m)
 *   x_n can have range [0, m) with period length m when parameters are chosen correctly
 * </pre>
 */
public class LinearCongruentialGenerator implements RandomNumberGenerator {

  private int m;
  private int a;
  private int b;
  private int prevVal;

  /**
   * Creates instance of Linear Congruential random number generator with random values for the
   * generator's parameters.  This generator will have maximal period.
   *
   * @param rand for randomly generating the generator's parameters
   */
  public LinearCongruentialGenerator(Random rand) {
    m = (int) pow(2, 30); // Max power of 2 that will fit in an int; 1073741824
    b = rand.nextInt((m - 1) / 2) * 2 + 1; // Odd in range [1, 1073741823]
    do {
      a = rand.nextInt(m);
    } while (a % 4 != 1);
    prevVal = rand.nextInt(m);
  } // LinearCongruentialGenerator

  /**
   * Creates instance of Linear Congruential random number generator with the provided values for
   * the generator's parameters.  This generator may not have maximal period.
   *
   * @param mod m value
   * @param aVal a value
   * @param bVal b value
   * @param seed x_0 value
   */
  public LinearCongruentialGenerator(int mod, int aVal, int bVal, int seed) {
    m = mod;
    a = aVal;
    b = bVal;
    prevVal = seed;
  } // LinearCongruentialGenerator

  /**
   * Returns m.
   *
   * @return m
   */
  public int getM() {
    return m;
  } // getM

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
  } // getPeriod

  @Override
  public boolean nextBoolean() {
    int temp;
    // Ensure both are equally likely
    while (true) {
      temp = nextInt();
      if (m % 2 == 0) { // m is even
        return temp % 2 == 1;
      } else { //  m is odd
        if (temp != m - 1) { // remove additional even number by excluding possible value m - 1
          return temp % 2 == 1;
        }
      }
    }
  } // nextBoolean

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    int difference = lessThan - min;
    int quotient = m / difference;
    // Ensures that each value in [min, lessThan) is equally likely to be chosen by grabbing
    // output from the random number generator until a number < quotient * difference is returned.
    // This will ensure that each value has the same probability of being chosen from prevVal %
    // difference.
    long temp;
    do {
      temp = (long) a * (long) prevVal + (long) b;
      temp = temp % (long) m;
      prevVal = (int) temp;
    } while (prevVal >= (quotient * difference));
    return min + (prevVal % difference);
  } // nextInt

  @Override
  public int nextInt() {
    return nextInt(0, m);
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
  public ArrayList<Integer> intList(int length) {
    return intList(0, m, length);
  } // intList
}
