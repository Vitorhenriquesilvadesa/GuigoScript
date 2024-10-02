package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class BinaryExpression extends Expression {

    public final Expression left;
    public final Token operator;
    public final Expression right;

    public BinaryExpression(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Binary(" + left.toString() + ", " + operator.type() + ", " + right.toString() + ")";
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitBinaryExpression(this);
    }
}
