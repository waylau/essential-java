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

当他们尝试调用 bowBack 两个线程将被阻塞。无论是块永远不会结束，因为每个线程都在等待对方退鞠躬。这就是死锁了。

### 饥饿和活锁（Starvation and Livelock）

## 守护线程
## 不可变对象
## 高级并发对象
