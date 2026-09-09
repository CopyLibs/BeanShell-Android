# BeanShell Android 使用指南

## 方法

### 基础

```kotlin
// 导入包
interpreter.nameSpace.importPackage(name)
// 导入类
interpreter.nameSpace.importClass(name)
// 设置变量
interpreter.nameSpace.setVariable(name, value)
// 设置方法
interpreter.nameSpace.setMethod(method)
// 执行代码
interpreter.eval(code)
// 执行文件
interpreter.source(path)
// 添加类加载器
interpreter.addClassLoader(loader)
// 安装扩展模块
interpreter.installModule(module)
```

### 辅助

```kotlin
// 获取 DEX 类加载器
BshLoaderHelper.getLoaderByDex(path, parentLoader)
// 获取 JAR 类加载器
BshLoaderHelper.getLoaderByJar(path, parentLoader)
// 获取 AAR 类加载器
BshLoaderHelper.getLoaderByAar(path, parentLoader)
// 获取 APK 上下文
BshApkLoaderHelper.getContextByApk(ctx, apkPath)
```

## 示例

### Lambda

```beanshell
// Java / BeanShell
Runnable task = () -> System.out.println("Hello World");
new Thread(task).start();
```

### 行尾分号可选

```beanshell
// Java
String message = "Hello World";

// BeanShell
String message = "Hello World"
```

### `val` 常量

```beanshell
// Java
final String name = "BeanShell";

// BeanShell
val name = "BeanShell"
```

### `when` 表达式

```beanshell
// Java
int number = 2;
String result = switch (number) {
    case 1 -> "one";
    case 2, 3 -> "small";
    default -> "other";
}; // "small"

// BeanShell
var number = 2
var result = when (number) {
    1 -> "one"
    2, 3 -> "small"
    else -> "other"
} // "small"
```

### 字符串模板

```beanshell
// Java
String language = "BeanShell";
String basic = "Hello, " + language;      // "Hello, BeanShell"
String expression = "1 + 2 = " + (1 + 2); // "1 + 2 = 3"
String escaped = "price=$9";              // "price=$9"

// BeanShell
var language = "BeanShell"
var basic = "Hello, $language"            // "Hello, BeanShell"
var expression = "1 + 2 = ${1 + 2}"       // "1 + 2 = 3"
var escaped = "price=\$9"                 // "price=$9"
```

### 默认参数

```beanshell
// Java
void greet(String name, int age) {
    System.out.println("name: " + name + ", age: " + age);
}
void greet(String name) {
    greet(name, 18);
}
greet("user");    // name: user, age: 18
greet("user", 1); // name: user, age: 1

// BeanShell
void greet(String name, int age = 18) {
    print("name: $name, age: $age")
}
greet("user")    // name: user, age: 18
greet("user", 1) // name: user, age: 1
```

### 扩展方法

```beanshell
// Java
static void log(String value) {
    System.out.println(value);
}
log("BeanShell");

// BeanShell
void String.log() {
    print(this)
}
"BeanShell".log() // BeanShell
```
