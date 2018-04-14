package Generators;

import java.util.ArrayList;
import java.util.Random;

public class JavaGenerator extends Random implements RandomNumberGenerator {

  public JavaGenerator(long seed) {
    super(seed);
  } // JavaGenerator

  @Override
  public boolean nextBoolean() {
    return super.nextBoolean();
  }

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    return RandomNumberGenerator.normalize(super.nextInt(), min, lessThan);
    // TODO: Reconsider this normalize function
  }

  @Override
  public ArrayList<Boolean> booleanList(int length) throws IllegalArgumentException {
    RandomNumberGenerator.testSize(length);
    ArrayList<Boolean> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(super.nextBoolean());
    }
    return null;
  }

  @Override
  public ArrayList<Integer> intList(int min, int lessThan, int length)
      throws IllegalArgumentException {
    RandomNumberGenerator.testRange(min, lessThan);
    RandomNumberGenerator.testSize(length);
    ArrayList<Integer> list = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      list.add(RandomNumberGenerator.normalize(super.nextInt(), min, lessThan));
    }
    return null;
  }
}