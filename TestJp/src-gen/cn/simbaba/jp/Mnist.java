package cn.simbaba.jp;

import cn.centipede.model.data.MNIST;
import cn.centipede.npz.NpzFile;
import cn.centipede.numpy.NDArray;
import cn.centipede.numpy.Numpy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class Mnist {
  public static void main(final String... args) {
    NDArray[] mnist = MNIST.numpy(false);
    CNN cnn = new CNN();
    
    
    try(FileInputStream f = new FileInputStream( new File("mnist.npz"))){
      {
        FileInputStream _f = f;
        NpzFile npz = new NpzFile(_f);
        cnn.loadNpz(npz);
      }
    } catch(IOException e){
    }
    Numpy.random.seed(System.currentTimeMillis());
    int rand = Numpy.random.randint(10000);
    NDArray label = mnist[1].row(rand);
    NDArray X = mnist[0].reshape(10000, 28, 28, 1).get(rand);
    int actual = cnn.predict(X);
    X.reshape(28, 28).dump();
    System.out.printf("rand=%d, label=%d, predict=%d\n", Integer.valueOf(rand), Integer.valueOf(label.asInt()), Integer.valueOf(actual));
    NDArray a = Numpy.arange(24).reshape(2, 3, 4);
    int a1 = a.shape()[0];
    int _a1 = a1;
    String _plus = ("\u7EF4\u5EA6:" + Integer.valueOf(_a1));
    InputOutput.<String>println(_plus);
    int[] c = new int[] { 1, 2 };
    String _plus_1 = ("\u6570\u7EC4:" + Integer.valueOf(c[0]));
    InputOutput.<String>println(_plus_1);
    NDArray b = a.get(new int[][]{{0,(-1)},{2},});
    b.operator_set(9, new int[][]{{0}});
    b.dump();
  }
}
