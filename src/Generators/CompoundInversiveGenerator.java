package Generators;

import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implements Compoud Inversive Generator random number generator.  I will outline the basics below.
 * For a quick overview see 'https://en.wikipedia .org/wiki/Inversive_congruential_generator.' For a
 * detailed explanation see the included "Compound Inversive Congruential Generator Design
 * Algorithm.pdf."
 *
 * <pre>
 *   Compound Congruential Generator Overview:
 *   p_1, p_2, ... , p_r are distinct primes greater than or equal to 5
 *   Each of these primes will be used to create an Inversive Congruential Generator of maximal
 *   period with sequence: (x_j_n) where 1 <= j <= r and n >= 0
 *   x_n with n >= 0 is the output of this compound congruential generator
 *   x_n = x_1_n + x_2_n + ... + x_r_n mod T where T = p_1 * p_2 * ... * p_r
 *   Period of generator will be T
 * </pre>
 */
public class CompoundInversiveGenerator implements RandomNumberGenerator {

  private InversiveCongruentialGenerator[] generators;
  private int primeProduct;

  /**
   * Creates a Compound Inversive Generator with the provided values for the generator's parameters.
   * The value of the primes is determined randomly.
   *
   * @param rand for randomness
   * @param size number of Inversive Congruential Generators within this generator
   * @param bVals values for b for Inversive Congruential Generators
   * @param seeds values for x_0s for Inversive Congruential Generators
   */
  public CompoundInversiveGenerator(Random rand, int size, int[] bVals, int[] seeds) {
    if (bVals.length < size || seeds.length < size) {
      throw new IllegalArgumentException("The length of the arrays for b values and seeds must be"
          + " greater than or equal to the size of the Compound Congruential Generator.");
    }
    generators = new InversiveCongruentialGenerator[size];
    for (int i = 0; i < generators.length; i++) {
      generators[i] = new InversiveCongruentialGenerator(rand, bVals[i], seeds[i]);
    }
    primeProduct = 1;
    for (InversiveCongruentialGenerator gen : generators) {
      primeProduct *= gen.getPrime();
    }
  }

  /**
   * Returns the product of the primes which made the Inversive Congruential Generators which make
   * up the Compound Congruential Generator.
   *
   * @return product of primes
   */
  public int getPrimeProduct() {
    return primeProduct;
  }

  @Override
  public boolean nextBoolean() {
    return nextInt(0, primeProduct) % 2 == 1; // Even and odd equally likely
  } // nextBoolean

  @Override
  public int nextInt() {
    return nextInt(0, primeProduct);
  }

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    if (lessThan > primeProduct) {
      throw new IllegalArgumentException("Value of lessThan is equal to or greater than the prime"
          + " product.  This is not permitted.");
    }
    int difference = lessThan - min;
    int quotient = primeProduct / difference;
    BigInteger sum;
    BigInteger bigValue;
    BigInteger bigPrimeProduct = new BigInteger(String.valueOf(primeProduct));
    BigInteger bigPrime;
    BigInteger bigResult;
    do {
      sum = ZERO;
      for (InversiveCongruentialGenerator gen : generators) {
        bigValue = new BigInteger(String.valueOf(gen.nextInt()));
        sum = sum.add(bigValue);
      }
      bigResult = sum.mod(bigPrimeProduct);
    } while (bigResult.intValue() >= (quotient * difference));
    return min + (bigResult.intValue() % difference);
  } // nextInt

  @Override
  public ArrayList<Boolean> booleanList(int length) {
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
}
