package Generators;

import java.math.BigInteger;
import java.util.ArrayList;

public class InversiveCongruentialGenerator implements RandomNumberGenerator {

  BigInteger q;

  public InversiveCongruentialGenerator() {

  } // InversiveCongruentialGenerator

  @Override
  public boolean nextBoolean() {
    return false;
  } // nextBoolean

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    return 0;
  } // nextInt

  @Override
  public ArrayList<Boolean> booleanList(int length) {
    return null;
  } // booleanList

  @Override
  public ArrayList<Integer> intList(int min, int lessThan, int length)
      throws IllegalArgumentException {
    return null;
  } // intList
}
