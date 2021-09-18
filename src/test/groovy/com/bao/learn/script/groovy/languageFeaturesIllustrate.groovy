package com.bao.learn.script.groovy

import org.junit.jupiter.api.Test

class languageFeaturesIllustrate {
    @Test
    void test() {
        def col = ["123", "12"].collect { it.length() }
        println col
        def star = ["123", "12"]*.length()
        println star
    }
}
