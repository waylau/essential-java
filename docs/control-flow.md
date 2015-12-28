# 控制流程语句

控制流程语句用于控制程序按照一定流程来执行。

## if-then

它告诉你要只有 if 后面是 true 时才执行特定的代码。

```
void applyBrakes() {
    // the "if" clause: bicycle must be moving
    if (isMoving){ 
        // the "then" clause: decrease current speed
        currentSpeed--;
    }
}
```

如果 if 后面是 false, 则跳到 if-then 语句后面。语句可以省略中括号，但在编码规范里面不推荐使用，如：

```
void applyBrakes() {
    // same as above, but without braces 
    if (isMoving)
        currentSpeed--;
}
```

## if-then-else 

该语句是在 if 后面是 false 时，提供了第二个执行路径。

```
void applyBrakes() {
    if (isMoving) {
        currentSpeed--;
    } else {
        System.err.println("The bicycle has already stopped!");
    } 
}
```

下面是一个完整的例子：

```java
class IfElseDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int testscore = 76;
		char grade;

		if (testscore >= 90) {
			grade = 'A';
		} else if (testscore >= 80) {
			grade = 'B';
		} else if (testscore >= 70) {
			grade = 'C';
		} else if (testscore >= 60) {
			grade = 'D';
		} else {
			grade = 'F';
		}
		System.out.println("Grade = " + grade);
	}
}
```

输出为：`Grade = C`

## switch

switch 语句可以有许多可能的执行路径。可以使用 byte, short, char, 和 int 基本数据类型，也可以是枚举类型（enumerated types）、String 以及少量的原始类型的包装类 Character, Byte, Short, 和 Integer。

下面是一个 SwitchDemo 例子：

```java
class SwitchDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int month = 8;
		String monthString;
		switch (month) {
		case 1:
			monthString = "January";
			break;
		case 2:
			monthString = "February";
			break;
		case 3:
			monthString = "March";
			break;
		case 4:
			monthString = "April";
			break;
		case 5:
			monthString = "May";
			break;
		case 6:
			monthString = "June";
			break;
		case 7:
			monthString = "July";
			break;
		case 8:
			monthString = "August";
			break;
		case 9:
			monthString = "September";
			break;
		case 10:
			monthString = "October";
			break;
		case 11:
			monthString = "November";
			break;
		case 12:
			monthString = "December";
			break;
		default:
			monthString = "Invalid month";
			break;
		}
		System.out.println(monthString);
	}
}
```

break 语句是为了防止 fall through。

```java
class SwitchDemoFallThrough {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.util.ArrayList<String> futureMonths = new java.util.ArrayList<String>();

		int month = 8;

		switch (month) {
		case 1:
			futureMonths.add("January");
		case 2:
			futureMonths.add("February");
		case 3:
			futureMonths.add("March");
		case 4:
			futureMonths.add("April");
		case 5:
			futureMonths.add("May");
		case 6:
			futureMonths.add("June");
		case 7:
			futureMonths.add("July");
		case 8:
			futureMonths.add("August");
		case 9:
			futureMonths.add("September");
		case 10:
			futureMonths.add("October");
		case 11:
			futureMonths.add("November");
		case 12:
			futureMonths.add("December");
			break;
		default:
			break;
		}

		if (futureMonths.isEmpty()) {
			System.out.println("Invalid month number");
		} else {
			for (String monthName : futureMonths) {
				System.out.println(monthName);
			}
		}
	}

}
```

输出为：

```
August
September
October
November
December
```

技术上来说，最后一个 break 并不是必须，因为流程跳出 switch 语句。但仍然推荐使用 break ，主要修改代码就会更加简单和防止出错。default 处理了所有不明确值的情况。

下面例子展示了一个局域多个 case 的情况，

```java
class SwitchDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int month = 2;
		int year = 2000;
		int numDays = 0;

		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			numDays = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			numDays = 30;
			break;
		case 2:
			if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
				numDays = 29;
			else
				numDays = 28;
			break;
		default:
			System.out.println("Invalid month.");
			break;
		}
		System.out.println("Number of Days = " + numDays);
	}
}
```

输出为：`Number of Days = 29`

### 使用 String

Java SE 7 开始，可以在 switch 语句里面使用 String,下面是一个例子

```java
class StringSwitchDemo {

	public static int getMonthNumber(String month) {

		int monthNumber = 0;

		if (month == null) {
			return monthNumber;
		}

		switch (month.toLowerCase()) {
		case "january":
			monthNumber = 1;
			break;
		case "february":
			monthNumber = 2;
			break;
		case "march":
			monthNumber = 3;
			break;
		case "april":
			monthNumber = 4;
			break;
		case "may":
			monthNumber = 5;
			break;
		case "june":
			monthNumber = 6;
			break;
		case "july":
			monthNumber = 7;
			break;
		case "august":
			monthNumber = 8;
			break;
		case "september":
			monthNumber = 9;
			break;
		case "october":
			monthNumber = 10;
			break;
		case "november":
			monthNumber = 11;
			break;
		case "december":
			monthNumber = 12;
			break;
		default:
			monthNumber = 0;
			break;
		}

		return monthNumber;
	}

	public static void main(String[] args) {

		String month = "August";

		int returnedMonthNumber = StringSwitchDemo.getMonthNumber(month);

		if (returnedMonthNumber == 0) {
			System.out.println("Invalid month");
		} else {
			System.out.println(returnedMonthNumber);
		}
	}
}
```

输出为：8

**注：**switch 语句表达式中不能有 null。

## while 

while 语句在判断条件是 true 时执行语句块。语法如下：

```java
while (expression) {
     statement(s)
}
```

while 语句计算的表达式，必须返回 boolean 值。如果表达式计算为 true，while 语句执行 while 块的所有语句。while 语句继续测试表达式，然后执行它的块，直到表达式计算为 false。完整的例子：

```java
class WhileDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count = 1;
		while (count < 11) {
			System.out.println("Count is: " + count);
			count++;
		}
	}
}
```

用 while 语句实现一个无限循环：

```
while (true){
    // your code goes here
}
```

## do-while 

语法如下：

```
do {
     statement(s)
} while (expression);
```

do-while 语句和 while 语句的区别是，do-while 计算它的表达式是在循环的底部，而不是顶部。所以，do 块的语句，至少会执行一次，如 DoWhileDemo 程序所示：

```java
class DoWhileDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count = 1;
		do {
			System.out.println("Count is: " + count);
			count++;
		} while (count < 11);
	}
}
```

输出为：

```
Count is: 1
Count is: 2
Count is: 3
Count is: 4
Count is: 5
Count is: 6
Count is: 7
Count is: 8
Count is: 9
Count is: 10
```

## for

for 语句提供了一个紧凑的方式来遍历一个范围值。程序经常引用为"for 循环"，因为它反复循环，直到满足特定的条件。for 语句的通常形式，表述如下：

```
for (initialization; termination;
     increment) {
    statement(s)
}
```

使 for 语句时要注意：

* initialization 初始化循环；它执行一次作为循环的开始。
* 当 termination 计算为 false，循环结束。
* increment 会在循环的每次迭代执行；该表达式可以接受递增或者递减的值

```
class ForDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=1; i<11; i++){
            System.out.println("Count is: " + i);
       }
	}

}
```

输出为：

```
Count is: 1
Count is: 2
Count is: 3
Count is: 4
Count is: 5
Count is: 6
Count is: 7
Count is: 8
Count is: 9
Count is: 10
```
注意：代码在 initialization 声明变量。该变量的存活范围，从它的声明到 for 语句的块的结束。所以，它可以用在 termination 和 increment。如果控制 for 语句的变量，不需要在循环外部使用，最好是在 initialization 声明。经常使用 i,j,k 经常用来控制 for 循环。在 initialization 声明他们，可以限制他们的生命周期，减少错误。

for 循环的三个表达式都是可选的，一个无限循环，可以这么写：

```
// infinite loop
for ( ; ; ) {
    
    // your code goes here
}
```

for 语句还可以用来迭代 集合（Collections） 和 数组（arrays），这个形式有时被称为增强的 for 语句（ enhanced for ），可以用来让你的循环更加紧凑，易于阅读。为了说明这一点，考虑下面的数组：

```
int[] numbers = {1,2,3,4,5,6,7,8,9,10};
```

使用 增强的 for 语句来循环数组

```
class EnhancedForDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] numbers = 
            {1,2,3,4,5,6,7,8,9,10};
        for (int item : numbers) {
            System.out.println("Count is: " + item);
        }
	}

}
```

输出：

```
Count is: 1
Count is: 2
Count is: 3
Count is: 4
Count is: 5
Count is: 6
Count is: 7
Count is: 8
Count is: 9
Count is: 10
```

尽可能使用这种形式的 for 替代传统的 for 形式。

## break

break 语句有两种形式：标签和非标签。在前面的 switch 语句，看到的 break 语句就是非标签形式。可以使用非标签 break 用来结束 for，while，do-while 循环，如下面的 BreakDemo 程序：

```java
class BreakDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] arrayOfInts = { 32, 87, 3, 589, 12, 1076, 2000, 8, 622, 127 };
		int searchfor = 12;

		int i;
		boolean foundIt = false;

		for (i = 0; i < arrayOfInts.length; i++) {
			if (arrayOfInts[i] == searchfor) {
				foundIt = true;
				break;
			}
		}

		if (foundIt) {
			System.out.println("Found " + searchfor + " at index " + i);
		} else {
			System.out.println(searchfor + " not in the array");
		}
	}

}
```

这个程序在数组终查找数字12。break 语句，当找到值时，结束 for 循环。控制流就跳转到 for 循环后面的语句。程序输出是：

```
Found 12 at index 4
```

无标签 break 语句结束最里面的 switch，for，while，do-while 语句。而标签break 结束最外面的语句。接下来的程序，BreakWithLabelDemo，类似前面的程序，但使用嵌套循环在二维数组里寻找一个值。但值找到后，标签 break 语句结束最外面的 for 循环(标签为"search"):

```java
class BreakWithLabelDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[][] arrayOfInts = { { 32, 87, 3, 589 }, { 12, 1076, 2000, 8 }, { 622, 127, 77, 955 } };
		int searchfor = 12;

		int i;
		int j = 0;
		boolean foundIt = false;

		search: for (i = 0; i < arrayOfInts.length; i++) {
			for (j = 0; j < arrayOfInts[i].length; j++) {
				if (arrayOfInts[i][j] == searchfor) {
					foundIt = true;
					break search;
				}
			}
		}

		if (foundIt) {
			System.out.println("Found " + searchfor + " at " + i + ", " + j);
		} else {
			System.out.println(searchfor + " not in the array");
		}
	}
}
```

程序输出是：

```
Found 12 at 1, 0
```

break 语句结束标签语句，它不是传送控制流到标签处。控制流传送到紧随标记（终止）声明。

**注：** Java 没有类似于 C 语言的 goto 语句，但带标签的 break 语句，实现了类似的效果。

## continue

continue 语句忽略 for，while，do-while 的当前迭代。非标签模式，忽略最里面的循环体，然后计算循环控制的 boolean 表达式。接下来的程序，ContinueDemo，通过一个字符串的步骤，计算字母“p”出现的次数。如果当前字符不是 p，continue 语句跳过循环的其他代码，然后处理下一个字符。如果当前字符是 p，程序自增字符数。

```java
class ContinueDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String searchMe = "peter piper picked a " + "peck of pickled peppers";
        int max = searchMe.length();
        int numPs = 0;

        for (int i = 0; i < max; i++) {
            // interested only in p's
            if (searchMe.charAt(i) != 'p')
                continue;

            // process p's
            numPs++;
        }
        System.out.println("Found " + numPs + " p's in the string.");
	}

}
```

程序输出：

```
Found 9 p's in the string
```

为了更清晰看效果，尝试去掉 continue 语句，重新编译。再跑程序，count 将是错误的，输出是 35，而不是 9.

带标签的 continue 语句忽略标签标记的外层循环的当前迭代。下面的程序例子，ContinueWithLabelDemo，使用嵌套循环在字符传的字串中搜索字串。需要两个嵌套循环：一个迭代字串，一个迭代正在被搜索的字串。下面的程序ContinueWithLabelDemo，使用 continue 的标签形式，忽略最外层的循环。

```java
class ContinueWithLabelDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String searchMe = "Look for a substring in me";
		String substring = "sub";
		boolean foundIt = false;

		int max = searchMe.length() - substring.length();

		test: for (int i = 0; i <= max; i++) {
			int n = substring.length();
			int j = i;
			int k = 0;
			while (n-- != 0) {
				if (searchMe.charAt(j++) != substring.charAt(k++)) {
					continue test;
				}
			}
			foundIt = true;
			break test;
		}
		System.out.println(foundIt ? "Found it" : "Didn't find it");
	}

}
```

这里是程序输出：

```
Found it
```

## return

最后的分支语句是 return 语句。return 语句从当前方法退出，控制流返回到方法调用处。return 语句有两种形式：一个是返回值，一个是不返回值。为了返回一个值，简单在 return 关键字后面把值放进去(或者放一个表达式计算)

```
return ++count;
```
return 的值的数据类型，必须和方法声明的返回值的类型符合。当方法声明为 void，使用下面形式的 return 不需要返回值。

```
return;
```

## 源码

本章例子的源码，可以在 `com.waylau.essentialjava.flow` 包下找到。