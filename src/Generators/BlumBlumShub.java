package Generators;

import static java.lang.Math.pow;
import static java.math.BigInteger.ONE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class BlumBlumShub implements RandomNumberGenerator {

  private Random rand;
  private BigInteger prevVal;
  private BigInteger M;
  private BigInteger p;
  private BigInteger q;

  public BlumBlumShub(Random r, long s) {
    this.rand = r;
    if (s < 2) {
      throw new IllegalArgumentException("Seed must be greater than 1");
    }
    // Constants
    BigInteger three = new BigInteger("3");
    BigInteger four = new BigInteger("4");
    BigInteger bigSeed = new BigInteger(String.valueOf(s));
    do {
      p = RandomNumberGenerator.getPrime(rand, 512);
    } while (!p.mod(four).equals(three));
    do {
      q = RandomNumberGenerator.getPrime(rand, 512);
    } while (!q.mod(four).equals(three));
    M = p.multiply(q);
    if (!bigSeed.gcd(M).equals(ONE)) {
      throw new IllegalArgumentException("The seed is not congruent to sum of the primes");
    }
    prevVal = bigSeed;
  } // BlumBlumShub

  @Override
  public boolean nextBoolean() {
    BigInteger two = new BigInteger("2");
    prevVal = prevVal.modPow(two, M); // prevVal = (prevVal^2) mod M
    if (prevVal.remainder(two).equals(ONE)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);

    // Get the number of bits used to represent the difference
    int difference = lessThan - min;
    BigInteger bigDifference = new BigInteger(String.valueOf(difference));
    int numBits = bigDifference.bitLength();

    // Find the random number
    ArrayList<Boolean> randomList;
    BigInteger randomBig;
    do {
      randomList = booleanList(numBits);
      int randInt = 0;
      // Get decimal representation of binary number
      for (int i = randomList.size() - 1; i >= 0; i--) {
        if (randomList.get(i)) {
          randInt += pow(2, i);
        }
      }
      randomBig = new BigInteger(String.valueOf(randInt));
      // While randomBig is greater than or equal to bigDifference
    } while (randomBig.max(bigDifference).equals(randomBig) || randomBig.equals(bigDifference));
    return randomBig.intValue() + min;
  }

  @Override
  public ArrayList<Boolean> booleanList(int length) {
    RandomNumberGenerator.testSize(length);
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(nextBoolean());
    }
    return list;
  }

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
  }
}
