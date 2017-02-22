# 使用异常带来的优势

现在你知道什么是异常，以及如何使用它们，现在是时候了解在程序中使用异常的优点。

## 优点1：将错误处理代码与“常规”代码分离

异常提供了一种方法来分离当一个程序的主逻辑发生异常情况时应该做什么的细节。 在传统的编程中，错误检测、报告和处理常常导致混淆意大利面条代码（spaghetti code）。 例如，考虑这里的伪代码方法将整个文件读入内存。

```java
readFile {
    open the file;
    determine its size;
    allocate that much memory;
    read the file into memory;
    close the file;
}
```

乍一看，这个功能看起来很简单，但它忽略了以下所有潜在错误。

* 如果无法打开文件会发生什么？
* 如果无法确定文件的长度，会发生什么？
* 如果不能分配足够的内存，会发生什么？
* 如果读取失败会发生什么？
* 如果文件无法关闭会怎么样？


为了处理这种情况，readFile函数必须有更多的代码来执行错误检测\报告和处理。 这里是一个示例，来展示该函数可能会是什么样子。


```java
errorCodeType readFile {
    initialize errorCode = 0;
    
    open the file;
    if (theFileIsOpen) {
        determine the length of the file;
        if (gotTheFileLength) {
            allocate that much memory;
            if (gotEnoughMemory) {
                read the file into memory;
                if (readFailed) {
                    errorCode = -1;
                }
            } else {
                errorCode = -2;
            }
        } else {
            errorCode = -3;
        }
        close the file;
        if (theFileDidntClose && errorCode == 0) {
            errorCode = -4;
        } else {
            errorCode = errorCode and -4;
        }
    } else {
        errorCode = -5;
    }
    return errorCode;
}
```

这里面会有很多错误检测、报告的细节，使得原来的七行代码被淹没在这杂乱的代码中。 更糟的是，代码的逻辑流也已经丢失，因此很难判断代码是否正确：如果函数无法分配足够的内存，文件是否真的被关闭？ 在编写方法三个月后修改方法时，更难以确保代码能够继续正确的操作。 因此，许多程序员通过简单地忽略它来解决这个问题。这样当他们的程序崩溃时，就生成了报告错误。

异常使您能够编写代码的主要流程，并处理其他地方的特殊情况。 如果readFile函数使用异常而不是传统的错误管理技术，它将看起来更像下面。

```java
readFile {
    try {
        open the file;
        determine its size;
        allocate that much memory;
        read the file into memory;
        close the file;
    } catch (fileOpenFailed) {
       doSomething;
    } catch (sizeDeterminationFailed) {
        doSomething;
    } catch (memoryAllocationFailed) {
        doSomething;
    } catch (readFailed) {
        doSomething;
    } catch (fileCloseFailed) {
        doSomething;
    }
}
```


请注意，异常不会减少你在法执行检测、报告和处理错误方面的工作，但它们可以帮助您更有效地组织工作。

## 优点2：将错误沿调用推栈向上传递

异常的第二个优点是能够在方法的调用堆栈上将错误向上传递。 假设 readFile 方法是由主程序进行的一系列嵌套方法调用中的第四个方法：method1调用method2，它调用了method3，最后调用readFile。

```java
method1 {
    call method2;
}

method2 {
    call method3;
}

method3 {
    call readFile;
}
```

还假设method1是对readFile中可能发生的错误感兴趣的唯一方法。 传统的错误通知技术强制method2和method3将readFile返回的错误代码传递到调用堆栈，直到错误代码最终到达method1 - 对它们感兴趣的唯一方法。

```java
method1 {
    errorCodeType error;
    error = call method2;
    if (error)
        doErrorProcessing;
    else
        proceed;
}

errorCodeType method2 {
    errorCodeType error;
    error = call method3;
    if (error)
        return error;
    else
        proceed;
}

errorCodeType method3 {
    errorCodeType error;
    error = call readFile;
    if (error)
        return error;
    else
        proceed;
}
```

回想一下，Java运行时环境通过调用堆栈向后搜索以找到任何对处理特定异常感兴趣的方法。 一个方法可以阻止在其中抛出的任何异常，从而允许一个方法在调用栈上更远的地方来捕获它。 因此，只有关心错误的方法才需要担心检测错误。

```java
method1 {
    try {
        call method2;
    } catch (exception e) {
        doErrorProcessing;
    }
}

method2 throws exception {
    call method3;
}

method3 throws exception {
    call readFile;
}
```

然而，如伪代码所示，抛弃异常需要中间人方法的一些努力。 任何可以在方法中抛出的已检查异常都必须在其throws子句中指定。



## 优点3：对错误类型进行分组和区分


因为在程序中抛出的所有异常都是对象，异常的分组或分类是类层次结构的自然结果。 Java平台中一组相关异常类的示例是java.io - IOException中定义的那些异常类及其后代。 IOException是最常见的，表示执行I/O时可能发生的任何类型的错误。 它的后代表示更具体的错误。 例如，FileNotFoundException意味着文件无法在磁盘上找到。

一个方法可以编写可以处理非常特定异常的特定处理程序。 FileNotFoundException类没有后代，因此下面的处理程序只能处理一种类型的异常。

```java
catch (FileNotFoundException e) {
    ...
}
```

方法可以通过在catch语句中指定任何异常的超类来基于其组或常规类型捕获异常。 例如，为了捕获所有I/O异常，无论其具体类型如何，异常处理程序都会指定一个IOException参数。

```java
catch (IOException e) {
    ...
}
```

这个处理程序将能够捕获所有I/O异常，包括FileNotFoundException、EOFException等等。 您可以通过查询传递给异常处理程序的参数来查找有关发生的详细信息。 例如，使用以下命令打印堆栈跟踪。

```java
catch (IOException e) {
    // Output goes to System.err.
    e.printStackTrace();
    // Send trace to stdout.
    e.printStackTrace(System.out);
}
```


下面例子可以处理所有的异常：

```java
// A (too) general exception handler
catch (Exception e) {
    ...
}
```

Exception 类接近Throwable类层次结构的顶部。因此，这个处理程序将会捕获除处理程序想要捕获的那些异常之外的许多其他异常。在程序中如果是以这种方式来处理异常，那么你程序一般的做法就是，例如，是打印出一个错误消息给用户，然后退出。

在大多数情况下，异常处理程序应该尽可能的具体。原因是处理程序必须做的第一件事是在选择最佳恢复策略之前，首先要确定发生的是什么类型的异常。实际上，如果不捕获特定的错误，处理程序必须适应任何可能性。太过通用的异常处理程序可能会捕获和处理程序员不期望的并且处理程序不想要的异常，从而使代码更容易出错。

如上所述，您可以以常规方式创建异常分组来处理异常，也可以使用特定的异常类型来区分异常从而可以以确切的方式来处理异常。
