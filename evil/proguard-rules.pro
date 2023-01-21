# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-repackageclasses "superevil"
-allowaccessmodification
-ignorewarnings

#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
#
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

### Xi 配置
###
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共库的成员
-dontskipnonpubliclibraryclassmembers

# 混淆时不做预校验
-dontpreverify

# 混淆时不记录日志
-verbose

# 忽略警告
# -ignorewarning

# 代码优化
-dontshrink

# 不优化输入的类文件
#-dontoptimize

# 保留注解不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 保留代码行号，方便异常信息的追踪
-keepattributes SourceFile,LineNumberTable

# 混淆采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*

# dump.txt文件列出apk包内所有class的内部结构
-dump class_files.txt

# seeds.txt文件列出未混淆的类和成员
-printseeds seeds.txt

# usage.txt文件列出从apk中删除的代码
-printusage unused.txt

# mapping.txt文件列出混淆前后的映射
-printmapping mapping.txt

# 避免混淆自定义控件类的 get/set 方法和构造函数
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 关闭 Log日志
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#    public static *** i(...);
#    public static *** e(...);
#    public static *** w(...);
#}

# 避免资源混淆
-keep class **.R$* {*;}

# 避免layout中onclick方法（android:οnclick="onClick"）混淆
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 避免回调函数 onXXEvent 混淆
-keepclassmembers class * {
    void *(*Event);
}

# Natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}