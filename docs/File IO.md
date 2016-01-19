#  文件 I/O

本教程讲述的是 JDK 7 版本以来引入的新的 I/O 机制（也被称为 NIO.2）。

相关的包在 java.nio.file ，其中 java.nio.file.attribute 提供对文件 I/O 以及访问默认文件系统的全面支持。虽然 API 有很多类，但你只需要重点关注几个。你会看到，这个 API 是非常直观和易于使用。

## 什么是路径（Path）？在其他文件系统的实际是怎么样的？

文件系统是用某种媒体形式存储和组织文件，一般是一个或多个硬盘驱动器，以这样的方式，它们可以很容易地检索文件。目前使用的大多数文件系统存储文件是以树（或层次）结构。在树的顶部是一个（或多个）根节点。根节点下，有文件和目录（在Microsoft Windows 系统是指文件夹）。每个目录可以包含文件和子目录，而这又可以包含文件和子目录，以此类推，有可能是无限深度。

### 什么是路径（Path）？

下图显示了一个包含一个根节点的目录树。Microsoft Windows 支持多个根节点。每个根节点映射到一个卷，如 `C:\` 或 `D:\`。 Solaris OS 支持一个根节点，这由斜杠`/`表示。

![](../images/io/io-dirStructure.gif)

文件系统通过路径来确定文件。例如，上图 statusReport 在 Solaris OS 描述为：

    /home/sally/statusReport

而在 Microsoft Windows 下，描述如下：

    C:\home\sally\statusReport
    
用来分隔目录名称的字符（也称为分隔符）是特定于文件系统的：Solaris OS 中使用正斜杠（/），而 Microsoft Windows 使用反斜杠（\）。

### 相对或绝对路径？

路径可以是相对或绝对的。绝对路径总是包含根元素以及找到该文件所需要的完整的目录列表。对于例如，` /home/sally/statusReport` 是一个绝对路径。所有找到的文件所需的信息都包含在路径字符串里。

相对路径需要与另一路径进行组合才能访问到文件。例如，`joe/foo` 是一个相对路径,没有更多的信息，程序不能可靠地定位 `joe/foo` 目录。

### 符号链接（Symbolic Links）

文件系统对象最典型的是目录或文件。每个人都熟悉这些对象。但是，某些文件系统还支持符号链接的概念。符号链接也被称为符号链接（symlink）或软链接（soft link）。

符号链接是，作为一个引用到另一个文件的特殊文件。在大多数情况下，符号链接对于应用程序来说透明的，符号链接上面的操作会被自动重定向到链接的目标（链接的目标是指该所指向的文件或目录）。当符号链接删除或重命名，在这种情况下，链接本身被删除或重命名，而不是链接的目标。

在下图中，logFile 对于用户来说看起来似乎是一个普通文件，但它实际上是 `dir/logs/HomeLogFile` 文件的符号链接。HomeLogFile 是链接的目标。

![](../images/io/io-symlink.gif)


符号链接通常对用户来说是透明。读取或写入符号链接是和读取或写入到任何其他文件或目录是一样的。

解析链接（resolving a link）是指在文件系统中用实际位置取代符号链接。在这个例子中，logFile 解析为 `dir/logs/HomeLogFile`

在实际情况下，大多数文件系统自由使用的符号链接。有时，一不小心创建符号链接会导致循环引用。循环引用是指，当链接的目标点回到原来的链接。循环引用可能是间接的：目录 a 指向目录 b，b 指向目录 c，其中包含的子目录指回目录 a 。当一个程序被递归遍历目录结构时，循环引用可能会导致混乱。但是，这种情况已经做了限制，不会导致程序无限循环。

接下来章节将讨论 Java 文件 I/O 的核心 Path 类。

## Path 类

该 [Path](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html) 类是从 Java SE 7 开始引入的， 是 [java.nio.file](https://docs.oracle.com/javase/8/docs/api/java/nio/file/package-summary.html) 包的主要进入点之一。

*注：若果 Java SE 7 之前的版本，可以使用 [File.toPath](https://docs.oracle.com/javase/8/docs/api/java/io/File.html#toPath--) 实现 Path 类似的功能*

Path 类是在文件系统路径的编程表示。Path 对象包含了文件名和目录列表，用于构建路径，以及检查，定位和操作文件。

Path 实例反映了基础平台。在 Solaris OS,路径使用 Solaris 语法（`/home/joe/foo`）,而在 Microsoft Windows，路径使用 Windows 语法（`C:\home\joe\foo`）。路径是与系统相关，即 Solaris 文件系统中的路径不能与 Windows 文件系统的路径进行匹配。

对应于该路径的文件或目录可能不存在。您可以创建一个 Path 实例，并以各种方式操纵它：您可以附加到它，提取它的一部分，把它比作其他路径。在适当的时候，可以使用在 [Files](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html) 类的方法来检查对应路径的文件是否存在，创建文件，打开它，删除它，改变它的权限，等等。

### Path 操作

Path 类包括各种方法，可用于获得关于路径信息，路径的接入元件，路径转换为其它形式，或提取路径的部分。也有用于匹配的路径字符串的方法，也有用于在一个路径去除冗余的方法。这些路径方法，有时也被称为语义操作（syntactic operations），因为是在它们的路径本身进行操作，而不是访问文件系统。

#### 创建路径

Path 实例包含用于指定文件或目录的位置的信息。在它被定义的时候，一个 Path 上设置了一系列的一个或多个名称。根元素或文件名可能被包括在内，但也不是必需的。Path 可能包含只是一个单一的目录或文件名。

您可以通过 [Paths](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html)（注意是复数）助手类的 get 方法很容易地创建一个 Path 对象：

    Path p1 = Paths.get("/tmp/foo");
    Path p2 = Paths.get(args[0]);
    Path p3 = Paths.get(URI.create("file:///Users/joe/FileTest.java"));

Paths.get 是下面方式的简写：

    Path p4 = FileSystems.getDefault().getPath("/users/sally");
    
下面的例子假设你的 home 目录是 `/u/joe`,则将创建`/u/joe/logs/foo.log`, 或若果是 Windows 环境，则为`C:\joe\logs\foo.log`

#### 检索有关一个路径

你可以把路径作为储存这些名称元素的序列。在目录结构中的最高元素将设在索引为0的目录结构中，而最低元件将设在索引 [n-1]，其中 n 是 Path 的元素个数。方法可用于检索各个元素或使用这些索引 Path 的子序列。

本示例使用下面的目录结构：

![](../images/io/io-dirStructure.gif)

下面的代码片段定义了一个 Path 实例，然后调用一些方法来获取有关的路径信息：

    // None of these methods requires that the file corresponding
    // to the Path exists.
    // Microsoft Windows syntax
    Path path = Paths.get("C:\\home\\joe\\foo");
    
    // Solaris syntax
    Path path = Paths.get("/home/joe/foo");
    
    System.out.format("toString: %s%n", path.toString());
    System.out.format("getFileName: %s%n", path.getFileName());
    System.out.format("getName(0): %s%n", path.getName(0));
    System.out.format("getNameCount: %d%n", path.getNameCount());
    System.out.format("subpath(0,2): %s%n", path.subpath(0,2));
    System.out.format("getParent: %s%n", path.getParent());
    System.out.format("getRoot: %s%n", path.getRoot());

下面是 Windows 和 Solaris OS 不同的输出:

方法 |  Solaris OS	 返回 | Microsoft Windows	返回
---- | ---- | ----
toString |	/home/joe/foo	| C:\home\joe\foo	
getFileName	 |	foo	 |	foo	 
getName(0)	 |	home |	home
getNameCount	 |	3	 |	3	
subpath(0,2)	 |	home/joe	 |	home\joe
getParent	 |	/home/joe  |	\home\joe	
getRoot	 |	/  |	C:\	

下面是一个相对路径的例子：

    // Solaris syntax
    Path path = Paths.get("sally/bar");
    or
    // Microsoft Windows syntax
    Path path = Paths.get("sally\\bar");
    
下面是 Windows 和 Solaris OS 不同的输出:

方法 |  Solaris OS	 返回 | Microsoft Windows	返回
---- | ---- | ----
toString |  	sally/bar	 |  sally\bar
getFileName |  	bar |  	bar
getName(0)	 |  sally |  	sally
getNameCount |  	2 |  2
subpath(0,1) |  	sally	 |  sally
getParent	 |  sally	 |  sally
getRoot	 |  null |  	null

#### 从 Path 中移除冗余

许多文件系统使用“.”符号表示当前目录，“..”来表示父目录。您可能有一个 Path 包含冗余目录信息的情况。也许一个服务器配置为保存日志文件在“/dir/logs/.”目录，你想删除后面的“/.”

下面的例子都包含冗余：

    /home/./joe/foo
    /home/sally/../joe/foo

normalize 方法是删除任何多余的元素，其中包括任何 出现的“.”或“directory/...”。前面的例子规范化为 `/home/joe/foo`

要注意，当它清理一个路径时，normalize 不检查文件系统。这是一个纯粹的句法操作。在第二个例子中，如果 sally 是一个符号链接，删除`sally/..` 可能会导致不能定位的预期文件。

清理路径的同时，你可以使用 toRealPath 方法来确保结果定位正确的文件。此方法在下一节中描述

#### 转换一个路径

可以使用3个方法来转换路径。[toUri](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html#toUri--) 将路径转换为可以在浏览器中打开一个字符串，例如：

    Path p1 = Paths.get("/home/logfile");
    // Result is file:///home/logfile
    System.out.format("%s%n", p1.toUri());
   
[toAbsolutePath](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html#toAbsolutePath--) 方法将路径转为相对路径。如果传递的路径已是绝对的，则返回同一Path 对象。toAbsolutePath 方法可以非常有助于处理用户输入的文件名。例如

```java
public class FileTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("usage: FileTest file");
            System.exit(-1);
        }

        // Converts the input string to a Path object.
        Path inputPath = Paths.get(args[0]);

        // Converts the input Path
        // to an absolute path.
        // Generally, this means prepending
        // the current working
        // directory.  If this example
        // were called like this:
        //     java FileTest foo
        // the getRoot and getParent methods
        // would return null
        // on the original "inputPath"
        // instance.  Invoking getRoot and
        // getParent on the "fullPath"
        // instance returns expected values.
        Path fullPath = inputPath.toAbsolutePath();
	}
}
```

该 toAbsolutePath 方法转换用户输入并返回一个 Path 对于查询时返回是有用的值。此方法不需要文件存在才能正常工作。


#### 加入两条路径
#### 创建两条道路之间的路径
#### 比较两个路径

## 文件操作
## 检查文件或目录
## 删除文件或目录
## 复制文件或目录
## 移动一个文件或目录
## 管理元数据（文件和文件存储的属性）
## 阅读，写作，和创建文件
## 随机访问文件
## 创建和读取目录
## 链接，符号或否则
## 走在文件树
## 查找文件
## 看目录的更改
## 其他有用的方法
## 传统的文件I/ O代码