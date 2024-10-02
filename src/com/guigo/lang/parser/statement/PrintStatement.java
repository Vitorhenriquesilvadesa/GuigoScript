package com.guigo.lang.parser.statement;

import com.guigo.lang.parser.expression.Expression;
import com.guigo.lang.scanner.Token;

public class PrintStatement extends Statement {

    public final Token paren;
    public final Expression expression;

    public PrintStatement(Token paren, Expression expression) {
        this.paren = paren;
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitPrintStatement(this);
    }
}
