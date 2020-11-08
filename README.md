# StateMachine

[![](https://jitpack.io/v/genaku/StateMachine.svg)](https://jitpack.io/#genaku/StateMachine)

Declarative way of creating state machine.



## Dependency

Step 1. Add the JitPack repository to your build file

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
  
Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.genaku:StateMachine:Tag'
}
```

## Usage

```
stateMachine {
    mappings(
        Heat moves Ice to Liquid,
        Heat moves Liquid to Steam,
        Heat moves Steam to Steam,

        Chill moves Steam to Liquid,
        Chill moves Liquid to Ice,
        Chill moves Ice to Ice,

        Drink moves Liquid to Empty,
        Fill moves Empty to Liquid
    )
    initialState = Empty
}
```
or
```
stateMachine {
    mappings(
        Ice by Heat goesTo Liquid,
        Ice by Chill goesTo Ice,

        Liquid by Heat goesTo Steam,
        Liquid by Chill goesTo Ice,
        Liquid by Drink goesTo Empty,

        Steam by Heat goesTo Steam,
        Steam by Chill goesTo Liquid,

        Empty by Fill goesTo Liquid
    )
    initialState = Empty
}
```

## License
```
 Copyright 2020, Gena Kuchergin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```