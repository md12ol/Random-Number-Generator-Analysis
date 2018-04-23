package Generators;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Outlines common methods that all generators need to implement and provides utility methods that
 * are needed by some of the generators.
 */
public interface RandomNumberGenerator {

  /**
   * Implements Gordon's algorithm for generating large strong primes.  Algorithm found within
   * Chapter 4 of Handbook of Applied Cryptography by A. Menezes, P. Van Oorschot, and S. Vanstone
   * which is included as 'Handbook of Applied Crytography Chapter 4 - Public Key Parameters.pdf.' A
   * prime is returned with probability 1 - 2^(-100).  This prime has length ~bitLength bits.
   *
   * @param rand Random number generator
   * @param bitLength Approximate length of prime returned
   * @return Probable prime of length ~bitLength bits with probability of 1 - 2^(-100) being prime
   */
  static BigInteger getBigPrime(Random rand, int bitLength) {
    // Declare BigInteger values needed for arithmetic
    BigInteger rminus2;
    BigInteger minus1 = new BigInteger("-1");
    BigInteger one = new BigInteger("1");
    BigInteger two = new BigInteger("2");

    // Algorithm variables not otherwise organized
    BigInteger p; // Prime being returned
    BigInteger r; // Intermediate prime used for finding p
    BigInteger p0; // Intermediate value used for finding p

    // Probable primes of size bitLength/2 bits
    BigInteger s = BigInteger.probablePrime(bitLength / 2, rand);
    BigInteger t = BigInteger.probablePrime(bitLength / 2, rand);

    // Random integers of size 4 bits
    BigInteger i0 = new BigInteger(4, rand);
    BigInteger j0 = new BigInteger(4, rand);

    // For iterating through i0 + offset with offset = 0, 1, 2, ... until desired result
    BigInteger i;
    BigInteger offset;

    // Find prime r
    offset = new BigInteger("0");
    do {
      i = i0.add(offset); // i = i0 + offset
      r = two.multiply(i).multiply(t).add(one); // r = 2it + 1
      offset = offset.add(one);
    } while (!r.isProbablePrime(100)); // Continue if r is not prime

    rminus2 = r.add(new BigInteger("-2")); // rminus2 = r - 2
    p0 = two.multiply(s.modPow(rminus2, r)).multiply(s).add(minus1); // 2((s^(r - 1)) mod r)s - 1

    // Find prime p
    offset = new BigInteger("0");
    do {
      i = j0.add(offset); // i = j0 + offset
      p = p0.add(two.multiply(i).multiply(r).multiply(s)); // p = p0 + 2jrs
      offset = offset.add(one);
    } while (!p.isProbablePrime(100)); // Continue if p is not prime
    return p;
  } // getBigPrime

  /**
   * Returns a prime with probability 1 - 2^(-100).  This prime has length ~bitLength bits.
   *
   * @param rand Random number generator
   * @param bitLength Approximate length of prime returned
   * @return Probable prime of length ~bitLength bits with probability of 1 - 2^(-100) being prime
   */
  static int getPrime(Random rand, int bitLength) {
    return getBigPrime(rand, bitLength).intValue();
  } // getPrime

  /**
   * Tests if lessThan is greater than min.  If not, an IllegalArguementException is thrown.
   *
   * @param min lower bound of range (inclusive)
   * @param lessThan upper bound of range (exclusive)
   * @throws IllegalArgumentException if lessThan is less than or equal to min
   */
  static void testRange(int min, int lessThan) throws IllegalArgumentException {
    if (lessThan <= min) {
      throw new IllegalArgumentException("Cannot return a value greater than or equal to min and "
          + "less than lessThan when lessThan is less than or equal to min.");
    }
  } // testRange

  /**
   * Tests if length is greater than 0.  If not, an IllegalArguementException is thrown.
   *
   * @param length length of list
   * @throws IllegalArgumentException if length is less than or equal to 0
   */
  static void testSize(int length) throws IllegalArgumentException {
    if (length < 1) {
      throw new IllegalArgumentException("Cannot return a list of length less than 1");
    }
  } // testSize

  /**
   * Returns the next boolean value from the random number generator.
   *
   * @return boolean from random number generator
   */
  boolean nextBoolean();

  /**
   * Returns the next int in range [min, lessThan) from the random number generator.
   *
   * @param min minimum value of the int returned
   * @param lessThan the int returned will be less than this
   * @return int greater than or equal to min and less than lessThan
   * @throws IllegalArgumentException if lessThan is less than or equal to min
   */
  int nextInt(int min, int lessThan) throws IllegalArgumentException;

  /**
   * Returns the next integer from the random number generator.  This returned integer either has
   * some default range or the range is set to all possible values returned by the generator.
   *
   * @return next integer from random number generator
   */
  int nextInt();

  /**
   * Returns a list of size length of booleans from the random number generator.
   *
   * @param length length of returned list
   * @return list of booleans from random number generator
   * @throws IllegalArgumentException if length is less than 1
   */
  ArrayList<Boolean> booleanList(int length) throws IllegalArgumentException;

  /**
   * Returns a list of size length of ints in range [min, lessThan) from the random number
   * generator.
   *
   * @param min minimum value of the ints
   * @param lessThan the ints returned will bt less than this
   * @param length length of returned list
   * @return list of ints greater than or equal to min and less than lessThan
   * @throws IllegalArgumentException if lessThan is less than or equal to min or if length is less
   * than 1
   */
  ArrayList<Integer> intList(int min, int lessThan, int length) throws IllegalArgumentException;

  /**
   * Returns a list of size length of ints from the random number generator.
   *
   * @param length length of returned list
   * @return list of ints
   * @throws IllegalArgumentException if lessThan is less than or equal to min or if length is less
   * than 1
   */
  ArrayList<Integer> intList(int length) throws IllegalArgumentException;
}