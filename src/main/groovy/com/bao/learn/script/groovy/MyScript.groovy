package com.bao.learn.script.groovy

abstract class MyScript extends Script {
    String name

    String greet() {
        "Hello, $name!"
    }
}
