package com.guigo.lang.scanner;

public record Token(int line, TokenType type, String lexeme, Object literal) {

    @Override
    public String toString() {
        return "<" + type.toString() + ", " + lexeme + ", " + literal + ">";
    }
}
