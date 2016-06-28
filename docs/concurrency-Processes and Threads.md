## 进程（Processes ）和线程（Threads）

进程和线程是并发编程的两个基本的执行单元。在 Java 中，并发编程主要涉及线程。

一个计算机系统通常有许多活动的进程和线程。在给定的时间内，每个处理器只能有一个线程得到真正的运行。对于单核处理器来说，处理时间是通过时间切片来在进程和线程之间进行共享的。

现在多核处理器或多进程的电脑系统越来越流行。这大大增强了系统的进程和线程的并发执行能力。但即便是没有多处理器或多进程的系统中，并发仍然是可能的。

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

Java 中有两种方式创建 Thread 的实例：

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

Thread 类还定义了大量的方法用于线程管理。

### Sleep 来暂停执行

Thread.sleep 可以让当前线程执行暂停一个时间段，这样处理器时间就可以给其他线程使用。

sleep 有两种重载形式：一个是指定睡眠时间为毫秒，另外一个是指定睡眠时间为纳秒级。然而，这些睡眠时间不能保证是精确的，因为它们是通过由操作系统来提供的，并受其限制，因而不能假设 sleep 的睡眠时间是精确的。此外，睡眠周期也可以通过中断终止，我们将在后面的章节中看到。

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

中断是表明一个线程，它应该停止它正在做和将要做的事。线程通过在 Thread 对象调用 [interrupt](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html#interrupt--) 来实现线程的中断。为了中断机制能正常工作，被中断的线程必须支持自己的中断。

#### 支持中断

如何实现线程支持自己的中断？这要看是它目前正在做什么。如果线程调用方法频繁抛出 InterruptedException 异常，那么它只要在 run 方法捕获了异常之后返回即可。例如 ：

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

若线程长时间没有调用方法抛出 InterruptedException 的话，那么它必须定期调用 Thread.interrupted ，该方法在接收到中断后将返回 true。

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

中断机制是使用被称为中断状态的内部标志实现的。调用 Thread.interrupt 可以设置该标志。当一个线程通过调用静态方法 Thread.interrupted 来检查中断，中断状态被清除。非静态 isInterrupted 方法，它是用于线程来查询另一个线程的中断状态，而不会改变中断状态标志。

按照惯例，任何方法因抛出一个 InterruptedException 而退出都会清除中断状态。当然，它可能因为另一个线程调用 interrupt 而让那个中断状态立即被重新设置回来。

### join 方法

join 方法允许一个线程等待另一个完成。假设 t 是一个正在执行的 Thread 对象，那么

    t.join();

它会导致当前线程暂停执行直到 t 线程终止。join 允许程序员指定一个等待周期。与 sleep 一样，等待时间是依赖于操作系统的时间，同时不能假设 join 等待时间是精确的。

像 sleep 一样，join 并通过 InterruptedException 退出来响应中断。

### SimpleThreads 示例

SimpleThreads 示例由两个线程。第一个线程是每个 Java 应用程序都有的主线程。主线程创建的 Runnable 对象 MessageLoop，并等待它完成。如果  MessageLoop 需要很长时间才能完成，主线程就中断它。

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
                for (int i = 0; i < importantInfo.length; i++) {
                
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

