package com.guigo.lang.parser.statement;

import com.guigo.lang.parser.declaration.VariableDeclaration;

public interface StatementVisitor<T> {

    T visitPrintStatement(PrintStatement statement);

    T visitIfStatement(IfStatement ifStatement);

    T visitBodyStatement(BodyStatement bodyStatement);

    T visitWhileStatement(WhileStatement whileStatement);

    T visitVarDeclStatement(VariableDeclaration variableDeclaration);

    T visitExpressionStatement(ExpressionStatement expressionStatement);
}
