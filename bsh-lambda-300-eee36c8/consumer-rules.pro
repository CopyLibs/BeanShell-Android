
-keep class bsh.This { *; }
-keep class bsh.This$* { *; }
-keep class bsh.GeneratedClass { *; }
-keep class bsh.Primitive { *; }
-keep class bsh.classpath.ClassManagerImpl
-keep class bsh.commands.** {
    public static *** invoke(...);
}
-keepclassmembers class bsh.BshLambda {
    public final *** invoke*(...);
}
