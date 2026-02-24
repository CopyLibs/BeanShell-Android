# BeanShell-Android-Lambda

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android-lambda)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android-lambda:$version")
```

## 用法

### 导入 Class

```kotlin
interpreter.nameSpace.importClass(name)
```

### 导入 Package

```kotlin
interpreter.nameSpace.importPackage(name)
```

### 设置 Variable

```kotlin
interpreter.set(name, value)
```

### 设置 Method

```kotlin
interpreter.nameSpace.setMethod(method)
```

### 执行 Code

```kotlin
interpreter.eval(code)
```

### 执行 Path

```kotlin
interpreter.source(path)
```

### 添加 ClassLoader

```kotlin
interpreter.addClassLoader(clsLoader)
```

### 获取 Dex ClassLoader

```kotlin
BshLoaderHelper.getLoaderByDex(dexPath, parentLoader)
```

### 获取 Jar ClassLoader

```kotlin
BshLoaderHelper.getLoaderByJar(jarPath, parentLoader)
```

## 致谢

- [BeanShell](https://github.com/beanshell/beanshell) - 原始仓库
- [Hicores@BeanShell](https://github.com/Hicores/BeanShell) - Android 支持
- [Net-0@PR #772](https://github.com/beanshell/beanshell/pull/772) - SecurityGuard 支持
- [opeongo@PR #768](https://github.com/beanshell/beanshell/pull/768) - 浮点数值扩大问题
- [Net-0@PR #766](https://github.com/beanshell/beanshell/pull/766) - Lambda 支持
- [opeongo@PR #729](https://github.com/beanshell/beanshell/pull/729) - 数字类型转换异常
