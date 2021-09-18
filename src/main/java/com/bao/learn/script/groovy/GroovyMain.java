package com.bao.learn.script.groovy;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GroovyMain {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("groovy");

        Integer sum = (Integer) engine.eval("(1..10).sum()");
        System.out.println(sum);

        engine.put("first", "HELLO");
        engine.put("second", "world");
        String result = (String) engine.eval("first.toLowerCase() + ' ' + second.toUpperCase()");
        System.out.println(result);

        String fact = "def factorial(n) { n == 1 ? 1 : n * factorial(n - 1) }";
        engine.eval(fact);
        Invocable inv = (Invocable) engine;
        Object[] params = {5};
        Object factorial = inv.invokeFunction("factorial", params);
        System.out.println(factorial);
    }
}
