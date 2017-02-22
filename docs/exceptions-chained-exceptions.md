# 异常链

应用程序通常会通过抛出另一个异常来响应异常。 实际上，第一个异常引起第二个异常。 它可以是非常有助于用户知道什么时候一个异常导致另一个异常。 “异常链（Chained Exceptions）”帮助程序员做到这一点。

以下是Throwable中支持异常链的方法和构造函数。

```java
Throwable getCause()
Throwable initCause(Throwable)
Throwable(String, Throwable)
Throwable(Throwable)
```



initCause和Throwable构造函数的Throwable参数是导致当前异常的异常。 getCause返回导致当前异常的异常，initCause设置当前异常的原因。

以下示例显示如何使用异常链。

```java
try {

} catch (IOException e) {
    throw new SampleException("Other IOException", e);
}
```


在此示例中，当捕获到IOException时，将创建一个新的SampleException异常，并附加原始的异常原因，并将异常链抛出到下一个更高级别的异常处理程序。

## 访问堆栈跟踪信息
 
现在让我们假设更高级别的异常处理程序想要以自己的格式转储堆栈跟踪。

定义：堆栈跟踪（stack trace）提供有关当前线程的执行历史的信息，并列出在异常发生时调用的类和方法的名称。 堆栈跟踪是一个有用的调试工具，通常在抛出异常时会利用它。

以下代码显示了如何在异常对象上调用getStackTrace方法。

```java
catch (Exception cause) {
    StackTraceElement elements[] = cause.getStackTrace();
    for (int i = 0, n = elements.length; i < n; i++) {       
        System.err.println(elements[i].getFileName()
            + ":" + elements[i].getLineNumber() 
            + ">> "
            + elements[i].getMethodName() + "()");
    }
}
```

## 日志 API


如果要记录catch块中所发生异常，最好不要手动解析堆栈跟踪并将输出发送到 System.err()，而是使用[java.util.logging](https://docs.oracle.com/javase/8/docs/api/java/util/logging/package-summary.html)包中的日志记录工具将输出发送到文件。


```java
try {
    Handler handler = new FileHandler("OutFile.log");
    Logger.getLogger("").addHandler(handler);
    
} catch (IOException e) {
    Logger logger = Logger.getLogger("package.name"); 
    StackTraceElement elements[] = e.getStackTrace();
    for (int i = 0, n = elements.length; i < n; i++) {
        logger.log(Level.WARNING, elements[i].getMethodName());
    }
}
```



