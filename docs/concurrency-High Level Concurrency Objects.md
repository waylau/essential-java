## 高级并发对象

目前为止，之前的教程都是重点讲述了最初作为 Java 平台一部分的低级别 API。这些API 对于非常基本的任务来说已经足够，但是对于更高级的任务就需要更高级的 API。特别是针对充分利用了当今多处理器和多核系统的大规模并发应用程序。 本章，我们将着眼于 Java 5.0 新增的一些高级并发特征。大多数功能已经在新的java.util.concurrent 包中实现。Java 集合框架中也定义了新的并发数据结构。

### 锁对象

提供了可以简化许多并发应用的锁的惯用法。

同步代码依赖于一种简单的可重入锁。这种锁使用简单，但也有诸多限制。java.util.concurrent.locks 包提供了更复杂的锁。这里会重点关注其最基本的接口 [Lock](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/Lock.html)。 Lock 对象作用非常类似同步代码使用的内部锁。如同内部锁，每次只有一个线程可以获得 Lock  对象。通过关联 Condition 对象，Lock 对象也支持 wait/notify 机制。 

Lock 对象之于隐式锁最大的优势在于，它们有能力收回获得锁的尝试。如果当前锁对象不可用，或者锁请求超时（如果超时时间已指定），tryLock 方法会收回获取锁的请求。如果在锁获取前，另一个线程发送了一个中断，lockInterruptibly 方法也会收回获取锁的请求。 

让我们使用 Lock 对象来解决我们在活跃度中见到的死锁问题。Alphonse 和 Gaston 已经把自己训练成能注意到朋友何时要鞠躬。我们通过要求 Friend 对象在双方鞠躬前必须先获得锁来模拟这次改善。下面是改善后模型的源代码 Safelock :

```java

public class Safelock {
	static class Friend {
		private final String name;
		private final Lock lock = new ReentrantLock();

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public boolean impendingBow(Friend bower) {
			Boolean myLock = false;
			Boolean yourLock = false;
			try {
				myLock = lock.tryLock();
				yourLock = bower.lock.tryLock();
			} finally {
				if (!(myLock && yourLock)) {
					if (myLock) {
						lock.unlock();
					}
					if (yourLock) {
						bower.lock.unlock();
					}
				}
			}
			return myLock && yourLock;
		}

		public void bow(Friend bower) {
			if (impendingBow(bower)) {
				try {
					System.out.format("%s: %s has" + " bowed to me!%n", this.name, bower.getName());
					bower.bowBack(this);
				} finally {
					lock.unlock();
					bower.lock.unlock();
				}
			} else {
				System.out.format(
						"%s: %s started" + " to bow to me, but saw that" + " I was already bowing to" + " him.%n",
						this.name, bower.getName());
			}
		}

		public void bowBack(Friend bower) {
			System.out.format("%s: %s has" + " bowed back to me!%n", this.name, bower.getName());
		}
	}

	static class BowLoop implements Runnable {
		private Friend bower;
		private Friend bowee;

		public BowLoop(Friend bower, Friend bowee) {
			this.bower = bower;
			this.bowee = bowee;
		}

		public void run() {
			Random random = new Random();
			for (;;) {
				try {
					Thread.sleep(random.nextInt(10));
				} catch (InterruptedException e) {
				}
				bowee.bow(bower);
			}
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new BowLoop(alphonse, gaston)).start();
		new Thread(new BowLoop(gaston, alphonse)).start();
	}
}
```


### 执行器（Executors）

为加载和管理线程定义了高级 API。Executors 的实现由 java.util.concurrent 包提供，提供了适合大规模应用的线程池管理。

在之前所有的例子中，Thread 对象表示的线程和 Runnable 对象表示的线程所执行的任务之间是紧耦合的。这对于小型应用程序来说没问题，但对于大规模并发应用来说，合理的做法是将线程的创建与管理和程序的其他部分分离开。封装这些功能的对象就是执行器，接下来的部分将讲详细描述执行器。 

#### 执行器接口

在 java.util.concurrent 中包括三个执行器接口：

* Executor，一个运行新任务的简单接口。
* ExecutorService，扩展了 Executor 接口。添加了一些用来管理执行器生命周期和任务生命周期的方法。
* ScheduledExecutorService，扩展了 ExecutorService。支持 future 和（或）定期执行任务。

通常来说，指向 executor 对象的变量应被声明为以上三种接口之一，而不是具体的实现类

##### Executor 接口

[Executor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executor.html) 接口只有一个 execute 方法，用来替代通常创建（启动）线程的方法。例如：r 是一个 Runnable 对象，e 是一个 Executor 对象。可以使用

    e.execute(r);

代替

    (new Thread(r)).start();

但 execute 方法没有定义具体的实现方式。对于不同的 Executor 实现，execute 方法可能是创建一个新线程并立即启动，但更有可能是使用已有的工作线程运行r，或者将 r放入到队列中等待可用的工作线程。（我们将在线程池一节中描述工作线程。）

##### ExecutorService 接口

[ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html) 接口在提供了 execute 方法的同时，新加了更加通用的 submit 方法。submit 方法除了和 execute 方法一样可以接受 Runnable 对象作为参数，还可以接受 [Callable](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html) 对象作为参数。使用 Callable对象可以能使任务返还执行的结果。通过 submit 方法返回的 [Future](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Future.html) 对象可以读取 Callable 任务的执行结果，或是管理 Callable 任务和 Runnable 任务的状态。 ExecutorService 也提供了批量运行 Callable 任务的方法。最后，ExecutorService 还提供了一些关闭执行器的方法。如果需要支持即时关闭，执行器所执行的任务需要正确处理中断。

##### ScheduledExecutorService 接口

[ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html) 扩展 ExecutorService接口并添加了 schedule 方法。调用 schedule 方法可以在指定的延时后执行一个Runnable 或者 Callable 任务。ScheduledExecutorService 接口还定义了按照指定时间间隔定期执行任务的 scheduleAtFixedRate 方法和 scheduleWithFixedDelay 方法。

#### 线程池

线程池是最常见的一种执行器的实现。

在 java.util.concurrent 包中多数的执行器实现都使用了由工作线程组成的线程池，工作线程独立于所它所执行的 Runnable 任务和 Callable 任务，并且常用来执行多个任务。 

使用工作线程可以使创建线程的开销最小化。在大规模并发应用中，创建大量的 Thread 对象会占用占用大量系统内存，分配和回收这些对象会产生很大的开销。

一种最常见的线程池是固定大小的线程池。这种线程池始终有一定数量的线程在运行，如果一个线程由于某种原因终止运行了，线程池会自动创建一个新的线程来代替它。需要执行的任务通过一个内部队列提交给线程，当没有更多的工作线程可以用来执行任务时，队列保存额外的任务。 

使用固定大小的线程池一个很重要的好处是可以实现优雅退化(degrade gracefully)。例如一个 Web 服务器，每一个 HTTP 请求都是由一个单独的线程来处理的，如果为每一个 HTTP 都创建一个新线程，那么当系统的开销超出其能力时，会突然地对所有请求都停止响应。如果限制 Web 服务器可以创建的线程数量，那么它就不必立即处理所有收到的请求，而是在有能力处理请求时才处理。 

创建一个使用线程池的执行器最简单的方法是调用 [java.util.concurrent.Executors](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html) 的 [newFixedThreadPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html#newFixedThreadPool-int-) 方法。Executors 类还提供了下列一下方法：

* [newCachedThreadPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html#newCachedThreadPool-int-) 方法创建了一个可扩展的线程池。适合用来启动很多短任务的应用程序。
* newSingleThreadExecutor 方法创建了每次执行一个任务的执行器。
* 还有一些 ScheduledExecutorService 执行器创建的工厂方法。

如果上面的方法都不满足需要，可以尝试
[java.util.concurrent.ThreadPoolExecutor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html) 或者[java.util.concurrent.ScheduledThreadPoolExecutor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledThreadPoolExecutor.html)。

#### Fork/Join

该框架是 JDK 7 中引入的并发框架。

fork/join 框架是 ExecutorService 接口的一种具体实现，目的是为了帮助你更好地利用多处理器带来的好处。它是为那些能够被递归地拆解成子任务的工作类型量身设计的。其目的在于能够使用所有可用的运算能力来提升你的应用的性能。 

类似于 ExecutorService 接口的其他实现，fork/join 框架会将任务分发给线程池中的工作线程。fork/join 框架的独特之处在与它使用工作窃取(work-stealing)算法。完成自己的工作而处于空闲的工作线程能够从其他仍然处于忙碌(busy)状态的工作线程处窃取等待执行的任务。 

fork/join 框架的核心是 ForkJoinPool 类，它是对 AbstractExecutorService 类的扩展。ForkJoinPool 实现了工作窃取算法，并可以执行 [ForkJoinTask](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinTask.html) 任务。

##### 基本使用方法

使用 fork/join 框架的第一步是编写执行一部分工作的代码。你的代码结构看起来应该与下面所示的伪代码类似：

    if (my portion of the work is small enough)
      do the work directly
    else
      split my work into two pieces
      invoke the two pieces and wait for the results
      
翻译为中文为：

    if (当前这个任务工作量足够小)
        直接完成这个任务
    else
        将这个任务或这部分工作分解成两个部分
        分别触发(invoke)这两个子任务的执行，并等待结果
        
你需要将这段代码包裹在一个 ForkJoinTask 的子类中。不过，通常情况下会使用一种更为具体的的类型，或者是 [RecursiveTask](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/RecursiveTask.html)(会返回一个结果)，或者是 [RecursiveAction](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/RecursiveAction.html)。 当你的 ForkJoinTask 子类准备好了，创建一个代表所有需要完成工作的对象，然后将其作为参数传递给一个ForkJoinPool 实例的 invoke() 方法即可。

##### 模糊图片的例子

想要了解 fork/join 框架的基本工作原理，接下来的这个例子会有所帮助。假设你想要模糊一张图片。原始的 source 图片由一个整数的数组表示，每个整数表示一个像素点的颜色数值。与 source 图片相同，模糊之后的 destination 图片也由一个整数数组表示。 对图片的模糊操作是通过对 source 数组中的每一个像素点进行处理完成的。处理的过程是这样的：将每个像素点的色值取出，与周围像素的色值（红、黄、蓝三个组成部分）放在一起取平均值，得到的结果被放入 destination 数组。因为一张图片会由一个很大的数组来表示，这个流程会花费一段较长的时间。如果使用 fork/join 框架来实现这个模糊算法，你就能够借助多处理器系统的并行处理能力。下面是上述算法结合 fork/join 框架的一种简单实现：


```java
public class ForkBlur extends RecursiveAction {
    private int[] mSource;
    private int mStart;
    private int mLength;
    private int[] mDestination;
  
    // Processing window size; should be odd.
    private int mBlurWidth = 15;
  
    public ForkBlur(int[] src, int start, int length, int[] dst) {
        mSource = src;
        mStart = start;
        mLength = length;
        mDestination = dst;
    }

    protected void computeDirectly() {
        int sidePixels = (mBlurWidth - 1) / 2;
        for (int index = mStart; index < mStart + mLength; index++) {
            // Calculate average.
            float rt = 0, gt = 0, bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mindex = Math.min(Math.max(mi + index, 0),
                                    mSource.length - 1);
                int pixel = mSource[mindex];
                rt += (float)((pixel & 0x00ff0000) >> 16)
                      / mBlurWidth;
                gt += (float)((pixel & 0x0000ff00) >>  8)
                      / mBlurWidth;
                bt += (float)((pixel & 0x000000ff) >>  0)
                      / mBlurWidth;
            }
          
            // Reassemble destination pixel.
            int dpixel = (0xff000000     ) |
                   (((int)rt) << 16) |
                   (((int)gt) <<  8) |
                   (((int)bt) <<  0);
            mDestination[index] = dpixel;
        }
    }
  
  ...
```

接下来你需要实现父类中的 compute() 方法，它会直接执行模糊处理，或者将当前的工作拆分成两个更小的任务。数组的长度可以作为一个简单的阀值来判断任务是应该直接完成还是应该被拆分。

    protected static int sThreshold = 100000;
    
    protected void compute() {
        if (mLength < sThreshold) {
            computeDirectly();
            return;
        }
        
        int split = mLength / 2;
        
        invokeAll(new ForkBlur(mSource, mStart, split, mDestination),
                  new ForkBlur(mSource, mStart + split, mLength - split,
                               mDestination));
    }

如果前面这个方法是在一个 RecursiveAction 的子类中，那么设置任务在ForkJoinPool 中执行就再直观不过了。通常会包含以下一些步骤：

1. 创建一个表示所有需要完成工作的任务。

    // source image pixels are in src
    // destination image pixels are in dst
    ForkBlur fb = new ForkBlur(src, 0, src.length, dst);

2. 创建将要用来执行任务的 ForkJoinPool。

    ForkJoinPool pool = new ForkJoinPool();

3. 执行任务。

    pool.invoke(fb);
    
想要浏览完成的源代码，请查看 ForkBlur示例，其中还包含一些创建 destination 图片文件的额外代码。


##### 标准实现

除了能够使用 fork/join 框架来实现能够在多处理系统中被并行执行的定制化算法（如前文中的 ForkBlur.java 例子），在 Java SE 中一些比较常用的功能点也已经使用 fork/join 框架来实现了。在 Java SE 8 中，[java.util.Arrays](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html) 类的一系列parallelSort() 方法就使用了 fork/join 来实现。这些方法与 sort() 方法很类似，但是通过使用 fork/join框 架，借助了并发来完成相关工作。在多处理器系统中，对大数组的并行排序会比串行排序更快。这些方法究竟是如何运用 fork/join 框架并不在本教程的讨论范围内。想要了解更多的信息，请参见 Java API 文档。 其他采用了 fork/join 框架的方法还包括java.util.streams包中的一些方法，此包是作为 Java SE 8 发行版中 [Project Lambda](http://openjdk.java.net/projects/lambda/) 的一部分。想要了解更多信息，请参见 Lambda 表达式一节。


### 并发集合

并发集合简化了大型数据集合管理，且极大的减少了同步的需求。

java.util.concurrent 包囊括了 Java 集合框架的一些附加类。它们也最容易按照集合类所提供的接口来进行分类：

* [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html) 定义了一个先进先出的数据结构，当你尝试往满队列中添加元素，或者从空队列中获取元素时，将会阻塞或者超时。
* [ConcurrentMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentMap.html) 是 [java.util.Map](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html) 的子接口，定义了一些有用的原子操作。移除或者替换键值对的操作只有当 key 存在时才能进行，而新增操作只有当 key 不存在时。使这些操作原子化，可以避免同步。ConcurrentMap 的标准实现是 [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html)，它是 [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) 的并发模式。
* [ConcurrentNavigableMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentNavigableMap.html) 是 ConcurrentMap 的子接口，支持近似匹配。[ConcurrentNavigableMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentSkipListMap.html) 的标准实现是 ConcurrentSkipListMap，它是 [TreeMap](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html) 的并发模式。

所有这些集合，通过在集合里新增对象和访问或移除对象的操作之间，定义一个happens-before 的关系，来帮助程序员避免内存一致性错误。

### 原子变量

[java.util.concurrent.atomic](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/package-summary.html) 包定义了对单一变量进行原子操作的类。所有的类都提供了 get 和 set 方法，可以使用它们像读写 volatile 变量一样读写原子类。就是说，同一变量上的一个 set 操作对于任意后续的 get 操作存在 happens-before 关系。原子的 compareAndSet 方法也有内存一致性特点，就像应用到整型原子变量中的简单原子算法。  

为了看看这个包如何使用，让我们返回到最初用于演示线程干扰的 Counter 类：

```java
class Counter {
    private int c = 0;

    public void increment() {
        c++;
    }

    public void decrement() {
        c--;
    }

    public int value() {
        return c;
    }
}
```

使用同步是一种使 Counter 类变得线程安全的方法，如 SynchronizedCounter：

```java
class SynchronizedCounter {
    private int c = 0;

    public synchronized void increment() {
        c++;
    }

    public synchronized void decrement() {
        c--;
    }

    public synchronized int value() {
        return c;
    }
}
```

对于这个简单的类，同步是一种可接受的解决方案。但是对于更复杂的类，我们可能想要避免不必要同步所带来的活跃度影响。将 int 替换为 AtomicInteger 允许我们在不进行同步的情况下阻止线程干扰，如 AtomicCounter：

```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }

    public void decrement() {
        c.decrementAndGet();
    }

    public int value() {
        return c.get();
    }
}
```

### 并发随机数

并发随机数（JDK7）提供了高效的多线程生成伪随机数的方法。

在 JDK7 中，[java.util.concurrent](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html) 包含了一个相当便利的类 [ThreadLocalRandom](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadLocalRandom.html)，可以在当应用程序期望在多个线程或 ForkJoinTasks 中使用随机数时使用。

对于并发访问，使用 TheadLocalRandom 代替 Math.random() 可以减少竞争，从而获得更好的性能。

你只需调用 ThreadLocalRandom.current()， 然后调用它的其中一个方法去获取一个随机数即可。下面是一个例子：

    int r = ThreadLocalRandom.current().nextInt(4, 77);
    
