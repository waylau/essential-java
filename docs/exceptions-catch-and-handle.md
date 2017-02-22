# 异常捕获与处理

本节介绍如何使用三个异常处理程序组件（try、catch 和 finally）来编写异常处理程序。 然后，介绍了 Java SE 7中引入的 try-with-resources 语句。 try-with-resources 语句特别适合于使用`Closeable`的资源（例如流）的情况。

本节的最后一部分将通过一个示例来分析在各种情况下发生的情况。

以下示例定义并实现了一个名为ListOfNumbers的类。 构造时，ListOfNumbers 创建一个ArrayList，其中包含10个序列值为0到9的整数元素。ListOfNumbers类还定义了一个名为writeList的方法，该方法将数列表写入一个名为`OutFile.txt`的文本文件中。 此示例使用在`java.io`中定义的输出类，这些类包含在基本I/O中。

```java
// Note: This class will not compile yet.
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ListOfNumbers {

    private List<Integer> list;
    private static final int SIZE = 10;

    public ListOfNumbers () {
        list = new ArrayList<Integer>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            list.add(new Integer(i));
        }
    }

    public void writeList() {
	// The FileWriter constructor throws IOException, which must be caught.
        PrintWriter out = new PrintWriter(new FileWriter("OutFile.txt"));

        for (int i = 0; i < SIZE; i++) {
            // The get(int) method throws IndexOutOfBoundsException, which must be caught.
            out.println("Value at: " + i + " = " + list.get(i));
        }
        out.close();
    }
}
```


构造函数 FileWriter 初始化文件上的输出流。如果文件无法打开，构造函数会抛出一个IOException异常。第二个对ArrayList类的get方法的调用，如果其参数的值太小（小于0）或太大（超过ArrayList当前包含的元素数量），它将抛出 IndexOutOfBoundsException。

如果尝试编译ListOfNumbers类，则编译器将打印有关FileWriter构造函数抛出的异常的错误消息。但是，它不显示有关get抛出的异常的错误消息。原因是构造函数IOException抛出的异常是一个检查异常，而get方法IndexOutOfBoundsException抛出的异常是未检查的异常。

现在，我们已经熟悉ListOfNumbers类，并且知道了其中那些地方可能抛出异常。下一步我们就可以编写异常处理程序来捕获和处理这些异常。


## try块 


构造异常处理程序的第一步是封装可能在try块中抛出异常的代码。 一般来说，try块看起来像下面这样：

```java
try {
    code
}
catch and finally blocks . . .
```

示例标记 `code` 中的段可以包含一个或多个可能抛出的异常。

每行可能抛出异常的代码都可以用单独的一个 try 块，或者多个异常放置在一个 try 块中。 以下示例由于非常简短，所有使用一个try块。

```java
private List<Integer> list;
private static final int SIZE = 10;

public void writeList() {
    PrintWriter out = null;
    try {
        System.out.println("Entered try statement");
        out = new PrintWriter(new FileWriter("OutFile.txt"));
        for (int i = 0; i < SIZE; i++) {
            out.println("Value at: " + i + " = " + list.get(i));
        }
    }
    catch and finally blocks  . . .
}
```


如果在try块中发生异常，那么该异常由与其相关联的异常处理程序将会进行处理。 要将异常处理程序与try块关联，必须在其后面放置一个catch块。

## catch块

通过在try块之后直接提供一个或多个catch块，可以将异常处理程序与try块关联。 在try块的结尾和第一个catch块的开始之间没有代码。

```java
try {

} catch (ExceptionType name) {

} catch (ExceptionType name) {

}
```


每个catch块是一个异常处理程序，处理由其参数指示的异常类型。 参数类型ExceptionType声明了处理程序可以处理的异常类型，并且必须是从Throwable类继承的类的名称。 处理程序可以使用名称引用异常。

catch块包含了在调用异常处理程序时执行的代码。 当处理程序是调用堆栈中第一个与ExceptionType匹配的异常抛出的类型时，运行时系统将调用异常处理程序。 如果抛出的对象可以合法地分配给异常处理程序的参数，则系统认为它是匹配。

以下是writeList方法的两个异常处理程序：

```java
try {

} catch (IndexOutOfBoundsException e) {
    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
} catch (IOException e) {
    System.err.println("Caught IOException: " + e.getMessage());
}
```

异常处理程序可以做的不仅仅是打印错误消息或停止程序。 它们可以执行错误恢复，提示用户做出决定，或者使用异常链将错误传播到更高级别的处理程序，如“异常链”部分所述。

### 在一个异常处理程序中处理多个类型的异常


在Java SE 7和更高版本中，单个catch块可以处理多种类型的异常。 此功能可以减少代码重复，并减少定义过于宽泛的异常。

在catch子句中，多个类型的异常使用竖线（|）分隔每个异常类型：


```java
catch (IOException|SQLException ex) {
    logger.log(ex);
    throw ex;
}
```

注意：如果catch块处理多个异常类型，则catch参数将隐式为final。 在本示例中，catch参数ex是final，因此您不能在catch块中为其分配任何值。

## finally 块

finally块总是在try块退出时执行。这确保即使发生意外异常也会执行finally块。但 finally 的用处不仅仅是异常处理 - 它允许程序员避免清理代码意外绕过 return、continue 或 break 。将清理代码放在finally块中总是一个好的做法，即使没有预期的异常。

注意：如果在执行try或catch代码时JVM退出，则finally块可能无法执行。同样，如果执行try或catch代码的线程被中断或杀死，则finally块可能不执行，即使应用程序作为一个整体继续。


writeList方法的try块打开一个PrintWriter。程序应该在退出writeList方法之前关闭该流。这提出了一个有点复杂的问题，因为writeList的try块可以以三种方式中的一种退出。

* new FileWriter语句失败并抛出IOException。
* list.get(i)语句失败，并抛出IndexOutOfBoundsException。
* 一切成功，try块正常退出。

运行时系统总是执行finally块内的语句，而不管try块内发生了什么。所以它是执行清理的完美场所。

下面的finally块为writeList方法清理，然后关闭PrintWriter。

```java
finally {
    if (out != null) { 
        System.out.println("Closing PrintWriter");
        out.close(); 
    } else { 
        System.out.println("PrintWriter not open");
    } 
} 
```

重要：finally块是防止资源泄漏的关键工具。 当关闭文件或恢复资源时，将代码放在finally块中，以确保资源始终恢复。

考虑在这些情况下使用try-with-resources语句，当不再需要时自动释放系统资源。 

### 源码

本章例子的源码，可以在 <https://github.com/waylau/essential-java> 中 `com.waylau.essentialjava.exception` 包下找到。


## try-with-resources 语句

try-with-resources 是 JDK 7 中一个新的异常处理机制，它能够很容易地关闭在 try-catch 语句块中使用的资源。所谓的资源（resource）是指在程序完成后，必须关闭的对象。try-with-resources 语句确保了每个资源在语句结束时关闭。所有实现了 [java.lang.AutoCloseable](http://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html) 接口（其中，它包括实现了 [java.io.Closeable](http://docs.oracle.com/javase/8/docs/api/java/io/Closeable.html) 的所有对象），可以使用作为资源。

例如，我们自定义一个资源类

```java
public class Demo {    
    public static void main(String[] args) {
        try(Resource res = new Resource()) {
            res.doSome();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Resource implements AutoCloseable {
    void doSome() {
        System.out.println("do something");
    }
    @Override
    public void close() throws Exception {
        System.out.println("resource is closed");
    }
}
```

执行输出如下：

    do something
    resource is closed
    
可以看到，资源终止被自动关闭了。

再来看一个例子，是同时关闭多个资源的情况：

```java
public class Main2 {    
    public static void main(String[] args) {
        try(ResourceSome some = new ResourceSome();
             ResourceOther other = new ResourceOther()) {
            some.doSome();
            other.doOther();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

class ResourceSome implements AutoCloseable {
    void doSome() {
        System.out.println("do something");
    }
    @Override
    public void close() throws Exception {
        System.out.println("some resource is closed");
    }
}

class ResourceOther implements AutoCloseable {
    void doOther() {
        System.out.println("do other things");
    }
    @Override
    public void close() throws Exception {
        System.out.println("other resource is closed");
    }
}
```

最终输出为：

    do something
    do other things
    other resource is closed
    some resource is closed

在 try 语句中越是最后使用的资源，越是最早被关闭。

### try-with-resources 在 JDK 9 中的改进

作为 [Milling Project Coin](http://openjdk.java.net/jeps/213) 的一部分, try-with-resources 声明在 JDK 9  已得到改进。如果你已经有一个资源是 final 或等效于 final 变量,您可以在 try-with-resources 语句中使用该变量，而无需在 try-with-resources 语句中声明一个新变量。

例如,给定资源的声明

    // A final resource
    final Resource resource1 = new Resource("resource1");
    // An effectively final resource
    Resource resource2 = new Resource("resource2");
        
老方法编写代码来管理这些资源是类似的:

    // Original try-with-resources statement from JDK 7 or 8
    try (Resource r1 = resource1;
         Resource r2 = resource2) {
        // Use of resource1 and resource 2 through r1 and r2.
    }
    
而新方法可以是

    // New and improved try-with-resources statement in JDK 9
    try (resource1;
         resource2) {
        // Use of resource1 and resource 2.
    }
    
看上去简洁很多吧。对 Java 未来的发展信心满满。

愿意尝试 JDK 9 这种新语言特性的可以下载使用 [JDK 9 快照](https://jdk9.java.net/download/)。Enjoy!

### 源码

本章例子的源码，可以在 <https://github.com/waylau/essential-java> 中 `com.waylau.essentialjava.exception.trywithresources` 包下找到。



