# Keep protocol models, wire codes and commands intact
-keep class com.realbuds.app.proto.** { *; }
-keepclassmembers class com.realbuds.app.proto.** { *; }

# Keep UI state models and ViewModels
-keep class com.realbuds.app.ui.BudsState { *; }
-keep class com.realbuds.app.ui.ConnState { *; }
-keep class com.realbuds.app.ui.DeviceEntry { *; }
-keep class com.realbuds.app.ui.Tab { *; }
-keep class com.realbuds.app.ui.ThemeMode { *; }
-keep class com.realbuds.app.ui.BudsViewModel { *; }

# Keep data classes and properties
-keepclassmembers class * {
    ** copy*(...);
    ** component*(...);
}

# Keep annotations, signatures and inner classes for Compose and Coroutines
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature
-keepclassmembers class * extends kotlinx.coroutines.** { *; }

# Keep Android Bluetooth classes and interfaces
-keep class android.bluetooth.** { *; }
-keep interface android.bluetooth.** { *; }

