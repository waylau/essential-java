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

完整的例子：

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