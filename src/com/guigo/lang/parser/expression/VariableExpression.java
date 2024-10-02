package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class VariableExpression extends Expression {

    public final Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitVariableExpression(this);
    }
}
