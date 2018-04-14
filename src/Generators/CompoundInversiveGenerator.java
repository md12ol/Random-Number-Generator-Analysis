package Generators;

import java.util.ArrayList;

public class CompoundInversiveGenerator implements RandomNumberGenerator {

  @Override
  public boolean nextBoolean() {
    return false;
  }

  @Override
  public int nextInt(int min, int lessThan) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public ArrayList<Boolean> booleanList(int length) {
    return null;
  }

  @Override
  public ArrayList<Integer> intList(int min, int lessThan, int length)
      throws IllegalArgumentException {
    return null;
  }
}
