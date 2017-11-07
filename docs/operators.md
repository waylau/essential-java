# 运算符

靠近表顶部的运算符，其优先级最高。具有较高优先级的运算符在相对较低的优先级的运算符之前被评估。在同一行上的运算符具有相同的优先级。当在相同的表达式中出现相同优先级的运算符时，必须首先对该规则进行评估。除了赋值运算符外，所有二进制运算符进行评估从左到右，赋值操作符是从右到左。

运算符优先级表：

运算符 | 优先级
---- | ----
后缀（postfix） | `expr++ expr--`
一元运算（unary） | `++expr --expr +expr -expr ~ !`
乘法（multiplicative）	| `* / %`
加法（additive）	| `+ -`
移位运算（shift） | `<< >> >>>`
关系（relational） | `< > <= >= instanceof`
相等（equality） | `== !=`
与运算（bitwise AND）	 | `&`
异或运算（bitwise exclusive OR）	 | `^`
或运算（bitwise inclusive OR）	 | `|`
逻辑与运算（logical AND）	 | `&&`
逻辑或运算（logical OR）	 |`||`
三元运算（ternary）	 | `? :`
赋值运算（assignment）	 | `= += -= *= /= %= &= ^= |= <<= >>= >>>=`

## 赋值运算符

赋值运算符是最常用和最简单的运算符就是`=`，如下：

```
int cadence = 0;
int speed = 0;
int gear = 1;
```

该运算符也用于对象的引用关联。

## 算术运算符

算术运算符包括：

运算符 | 描述
|:----:| ----|
|+	| 加 (也用于 String 的连接)|
|-	| 减|
|*	| 乘|
|/	| 除|
|%	| 取余|

ArithmeticDemo 的例子

```java
class ArithmeticDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int result = 1 + 2;
		// result is now 3
		System.out.println("1 + 2 = " + result);
		int original_result = result;

		result = result - 1;
		// result is now 2
		System.out.println(original_result + " - 1 = " + result);
		original_result = result;

		result = result * 2;
		// result is now 4
		System.out.println(original_result + " * 2 = " + result);
		original_result = result;

		result = result / 2;
		// result is now 2
		System.out.println(original_result + " / 2 = " + result);
		original_result = result;

		result = result + 8;
		// result is now 10
		System.out.println(original_result + " + 8 = " + result);
		original_result = result;

		result = result % 7;
		// result is now 3
		System.out.println(original_result + " % 7 = " + result);
	}

}
```

输出为：

```
1 + 2 = 3
3 - 1 = 2
2 * 2 = 4
4 / 2 = 2
2 + 8 = 10
10 % 7 = 3
```

`+` 用于字符串连接的例子 ConcatDemo ：

```java
class ConcatDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        String firstString = "This is";
        String secondString = " a concatenated string.";
        String thirdString = firstString+secondString;
        System.out.println(thirdString);
	}
}
```

## 一元操作

一元运算符只需要一个操作数。

运算符 | 描述
|:----:| ----|
|+ | 加运算;指正值|
|- | 减运算符;表达成负值|
|++ | 递增运算符;递增值1|
|-- | 递减运算符;递减值1|
|！| 逻辑补运算;反转一个布尔值|

下面是 UnaryDemo 的示例：

```java
class UnaryDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int result = +1;
        // result is now 1
        System.out.println(result);

        result--;
        // result is now 0
        System.out.println(result);

        result++;
        // result is now 1
        System.out.println(result);

        result = -result;
        // result is now -1
        System.out.println(result);

        boolean success = false;
        // false
        System.out.println(success);
        // true
        System.out.println(!success);
	}

}
```

递增/递减运算符可以之前（前缀）或（后缀）操作数后应用。该代码`result++;` 和 `++result;` 两个的 result 都被加一。唯一的区别是，该前缀版本`++result;`增递增了，而后缀版本`result++;`的计算结果为原始值。下面是 PrePostDemo  的示例，说明了两者的区别：

```java
class PrePostDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i = 3;
        i++;
        // prints 4
        System.out.println(i);
        ++i;			   
        // prints 5
        System.out.println(i);
        // prints 6
        System.out.println(++i);
        // prints 6
        System.out.println(i++);
        // prints 7
        System.out.println(i);
	}
}
```

## 等价和关系运算符

等价和关系运算符包括

运算符 | 描述
|:----:| ----|
|==   |    相等（equal to）|
|!=   |    不相等（not equal to）|
|>    |    大于（greater than）|
|>=   |    大于等于（greater than or equal to）|
|<    |    小于（less than）|
|<=   |    小于等于（less than or equal to）|

ComparisonDemo 对比的例子：


```java
class ComparisonDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		if (value1 == value2)
			System.out.println("value1 == value2");
		if (value1 != value2)
			System.out.println("value1 != value2");
		if (value1 > value2)
			System.out.println("value1 > value2");
		if (value1 < value2)
			System.out.println("value1 < value2");
		if (value1 <= value2)
			System.out.println("value1 <= value2");
	}
}
```

输出为：

```
value1 != value2
value1 < value2
value1 <= value2
```

## 条件运算符

条件运算符包括：

运算符 | 描述
|:----:| ----|
| `&&`  | 条件与（Conditional-AND）|
| `||` | 条件或（Conditional-OR）|
| `?:` | 三元运算符（ternary operator）|

条件与、条件或的运算符例子 ConditionalDemo1： 

```java
class ConditionalDemo1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		if ((value1 == 1) && (value2 == 2))
			System.out.println("value1 is 1 AND value2 is 2");
		if ((value1 == 1) || (value2 == 1))
			System.out.println("value1 is 1 OR value2 is 1");
	}
}
```

输出：

value1 is 1 AND value2 is 2
value1 is 1 OR value2 is 1

下面是一个三元运算符的例子,类似与 if-then-else 语句，见 ConditionalDemo2 ：

```java
class ConditionalDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int value1 = 1;
		int value2 = 2;
		int result;
		boolean someCondition = true;
		result = someCondition ? value1 : value2;

		System.out.println(result);
	}
}
```

## instanceof 运算符

instanceof 用于匹配判断对象的类型。可以用它来测试对象是否是类的一个实例，子类的实例，或者是实现了一个特定接口的类的实例。见例子 InstanceofDemo,父类是 Parent ，接口是 MyInterface ，子类是 Child 继承了父类并实现了接口。

```java
class InstanceofDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Must qualify the allocation with an enclosing instance of type InstanceofDemo 
		Parent obj1 = new InstanceofDemo().new Parent();
        Parent obj2 = new InstanceofDemo().new Child();

        System.out.println("obj1 instanceof Parent: "
            + (obj1 instanceof Parent));
        System.out.println("obj1 instanceof Child: "
            + (obj1 instanceof Child));
        System.out.println("obj1 instanceof MyInterface: "
            + (obj1 instanceof MyInterface));
        System.out.println("obj2 instanceof Parent: "
            + (obj2 instanceof Parent));
        System.out.println("obj2 instanceof Child: "
            + (obj2 instanceof Child));
        System.out.println("obj2 instanceof MyInterface: "
            + (obj2 instanceof MyInterface));
	}

	class Parent {}
	class Child extends Parent implements MyInterface {}
	interface MyInterface {}
}
```

输出为：

```
obj1 instanceof Parent: true
obj1 instanceof Child: false
obj1 instanceof MyInterface: false
obj2 instanceof Parent: true
obj2 instanceof Child: true
obj2 instanceof MyInterface: true
```

**注：**null 不是任何类的实例

## 位运算和位移运算符

位运算和位移运算符适用于整型。

### 位运算符

运算符 | 描述
|:----:| ----|
| `&` | 与|
| `|` | 或|
| `^` | 异或|
| `~` | 非（把0变成1，把1变成0）|

BitDemo 例子：

```java
class BitDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int bitmask = 0x000F;
		int val = 0x2222;
		// prints "2"
		System.out.println(val & bitmask);
	}
}
```

### 位移运算符

首先我们先阐述一下符号位的概念:

* 符号位：是数的最后一位，不用来计算的；
* 当符号位为0时，值为正数；当符号位为1时，值为负数；
* 无符号位时为正数，有符号位时为正数或者负数；

运算符 | 描述
|:----:| ----|
| `<<` | 左移|
| `>>` | 右移|
| `>>>` | 右移（补零）|

左移（<<） 运算形式：值 << 位数

右移（>>） 运算形式：值 >> 位数

移动后，左移、右移都会保留符号位！

右移（补零），移动后，不保留符号位，永远为正数，因为其符号位总是被补零；

## 源码

本章例子的源码，可以在 `com.waylau.essentialjava.operator` 包下找到。
