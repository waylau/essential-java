# 表达式、语句和块

运算符用于计算构建成了表达式(expressions)，而表达式是语句(statements)的核心组成，而语句是组织形式为块(blocks)。

## 表达式

表达式是由变量、运算符以及方法调用所构成的结构，如下：

```java
int cadence = 0;
anArray[0] = 100;
System.out.println("Element 1 at index 0: " + anArray[0]);

int result = 1 + 2; // result is now 3
if (value1 == value2) 
    System.out.println("value1 == value2");
```

表达式返回的数据类型取决于表达式中的元素。表达式`cadence = 0`返回一个int，因为赋值运算符将返回相同的数据类型作为其左侧操作数的值;在这种情况下，cadence  是一个 int。

下面是一个复合表达式：

```
1 * 2 * 3
```

表达式应该尽量避免歧义，比如：

```
x + y / 100  
```

有歧义，推荐写成 `(x + y) / 100` 或 `x + (y / 100)`。

## 语句

语句相当于自然语言中的句子。一条语句就是一个执行单元。用分号（;）结束一条语句。下面是表达式语句（expression statements），包括：

* 赋值表达式（Assignment expressions）
* ++ 或者 --（Any use of ++ or --）
* 方法调用（Method invocations—）
* 对象创建（Object creation expressions）

下面是表达式语句的例子

```java
// assignment statement
aValue = 8933.234;
// increment statement
aValue++;
// method invocation statement
System.out.println("Hello World!");
// object creation statement
Bicycle myBike = new Bicycle();
```

除了表达式语句，其他的还有声明语句（declaration statements）:

```java
// declaration statement
double aValue = 8933.234;
```

以及[控制流程语句](../docs/control-flow.md)（control flow statements）:

```
if (isMoving)
        currentSpeed--;
```

## 块

块是一组零个或多个成对大括号之间的语句，并可以在任何地方允许使用一个单独的语句。下面的 BlockDemo  例子：

```java
class BlockDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean condition = true;
		if (condition) { // begin block 1
			System.out.println("Condition is true.");
		} // end block one
		else { // begin block 2
			System.out.println("Condition is false.");
		} // end block 2
	}
}
```

## 源码

本章例子的源码，可以在 `com.waylau.essentialjava.expression` 包下找到。