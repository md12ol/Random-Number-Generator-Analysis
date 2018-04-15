package Tests;

import java.util.ArrayList;

public class NaiveTest {

  // TODO: Add JavaDoc and return percents
  public static ArrayList<Integer> runTest(int min, int lessThan, ArrayList<Integer> nums) {
    ArrayList<Integer> list = new ArrayList<>(lessThan - min);
    for (int i = 0; i < (lessThan - min); i++) {
      list.add(0);
    }
    for (Integer num : nums) {
      list.set(num, list.get(num) + 1);
    }
    return list;
  }

  // TODO: Boolean version
}