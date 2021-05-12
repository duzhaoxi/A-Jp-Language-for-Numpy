package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class Linear {
  private NDArray x;
  
  public NDArray getX() {
    return this.x;
  }
  
  public void setX(final NDArray x) {
    this.x = x;
  }
  
  private NDArray w;
  
  public NDArray getW() {
    return this.w;
  }
  
  public void setW(final NDArray w) {
    this.w = w;
  }
  
  private NDArray b;
  
  public NDArray getB() {
    return this.b;
  }
  
  public void setB(final NDArray b) {
    this.b = b;
  }
  
  private NDArray w_gradient;
  
  public NDArray getW_gradient() {
    return this.w_gradient;
  }
  
  public void setW_gradient(final NDArray w_gradient) {
    this.w_gradient = w_gradient;
  }
  
  private NDArray b_gradient;
  
  public NDArray getB_gradient() {
    return this.b_gradient;
  }
  
  public void setB_gradient(final NDArray b_gradient) {
    this.b_gradient = b_gradient;
  }
  
  public Linear(final int inChannel, final int outChannel) {
    double scale = Math.sqrt((inChannel / 2.0));
    NDArray _standard_normal = Numpy.random.standard_normal(inChannel, outChannel);
    NDArray _divide = _standard_normal.operator_divide(Double.valueOf(scale));
    w = _divide;
    NDArray _standard_normal_1 = Numpy.random.standard_normal(outChannel);
    NDArray _divide_1 = _standard_normal_1.operator_divide(Double.valueOf(scale));
    b = _divide_1;
    w_gradient = Numpy.zeros(inChannel, outChannel);
    b_gradient = Numpy.zeros(outChannel);
  }
  
  public NDArray forward(final NDArray x) {
    this.x = x;
    NDArray _dot = Numpy.dot(x, this.w);
    NDArray x_forward = _dot.operator_plus(this.b);
    return x_forward;
  }
  
  public NDArray backward(final NDArray delta, final double learning_rate) {
    int[] shape = this.x.shape();
    int batch_size = shape[0];
    NDArray _dot = Numpy.dot(this.x.T(), delta);
    NDArray _divide = _dot.operator_divide(Integer.valueOf(batch_size));
    w_gradient = _divide;
    NDArray _sum = Numpy.sum(delta, 0);
    NDArray _divide_1 = _sum.operator_divide(Integer.valueOf(batch_size));
    b_gradient = _divide_1;
    NDArray delta_backward = Numpy.dot(delta, this.w.T());
    NDArray _w = this.w;
    NDArray _multiply = this.w_gradient.operator_multiply(Double.valueOf(learning_rate));
    this.w = _w.operator_minus(_multiply);
    NDArray _b = this.b;
    NDArray _multiply_1 = this.b_gradient.operator_multiply(Double.valueOf(learning_rate));
    this.b = _b.operator_minus(_multiply_1);
    return delta_backward;
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
