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
interpreter.nameSpace.importClass(name)
// 导入 包
interpreter.nameSpace.importPackage(name)
// 设置 变量
interpreter.set(name, value)
// 设置 方法
interpreter.nameSpace.setMethod(method)
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

### List

```beanshell
var list = new List { 1, 2, 3, 4 };
var list = (List) { 1, 2, 3, 4 };
List list = { 1, 2, 3, 4 };
```

### Lambda

```beanshell
new Thread(() -> System.out.println("Hello World")).start();
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
