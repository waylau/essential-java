# 通过方法声明异常抛出

上一节展示了如何为ListOfNumbers类中的writeList方法编写异常处理程序。 有时，它适合代码捕获可能发生在其中的异常。 但在其他情况下，最好让一个方法进一步推给上层来调用堆栈处理异常。 例如，如果您将ListOfNumbers类提供为类包的一部分，则可能无法预期包的所有用户的需求。 在这种情况下，最好不要捕获异常，并允许一个方法进一步推给上层来调用堆栈来处理它。

如果writeList方法没有捕获其中可能发生的已检查异常，则writeList方法必须指定它可以抛出这些异常。 让我们修改原始的writeList方法来指定它可以抛出的异常，而不是捕捉它们。 请注意，下面是不能编译的writeList方法的原始版本。


```
public void writeList() {
    PrintWriter out = new PrintWriter(new FileWriter("OutFile.txt"));
    for (int i = 0; i < SIZE; i++) {
        out.println("Value at: " + i + " = " + list.get(i));
    }
    out.close();
}
```


要指定writeList可以抛出两个异常，请为writeList方法的方法声明添加一个throws子句。 throws子句包含throws关键字，后面是由该方法抛出的所有异常的逗号分隔列表。 该子句在方法名和参数列表之后，在定义方法范围的大括号之前。这里是一个例子。

```
public void writeList() throws IOException, IndexOutOfBoundsException {
```

记住 IndexOutOfBoundsException是未检查异常（unchecked exception），包括它在throws子句中不是强制性的。 你可以写成下面这样

```
public void writeList() throws IOException {
```