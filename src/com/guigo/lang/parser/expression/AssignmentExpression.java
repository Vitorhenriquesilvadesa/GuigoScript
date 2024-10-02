package com.guigo.lang.parser.expression;

import com.guigo.lang.scanner.Token;

public class AssignmentExpression extends Expression {

    public final Token name;
    public final Expression value;

    public AssignmentExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAssignmentExpression(this);
    }
}
