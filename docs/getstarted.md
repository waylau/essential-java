# 快速开始

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

本书中所使用JDK版本为：Java Platform (JDK) 8u66
本书所使用的操作系统为：Win7 Sp1 x64

## 设置执行路径

### UNIX

包括 Linux、Mac OS X 和 Solaris 环境下，在`~/.bashrc`或 `~/.bash_profile` 文件末尾添加

```
export PATH=usr/local/java/jdk1.8.0_66:$PATH
```

### Windows

把安装目录下的 `C:\Program Files\Java\jdk1.8.0_66\bin` 设置为 `PATH` 的环境变量。比如：

```
C:\WINDOWS\system32;C:\WINDOWS;C:\Program Files\Java\jdk1.8.0_66\bin
```

## 测试

测试安装是否正确，可以在 shell 窗口，键入：

```
java -version

```

若能看到如下信息，则说明安装成功：

```
java version "1.8.0_66"
Java(TM) SE Runtime Environment (build 1.8.0_66-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.66-b17, mixed mode)
```