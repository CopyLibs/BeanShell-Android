# BeanShell-Android-Lambda

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android-lambda)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android-lambda:$version")
```

## 用法

### 导入类

```kotlin
interpreter.nameSpace.importClass(name)
```

### 导入包

```kotlin
interpreter.nameSpace.importPackage(name)
```

### 设置变量

```kotlin
interpreter.set(name, value)
```

### 设置方法

```kotlin
interpreter.nameSpace.setMethod(method)
```

### 执行代码

```kotlin
interpreter.eval(code)
```

### 执行文件

```kotlin
interpreter.source(path)
```

### 加载Dex

```kotlin
val loader = BshLoaderHelper.getLoaderByDex(dexPath, parentLoader)
interpreter.addClassLoader(clsLoader)
```

## 致谢

- [beanshell](https://github.com/beanshell/beanshell)
- [BeanShell](https://github.com/Hicores/BeanShell)
