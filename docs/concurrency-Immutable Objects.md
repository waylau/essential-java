## 不可变对象（Immutable Objects）

如果一个对象它被构造后其，状态不能改变，则这个对象被认为是不可变的（immutable ）。不可变对象的好处是可以创建简单的、可靠的代码。

不可变对象在并发应用种特别有用。因为他们不能改变状态，它们不能被线程干扰所中断或者被其他线程观察到内部不一致的状态。

程序员往往不愿使用不可变对象，因为他们担心创建一个新的对象要比更新对象的成本要高。实际上这种开销常常被过分高估，而且使用不可变对象所带来的一些效率提升也抵消了这种开销。例如：使用不可变对象降低了垃圾回收所产生的额外开销，也减少了用来确保使用可变对象不出现并发错误的一些额外代码。

接下来看一个可变对象的类，然后转化为一个不可变对象的类。通过这个例子说明转化的原则以及使用不可变对象的好处。

### 一个同步类的例子

SynchronizedRGB 是表示颜色的类，每一个对象代表一种颜色，使用三个整形数表示颜色的三基色，字符串表示颜色名称。

```java
public class SynchronizedRGB {
	// Values must be between 0 and 255.
    private int red;
    private int green;
    private int blue;
    private String name;

    private void check(int red,
                       int green,
                       int blue) {
        if (red < 0 || red > 255
            || green < 0 || green > 255
            || blue < 0 || blue > 255) {
            throw new IllegalArgumentException();
        }
    }

    public SynchronizedRGB(int red,
                           int green,
                           int blue,
                           String name) {
        check(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }

    public void set(int red,
                    int green,
                    int blue,
                    String name) {
        check(red, green, blue);
        synchronized (this) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.name = name;
        }
    }

    public synchronized int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void invert() {
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
        name = "Inverse of " + name;
    }
}
```
使用 SynchronizedRGB 时需要小心，避免其处于不一致的状态。例如一个线程执行了以下代码：

    SynchronizedRGB color =
        new SynchronizedRGB(0, 0, 0, "Pitch Black");
    ...
    int myColorInt = color.getRGB();      //Statement 1
    String myColorName = color.getName(); //Statement 2


如果有另外一个线程在 Statement 1 之后、Statement 2 之前调用了 color.set 方法，那么 myColorInt 的值和 myColorName 的值就会不匹配。为了避免出现这样的结果，必须要像下面这样把这两条语句绑定到一块执行：

    synchronized (color) {
        int myColorInt = color.getRGB();
        String myColorName = color.getName();
    } 

这种不一致的问题只可能发生在可变对象上。

### 定义不可变对象的策略

以下的一些创建不可变对象的简单策略。并非所有不可变类都完全遵守这些规则，不过这不是编写这些类的程序员们粗心大意造成的，很可能的是他们有充分的理由确保这些对象在创建后不会被修改。但这需要非常复杂细致的分析，并不适用于初学者。

* 不要提供 setter 方法。（包括修改字段的方法和修改字段引用对象的方法）
* 将类的所有字段定义为 final、private 的。
* 不允许子类重写方法。简单的办法是将类声明为 final，更好的方法是将构造函数声明为私有的，通过工厂方法创建对象。
* 如果类的字段是对可变对象的引用，不允许修改被引用对象。
    * 不提供修改可变对象的方法。
    * 不共享可变对象的引用。当一个引用被当做参数传递给构造函数，而这个引用指向的是一个外部的可变对象时，一定不要保存这个引用。如果必须要保存，那么创建可变对象的拷贝，然后保存拷贝对象的引用。同样如果需要返回内部的可变对象时，不要返回可变对象本身，而是返回其拷贝。
    
将这一策略应用到 SynchronizedRGB 有以下几步：

* SynchronizedRGB 类有两个 setter 方法。第一个 set 方法只是简单的为字段设值，第二个 invert 方法修改为创建一个新对象，而不是在原有对象上修改。
* 所有的字段都已经是私有的，加上 final 即可。
* 将类声明为 final 的
* 只有一个字段是对象引用，并且被引用的对象也是不可变对象。

经过以上这些修改后，我们得到了 ImmutableRGB：

```java
public class ImmutableRGB {
	  // Values must be between 0 and 255.
    final private int red;
    final private int green;
    final private int blue;
    final private String name;

    private void check(int red,
                       int green,
                       int blue) {
        if (red < 0 || red > 255
            || green < 0 || green > 255
            || blue < 0 || blue > 255) {
            throw new IllegalArgumentException();
        }
    }

    public ImmutableRGB(int red,
                        int green,
                        int blue,
                        String name) {
        check(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }


    public int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public String getName() {
        return name;
    }

    public ImmutableRGB invert() {
        return new ImmutableRGB(255 - red,
                       255 - green,
                       255 - blue,
                       "Inverse of " + name);
    }
}
```

