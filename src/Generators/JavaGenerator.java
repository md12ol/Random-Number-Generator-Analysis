package Generators;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that extends Random to provide similar functionality to the other generators I
 * implemented.
 */
public class JavaGenerator extends Random implements RandomNumberGenerator {

  /**
   * Prepares Random generator with seed.
   *
   * @param seed seed value
   */
  public JavaGenerator(long seed) {
    super(seed);
  } // JavaGenerator

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    return min + super.nextInt(lessThan - min);
  } // nextInt

  @Override
  public ArrayList<Boolean> booleanList(int length) throws IllegalArgumentException {
    RandomNumberGenerator.testSize(length);
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(super.nextBoolean());
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
    return intList(0, Integer.MAX_VALUE, length);
  } // intList
}