package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;

@SuppressWarnings("all")
public class Xyz {
  public static void main(final String... args) {
    NDArray a = Numpy.arange(12).reshape(3, 4);
    NDArray b = a.get(new int[][]{{2,-999_999_999},}).get(new int[][]{{1},});
    b.dump();
  }
}
