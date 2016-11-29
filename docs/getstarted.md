# 快速开始

本章介绍了如何下载、安装、配置和调试 JDK。

## 下载、安装 JDK

JDK(Java Development Kit)是用于 Java 开发的工具箱。

在<http://www.oracle.com/technetwork/java/javase/downloads/index.html>下载

JDK 支持如下操作系统的安装：

操作系统类型 | 文件大小 | 文件
---- | ---- | ----
Linux x86	| 154.67 MB | jdk-8u66-linux-i586.rpm
Linux x86	| 174.83 MB | jdk-8u66-linux-i586.tar.gz
Linux x64	| 152.69 MB | jdk-8u66-linux-x64.rpm
Linux x64	| 172.89 MB | jdk-8u66-linux-x64.tar.gz
Mac OS X x64	| 227.12 MB | jdk-8u66-macosx-x64.dmg
Solaris SPARC 64-bit (SVR4 package)	| 139.65 MB | jdk-8u66-solaris-sparcv9.tar.Z
Solaris SPARC 64-bit	 | 99.05 MB | jdk-8u66-solaris-sparcv9.tar.gz
Solaris x64 (SVR4 package) | 140 MB | jdk-8u66-solaris-x64.tar.Z
Solaris x64	| 96.2 MB | jdk-8u66-solaris-x64.tar.gz
Windows x86	| 181.33 MB | jdk-8u66-windows-i586.exe
Windows x64	| 186.65 MB | jdk-8u66-windows-x64.exe

安装路径默认安装在 `C:\Program Files\Java\jdk1.8.0_66` 或者 `usr/local/java/jdk1.8.0_66`

**注：**本书中所使用JDK版本为：Java Platform (JDK) 8u66。
本书所使用的操作系统为：Win7 Sp1 x64。本书的示例是在 Eclipse  Mars.1 Release (4.5.1) 工具下编写。

### 基于 RPM 的 Linux

（1）下载安装文件

文件名类似于`jdk-8uversion-linux-x64.rpm`。
 

（2）切换到 root 用户身份

（3）检查当前的安装情况。卸载老版本的 JDK

检查当前的安装情况，比如：

```shell
$ rpm -qa | grep  jdk
jdk1.8.0_102-1.8.0_102-fcs.x86_64
```

若有老版本 JDK，则需先卸载老版本：

```shell
$ rpm -e package_name
 ```
 
比如：

```shell
$ rpm -e jdk1.8.0_102-1.8.0_102-fcs.x86_64
 ```

（4）安装

```shell
$ rpm -ivh jdk-8uversion-linux-x64.rpm
```

比如：

```shell
$ rpm -ivh jdk-8u102-linux-x64.rpm
Preparing...                ########################################### [100%]
   1:jdk1.8.0_102           ########################################### [100%]
Unpacking JAR files...
	tools.jar...
	plugin.jar...
	javaws.jar...
	deploy.jar...
	rt.jar...
	jsse.jar...
	charsets.jar...
	localedata.jar...
 ```
 
（5）升级

```shell
$ rpm -Uvh jdk-8uversion-linux-x64.rpm
```

安装完成后，可以删除`.rpm`文件，以节省空间。 安装完后，无需重启主机，即可使用 JDK。


## 设置执行路径


### Windows

增加一个 `JAVA_HOME` 环境变量，值是 JDK 的安装目录。如 `C:\Program Files\Java\jdk1.8.0_66` ，注意后边不带分号

在 `PATH` 的环境变量里面增加 `%JAVA_HOME%\bin;` 

在 `CLASSPATH`增加`.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;`（前面有点号和分号，后边结尾也有分号。
或者可以写成`.;%JAVA_HOME%\lib`如图所示，一样的效果。
 
### UNIX

包括 Linux、Mac OS X 和 Solaris 环境下，在`~/.profile`、`~/.bashrc`或 `~/.bash_profile` 文件末尾添加：

```
export JAVA_HOME=/usr/java/jdk1.8.0_66
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```

其中：

* JAVA_HOME 是 JDK 安装目录
* Linux 下用冒号“:”来分隔路径
* $PATH 、$CLASSPATH、 $JAVA_HOME 是用来引用原来的环境变量的值
* export 是把这三个变量导出为全局变量

比如，在 CentOS 下，需编辑`/etc/profile`文件。

## 测试

测试安装是否正确，可以在 shell 窗口，键入：

```shell
$ java -version
```

若能看到如下信息，则说明 JDK 安装成功：

```
java version "1.8.0_66"
Java(TM) SE Runtime Environment (build 1.8.0_66-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.66-b17, mixed mode)
```

最好再执行下`javac`，以测试环境变量是否设置正确：

```shell
$ javac
用法: javac <options> <source files>
其中, 可能的选项包括:
  -g                         生成所有调试信息
  -g:none                    不生成任何调试信息
  -g:{lines,vars,source}     只生成某些调试信息
  -nowarn                    不生成任何警告
  -verbose                   输出有关编译器正在执行的操作的消息
  -deprecation               输出使用已过时的 API 的源位置
  -classpath <路径>            指定查找用户类文件和注释处理程序的位置
  -cp <路径>                   指定查找用户类文件和注释处理程序的位置
  -sourcepath <路径>           指定查找输入源文件的位置
  -bootclasspath <路径>        覆盖引导类文件的位置
  -extdirs <目录>              覆盖所安装扩展的位置
  -endorseddirs <目录>         覆盖签名的标准路径的位置
  -proc:{none,only}          控制是否执行注释处理和/或编译。
  -processor <class1>[,<class2>,<class3>...] 要运行的注释处理程序的名称; 绕过默认的搜索进程
  -processorpath <路径>        指定查找注释处理程序的位置
  -parameters                生成元数据以用于方法参数的反射
  -d <目录>                    指定放置生成的类文件的位置
  -s <目录>                    指定放置生成的源文件的位置
  -h <目录>                    指定放置生成的本机标头文件的位置
  -implicit:{none,class}     指定是否为隐式引用文件生成类文件
  -encoding <编码>             指定源文件使用的字符编码
  -source <发行版>              提供与指定发行版的源兼容性
  -target <发行版>              生成特定 VM 版本的类文件
  -profile <配置文件>            请确保使用的 API 在指定的配置文件中可用
  -version                   版本信息
  -help                      输出标准选项的提要
  -A关键字[=值]                  传递给注释处理程序的选项
  -X                         输出非标准选项的提要
  -J<标记>                     直接将 <标记> 传递给运行时系统
  -Werror                    出现警告时终止编译
  @<文件名>                     从文件读取选项和文件名
```

有读者反映有时候`java -version`能够执行成功，但`javac`命令不成功的情况，一般是环境变量配置问题，请参阅上面“设置执行路径”章节内容，再仔细检测环境变量的配置。


更多安装细节，可以参考 <http://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html>，以及<http://docs.oracle.com/javase/tutorial/essential/environment/paths.html>
