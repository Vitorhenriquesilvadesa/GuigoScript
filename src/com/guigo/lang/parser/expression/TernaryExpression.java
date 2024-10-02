package com.guigo.lang.parser.expression;

public class TernaryExpression extends Expression {

    // public final Token operator;

    public final Expression condition;
    public final Expression trueBranch;
    public final Expression falseBranch;

    public TernaryExpression(Expression condition, Expression trueBranch, Expression falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitTernaryExpression(this);
    }
}
