package com.guigo.lang.parser.expression;

public interface ExpressionVisitor<T> {

    T visitBinaryExpression(BinaryExpression expression);

    T visitUnaryExpression(UnaryExpression expression);

    T visitLiteralExpression(LiteralExpression expression);

    T visitGroupExpression(GroupExpression expression);

    T visitTernaryExpression(TernaryExpression ternaryExpression);

    T visitAssignmentExpression(AssignmentExpression assignmentExpression);

    T visitVariableExpression(VariableExpression variableExpression);
}
