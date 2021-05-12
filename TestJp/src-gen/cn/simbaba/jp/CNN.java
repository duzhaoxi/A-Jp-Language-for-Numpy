package cn.simbaba.jp;

import cn.centipede.model.data.MNIST;
import cn.centipede.npz.NpzFile;
import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class CNN {
  private Conv conv1;
  
  public Conv getConv1() {
    return this.conv1;
  }
  
  public void setConv1(final Conv conv1) {
    this.conv1 = conv1;
  }
  
  private Conv conv2;
  
  public Conv getConv2() {
    return this.conv2;
  }
  
  public void setConv2(final Conv conv2) {
    this.conv2 = conv2;
  }
  
  private Relu relu1;
  
  public Relu getRelu1() {
    return this.relu1;
  }
  
  public void setRelu1(final Relu relu1) {
    this.relu1 = relu1;
  }
  
  private Relu relu2;
  
  public Relu getRelu2() {
    return this.relu2;
  }
  
  public void setRelu2(final Relu relu2) {
    this.relu2 = relu2;
  }
  
  private Pool pool1;
  
  public Pool getPool1() {
    return this.pool1;
  }
  
  public void setPool1(final Pool pool1) {
    this.pool1 = pool1;
  }
  
  private Pool pool2;
  
  public Pool getPool2() {
    return this.pool2;
  }
  
  public void setPool2(final Pool pool2) {
    this.pool2 = pool2;
  }
  
  private Linear nn;
  
  public Linear getNn() {
    return this.nn;
  }
  
  public void setNn(final Linear nn) {
    this.nn = nn;
  }
  
  private Softmax softmax;
  
  public Softmax getSoftmax() {
    return this.softmax;
  }
  
  public void setSoftmax(final Softmax softmax) {
    this.softmax = softmax;
  }
  
  public CNN() {
    Conv _conv = new Conv(new int[] { 5, 5, 1, 6 });
    conv1 = _conv;
    Conv _conv_1 = new Conv(new int[] { 5, 5, 6, 16 });
    conv2 = _conv_1;
    Relu _relu = new Relu();
    relu1 = _relu;
    Relu _relu_1 = new Relu();
    relu2 = _relu_1;
    Pool _pool = new Pool();
    pool1 = _pool;
    Pool _pool_1 = new Pool();
    pool2 = _pool_1;
    Linear _linear = new Linear(256, 10);
    nn = _linear;
    Softmax _softmax = new Softmax();
    softmax = _softmax;
  }
  
  public void loadNpz(final NpzFile npz) {
    this.conv1.setK(npz.get("k1"));
    this.conv1.setB(npz.get("b1"));
    this.conv2.setK(npz.get("k2"));
    this.conv2.setB(npz.get("b2"));
    this.nn.setW(npz.get("w3"));
    this.nn.setB(npz.get("b3"));
  }
  
  public NDArray onehot(final NDArray targets, final int num) {
    NDArray result = Numpy.zeros(num, 10);
    for (int i = 0; (i < num); i++) {
      result.set(Integer.valueOf(1), i, targets.get(i).asInt());
    }
    return result;
  }
  
  public void train() {
    NDArray[] mnist = MNIST.numpy(true);
    NDArray targets = mnist[1];
    NDArray data = mnist[0].reshape(60000, 28, 28, 1).divide(Integer.valueOf(255));
    targets = this.onehot(targets, 60000);
    Conv _conv = new Conv(new int[] { 5, 5, 1, 6 });
    conv1 = _conv;
    Relu _relu = new Relu();
    relu1 = _relu;
    Pool _pool = new Pool();
    pool1 = _pool;
    Conv _conv_1 = new Conv(new int[] { 5, 5, 6, 16 });
    conv2 = _conv_1;
    Relu _relu_1 = new Relu();
    relu2 = _relu_1;
    Pool _pool_1 = new Pool();
    pool2 = _pool_1;
    Linear _linear = new Linear(256, 10);
    nn = _linear;
    Softmax _softmax = new Softmax();
    softmax = _softmax;
    double lr = 0.01;
    int batch = 3;
    for (int epoch = 0; (epoch < 1); epoch++) {
      {
        {
          int i = 0;
          boolean _while = (i < 6000);
          while (_while) {
            {
              NDArray X = data.get(new int[][] { new int[] { i, (i + batch) } });
              NDArray Y = targets.get(new int[][] { new int[] { i, (i + batch) } });
              NDArray predict = this.conv1.forward(X);
              predict = this.relu1.forward(predict);
              predict = this.pool1.forward(predict);
              predict = this.conv2.forward(predict);
              predict = this.relu2.forward(predict);
              predict = this.pool2.forward(predict);
              predict = predict.reshape(batch, (-1));
              predict = this.nn.forward(predict);
              NDArray delta = this.softmax.loss(predict, Y);
              delta = this.nn.backward(delta, lr);
              delta = delta.reshape(batch, 4, 4, 16);
              delta = this.pool2.backward(delta);
              delta = this.relu2.backward(delta);
              delta = this.conv2.backward(delta, lr);
              delta = this.pool1.backward(delta);
              delta = this.relu1.backward(delta);
              this.conv1.backward(delta, lr);
            }
            int _i = i;
            i = (_i + batch);
            _while = (i < 6000);
          }
        }
        double _lr = lr;
        double _pow = Math.pow(0.95, (epoch + 1));
        lr = (_lr * _pow);
      }
    }
  }
  
  public int predict(final NDArray X) {
    NDArray x = X.reshape(Numpy.newaxis, Numpy.ALL);
    NDArray predict = this.conv1.forward(x);
    predict = this.relu1.forward(predict);
    predict = this.pool1.forward(predict);
    predict = this.conv2.forward(predict);
    predict = this.relu2.forward(predict);
    predict = this.pool2.forward(predict);
    predict = predict.reshape(1, (-1));
    predict = this.nn.forward(predict);
    predict = this.softmax.predict(predict);
    return Numpy.argmax(predict);
  }
  
  public void eval() {
    NDArray[] mnist = MNIST.numpy(false);
    mnist[1].get(1).dump();
    NDArray test = mnist[0].reshape(10000, 28, 28, 1);
    NDArray img2col = Img2Col.img2col(test.get(1), 5, 1);
    System.out.println(img2col.shape());
  }
  
  @Override
  public String toString() {
    String result = new ToStringBuilder(this).addAllFields().toString();
    return result;
  }
}
