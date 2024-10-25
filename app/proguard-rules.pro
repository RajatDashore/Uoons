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

# below this line is proguard rules.===============================
#-ignorewarnings
#
#-keepattributes *Annotation*
#
#-dontnote junit.framework.**
#-dontnote junit.runner.**
#
#-dontwarn android.test.**
#-dontwarn android.support.test.**
#-dontwarn org.junit.**
#-dontwarn org.hamcrest.**
#-dontwarn com.squareup.javawriter.JavaWriter
#
#-dontobfuscate
#-dontoptimize
#-dontwarn
#
#-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.view.ViewCompat$Api* {
#  <methods>;
#}
#-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.view.WindowInsetsCompat$*Impl* {
#  <methods>;
#}
#-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.app.NotificationCompat$*$Api*Impl {
#  <methods>;
#}
#-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.os.UserHandleCompat$Api*Impl {
#  <methods>;
#}
#-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.widget.EdgeEffectCompat$Api*Impl {
#  <methods>;
#}
#
## android support
#-keep class android.support.v4.app.** { *; }
#-keep class android.support.v7.app.** { *; }
#
#-keep interface android.support.v4.app.** { *; }
#
##androidx
#-dontwarn com.google.android.material.**
#-keep class com.google.android.material.** { *; }
#
#-dontwarn androidx.**
#-keep class androidx.** { *; }
#-keep interface androidx.** { *; }
#
#-keep class androidx.navigation.fragment.NavHostFragment
#-keep class * extends androidx.fragment.app.Fragment{}
#-keepnames class * extends android.os.Parcelable
#-keepnames class * extends java.io.Serializable
#-keepnames class com.your.package.models.*
#
#-keepattributes *Annotation*
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgent
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.app.Fragment
#-keep class androidx.datastore.*.** {*;}
#-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler
#-keep class kotlinx.coroutines.android.AndroidDispatcherFactory
#
#
#
#-keepattributes *Annotation*
#-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
#    <fields>;
#}
#-keep !interface * implements androidx.lifecycle.LifecycleObserver {
#}
#-keep class * implements androidx.lifecycle.GeneratedAdapter {
#    <init>(...);
#}
#-keepclassmembers class ** {
#    @androidx.lifecycle.OnLifecycleEvent *;
#}
## this rule is need to work properly when app is compiled with api 28, see b/142778206
## Also this rule prevents registerIn from being inlined.
#-keepclassmembers class androidx.lifecycle.ReportFragment$LifecycleCallbacks { *; }
#
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#-dontwarn com.squareup.okhttp.**
#-dontwarn rx.**
#-dontwarn retrofit.**
#-keep class retrofit.** { *; }
##your package path where your gson models are stored
#-keep class com.example.models.** { *; }


