package com.bao.learn.script.groovy

import org.codehaus.groovy.control.CompilerConfiguration
import org.junit.jupiter.api.Test

class TestMain {
    @Test
    void eval() {
        assert Eval.me('33*3') == 99
        assert Eval.me('"foo".toUpperCase()') == 'FOO'

        // Simple evaluation with one bound parameter named x
        assert Eval.x(4, '2*x') == 8
        // Same evaluation, with a custom bound parameter named k
        assert Eval.me('k', 4, '2*k') == 8
        // Simple evaluation with two bound parameters named x and y
        assert Eval.xy(4, 5, 'x*y') == 20
        // Simple evaluation with two bound parameters named x and y
        assert Eval.xyz(4, 5, 6, 'x*y+z') == 26
    }

    @Test
    void "GroovyShell Multiple sources"() {
        GroovyShell shell = new GroovyShell() // create a new GroovyShell instance
        Object result = shell.evaluate('3*5') // can be used as Eval with direct execution of the code
        Object result2 = shell.evaluate(new StringReader('3*5'))
        // can read from multiple sources (String, Reader, File, InputStream)
        assert result == result2
        Script script = shell.parse('3*5') // an defer execution of the script. parse returns a Script instance
        assert script instanceof Script
        assert script.run() == 15 // Script defines a run method
    }

    @Test
    void "GroovyShell bindings_1 read from bindings"() {
        def sharedData = new Binding() // create a new Binding that will contain shared data
        def shell = new GroovyShell(sharedData) // create a GroovyShell using this shared data
        def now = new Date()
        sharedData.setProperty('text', 'I am shared data!') // add a string to the binding
        sharedData.setProperty('date', now) // add a date to the binding (you are not limited to simple types)

        String result = shell.evaluate('"At $date, $text"') // evaluate the script

        assert result == "At $now, I am shared data!"
    }

    @Test
    void "GroovyShell bindings_2 write to bindings"() {
        def sharedData = new Binding() // create a new Binding instance
        def shell = new GroovyShell(sharedData)

        shell.evaluate('foo=123')

        assert sharedData.getProperty('foo') == 123
    }

    @Test
    void "GroovyShell bindings_3 def overlap"() {
        def sharedData = new Binding()
        def shell = new GroovyShell(sharedData)

        shell.evaluate('int foo=123') // foo = 123 则相当于 bingdings["foo"] = 123

        try {
            assert sharedData.getProperty('foo')
        } catch (MissingPropertyException e) {
            println "foo is defined as a local variable"
        }
    }

    @Test
    void "GroovyShell bindings_4 work around shared instance"() {
        def shell = new GroovyShell()

        def b1 = new Binding(x: 3)
        def b2 = new Binding(x: 4)
        def script = shell.parse('x = 2*x')
        script.binding = b1
        script.run()
        script.binding = b2
        script.run()
        assert b1.getProperty('x') == 6
        assert b2.getProperty('x') == 8
        assert b1 != b2
    }

    @Test
    void "GroovyShell bindings_5 thread safety"() {
        def shell = new GroovyShell()

        def b1 = new Binding(x: 3)
        def b2 = new Binding(x: 4)
        def script1 = shell.parse('x = 2*x') // create an instance of script for thread 1
        def script2 = shell.parse('x = 2*x') // create an instance of script for thread 2
        assert script1 != script2
        script1.binding = b1 // assign first binding to script 1
        script2.binding = b2 // assign second binding to script 2
        def t1 = Thread.start { script1.run() } // start first script in a separate thread
        def t2 = Thread.start { script2.run() } // start second script in a separate thread
        [t1, t2]*.join()
        assert b1.getProperty('x') == 6
        assert b2.getProperty('x') == 8
        assert b1 != b2
    }

    @Test
    void "GroovyShell bindings_6 Custom script class"() {
        def config = new CompilerConfiguration() // create a CompilerConfiguration instance
        config.scriptBaseClass = 'com.bao.learn.script.groovy.MyScript'
        // instruct it to use MyScript as the base class for scripts

        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        // then use the compiler configuration when you create the shell
        def script = shell.parse('greet()') // the script now has access to the new method greet
        assert script instanceof MyScript
        script.setName('Michel')
        assert script.run() == 'Hello, Michel!'
    }

    @Test
    void "GroovyClassLoader_1"() {
        def gcl = new GroovyClassLoader() // create a new GroovyClassLoader
        def clazz = gcl.parseClass('class Foo { void doIt() { println "ok" } }')
        // parseClass will return an instance of Class
        assert clazz.name == 'Foo'
        // you can check that the class which is returns is really the one defined in the script
        def o = clazz.newInstance() // and you can create a new instance of the class, which is not a script
        o.doIt() // then call any method on it
    }

    @Test
    void "GroovyClassLoader_2_1"() {
        def gcl = new GroovyClassLoader()
        def clazz1 = gcl.parseClass('class Foo { }')
        def clazz2 = gcl.parseClass('class Foo { }')
        assert clazz1.name == 'Foo'
        assert clazz2.name == 'Foo'
        assert clazz1 != clazz2
    }

    @Test
    void "GroovyClassLoader_2_2"() {
        def file = new File(getClass().getResource('/Foo.groovy').toURI())
        def gcl = new GroovyClassLoader()
        def clazz1 = gcl.parseClass(file)
        def clazz2 = gcl.parseClass(new File(file.absolutePath))
        assert clazz1.name == 'Foo'
        assert clazz2.name == 'Foo'
        assert clazz1 == clazz2
    }
}
