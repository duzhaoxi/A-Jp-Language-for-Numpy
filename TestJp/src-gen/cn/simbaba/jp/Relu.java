package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class Relu {
  private NDArray x;
  
  public NDArray getX() {
    return this.x;
  }
  
  public void setX(final NDArray x) {
    this.x = x;
  }
  
  public NDArray forward(final NDArray x) {
    this.x = x;
    return Numpy.maximum(x, 0.0);
  }
  
  public NDArray backward(final NDArray delta) {
    Numpy.checkset(delta, this.x, 0.0, 0.0);
    return delta;
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
