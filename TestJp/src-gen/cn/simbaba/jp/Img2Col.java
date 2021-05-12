package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;

@SuppressWarnings("all")
public class Img2Col {
  public static NDArray img2col(final NDArray x, final int ksize, final int stride) {
    int[] xshape = x.shape();
    int wx=xshape[0];
    int cx=xshape[2];
    int _wx = wx;
    int _minus = (_wx - ksize);
    int _divide = (_minus / stride);
    int feature_w = (_divide + 1);
    int _cx = cx;
    int _multiply = ((ksize * ksize) * _cx);
    NDArray imageCol = Numpy.zeros((feature_w * feature_w), _multiply);
    int num = 0;
    for (int i = 0; (i < feature_w); i++) {
      for (int j = 0; (j < feature_w); j++) {
        {
          int[][] range = new int[][] { new int[] { (i * stride), ((i * stride) + ksize) }, new int[] { (j * stride), ((j * stride) + ksize) } };
          NDArray get = x.get(range).reshape((-1));
          Numpy.set(imageCol, get, num);
          num++;
        }
      }
    }
    return imageCol;
  }
  
  public static void main(final String... args) {
  }
}
