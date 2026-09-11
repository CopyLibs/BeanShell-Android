# BeanShell Android

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.copylibs/beanshell-android-lambda)

BeanShell Support For Android

## 依赖

```kotlin
implementation("io.github.copylibs:beanshell-android-lambda:$version")
```

## 特性

- Lambda 支持
- DEX、JAR、AAR 和 APK 动态加载
- 行尾分号可选、`val` 常量、`when` 表达式
- 字符串模板、默认参数、注解忽略和泛型兼容
- 扩展方法
- 扩展模块

## 文档

示例 请查看 [使用指南](docs/GUIDE.md)

## 致谢

- [beanshell@beanshell](https://github.com/beanshell/beanshell) - 上游仓库
- [Hicores@BeanShell](https://github.com/Hicores/BeanShell) - Android 支持
- [Net-0@PR #766](https://github.com/beanshell/beanshell/pull/766) - Lambda 支持
- [Net-0@PR #772](https://github.com/beanshell/beanshell/pull/772) - SecurityGuard 支持
- [opeongo@PR #729](https://github.com/beanshell/beanshell/pull/729) - 数字转换 问题
- [opeongo@PR #732](https://github.com/beanshell/beanshell/pull/732) - 方法查找 问题
- [opeongo@PR #741](https://github.com/beanshell/beanshell/pull/741) - 变量查找 问题
- [opeongo@PR #756](https://github.com/beanshell/beanshell/pull/756) - 多行注释 问题
- [opeongo@PR #768](https://github.com/beanshell/beanshell/pull/768) - 浮点拓宽 问题
- [wilx@PR #789](https://github.com/beanshell/beanshell/pull/789) - 缓存机制 问题
- [wilx@PR #793](https://github.com/beanshell/beanshell/pull/793) - 数组维度 问题
- [wilx@PR #795](https://github.com/beanshell/beanshell/pull/795) - 装箱运算 问题
- [wilx@PR #796](https://github.com/beanshell/beanshell/pull/796) - 浮点运算 问题
- [wilx@PR #798](https://github.com/beanshell/beanshell/pull/798) - 方法别名 问题
