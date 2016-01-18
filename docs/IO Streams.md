# I/O 流

## 字节流(Byte Streams)

字节流处理原始的二进制数据 I/O。输入输出的是8位字节，相关的类为 [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) 和 [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html).

字节流的类有许多。为了演示字节流的工作，我们将重点放在文件 I/O字节流 [FileInputStream](https://docs.oracle.com/javase/8/docs/api/java/io/FileInputStream.html) 和 [FileOutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/FileOutputStream.html) 上。其他种类的字节流用法类似，主要区别在于它们构造的方式，大家可以举一反三。

### 用法

下面一例子 CopyBytes， 从 xanadu.txt 文件复制到 outagain.txt，每次只复制一个字节：

```java
public class CopyBytes {
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream("resources/xanadu.txt");
			out = new FileOutputStream("resources/outagain.txt");
			int c;

			while ((c = in.read()) != -1) {
				out.write(c);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}
```

CopyBytes 花费其大部分时间在简单的循环里面，从输入流每次读取一个字节到输出流，如图所示：


![](../images/io/byteStream.gif)

### 记得始终关闭流

不再需要一个流记得要关闭它，这点很重要。所以，CopyBytes 使用 finally 块来保证即使发生错误两个流还是能被关闭。这种做法有助于避免严重的资源泄漏。

一个可能的错误是，CopyBytes 无法打开一个或两个文件。当发生这种情况，对应解决方案是判断该文件的流是否是其初始 null 值。这就是为什么 CopyBytes 可以确保每个流变量在调用前都包含了一个对象的引用。

### 何时不使用字节流

CopyBytes 似乎是一个正常的程序，但它实际上代表了一种低级别的 I/O，你应该避免。因为 xanadu.txt 包含字符数据时，最好的方法是使用字符流，下文会有讨论。字节流应只用于最原始的 I/O。所有其他流类型是建立在字节流之上的。

## 字符流(Character Streams)

字符流处理字符数据的 I/O，自动处理与本地字符集转化。

## 缓冲流（Buffered Streams） optimize input and output by reducing the number of calls to the native API.

缓冲流通过减少调用本地 API 的次数来优化的输入和输出。

## Scanning 和 Formatting

Scanning 和 Formatting 允许程序读取和写入格式化的文本。

## 命令行 I/O

命令行 I/O 描述了标准流和控制台对象。

## Data Streams

Data Streams 处理原始数据类型和字符串值的二进制 I/O。

## 对象流（Object Streams） 

对象流处理对象的二进制 I/O。