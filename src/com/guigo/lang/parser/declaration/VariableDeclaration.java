package com.guigo.lang.parser.declaration;

import com.guigo.lang.parser.expression.Expression;
import com.guigo.lang.parser.statement.StatementVisitor;
import com.guigo.lang.scanner.Token;

public class VariableDeclaration extends Declaration {

    public final Token name;
    public final Expression expression;

    public VariableDeclaration(Token name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitVarDeclStatement(this);
    }
}
