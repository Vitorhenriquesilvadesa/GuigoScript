package com.guigo.lang.parser.statement;

import com.guigo.lang.parser.expression.Expression;

public class ExpressionStatement extends Statement {

    public final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitExpressionStatement(this);
    }
}
