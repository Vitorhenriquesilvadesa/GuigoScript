package com.guigo.lang.parser.statement;

import com.guigo.lang.parser.expression.Expression;

public class IfStatement extends Statement {

    public final Expression condition;
    public final Statement thenBranch;
    public final Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitIfStatement(this);
    }
}
