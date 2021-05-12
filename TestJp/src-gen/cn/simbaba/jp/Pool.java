package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class Pool {
  private NDArray feature_mask;
  
  public NDArray getFeature_mask() {
    return this.feature_mask;
  }
  
  public void setFeature_mask(final NDArray feature_mask) {
    this.feature_mask = feature_mask;
  }
  
  public NDArray forward(final NDArray x) {
    int[] xshape = x.shape();
    int b=xshape[0];
    int w=xshape[1];
    int h=xshape[2];
    int c=xshape[3];
    int _w = w;
    int feature_w = (_w / 2);
    NDArray feature = Numpy.zeros(new int[] { b, feature_w, feature_w, c });
    this.feature_mask = Numpy.zeros(new int[] { b, w, h, c });
    for (int bi = 0; (bi < b); bi++) {
      for (int ci = 0; (ci < c); ci++) {
        for (int i = 0; (i < feature_w); i++) {
          for (int j = 0; (j < feature_w); j++) {
            {
              NDArray dat = x.get(new int[][] { new int[] { bi }, new int[] { (i * 2), ((i * 2) + 2) }, new int[] { (j * 2), ((j * 2) + 2) }, new int[] { ci } });
              feature.operator_set(Numpy.max(dat), new int[][]{{bi}, {i}, {j}, {ci}});
              int index = Numpy.argmax(dat);
              this.feature_mask.operator_set(1, new int[][]{{bi}, {((i * 2) + (index / 2))}, {((j * 2) + (index % 2))}, {ci}});
            }
          }
        }
      }
    }
    return feature;
  }
  
  public NDArray backward(final NDArray delta) {
    NDArray _repeat = Numpy.repeat(Numpy.repeat(delta, 2, 1), 2, 2);
    return _repeat.operator_multiply(this.feature_mask);
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
