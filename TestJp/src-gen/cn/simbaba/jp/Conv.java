package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class Conv {
  private int stride;
  
  public int getStride() {
    return this.stride;
  }
  
  public void setStride(final int stride) {
    this.stride = stride;
  }
  
  private int pad;
  
  public int getPad() {
    return this.pad;
  }
  
  public void setPad(final int pad) {
    this.pad = pad;
  }
  
  private NDArray x;
  
  public NDArray getX() {
    return this.x;
  }
  
  public void setX(final NDArray x) {
    this.x = x;
  }
  
  private NDArray k;
  
  public NDArray getK() {
    return this.k;
  }
  
  public void setK(final NDArray k) {
    this.k = k;
  }
  
  private NDArray b;
  
  public NDArray getB() {
    return this.b;
  }
  
  public void setB(final NDArray b) {
    this.b = b;
  }
  
  private NDArray k_gradient;
  
  public NDArray getK_gradient() {
    return this.k_gradient;
  }
  
  public void setK_gradient(final NDArray k_gradient) {
    this.k_gradient = k_gradient;
  }
  
  private NDArray b_gradient;
  
  public NDArray getB_gradient() {
    return this.b_gradient;
  }
  
  public void setB_gradient(final NDArray b_gradient) {
    this.b_gradient = b_gradient;
  }
  
  private ArrayList<NDArray> image_col;
  
  public ArrayList<NDArray> getImage_col() {
    return this.image_col;
  }
  
  public void setImage_col(final ArrayList<NDArray> image_col) {
    this.image_col = image_col;
  }
  
  public Conv(final int[] kernel_shape, final int stride, final int pad) {
    int width=kernel_shape[0];
    int height=kernel_shape[1];
    int in_channel=kernel_shape[2];
    int out_channel=kernel_shape[3];
    this.stride = stride;
    this.pad = pad;
    int _in_channel = in_channel;
    int _multiply = (_in_channel * 3);
    int _width = width;
    int _multiply_1 = (_multiply * _width);
    int _height = height;
    int _multiply_2 = (_multiply_1 * _height);
    int _out_channel = out_channel;
    int _divide = (_multiply_2 / _out_channel);
    double scale = Math.sqrt(_divide);
    NDArray _standard_normal = Numpy.random.standard_normal(kernel_shape);
    NDArray _divide_1 = _standard_normal.operator_divide(Double.valueOf(scale));
    k = _divide_1;
    NDArray _standard_normal_1 = Numpy.random.standard_normal(out_channel);
    NDArray _divide_2 = _standard_normal_1.operator_divide(Double.valueOf(scale));
    b = _divide_2;
    k_gradient = Numpy.zeros(kernel_shape);
    b_gradient = Numpy.zeros(out_channel);
    ArrayList<NDArray> _arrayList = new ArrayList<NDArray>();
    image_col = _arrayList;
  }
  
  public Conv(final int[] kshape) {
    int stride = 1;
    int pad = 0;
    int width=kshape[0];
    int height=kshape[1];
    int in_channel=kshape[2];
    int out_channel=kshape[3];
    this.stride = stride;
    this.pad = pad;
    int _in_channel = in_channel;
    int _multiply = (_in_channel * 3);
    int _width = width;
    int _multiply_1 = (_multiply * _width);
    int _height = height;
    int _multiply_2 = (_multiply_1 * _height);
    int _out_channel = out_channel;
    int _divide = (_multiply_2 / _out_channel);
    double scale = Math.sqrt(_divide);
    NDArray _standard_normal = Numpy.random.standard_normal(kshape);
    NDArray _divide_1 = _standard_normal.operator_divide(Double.valueOf(scale));
    k = _divide_1;
    NDArray _standard_normal_1 = Numpy.random.standard_normal(out_channel);
    NDArray _divide_2 = _standard_normal_1.operator_divide(Double.valueOf(scale));
    b = _divide_2;
    k_gradient = Numpy.zeros(kshape);
    b_gradient = Numpy.zeros(out_channel);
    ArrayList<NDArray> _arrayList = new ArrayList<NDArray>();
    image_col = _arrayList;
  }
  
  public NDArray forward(final NDArray x) {
    this.x = x;
    if ((this.pad != 0)) {
      this.x = Numpy.pad(this.x, new int[][] { new int[] { 0, 0 }, new int[] { this.pad, this.pad }, new int[] { this.pad, this.pad }, new int[] { 0, 0 } });
    }
    int[] xshape = this.x.shape();
    int bx=xshape[0];
    int wx=xshape[1];
    int[] kshape = this.k.shape();
    int wk=kshape[0];
    int nk=kshape[3];
    int _wx = wx;
    int _wk = wk;
    int _minus = (_wx - _wk);
    int _divide = (_minus / this.stride);
    int feature_w = (_divide + 1);
    NDArray feature = Numpy.zeros(bx, feature_w, feature_w, nk);
    this.image_col.clear();
    NDArray kernel = this.k.reshape((-1), nk);
    for (int i = 0; (i < bx); i++) {
      {
        NDArray xi = x.get(new int[][]{{i},});
        NDArray imagecol = Img2Col.img2col(xi, wk, this.stride);
        NDArray _dot = Numpy.dot(imagecol, kernel);
        NDArray xx = _dot.operator_plus(this.b);
        feature.operator_set(xx.reshape(feature_w, feature_w, nk), new int[][]{{i}});
        this.image_col.add(imagecol);
      }
    }
    return feature;
  }
  
  public NDArray backward(final NDArray delta, final double learning_rate) {
    int[] xshape = this.x.shape();
    int bx=xshape[0];
    int wx=xshape[1];
    int hx=xshape[2];
    int[] kshape = this.k.shape();
    int wk=kshape[0];
    int hk=kshape[1];
    int ck=kshape[2];
    int[] dshape = delta.shape();
    int bd=dshape[0];
    int hd=dshape[1];
    int cd=dshape[2];
    NDArray delta_col = delta.reshape(bd, (-1), cd);
    for (int i = 0; (i < bx); i++) {
      NDArray _k_gradient = this.k_gradient;
      NDArray _reshape = Numpy.dot(this.image_col.get(i).T(), delta_col.get(i)).reshape(this.k.shape());
      this.k_gradient = _k_gradient.operator_plus(_reshape);
    }
    int _bx = bx;
    NDArray _divide = this.k_gradient.operator_divide(Integer.valueOf(_bx));
    this.k_gradient = _divide;
    NDArray _sum = Numpy.sum(delta_col, new int[] { 0, 1 });
    NDArray _plus = this.b_gradient.operator_plus(_sum);
    this.b_gradient = _plus;
    int _bx_1 = bx;
    NDArray _divide_1 = this.b_gradient.operator_divide(Integer.valueOf(_bx_1));
    this.b_gradient = _divide_1;
    NDArray delta_backward = Numpy.zeros(xshape);
    NDArray k_180 = Numpy.rot90(this.k, 2, new int[] { 0, 1 });
    k_180 = Numpy.swapaxes(k_180, 2, 3);
    NDArray k_180_col = k_180.reshape((-1), ck);
    NDArray pad_delta = delta;
    int _hx = hx;
    int _hd = hd;
    int _minus = (_hx - _hd);
    int _hk = hk;
    int _plus_1 = (_minus + _hk);
    int _minus_1 = (_plus_1 - 1);
    int pad = (_minus_1 / 2);
    int[][] pads = new int[][] { new int[] { 0, 0 }, new int[] { pad, pad }, new int[] { pad, pad }, new int[] { 0, 0 } };
    int _hd_1 = hd;
    int _hk_1 = hk;
    int _minus_2 = (_hd_1 - _hk_1);
    int _plus_2 = (_minus_2 + 1);
    int _hx_1 = hx;
    boolean _tripleNotEquals = (_plus_2 != _hx_1);
    if (_tripleNotEquals) {
      pad_delta = Numpy.pad(delta, pads);
    }
    for (int i = 0; (i < bx); i++) {
      {
        NDArray pad_delta_col = Img2Col.img2col(pad_delta.get(new int[][]{{i},}), wk, this.stride);
        delta_backward.set(Numpy.dot(pad_delta_col, k_180_col).reshape(wx, hx, ck), i);
      }
    }
    NDArray _multiply = this.k_gradient.operator_multiply(Double.valueOf(learning_rate));
    NDArray _minus_3 = this.k.operator_minus(_multiply);
    this.k = _minus_3;
    NDArray _multiply_1 = this.b_gradient.operator_multiply(Double.valueOf(learning_rate));
    NDArray _minus_4 = this.b.operator_minus(_multiply_1);
    this.b = _minus_4;
    return delta_backward;
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
