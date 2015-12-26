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
---- | ----
+	| 加 (也用于 String 的连接)
-	| 减
*	| 乘
/	| 除
%	| 取余

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
---- | ----
+ | 加运算;指正值
- | 减运算符;表达成负值
++ | 递增运算符;递增值1
- | 递减运算符;递减值1
！| 逻辑补运算;反转一个布尔值

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