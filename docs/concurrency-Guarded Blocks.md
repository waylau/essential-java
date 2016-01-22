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
