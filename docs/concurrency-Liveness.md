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
