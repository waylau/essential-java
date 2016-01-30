# 网络编程

## 网络基础

在互联网上之间的通信交流，一般是基于 Transmission Control Protocol (TCP) 或者 User Datagram Protocol (UDP) 协议，如下图：

![](../images/net/1netw.gif)

编写 Java 应用，我们只需关注于应用层 （application layer），而不用关心 TCP 和 UDP。java.net 包含了你编程所需的类，这些类是与操作系统无关的。

## TCP

TCP (Transmission Control Protocol) 是基于连接的提供两个主机间可靠的数据流(flow of data)

## UDP

UDP (User Datagram Protocol) 不是基于连接的，主机发送独立的数据包（datagram）给其他主机，不保证数据到达。