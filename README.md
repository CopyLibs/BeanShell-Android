# BeanShell-Android

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android:$version")
```

## 用法

```kotlin
class PluginMethod {
    fun log(tag: String, msg: String) {
        println("[$tag] $msg")
    }
}

Interpreter().apply {
    nameSpace.setVariable("TAG", "BeanShell", false)
    nameSpace.setMethod(BshMethod(PluginMethod::class.java.getMethod("log", String::class.java, String::class.java), PluginMethod()))
}.eval("log(TAG, \"Hello World\")")
```

## 致谢

- [beanshell](https://github.com/beanshell/beanshell)
- [BeanShell](https://github.com/Hicores/BeanShell)
