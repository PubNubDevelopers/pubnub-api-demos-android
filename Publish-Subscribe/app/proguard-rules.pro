-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

-keep class com.google.android.gms.ads.identifier.** { *; }

# joda time
-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**

# jackson
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.**



# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Pubnub
-dontwarn com.pubnub.**
-keep class com.pubnub.** { *; }


-dontwarn org.slf4j.**