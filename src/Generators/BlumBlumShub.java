package Generators;

import static java.lang.Math.pow;
import static java.math.BigInteger.ONE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implements Blum Blum Shub random number generator.  I will outline the basics below.  For a quick
 * overview see 'https://en.wikipedia.org/wiki/Blum_Blum_Shub.'  For a detailed explanation see the
 * included 'Blum Blum Shub.pdf.'  In this case the the length of p and q was chosen to be 512 bits
 * which results in M being of size at least 1024 bits (often more).
 *
 * <pre>
 *   Blum Blum Shub Overview:
 *   p, q are large primes such that p ≡ 3 (mod 4), q ≡ 3 (mod 4), and |p| = |q| (length in bits)
 *   M = p * q
 *   n >= 0
 *   x_n+1 = (x_n)^2 mod M
 *   x_0 is chosen such that gcd(x_0, M) = 1 and x_0 > 1
 *   Output of x_n: least significant bit of x_n
 * </pre>
 */
public class BlumBlumShub implements RandomNumberGenerator {

  private BigInteger prevVal;
  private BigInteger M;

  /**
   * Creates instance of Blum Blum Shub random number generator using rand for any random values
   * that need to be generated and seed as the value of x_0.
   *
   * @param rand random number generator for creation of random values
   * @param seed value x_0
   */
  public BlumBlumShub(Random rand, long seed) {
    if (seed < 2) {
      throw new IllegalArgumentException("Seed must be greater than 1");
    }
    // Constants
    BigInteger bigSeed = new BigInteger(String.valueOf(seed));
    BigInteger three = new BigInteger("3");
    BigInteger four = new BigInteger("4");

    // Find p, q such that they are both congruent to 3 mod 4 and |p| = |q|
    BigInteger p, q;
    do {
      p = RandomNumberGenerator.getBigPrime(rand, 512);
    } while (!p.mod(four).equals(three));
    do {
      q = RandomNumberGenerator.getBigPrime(rand, 512);
    } while (!q.mod(four).equals(three) || q.bitLength() != p.bitLength());

    // Calculate M
    M = p.multiply(q);
    if (!bigSeed.gcd(M).equals(ONE)) {
      throw new IllegalArgumentException("The seed is not congruent to product of the primes p "
          + "and q");
    }
    prevVal = bigSeed;
  } // BlumBlumShub

  @Override
  public int nextInt() {
    return nextInt(0, 100);
  }

  @Override
  public boolean nextBoolean() {
    BigInteger two = new BigInteger("2");
    prevVal = prevVal.modPow(two, M); // prevVal = (prevVal^2) mod M
    return (prevVal.mod(two).equals(ONE)); // return ((prevVal mod 2) == 1)
  } // nextBoolean

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);

    // Get the number of bits used to represent the difference
    int difference = lessThan - min - 1; // Do not include lessThan is potential output
    BigInteger bigDifference = new BigInteger(String.valueOf(difference));
    int numBits = bigDifference.bitLength();

    // Get a random int in range 0 (inclusive) to difference (inclusive)
    ArrayList<Boolean> randomList;
    int randInt;
    do {
      randomList = booleanList(numBits);
      randInt = 0;
      // Get decimal representation of binary number (list of booleans)
      for (int i = 0; i < randomList.size(); i++) {
        if (randomList.get(i)) {
          randInt += pow(2, i);
        }
      }
    } while (randInt > difference); // Ensure that randInt is within range of desired int
    return min + randInt;
  } // nextInt

  @Override
  public ArrayList<Boolean> booleanList(int length) {
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
}
