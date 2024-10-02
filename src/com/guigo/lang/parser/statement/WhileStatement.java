package com.guigo.lang.parser.statement;

import com.guigo.lang.parser.expression.Expression;

public class WhileStatement extends Statement {
    public final Expression condition;
    public final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitWhileStatement(this);
    }
}
