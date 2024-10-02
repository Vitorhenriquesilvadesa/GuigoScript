package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class GroupExpression extends Expression {

    public final Token paren;
    public final Expression expression;

    public GroupExpression(Token paren, Expression expression) {
        this.paren = paren;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Group(" + expression.toString() + ")";
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGroupExpression(this);
    }
}
