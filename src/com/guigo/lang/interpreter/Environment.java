package com.guigo.lang.interpreter;

import com.guigo.lang.GuigoLang;
import com.guigo.lang.error.GuigoErrorCode;
import com.guigo.lang.scanner.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<String, Object> variables;
    public final Environment enclosing;
    public final String debugName;

    public Environment(Environment enclosing, String debugName) {
        this.enclosing = enclosing;
        this.debugName = debugName;
        variables = new HashMap<>();
    }

    public Environment(String debugName) {
        this.debugName = debugName;
        enclosing = null;
        variables = new HashMap<>();
    }

    public boolean isDefineCurrent(Token name) {
        return variables.containsKey(name.lexeme());
    }

    public boolean isDefined(Token name) {
        if (variables.containsKey(name.lexeme())) {
            return true;
        } else if (enclosing != null) {
            return enclosing.isDefined(name);
        } else {
            return false;
        }
    }

    public void define(String key, Object value) {
        variables.put(key, value);
    }

    public Object get(Token key) {
        if (variables.containsKey(key.lexeme())) {
            return variables.get(key.lexeme());
        } else if (enclosing != null) {
            return enclosing.get(key);
        } else {
            GuigoLang.error(key, "Undefined variable '" + key.lexeme() + "'.", GuigoErrorCode.UndefinedVariable);
            return null;
        }
    }

    public void set(Token name, Object value) {
        if (variables.containsKey(name.lexeme())) {
            variables.put(name.lexeme(), value);
        } else if (enclosing != null) {
            enclosing.set(name, value);
        } else {
            GuigoLang.error(name, "Undefined variable '" + name.lexeme() + "'.", GuigoErrorCode.UndefinedVariable);
        }
    }
}
