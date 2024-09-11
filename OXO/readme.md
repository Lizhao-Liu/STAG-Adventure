# OXO Game
> Your aim in this assignment is to build a digital version of the classic turn-taking game "Noughts and Crosses" / "Tic-Tac-Toe" / "OXO". You will not be required to construct the entire game. 

## Model-View-Controller
![image](https://github.com/Lizhao-Liu/JAVA_Notes/blob/main/Polymorphism%20and%20Collections%20%26%20Error%20Handling/img/Screenshot%202021-02-27%20at%2023.47.54.png)

## Using Collections

### dynamic data types
- there are some dynamic data types (e.g. Queues, Stacks, Lists etc.) provided by some of the core Java libraries that allow us to store dynamically sized groups of Object

### Generics
- This mechanism allows us to designate a particular compound data structure to hold a specific object type.  e.g. `private ArrayList<ArrayList<OXOPlayer>> cells;`
- solve downcasting problem

### Collections
![image](https://github.com/Lizhao-Liu/JAVA_Notes/blob/main/Polymorphism%20and%20Collections%20%26%20Error%20Handling/img/Screenshot%202021-02-28%20at%2000.21.10.png)

### Using an Interface as a Type

When you define a new interface, you are defining a new reference data type. You can use interface names anywhere you can use any other data type name. If you define a reference variable whose type is an interface, any object you assign to it must be an instance of a class that implements the interface.

### Index of linkedlist

They have a logical index, yes - effectively the number of times you need to iterate, starting from the head, before getting to that node.

That's not the same as saying "it can directly search using index of the object" - because it can't.

### Java队列（Queue）了解及使用

[Java队列（Queue）了解及使用](https://www.jianshu.com/p/7a86c56c632b)


## 异常（error handling）
- `try {...} catch(Exception 3){...}` can handle errors without the program breaking.
- 如果必须调用一个声明会抛出异常的函数，那么必须： 1. 把函数的调用放到try catch block中，设置catch来捕捉所有可能抛出的异常； 2. 声明自己会抛出无法处理的异常
- business logic (如何使代码更加整洁）

## Inheritance Hierarchy
- if a method overrides one of its parent class's methods, invoke the overhidden method or the shadowed field using `super`.
- in constructor method, `super`(should be put on the first line) refers to the particular parent constructor method.
- 
### UNIFIED MODELLING LANGUAGE(UML)
![image](https://github.com/Lizhao-Liu/JAVA_Notes/blob/main/Polymorphism%20and%20Collections%20%26%20Error%20Handling/img/Screenshot%202021-02-28%20at%2000.42.32.png)


### 构造函数与默认构造函数

当有多个构造函数存在时，需要注意，在创建子类对象、调用构造函数时，如果在构造函数中没有特意声明，调用哪个父类的构造函数，则默认调用父类的无参构造函数（通常编译器会自动在子类构造函数的第一行加上super()方法）。

如果父类没有无参构造函数，或想调用父类的有参构造方法，则需要在子类构造函数的第一行用super()方法，声明调用父类的哪个构造函数。

