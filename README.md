# Kartographer

An Android routing library for Kotlin based on Naviganto and heavily inspired in HTTP protocol url schema; also, do you remember the name of that _Forerunner facility_? yeah

[![Build Status](https://travis-ci.org/FireZenk/Kartographer.svg?branch=develop)](https://travis-ci.org/FireZenk/Kartographer)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

### GRADLE:

```groovy
...
apply plugin: 'kotlin-kapt'

repositories {
  ...
  maven { url 'https://github.com/FireZenk/maven-repo/raw/master/'}
}

dependencies {
  ...
  compileOnly 'javax.annotation:javax.annotation-api:1.2'
  compileOnly 'com.squareup:kotlinpoet:0.5.0'

  def NVersion = '0.6.0'
  compileOnly "org.firezenk.kartographer:annotations:$NVersion"
  implementation "org.firezenk.kartographer:animations:$NVersion" //android only
  implementation "org.firezenk.kartographer:library:$NVersion"
  kapt "org.firezenk.kartographer:processor:$NVersion"
}
```

### DESCRIPTION:

_Kartographer_ consists of 5 main classes:
- `Kartographer` which is in charge of navigate between views (`Activity` or `View`).
- `Route` that contains the desired route.
- `@RoutableActivity` and `@RoutableView` to use auto-routes.
- `Routable` the interface that is implemented for each of our _custom_ `Route`s.

Additionally, two custom exceptions are provided for make the debugging easier:
- `ParameterNotFoundException` launched when not found a path parameter that we need.
- `NotEnoughParametersException` which is launched if the route has not received all the necessary parameters.

### USAGE

###### 0. Inject

*Kartographer* needs a `Context`, so make sure to inject it with the initilization Context.

###### 1. Route between targets

- Move to a new route:
```kotlin
kartographer next route<Any> {
    target = ViewRoute::class
    params = arrayOf()
    anchor = viewGroup
    animation = CrossFade() //optional
}
```
- Back to the prev route:
```kotlin
kartographer back {}
```
- Return to last known route:
```kotlin
kartographer last(placeholder)
```
- Replay the last route on a path:
```kotlin
kartographer replay(path)
```

###### 2. Mark the target view as Route

```kotlin
@RoutableActivity(path = ..., params = {...}, requestCode = ...)
class SampleActivity : AppCompatActivity{...}

// or

@RoutableView(path = ..., params = {...}, requestCode = ...)
class SomeView : FrameLayout{...}
```

all parameters are totally optional, another way to create routes is create your own custom routes inheriting from `Routable.class`

- `path` defines the context of the navigation flow (but if you've a lineal navigation then, yagni).
- `params` an `arrayOf` parameters you need to pass to the next screen (like Android's Bundle type), ex: `arrayOf(Int::class, Float:class)`.
- `requestCode` in case you need to receive a response into `onActivityResult` this will be your request code number.

### ADDITIONAL NOTES

- No, it is not contemplated the use of fragments, although it is possible (using `View` sample)
- I recommend to use auto-routes because is safe, saves coding time and you can avoid to use `Parcelables`
- User `.clearHistory()` if you need to clear all the navigation history
- There is some more self documented [functions here](https://github.com/FireZenk/Kartographer/blob/develop/library/src/main/java/org/firezenk/kartographer/library/IKartographer.kt)
- For more info an samples, see `sample` module

### CHANGES

[See CHANGES.md](https://github.com/FireZenk/Kartographer/blob/develop/CHANGES.md)
