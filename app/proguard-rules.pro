#####################
# Kotlin & Core
#####################

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.** { *; }
-dontwarn kotlin.**

# Keep data classes and lambda classes
-keepclassmembers class ** {
    @kotlin.Metadata *;
}
-dontwarn kotlin.Unit

#####################
# Jetpack Compose
#####################

-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keep class androidx.activity.ComponentActivity
-keep class androidx.compose.ui.tooling.preview.PreviewParameterProvider { *; }

# Prevent Compose compiler-generated class removal
-keep class androidx.compose.runtime.** { *; }

# ViewModel and Lifecycle
-keepclassmembers class androidx.lifecycle.ViewModel { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**

#####################
# Retrofit & Gson
#####################

# Keep Retrofit interfaces
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep models for Gson (de/serialization)
-keep class com.izanhuang.cafe_hunter_android.core.data.** { *; }

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep Gson type adapters
-keepattributes Signature
-keepattributes *Annotation*

#####################
# Google Play Services / Maps / Location
#####################

-keep class com.google.android.gms.maps.** { *; }
-dontwarn com.google.android.gms.maps.**

-keep class com.google.android.gms.location.** { *; }
-dontwarn com.google.android.gms.location.**

#####################
# Firebase
#####################

# Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Firestore, Auth, Storage
-keep class com.google.firebase.firestore.** { *; }
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.firebase.storage.** { *; }
-dontwarn com.google.firebase.**

# FirebaseUI
-dontwarn com.firebase.ui.**
-keep class com.firebase.ui.auth.** { *; }

#####################
# Hilt / Dagger
#####################

-keep class dagger.** { *; }
-dontwarn dagger.**

-keep class javax.inject.** { *; }
-dontwarn javax.inject.**

# Hilt components
-keep class com.izanhuang.cafe_hunter_android.**_HiltComponents { *; }

# Keep generated Hilt code
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-dontwarn dagger.hilt.internal.GeneratedComponent

#####################
# Coil (image loading)
#####################

-keep class coil.** { *; }
-dontwarn coil.**

#####################
# Accompanist
#####################

-dontwarn com.google.accompanist.**
-keep class com.google.accompanist.** { *; }

#####################
# Miscellaneous
#####################

# Keep anything that extends Application
-keep class ** extends android.app.Application { *; }

# Prevent issues with reflection on constructors
-keepclassmembers class * {
    public <init>(...);
}

# Avoid warnings for missing classes from test libraries
-dontwarn junit.framework.**
-dontwarn org.junit.**

