package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class UnaryExpression extends Expression {

    public final Token operator;
    public final Expression expression;

    public UnaryExpression(Token operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Unary(" + operator.type() + ", " + expression.toString() + ")";
    }
}
