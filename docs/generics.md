# 泛型（Generics）

泛型通过在编译时检测到更多的代码 bug 从而使你的代码更加稳定。

## 泛型的作用

概括地说，泛型支持类型（类和接口）在定义类，接口和方法时作为参数。就像在方法声明中使用的形式参数（formal parameters），类型参数提供了一种输入可以不同但代码可以重用的方式。所不同的是，形式参数的输入是值，类型参数输入的是类型参数。

使用泛型对比非泛型代码有很多好处：

* 在编译时更强的类型检查。

如果代码违反了类型安全，Java 编译器将针对泛型和问题错误采用强大的类型检查。修正编译时的错误比修正运行时的错误更加容易。

* 消除了强制类型转换。

没有泛型的代码片需要强制转化：

```
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);
```

当重新编写使用泛型，代码不需要强转：

```
List<String> list = new ArrayList<String>();
list.add("hello");
String s = list.get(0);   // no cast
```

* 使编程人员能够实现通用算法。

通过使用泛型，程序员可以实现工作在不同类型集合的通用算法，并且是可定制，类型安全，易于阅读。

## 泛型类型（Generic Types）

泛型类型是参数化类型的泛型类或接口。下面是一个 Box 类例子来说明这个概念。

### 一个简单的 Box 类

```java
public class Box {
	private Object object;

	public void set(Object object) {
		this.object = object;
	}

	public Object get() {
		return object;
	}
}
```

由于它的方法接受或返回一个 Object，你可以自由地传入任何你想要的类型，只要它不是原始的类型之一。在编译时，没有办法验证如何使用这个类。代码的一部分可以设置 Integer 并期望得到 Integer ，而代码的另一部分可能会由于错误地传递一个String ，而导致运行错误。

### 一个泛型版本的 Box 类

泛型类定义语法如下：

```
class name<T1, T2, ..., Tn> { /* ... */ }
```

类型参数部分用 `<>` 包裹，制定了类型参数或称为类型变量（type parameters or  type variables) T1, T2, ..., 直到 Tn.

下面是代码：

```java
public class Box<T> {
	// T stands for "Type"
	private T t;

	public void set(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

}
```

主要，所有的 Object 被 T 代替了。类型变量可以是非基本类型的的任意类型，任意的类、接口、数组或其他类型变量。

这个技术同样适用于泛型接口的创建。

### 类型参数命名规范

按照惯例，类型参数名称是单个大写字母，用来区别普通的类或接口名称。

常用的类型参数名称如下：

```
E - Element (由 Java 集合框架广泛使用)
K - Key
N - Number
T - Type
V - Value
S,U,V 等. - 第二种、第三种、第四种类型
```

### 调用和实例化一个泛型

从代码中引用泛型 Box 类，则必须执行一个泛型调用(generic type invocation)，用具体的值，比如 Integer 取代 T ：

```
Box<Integer> integerBox;
```

泛型调用与普通的方法调用类似，所不同的是传递参数是类型参数（type argument ），本例就是传递 Integer 到 Box 类：

*Type Parameter 和 Type Argument 区别*

*编码时，提供  type argument 的一个原因是为了创建 参数化类型。因此，`Foo<T>` 中的 T 是一个 type parameter， 而 `Foo<String>` 中的 String 是一个 type argument*

与其他变量声明类似，代码实际上没有创建一个新的 Box 对象。它只是声明integerBox 在读到 `Box<Integer>` 时，保存一个“Integer 的 Box”的引用。

泛型的调用通常被称为一个参数化类型（parameterized type）。

实例化类，使用 new 关键字：

```
Box<Integer> integerBox = new Box<Integer>();
```

### 菱形（Diamond）

Java SE 7 开始泛型可以使用空的类型参数集`<>`，只要编译器能够确定，或推断，该类型参数所需的类型参数。这对尖括号`<>`，被非正式地称为“菱形（diamond）”。例如：

```
Box<Integer> integerBox = new Box<>();
```

### 多类型参数

下面是一个泛型 Pair 接口和一个泛型 OrderedPair ：

```
public interface Pair<K, V> {
    public K getKey();
    public V getValue();
}

public class OrderedPair<K, V> implements Pair<K, V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
	this.key = key;
	this.value = value;
    }

    public K getKey()	{ return key; }
    public V getValue() { return value; }
}
```

创建两个 OrderedPair 实例：

```
Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
Pair<String, String>  p2 = new OrderedPair<String, String>("hello", "world");
```

代码 `new OrderedPair<String, Integer>`，实例 K 作为一个 String 和 V 为 Integer。因此，OrderedPair 的构造函数的参数类型是 String 和 Integer。由于自动装箱（autoboxing），可以有效的传递一个 String 和 int 到这个类。

可以使用菱形（diamond）来简化代码：

```
OrderedPair<String, Integer> p1 = new OrderedPair<>("Even", 8);
OrderedPair<String, String>  p2 = new OrderedPair<>("hello", "world");
```

### 参数化类型

您也可以用 参数化类型（例如，`List<String>`的）来替换类型参数（即 K 或 V ）。例如，使用`OrderedPair<K，V>`例如：

```
OrderedPair<String, Box<Integer>> p = new OrderedPair<>("primes", new Box<Integer>(...));
```

### 原生类型（Raw Types）

原生类型是没有类型参数(type arguments)的泛型类和泛型接口，如泛型 Box 类;

```
public class Box<T> {
    public void set(T t) { /* ... */ }
    // ...
}
```

为了创建