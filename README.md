# BeanShell-Android-Lambda

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android-lambda)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android-lambda:$version")
```

## 用法

### 基础方法

```kotlin
// 导入 类
interpreter.nameSpace.importClass(clsName)
// 导入 包
interpreter.nameSpace.importPackage(pkgName)
// 设置 变量
interpreter.nameSpace.setVariable(varName, varValue)
// 设置 方法
interpreter.nameSpace.setMethod(bshMethod)
// 执行 代码
interpreter.eval(code)
// 执行 文件
interpreter.source(path)
// 添加 类加载器
interpreter.addClassLoader(clsLoader)
```

### 辅助方法

```kotlin
// 获取 Dex 类加载器
BshLoaderHelper.getLoaderByDex(dexPath, parentLoader)
// 获取 Jar 类加载器
BshLoaderHelper.getLoaderByJar(jarPath, parentLoader)
// 获取 Aar 类加载器
BshLoaderHelper.getLoaderByAar(aarPath, parentLoader)
```

## 示例

### 运算符

| 符号    | 描述                               | 用法           | 等价                               |
|-------|----------------------------------|--------------|----------------------------------|
| `??`  | 左侧非 `null` 时返回左侧，否则返回右侧。         | `a ?? b`     | `a != null ? a : b`              |
| `??=` | 仅当左侧为 `null` 时执行赋值。              | `a ??= b`    | `if (a == null) a = b`           |
| `?:`  | 左侧为真值时返回左侧，否则返回右侧。               | `a ?: b`     | `a ? a : b`                      |
| `?.`  | 左侧为 `null` 时直接返回 `null`，避免空指针错误。 | `obj?.field` | `obj == null ? null : obj.field` |
| `<=>` | 比较结果固定为 `-1 / 0 / 1`。            | `a <=> b`    | `a < b ? -1 : (a > b ? 1 : 0)`   |

### Lambda

```beanshell
new Thread(() -> print("Hello World")).start();
```

### List

```beanshell
// 定义
List list = {1, 2, 3, 4};
var list = new List {1, 2, 3, 4};
var list = (List) {1, 2, 3, 4};

// 相加
var list = (List) {1, 2} + {3, 4};
print(list); // [1, 2, 3, 4]

// 重复
var list = (List) {1, 2} * 2;
print(list); // [1, 2, 1, 2]

// 索引
var list = (List) {1, 2, 3, 4};
print(list[0]); // 1
print(list[-1]); // 4

// 切片
var list = (List) {1, 2, 3, 4};
print(list[1:3]); // [2, 3]
print(list[-3:-1]); // [2, 3]
print(list[:3]); // [1, 2, 3]
print(list[1:]); // [2, 3, 4]
print(list[::2]); // [1, 3]
```

### 新特性

```beanshell
// 尾分号可选
var msg = "Hello World"
print(msg)

// 定义常量(val)
val name = "BeanShell"

// 字符串模板
var lang = "BeanShell"
var str1 = "Hello, $lang"
print(str1) // "Hello, BeanShell"
var str2 = "1 + 2 = ${1 + 2}"
print(str2)  // "1 + 2 = 3"
var str3 = "price=\$9"
print(str3) // "price=$9"

// 默认参数
void hi(String name, int age = 18) {
    print("name: $name, age: $age")
}
hi("user") // name: user, age: 18
hi("user", 1) // name: user, age: 1
```

## 致谢

- [beanshell@beanshell](https://github.com/beanshell/beanshell) - 原始仓库
- [Hicores@BeanShell](https://github.com/Hicores/BeanShell) - Android 支持
- [Net-0@PR #772](https://github.com/beanshell/beanshell/pull/772) - SecurityGuard 支持
- [opeongo@PR #768](https://github.com/beanshell/beanshell/pull/768) - 浮点数值扩大 问题
- [Net-0@PR #766](https://github.com/beanshell/beanshell/pull/766) - Lambda 支持
- [opeongo@PR #729](https://github.com/beanshell/beanshell/pull/729) - 数字转换异常 问题
- [opeongo@PR #756](https://github.com/beanshell/beanshell/pull/756) - 多行注释处理 问题
- [opeongo@PR #732](https://github.com/beanshell/beanshell/pull/732) - 方法查找回归 问题
- [opeongo@PR #741](https://github.com/beanshell/beanshell/pull/741) - 变量查找混淆 问题
