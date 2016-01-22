# 并发

计算机用户想当然地认为他们的系统在一个时间可以做多件事。他们认为，他们可以工作在一个字处理器，而其他应用程序在下载文件，管理打印队列和音频流。即使是单一的应用程序通常也是被期望在一个时间来做多件事。例如，音频流应用程序必须同时读取数字音频，解压，管理播放，并更新显示。即使字处理器应该随时准备响应键盘和鼠标事件，不管多么繁忙，它总是能格式化文本或更新显示。可以做这样的事情的软件称为并发软件（concurrent software）。

在 Java 平台是完全支持并发编程。自从 5.0 版本以来，这个平台还包括高级并发 API, 主要集中在 java.util.concurrent 包。

## 进程（Processes ）和线程（Threads）

进程和线程是并发编程的两个基本的执行单元。在 Java 中，并发编程主要涉及线程。

一个计算机系统通常有许多活动的进程和线程。在给定的时间内，每个处理器只能有一个线程得到真正的运行。对于单核处理器来说，处理时间是通过时间切片来在进程和线程之间进行共享的。

现在多核处理器或多进程的电脑系统越来越流行。这大大增强了系统的进程和线程的并发执行能力。但即便是没有多处理器或多进程，并发仍然是可能的。

### 进程

进程有一个独立的执行环境。进程通常有一个完整的、私人的基本运行时资源;特别是,每个进程都有其自己的内存空间。

进程往往被视为等同于程序或应用程序。然而,用户将看到一个单独的应用程序可能实际上是一组合作的进程。大多数操作系统都支持进程间通信( Inter Process Communication，简称 IPC)资源,如管道和套接字。IPC 不仅用于同个系统的进程之间的通信，也可以用在不同系统的进程。

大多数 Java 虚拟机的实现作为一个进程运行。Java 应用程序可以使用 [ProcessBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html) 对象创建额外的进程。多进程应用程序超出了本书的讲解范围。

### 线程

线程有时被称为轻量级进程。进程和线程都提供一个执行环境,但创建一个新的线程比创建一个新的进程需要更少的资源。

线程中存在于进程中,每个进程都至少一个线程。线程共享进程的资源,包括内存和打开的文件。这使得工作变得高效，但也存在了一个潜在的问题——通信。

多线程执行是 Java 平台的一个重要特点。每个应用程序都至少有一个线程,或者几个,如果算上“系统”的线程（负责内存管理和信号处理）那就更多。但从程序员的角度来看,你启动只有一个线程，称为主线程。这个线程有能力创建额外的线程。

## 线程对象

每个线程都与 [Thread](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html) 类的一个实例相关联。有两种使用线程对象来创建并发应用程序的基本策略：

* 为了直接控制线程的创建和管理，简单地初始化线程，应用程序每次需要启动一个异步任务。
* 通过传递给应用程序任务给一个 executor，从而从应用程序的其他部分抽象出线程管理。

### 定义和启动一个线程

有两种方式穿件 Thread 的实例：

* 提供 Runnable 对象。Runnable  接口定义了一个方法 run ,用来包含线程要执行的代码。如 HelloRunnable 所示：

```java
public class HelloRunnable implements Runnable {
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Hello from a thread!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        (new Thread(new HelloRunnable())).start();
	}
}
```

* 继承 Thread。Thread  类本身是实现 Runnable，虽然它的 run 方法啥都没干。HelloThread  示例如下：

```java
public class HelloThread extends Thread {
	
	public void run() {
        System.out.println("Hello from a thread!");
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new HelloThread()).start();
	}
}
```

请注意,这两个例子调用 start 来启动线程。

第一种方式,它使用 Runnable 对象,在实际应用中更普遍,因为 Runnable 对象可以继承 Thread 以外的类。第二种方式，在简单的应用程序更容易使用,但受限于你的任务类必须是一个 Thread 的后代。本书推荐使用第一种方法,将 Runnable 任务从 Thread 对象分离来执行任务。这不仅更灵活,而且它适用于高级线程管理 API。

Thread 类定义了大量的方法用于线程管理。

### Sleep 来暂停执行

Thread.sleep 可以当前线程执行暂停一个时间段，这样处理器时间就可以给其他线程使用。

sleep 有两种重载形式：一个是指定睡眠时间到毫秒，另外一个是指定的睡眠时间为纳秒级。然而，这些睡眠时间不能保证是精确的，因为它们是通过由基础 OS 提供的，并受其限制。此外，睡眠周期也可以通过中断终止，我们将在后面的章节中看到。在任何情况下，你不能假设调用 sleep 会挂起线程用于指定精确的时间段。

SleepMessages 示例使用 sleep 每隔4秒打印一次消息：

```java
public class SleepMessages {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too" };

		for (int i = 0; i < importantInfo.length; i++) {
			// Pause for 4 seconds
			Thread.sleep(4000);
			// Print a message
			System.out.println(importantInfo[i]);
		}
	}
}
```

请注意 main 声明抛出 InterruptedException。当 sleep 是激活的时候，若有另一个线程中断当前线程时，则 sleep 抛出异常。由于该应用程序还没有定义的另一个线程来引起的中断，所以考虑捕捉 InterruptedException。

### 中断（interrupt）

中断是表明一个线程，它应该停止它正在做和将要做事的时。线程通过在 Thread 对象调用 [interrupt](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--) 来实现线程的中断。为了中断机制能正常工作，被中断的线程必须支持自己的中断。

#### 支持中断

如何实现线程支持自己的中断？这要看是什么它目前正在做。如果线程频繁调用抛出InterruptedException 的方法，它只要在  run 方法捕获了异常之后返回即可。例如 ：

    for (int i = 0; i < importantInfo.length; i++) {
        // Pause for 4 seconds
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // We've been interrupted: no more messages.
            return;
        }
        // Print a message
        System.out.println(importantInfo[i]);
    }

很多方法都会抛出 InterruptedException，如 sleep，被设计成在收到中断时立即取消他们当前的操作并返回。

若线程长时间没有调用方法抛出 InterruptedException 的话，那么它必须定期调用 Thread.interrupted ，在接收到中断后返回 true。

    for (int i = 0; i < inputs.length; i++) {
        heavyCrunch(inputs[i]);
        if (Thread.interrupted()) {
            // We've been interrupted: no more crunching.
            return;
        }
    }

在这个简单的例子中，代码简单地测试该中断，如果已接收到中断线程就退出。在更复杂的应用程序，它可能会更有意义抛出一个 InterruptedException：

    if (Thread.interrupted()) {
        throw new InterruptedException();
    }
    
#### 中断状态标志

中断机制是使用被称为中断状态的内部标志实现的。调用 Thread.interrupt 可以设置该标志。当一个线程通过调用静态方法 Thread.interrupted 检查中断，中断状态被清除。非静态 isInterrupted 方法，它是用于线程来查询另一个线程的中断状态，不会改变中断状态标志。

按照惯例，任何方法因抛出一个 InterruptedException 退出都会清除中断状态。当然，它可能因为另一个线程调用 interrupt 而让那个中断状态立即被重新设置。

### join 方法

join 方法允许一个线程等待另一个完成。假设 t 是一个 Thread 对象，

    t.join();

它会导致当前线程暂停执行直到 t 线程终止。join 允许程序员指定一个等待周期。与 sleep 一样，等待时间是依赖于操作系统的时间，不能假设 join 等待时间是精确的。

像 sleep 一样，join 响应中断并通过 InterruptedException 退出。

### SimpleThreads 示例

SimpleThreads 示例，有两个线程，第一个线程是每个 Java 应用程序都有主线程。主线程创建的 Runnable 对象 MessageLoop，并等待它完成。如果  MessageLoop 需要很长时间才能完成，主线程就中断它。

该 MessageLoop 线程打印出一系列消息。如果中断之前就已经打印了所有消息，则 MessageLoop 线程打印一条消息并退出。

```java
public class SimpleThreads {
	  // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName =
            Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                          threadName,
                          message);
    }

    private static class MessageLoop
        implements Runnable {
        public void run() {
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
            };
            try {
                for (int i = 0;
                     i < importantInfo.length;
                     i++) {
                    // Pause for 4 seconds
                    Thread.sleep(4000);
                    // Print a message
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }

    public static void main(String args[])
        throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience)
                  && t.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
            }
        }
        threadMessage("Finally!");
    }
}
```

## 同步（Synchronization）

线程间的通信主要是通过共享访问字段以及其字段所引用的对象来实现的。这种形式的通信是非常有效的，但可能导致2种可能的错误：线程干扰（thread interference）和内存一致性错误（memory consistency errors）。同步就是要需要避免这些错误的工具。

但是，同步可以引入线程竞争（thread contention），当两个或多个线程试图同时访问相同的资源时，并导致了 Java 运行时执行一个或多个线程更慢，或甚至暂停他们的执行。饥饿（Starvation）和活锁 （livelock） 是线程竞争的表现形式。

### 线程干扰

描述当多个线程访问共享数据时是错误如何出现。

考虑下面的一个简单的类 Counter：

```java
public class Counter {
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

其中的 increment 方法用来对 c 加1；decrement 方法用来对 c 减 1。然而，有多个线程中都存在对某个 Counter 对象的引用，那么线程间的干扰就可能导致出现我们不想要的结果。

线程间的干扰出现在多个线程对同一个数据进行多个操作的时候，也就是出现了“交错”。这就意味着操作是由多个步骤构成的，而此时，在这多个步骤的执行上出现了叠加。

Counter类对象的操作貌似不可能出现这种“交错(interleave)”，因为其中的两个关于c 的操作都很简单，只有一条语句。然而，即使是一条语句也是会被虚拟机翻译成多个步骤的。在这里，我们不深究虚拟机具体上上面的操作翻译成了什么样的步骤。只需要知道即使简单的 c++ 这样的表达式也是会被翻译成三个步骤的：

1. 获取 c 的当前值。
2. 对其当前值加 1。
3. 将增加后的值存储到 c 中。

表达式 c-- 也是会被按照同样的方式进行翻译，只不过第二步变成了减1，而不是加1。

假定线程 A 中调用 increment 方法，线程 B 中调用 decrement 方法，而调用时间基本上相同。如果 c 的初始值为 0，那么这两个操作的“交错”顺序可能如下：

1. 线程A：获取 c 的值。
2. 线程B：获取 c 的值。
3. 线程A：对获取到的值加1；其结果是1。
4. 线程B：对获取到的值减1；其结果是-1。
5. 线程A：将结果存储到 c 中；此时c的值是1。
6. 线程B：将结果存储到 c 中；此时c的值是-1。

这样线程 A 计算的值就丢失了，也就是被线程 B 的值覆盖了。上面的这种“交错”只是其中的一种可能性。在不同的系统环境中，有可能是 B 线程的结果丢失了，或者是根本就不会出现错误。由于这种“交错”是不可预测的，线程间相互干扰造成的 bug 是很难定位和修改的。

### 内存一致性错误

介绍了通过共享内存出现的不一致的错误。

内存一致性错误(Memory consistency errors)发生在不同线程对同一数据产生不同的“看法”。导致内存一致性错误的原因很复杂，超出了本书的描述范围。庆幸的是，程序员并不需要知道出现这些原因的细节。我们需要的是一种可以避免这种错误的方法。

避免出现内存一致性错误的关键在于理解 happens-before 关系。这种关系是一种简单的方法，能够确保一条语句对内存的写操作对于其它特定的语句都是可见的。为了理解这点，我们可以考虑如下的示例。假定定义了一个简单的 int 类型的字段并对其进行了初始化：

    int counter = 0;
    
该字段由两个线程共享：A 和 B。假定线程 A 对 counter 进行了自增操作：

    counter++;
    
然后，线程 B 打印 counter 的值：

    System.out.println(counter);
    
如果以上两条语句是在同一个线程中执行的，那么输出的结果自然是1。但是如果这两条语句是在两个不同的线程中，那么输出的结构有可能是0。这是因为没有保证线程 A 对 counter 的修改对线程 B 来说是可见的。除非程序员在这两条语句间建立了一定的 happens-before 关系。

我们可以采取多种方式建立这种 happens-before 关系。使用同步就是其中之一，这点我们将会在下面的小节中看到。

到目前为止，我们已经看到了两种建立这种 happens-before 的方式：

* 当一条语句中调用了 Thread.start 方法，那么每一条和该语句已经建立了 happens-before 的语句都和新线程中的每一条语句有着这种 happens-before。引入并创建这个新线程的代码产生的结果对该新线程来说都是可见的。
* 当一个线程终止了并导致另外的线程中调用 Thread.join 的语句返回，那么此时这个终止了的线程中执行了的所有语句都与随后的 join 语句随后的所有语句建立了这种 happens-before 。也就是说终止了的线程中的代码效果对调用 join 方法的线程来说是可见。

关于哪些操作可以建立这种 happens-before，更多的信息请参阅“[java.util.concurrent 包的概要说明](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html#MemoryVisibility)”。

### 同步方法

描述了一个简单的做法，可以有效防止线程干扰和内存一致性错误。

Java 编程语言中提供了两种基本的同步用语：同步方法（synchronized methods）和同步语句（synchronized statements）。同步语句相对而言更为复杂一些，我们将在下一小节中进行描述。本节重点讨论同步方法。

我们只需要在声明方法的时候增加关键字 synchronized 即可：

```java
public class SynchronizedCounter {
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

如果 count 是 SynchronizedCounter 类的实例，设置其方法为同步方法将有两个效果：

* 首先，不可能出现对同一对象的同步方法的两个调用的“交错”。当一个线程在执行一个对象的同步方式的时候，其他所有的调用该对象的同步方法的线程都会被挂起，直到第一个线程对该对象操作完毕。
* 其次，当一个同步方法退出时，会自动与该对象的同步方法的后续调用建立  happens-before 关系。这就确保了对该对象的修改对其他线程是可见的。

注意：构造函数不能是 synchronized ——在构造函数前使用 synchronized 关键字将导致语义错误。同步构造函数是没有意义的。这是因为只有创建该对象的线程才能调用其构造函数。

警告：在创建多个线程共享的对象时，要特别小心对该对象的引用不能过早地“泄露”。例如，假定我们想要维护一个保存类的所有实例的列表 instances。我们可能会在构造函数中这样写到：

    instances.add(this);
    
但是，其他线程可会在该对象的构造完成之前就访问该对象。

同步方法是一种简单的可以避免线程相互干扰和内存一致性错误的策略：如果一个对象对多个线程都是可见的，那么所有对该对象的变量的读写都应该是通过同步方法完成的（一个例外就是 final 字段，他在对象创建完成后是不能被修改的，因此，在对象创建完毕后，可以通过非同步的方法对其进行安全的读取）。这种策略是有效的，但是可能导致“活跃度（liveness）”问题。这点我们会在本课程的后面进行描述。


### 内部锁和同步

描述了一个更通用的同步方法，并介绍了同步是如何基于内部锁的。

同步是构建在被称为“内部锁（intrinsic lock）”或者是“监视锁（monitor lock）”的内部实体上的。（在 API 中通常被称为是“监视器（monitor）”。）内部锁在两个方面都扮演着重要的角色：保证对对象状态访问的排他性和建立也对象可见性相关的重要的“ happens-before。

每一个对象都有一个与之相关联动的内部锁。按照传统的做法，当一个线程需要对一个对象的字段进行排他性访问并保持访问的一致性时，他必须在访问前先获取该对象的内部锁，然后才能访问之，最后释放该内部锁。在线程获取对象的内部锁到释放对象的内部锁的这段时间，我们说该线程拥有该对象的内部锁。只要有一个线程已经拥有了一个内部锁，其他线程就不能再拥有该锁了。其他线程将会在试图获取该锁的时候被阻塞了。

当一个线程释放了一个内部锁，那么就会建立起该动作和后续获取该锁之间的 happens-before 关系。

#### 同步方法中的锁

当一个线程调用一个同步方法的时候，他就自动地获得了该方法所属对象的内部锁，并在方法返回的时候释放该锁。即使是由于出现了没有被捕获的异常而导致方法返回，该锁也会被释放。

我们可能会感到疑惑：当调用一个静态的同步方法的时候会怎样了，静态方法是和类相关的，而不是和对象相关的。在这种情况下，线程获取的是该类的类对象的内部锁。这样对于静态字段的方法是通过一个和类的实例的锁相区分的另外的锁来进行的。

#### 同步语句

另外一种创建同步代码的方式就是使用同步语句。和同步方法不同，使用同步语句是必须指明是要使用哪个对象的内部锁：

    public void addName(String name) {
        synchronized(this) {
            lastName = name;
            nameCount++;
        }
        nameList.add(name);
    }

在上面的示例中，方法 addName 需要对 lastName 和 nameCount 的修改进行同步，还要避免同步调用其他对象的方法（在同步代码段中调用其他对象的方法可能导致“活跃度（Liveness）”中描述的问题）。如果没有使用同步语句，那么将不得不使用一个单独的，未同步的方法来完成对 nameList.add 的调用。

在改善并发性时，巧妙地使用同步语句能起到很大的帮助作用。例如，我们假定类 MsLunch 有两个实例字段，c1 和 c2，这两个变量绝不会一起使用。所有对这两个变量的更新都需要进行同步。但是没有理由阻止对 c1 的更新和对 c2 的更新出现交错——这样做会创建不必要的阻塞，进而降低并发性。此时，我们没有使用同步方法或者使用和this 相关的锁，而是创建了两个单独的对象来提供锁。

```
public class MsLunch {
    private long c1 = 0;
    private long c2 = 0;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void inc1() {
        synchronized(lock1) {
            c1++;
        }
    }

    public void inc2() {
        synchronized(lock2) {
            c2++;
        }
    }
}
```

采用这种方式时需要特别的小心。我们必须绝对确保相关字段的访问交错是完全安全的。

#### 重入同步（Reentrant Synchronization）

回忆前面提到的：线程不能获取已经被别的线程获取的锁。但是线程可以获取自身已经拥有的锁。允许一个线程能重复获得同一个锁就称为重入同步（reentrant synchronization）。它是这样的一种情况：在同步代码中直接或者间接地调用了还有同步代码的方法，两个同步代码段中使用的是同一个锁。如果没有重入同步，在编写同步代码时需要额外的小心，以避免线程将自己阻塞。


### 原子访问

介绍了不会被其他线程干扰的做法的总体思路。

在编程中，原子性动作就是指一次性有效完成的动作。原子性动作是不能在中间停止的：要么一次性完全执行完毕，要么就不执行。在动作没有执行完毕之前，是不会产生可见结果的。

通过前面的示例，我们已经发现了诸如 c++ 这样的自增表达式并不属于原子操作。即使是非常简单的表达式也包含了复杂的动作，这些动作可以被解释成许多别的动作。然而，的确存在一些原子操作的：

* 对几乎所有的原生数据类型变量（除了 long he  double）的读写以及引用变量的读写都是原子的。
* 对所有声明为 volatile 的变量的读写都是原子的，包括 long 和 double 类型。

原子性动作是不会出现交错的，因此，使用这些原子性动作时不用考虑线程间的干扰。然而，这并不意味着可以移除对原子操作的同步。因为内存一致性错误还是有可能出现的。使用 volatile 变量可以减少内存一致性错误的风险，因为任何对 volatile 变 量的写操作都和后续对该变量的读操作建立了 happens-before 关系。这就意味着对 volatile 类型变量的修改对于别的线程来说是可见的。更重要的是，这意味着当一个线程读取一个 volatile 类型的变量时，他看到的不仅仅是对该变量的最后一次修改，还看到了导致这种修改的代码带来的其他影响。

使用简单的原子变量访问比通过同步代码来访问变量更高效，但是需要程序员的更多细心考虑，以避免内存一致性错误。这种额外的付出是否值得完全取决于应用程序的大小和复杂度。

## 活跃度（Liveness）

一个并行应用程序的及时执行能力被称为它的活跃度（liveness）。本节将介绍最常见的一种活跃度的问题——死锁，以及另外两个活跃度的问题——饥饿和活锁。

### 死锁（Deadlock）

死锁是指两个或两个以上的线程永远被阻塞,一直等待对方的资源。

下面是一个例子。

Alphonse 和 Gaston 是朋友,都很有礼貌。礼貌的一个严格的规则是,当你给一个朋友鞠躬时,你必须保持鞠躬,直到你的朋友鞠躬回给你。不幸的是,这条规则有个缺陷，那就是如果两个朋友同一时间向对方鞠躬，那就永远不会完了。这个示例应用程序中,死锁模型是这样的:

```java
public class Deadlock {
	static class Friend {
		private final String name;

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public synchronized void bow(Friend bower) {
			System.out.format("%s: %s" + "  has bowed to me!%n", this.name, bower.getName());
			bower.bowBack(this);
		}

		public synchronized void bowBack(Friend bower) {
			System.out.format("%s: %s" + " has bowed back to me!%n", this.name, bower.getName());
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new Runnable() {
			public void run() {
				alphonse.bow(gaston);
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				gaston.bow(alphonse);
			}
		}).start();
	}
}
```

当他们尝试调用 bowBack 两个线程将被阻塞。无论是哪个线程永远不会结束，因为每个线程都在等待对方鞠躬。这就是死锁了。

### 饥饿和活锁（Starvation and Livelock）

饥饿和活锁虽比死锁问题稍微不常见点,但这些是在并发软件种每一个设计师仍然可能会遇到的问题。

#### 饥饿（Starvation）

饥饿描述了这样一个情况,一个线程不能获得定期访问共享资源,于是无法继续执行。这种情况一般出现在共享资源被某些“贪婪”线程占用，而导致资源长时间不被其他线程可用。例如,假设一个对象提供一个同步的方法,往往需要很长时间返回。如果一个线程频繁调用该方法,其他线程若也需要频繁的同步访问同一个对象通常会被阻塞。

#### 活锁（Livelock）

一个线程常常处于响应另一个线程的动作，如果其他线程也常常处于该线程的动作,那么就可能出现活锁。与死锁、活锁的线程一样，程序无法进一步执行。然而,线程是不会阻塞的，他们只是会忙于应对彼此的恢复工作。现实种的例子是，两人面对面试图通过一条走廊: Alphonse 移动到他的左则让路给 Gaston ,而 Gaston 移动到他的右侧想让 Alphonse 过去，两个人同时让路，但其实两人都挡住了对方没办法过去，他们仍然彼此阻塞。

## Guarded Blocks 

多线程之间经常需要协同工作，最常见的方式是使用 Guarded Blocks，它循环检查一个条件（通常初始值为 true），直到条件发生变化才跳出循环继续执行。在使用 Guarded Blocks 时有以下几个步骤需要注意：

假设 guardedJoy 方法必须要等待另一线程为共享变量 joy 设值才能继续执行。那么理论上可以用一个简单的条件循环来实现，但在等待过程中 guardedJoy 方法不停的检查循环条件实际上是一种资源浪费。

    public void guardedJoy() {
        // Simple loop guard. Wastes
        // processor time. Don't do this!
        while(!joy) {}
        System.out.println("Joy has been achieved!");
    }
    
更加高效的保护方法是调用 [Object.wait](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#wait--) 将当前线程挂起，直到有另一线程发起事件通知（尽管通知的事件不一定是当前线程等待的事件）。

    public synchronized void guardedJoy() {
        // This guard only loops once for each special event, which may not
        // be the event we're waiting for.
        while(!joy) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        System.out.println("Joy and efficiency have been achieved!");
    }
    
    
*注意：一定要在循环里面调用 wait 方法，不要想当然的认为线程唤醒后循环条件一定发生了改变。*

和其他可以暂停线程执行的方法一样，wait 方法会抛出 InterruptedException，在上面的例子中，因为我们关心的是 joy 的值，所以忽略了 InterruptedException。

为什么 guardedJoy 是 synchronized 的？假设 d 是用来调用 wait 的对象，当一个线程调用 d.wait，它必须要拥有  d的内部锁（否则会抛出异常），获得 d 的内部锁的最简单方法是在一个 synchronized 方法里面调用 wait。

当一个线程调用 wait 方法时，它释放锁并挂起。然后另一个线程请求并获得这个锁并调用 Object.notifyAll 通知所有等待该锁的线程。

    public synchronized notifyJoy() {
        joy = true;
        notifyAll();
    }
    
当第二个线程释放这个该锁后，第一个线程再次请求该锁，从 wait 方法返回并继续执行。

注意：还有另外一个通知方法，notify()，它只会唤醒一个线程。但由于它并不允许指定哪一个线程被唤醒，所以一般只在大规模并发应用（即系统有大量相似任务的线程）中使用。因为对于大规模并发应用，我们其实并不关心哪一个线程被唤醒。

现在我们使用 Guarded blocks 创建一个生产者/消费者应用。这类应用需要在两个线程之间共享数据：生产者生产数据，消费者使用数据。两个线程通过共享对象通信。在这里，线程协同工作的关键是：生产者发布数据之前，消费者不能够去读取数据；消费者没有读取旧数据前，生产者不能发布新数据。

在下面的例子中，数据通过 Drop 对象共享的一系列文本消息：

```java
public class Drop {
	  // Message sent from producer
    // to consumer.
    private String message;
    // True if consumer should wait
    // for producer to send message,
    // false if producer should wait for
    // consumer to retrieve message.
    private boolean empty = true;
 
    public synchronized String take() {
        // Wait until message is
        // available.
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        // Toggle status.
        empty = true;
        // Notify producer that
        // status has changed.
        notifyAll();
        return message;
    }
 
    public synchronized void put(String message) {
        // Wait until message has
        // been retrieved.
        while (!empty) {
            try { 
                wait();
            } catch (InterruptedException e) {}
        }
        // Toggle status.
        empty = false;
        // Store message.
        this.message = message;
        // Notify consumer that status
        // has changed.
        notifyAll();
    }
}
```

Producer 是生产者线程，发送一组消息，字符串 DONE 表示所有消息都已经发送完成。为了模拟现实情况，生产者线程还会在消息发送时随机的暂停。

```java
public class Producer implements Runnable {
	private Drop drop;

	public Producer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too" };
		Random random = new Random();

		for (int i = 0; i < importantInfo.length; i++) {
			drop.put(importantInfo[i]);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
			}
		}
		drop.put("DONE");
	}
}
```


Consumer 是消费者线程，读取消息并打印出来，直到读取到字符串 DONE 为止。消费者线程在消息读取时也会随机的暂停。

```java
public class Consumer implements Runnable {
	private Drop drop;

	public Consumer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		Random random = new Random();
		for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
			System.out.format("MESSAGE RECEIVED: %s%n", message);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
			}
		}
	}
}
```

ProducerConsumerExample 是主线程，它启动生产者线程和消费者线程。

```java
public class ProducerConsumerExample {
    public static void main(String[] args) {
        Drop drop = new Drop();
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
```
 
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

    int r = ThreadLocalRandom.current() .nextInt(4, 77);
    
## 源码

本章例子的源码，可以在 <https://github.com/waylau/essential-java> 中 com.waylau.essentialjava.concurrency 包下找到。