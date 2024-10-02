package com.guigo.lang.parser.statement;

import java.util.List;

public class BodyStatement extends Statement {
    public final List<Statement> statements;

    public BodyStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitBodyStatement(this);
    }
}
