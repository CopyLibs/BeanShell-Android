# BeanShell-Android

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android:$version")
```

## 用法

### 基础

```kotlin
class PluginMethod {
    fun log(msg: Any) {
        println("$msg")
    }
}

Interpreter().apply {
    nameSpace.setVariable("TAG", "BeanShell", false)
    nameSpace.setMethod(BshMethod(PluginMethod::class.java.getMethod("log", Any::class.java), PluginMethod()))
}.eval("log(TAG)")
```

### 其他

```kotlin
Interpreter().apply {
    val loader = BshLoaderManager.getDexLoader(dexPath, parentLoader)
    BshLoaderManager.addLoader(loader)
}.eval(
    """
        import test.Bean;

        Bean bean = new Bean(); 
        bean.setTitle("BeanShell");
        System.out.println(bean.toString());
    """.trimIndent()
)
```

## 致谢

- [beanshell](https://github.com/beanshell/beanshell)
- [BeanShell](https://github.com/Hicores/BeanShell)
