package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class LiteralExpression extends Expression {

    public final Token literal;

    public LiteralExpression(Token literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return "Literal(" + literal.literal().toString() + ", type: " + literal.literal().getClass().getSimpleName() + ")";
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteralExpression(this);
    }
}
