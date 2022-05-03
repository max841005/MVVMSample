# 指定程式碼的壓縮級別 0 - 7(指定程式碼進行迭代優化的次數，在Android裡面預設是5，這條指令也只有在可以優化時起作用。)
-optimizationpasses 5
# 混淆時不會產生形形色色的類名(混淆時不使用大小寫混合類名)
-dontusemixedcaseclassnames
#不進行優化，建議使用此選項，
-dontoptimize
# 遮蔽警告
-ignorewarnings
# 指定混淆是採用的演算法，後面的引數是一個過濾器
# 這個過濾器是谷歌推薦的演算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保護程式碼中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 這在JSON實體對映時非常重要
-keepattributes Signature
# 丟擲異常時保留程式碼行號
-keepattributes SourceFile,LineNumberTable
 #優化時允許訪問並修改有修飾符的類和類的成員，這可以提高優化步驟的結果。
# 比如，當內聯一個公共的getter方法時，這也可能需要外地公共訪問。
# 雖然java二進位制規範不需要這個，要不然有的虛擬機器處理這些程式碼會有問題。當有優化和使用-repackageclasses時才適用。
#指示語：不能用這個指令處理庫中的程式碼，因為有的類和類成員沒有設計成public ,而在api中可能變成public
-allowaccessmodification
#當有優化和使用-repackageclasses時才適用。
-repackageclasses
 # 混淆時記錄日誌(列印混淆的詳細資訊)
 # 這句話能夠使我們的專案混淆後產生對映檔案
 # 包含有類名->混淆後類名的對映關係
-verbose
# ----------------------------- 預設保留 -----------------------------
# 保持哪些類不被混淆
#繼承activity,application,service,broadcastReceiver,contentprovider....不進行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

#表示不混淆上面宣告的類，最後這兩個類我們基本也用不上，是接入Google原生的一些服務時使用的。
#----------------------------------------------------

#表示不混淆任何包含native方法的類的類名以及native方法名，這個和我們剛才驗證的結果是一致
-keepclasseswithmembernames class * {
    native <methods>;
}

#這個主要是在layout 中寫的onclick方法android:onclick="onClick"，不進行混淆
#表示不混淆Activity中引數是View的方法，因為有這樣一種用法，在XML中配置android:onClick=”buttonClick”屬性，
#當用戶點選該按鈕時就會呼叫Activity中的buttonClick(View view)方法，如果這個方法被混淆的話就找不到了
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#表示不混淆列舉中的values()和valueOf()方法，列舉我用的非常少，這個就不評論了
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#表示不混淆任何一個View中的setXxx()和getXxx()方法，
#因為屬性動畫需要有相應的setter和getter的方法實現，混淆了就無法工作了。
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#表示不混淆Parcelable實現類中的CREATOR欄位，
#毫無疑問，CREATOR欄位是絕對不能改變的，包括大小寫都不能變，不然整個Parcelable工作機制都會失敗。
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 這指定了繼承Serizalizable的類的如下成員不被移除混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留R下面的資源
#-keep class **.R$* {
# *;
#}
#不混淆資源類下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 對於帶有回撥函式的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 保留我們自定義控制元件（繼承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#
#----------------------------- WebView(專案中沒有可以忽略) -----------------------------
#
#webView需要進行特殊處理
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, jav.lang.String);
#}
##在app中與HTML5的JavaScript的互動進行特殊處理
##我們需要確保這些js要呼叫的原生方法不能夠被混淆，於是我們需要做如下處理：
#-keepclassmembers class com.ljd.example.JSInterface {
#    <methods>;
#}

#
#---------------------------------實體類---------------------------
#--------(實體Model不能混淆，否則找不到對應的屬性獲取不到值)-----
#
-dontwarn com.suchengkeji.android.confusiondemo.md.**
#對含有反射類的處理
#
# ----------------------------- 其他的 -----------------------------
#
# 刪除程式碼中Log相關的程式碼
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 保持測試相關的程式碼
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
#
# ----------------------------- 第三方庫、框架、SDK -----------------------------
#

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# This is generated automatically by the Android Gradle plugin.
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# CoordinatorLayout resolves the behaviors of its child components with reflection.
-keep public class * extends androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>();
}

# Make sure we keep annotations for CoordinatorLayout's DefaultBehavior
-keepattributes RuntimeVisible*Annotation*

# Kodein
-keep, allowobfuscation, allowoptimization class org.kodein.type.TypeReference
-keep, allowobfuscation, allowoptimization class org.kodein.type.JVMAbstractTypeToken$Companion$WrappingTest
-keep, allowobfuscation, allowoptimization class * extends org.kodein.type.TypeReference
-keep, allowobfuscation, allowoptimization class * extends org.kodein.type.JVMAbstractTypeToken$Companion$WrappingTest