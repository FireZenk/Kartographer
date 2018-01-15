# Kartographer

An Android routing library for Kotlin based on Naviganto and heavily inspired in HTTP protocol url schema; also, do you remember the name of that _Forerunner facility_? yeah

[![Build Status](https://travis-ci.org/FireZenk/Kartographer.svg?branch=develop)](https://travis-ci.org/FireZenk/Kartographer)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/FireZenk/Kartographer/issues)

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

  def NVersion = '0.7.6'
  implementation "org.firezenk.kartographer:annotations:$NVersion"
  implementation "org.firezenk.kartographer:animations:$NVersion@aar" //android only
  implementation "org.firezenk.kartographer:library:$NVersion"
  implementation "org.firezenk.kartographer:processor:$NVersion"
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

*Kartographer* needs keep track of the `Context`, so make sure to inject it with the initialisation like:
```kotlin
val monitor = ContextMonitor()
application.registerActivityLifecycleCallbacks(monitor)
Kartographer(application, monitor).debug()
```

###### 1. Route between targets

- Move to a new route:
```kotlin
kartographer next route {
    target = ViewRoute::class
    params = mapOf("key" to value, ...)
    anchor = parentViewGroup
    animation = CrossFade() //optional
}
```
- Back to the prev route, by overriding your Activity's onBackPressed method:
```kotlin
kartographer backOnPath {
    super.onBackPressed()
}
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
@RoutableActivity(path = ..., requestCode = ...)
class SampleActivity : AppCompatActivity{...}

// or

@RoutableView(path = ..., requestCode = ...)
class SomeView : FrameLayout{...}
```

all parameters are totally optional, another way to create routes is create your own custom routes inheriting from `Routable.class`

- `path` defines the context of the navigation flow (but if you've a lineal navigation then, you don't need it).
- `requestCode` in case you need to receive a response into `onActivityResult` this will be your request code number.


###### 3. Retrieve route params

On every view you will have all the route params simply calling the `payload` method:

```kotlin
val myParam: Int? = kartographer.payload<Int>("myParam")
```

### CUSTOMISATION

Kartographer has 3 predefined transitions between screens (also the transition is totally optional), but you can easily create your own by inheriting from `RouteAnimation.class`

### ADDITIONAL NOTES

- No, it is not contemplated the use of fragments, although it is possible (see `Page2Route.kt` sample)
- I recommend to use auto-routes because is safe, saves coding time and you can avoid to use `Parcelables`
- User `.clearHistory()` if you need to clear all the navigation history
- You can find all the available functions documented  [here](https://github.com/FireZenk/Kartographer/blob/develop/library/src/main/java/org/firezenk/kartographer/library/IKartographer.kt)
- For more info an samples, see `sample` module

### CHANGES

[See CHANGES.md](https://github.com/FireZenk/Kartographer/blob/develop/CHANGES.md)
