package cn.simbaba.jp;

import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class Softmax {
  private NDArray softmax;
  
  public NDArray getSoftmax() {
    return this.softmax;
  }
  
  public void setSoftmax(final NDArray softmax) {
    this.softmax = softmax;
  }
  
  public NDArray loss(final NDArray predict, final NDArray label) {
    int[] pdimens = predict.shape();
    int batchsize = pdimens[0];
    this.predict(predict);
    NDArray delta = Numpy.zeros(pdimens);
    double loss = 0.0;
    for (int i = 0; (i < batchsize); i++) {
      {
        NDArray label_i = label.get(new int[][]{{i},});
        NDArray softmanx_i = this.softmax.get(i);
        delta.operator_set(softmanx_i.operator_minus(label_i), new int[][]{{i}});
        double _loss = loss;
        NDArray _log = Numpy.log(softmanx_i);
        NDArray _multiply = _log.operator_multiply(label_i);
        double _sum = Numpy.sum(_multiply);
        loss = (_loss - _sum);
      }
    }
    double _loss = loss;
    loss = (_loss / batchsize);
    System.out.printf("Softmax: loss=%f\n", Double.valueOf(loss));
    return delta;
  }
  
  public NDArray predict(final NDArray predict) {
    int batchsize = predict.shape()[0];
    softmax = Numpy.zeros(predict.shape());
    for (int i = 0; (i < batchsize); i++) {
      {
        Object _max = Numpy.max(predict.get(new int[][]{{i},}));
        NDArray predict_tmp = predict.get(new int[][]{{i},}).operator_minus(_max);
        predict_tmp = Numpy.exp(predict_tmp);
        softmax.operator_set(predict_tmp.operator_divide(Double.valueOf(Numpy.sum(predict_tmp))), new int[][]{{i}});
      }
    }
    return this.softmax;
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
