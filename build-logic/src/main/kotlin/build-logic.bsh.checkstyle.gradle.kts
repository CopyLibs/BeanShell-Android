plugins {
    checkstyle
}

val checkstyleToolVersion = "9.2.1"
val embeddedCheckstyleConfig = configurations.detachedConfiguration(
    dependencies.create("com.puppycrawl.tools:checkstyle:$checkstyleToolVersion")
).apply {
    isTransitive = false
}

extensions.configure<CheckstyleExtension> {
    toolVersion = checkstyleToolVersion
    config = resources.text.fromArchiveEntry(embeddedCheckstyleConfig, "sun_checks.xml")
    configProperties["org.checkstyle.sun.suppressionfilter.config"] = project.file("src/conf/checkstyle-supressions.xml").absolutePath
    isShowViolations = true
}

val checkstyleMain by tasks.registering(Checkstyle::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Code 格式检查"

    source("src/main/java")
    include("**/*.java")
    exclude("**/build/**")
    exclude(
        listOf(
            "bsh/AbstractCharStream.java",
            "bsh/BSHAutoCloseable.java",
            "bsh/BSHEnumConstant.java",
            "bsh/BSHLabeledStatement.java",
            "bsh/BSHMultiCatch.java",
            "bsh/BSHTryWithResources.java",
            "bsh/CharStream.java",
            "bsh/JavaCharStream.java",
            "bsh/JJTParserState.java",
            "bsh/Node.java",
            "bsh/ParseException.java",
            "bsh/Parser.java",
            "bsh/ParserConstants.java",
            "bsh/ParserTokenManager.java",
            "bsh/ParserTreeConstants.java",
            "bsh/Token.java",
            "bsh/TokenMgrException.java",
        )
    )
    classpath = files()
}

tasks.matching { it.name == LifecycleBasePlugin.CHECK_TASK_NAME }.configureEach {
    dependsOn(checkstyleMain)
}
