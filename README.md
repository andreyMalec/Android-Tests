# EasySharedPref

EasySharedPref is a compile-time framework for Android that makes
SharedPreferences easy to use.

## How do I use EasySharedPref?
Simple use cases will look something like this:
```kotlin
@PrefEntity
data class User(
    val name: String,
    val age: Int
)

val userStorage: UserStorage = UserStorageImpl(context)
userStorage.name = "Bob"
Toast.makeText(context, userStorage.name, Toast.LENGTH_SHORT).show()
```
Annotating a class with `@PrefEntity` generates an interface that contains all the
class fields, and the `clear()` function:
```kotlin
interface UserStorage {
   var name: String

   var age: Int

   fun clear()
}
```
The implementation of this interface will be named UserStorageImpl.

## Install
EasySharedPref use [KSP](https://github.com/google/ksp) to process the annotations
* In the projects `settings.gradle`, add google() and gradlePluginPortal() to repositories for the KSP plugin:
```groovy
pluginManagement {
   repositories {
      gradlePluginPortal()
      google()
   }
}
  ```
* In your projects `build.gradle` file add a plugins block containing the ksp plugin:
```groovy
plugins {
   id "com.google.devtools.ksp" version "1.5.0-1.0.0-alpha10"
}
```
* In the modules `build.gradle`, add the following:
   * Apply the `com.google.devtools.ksp` plugin:
   ```groovy
   apply plugin: 'com.google.devtools.ksp'
   ```

   * Add `ksp EasySharedPref` to the list of dependencies.
   ```groovy
   dependencies {
      implementation project(path: ':EasySharedPref')
      ksp project(path: ':EasySharedPref')
   }
   ```

## Make IDE Aware Of Generated Code
By default, IntelliJ or other IDEs don't know about the generated code and therefore
references to those generated symbols will be marked unresolvable.
To make, for example, IntelliJ be able to reason about the generated symbols,
the following paths need to be marked as generated source root:
```
build/generated/ksp/main/kotlin/
build/generated/ksp/main/java/
```
