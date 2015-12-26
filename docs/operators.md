# 运算符

靠近表顶部的运算符，其优先级最高。具有较高优先级的运算符在相对较低的优先级的运算符之前被评估。在同一行上的运算符具有相同的优先级。当在相同的表达式中出现相同优先级的运算符时，必须首先对该规则进行评估。除了赋值运算符外，所有二进制运算符进行评估从左到右，赋值操作符是从右到左。

运算符优先级表：

运算符 | 优先级
后缀（postfix） | expr++ expr--
一元运算（unary） | ++expr --expr +expr -expr ~ !
乘法（multiplicative）	| * / %
加法（additive）	| + -
移位运算（shift） | << >> >>>
关系（relational） | < > <= >= instanceof
相等（equality） | == !=
与运算（bitwise AND）	 | &
异或运算（bitwise exclusive OR）	 | ^
或运算（bitwise inclusive OR）	 | |
逻辑与运算（logical AND）	 | &&
逻辑或运算（logical OR）	 | ||
三元运算（ternary）	 | ? :
赋值运算（assignment）	 | = += -= *= /= %= &= ^= |= <<= >>= >>>=

