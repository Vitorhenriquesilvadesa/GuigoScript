package com.guigo.lang.interpreter;

import com.guigo.lang.GuigoLang;
import com.guigo.lang.error.GuigoErrorCode;
import com.guigo.lang.parser.ParsedData;
import com.guigo.lang.parser.declaration.VariableDeclaration;
import com.guigo.lang.parser.expression.*;
import com.guigo.lang.parser.statement.*;
import com.guigo.lang.scanner.Token;
import com.guigo.lang.scanner.TokenType;
import static com.guigo.lang.scanner.TokenType.*;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Object> {

    private Environment current;
    private Environment previous = null;
    private int depth = 1;

    public Interpreter() {
        current = new Environment("globals");
    }

    public void interpret(ParsedData data) {

        for (Statement statement : data.expressions()) {
            execute(statement);
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        switch (expression.operator.type()) {
            case Plus:
                checkNumberOperands(left, right);
                return (Double) left + (Double) right;

            case Minus:
                checkNumberOperands(left, right);
                return (Double) left - (Double) right;

            case Star:
                checkNumberOperands(left, right);
                return (Double) left * (Double) right;

            case Slash:
                checkNumberOperands(left, right);
                return (Double) left / (Double) right;

            case Less:
                checkNumberOperands(left, right);
                return (Double) left < (Double) right;

            case LessEqual:
                checkNumberOperands(left, right);
                return (Double) left <= (Double) right;

            case Greater:
                checkNumberOperands(left, right);
                return (Double) left > (Double) right;

            case GreaterEqual:
                checkNumberOperands(left, right);

                return (Double) left >= (Double) right;

            case EqualEqual:
                return isObjectsEqual(left, right);

            case NotEqual:
                return !isObjectsEqual(left, right);

            case Or:
                return isTruthy(left) || isTruthy(right);

            case And:
                return isTruthy(left) && isTruthy(right);

            default:
                // Unreachable
                GuigoLang.error(expression.operator, "Invalid binary expression operator '" + expression.operator.lexeme() + "'.", GuigoErrorCode.InvalidExpression);
                return null;
        }
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression expression) {
        TokenType operator = expression.operator.type();
        Object right = evaluate(expression.expression);

        switch (operator) {
            case Minus: {
                if (right instanceof Double) {
                    return -((Double) right);
                } else {
                    GuigoLang.error(expression.operator, "Operand must be number", GuigoErrorCode.InvalidExpression);
                }
            }

            case Mark:
                break;
        }

        return null;
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression expression) {
        if(expression.literal.type() == Identifier) {
            return getVariable(expression.literal);
        }
        return expression.literal.literal();
    }

    @Override
    public Object visitGroupExpression(GroupExpression expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitTernaryExpression(TernaryExpression expression) {
        Object condition = evaluate(expression.condition);

        if (isTruthy(condition)) return evaluate(expression.trueBranch);

        return evaluate(expression.falseBranch);
    }

    @Override
    public Object visitAssignmentExpression(AssignmentExpression assignmentExpression) {
        Object value = evaluate(assignmentExpression.value);
        Token name = assignmentExpression.name;

        if(current.isDefined(name)) {
            current.set(name, value);
        }

        return null;
    }

    @Override
    public Object visitVariableExpression(VariableExpression variableExpression) {
        return getVariable(variableExpression.name);
    }

    private void checkNumberOperands(Object a, Object b) {
        if (!(a instanceof Double && b instanceof Double)) {
            GuigoLang.error("Operands must be numbers.", GuigoErrorCode.InvalidExpression);
        }
    }

    private boolean isTruthy(Object object) {
        if (object instanceof Boolean) return ((Boolean) object);
        if (object == null) return false;

        if (object instanceof String) return Boolean.parseBoolean((String) object);
        if (object instanceof Number) return ((Number) object).doubleValue() != 0;

        return true;
    }

    private boolean isObjectsEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    private String stringify(Object object) {
        if (object == null) return "null";
        return object.toString();
    }

    private Object getVariable(Token name) {
        return current.get(name);
    }

    private void beginScope() {
        depth++;
        previous = current;
        current = new Environment(previous, "local" + depth);
    }

    private void endScope() {
        depth--;
        current = previous;
        previous = current.enclosing;
    }

    @Override
    public Object visitPrintStatement(PrintStatement statement) {
        Object value = evaluate(statement.expression);

        System.out.println(stringify(value));

        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement) {
        Object condition = evaluate(ifStatement.condition);

        if (isTruthy(condition)) {
            execute(ifStatement.thenBranch);
        } else if (ifStatement.elseBranch != null) {
            execute(ifStatement.elseBranch);
        }

        return null;
    }

    @Override
    public Object visitBodyStatement(BodyStatement bodyStatement) {

        beginScope();

        for (Statement statement : bodyStatement.statements) {
            execute(statement);
        }

        endScope();

        return null;
    }

    @Override
    public Object visitWhileStatement(WhileStatement whileStatement) {
        while (isTruthy(evaluate(whileStatement.condition))) execute(whileStatement.body);
        return null;
    }

    @Override
    public Object visitVarDeclStatement(VariableDeclaration variableDeclaration) {
        if(current.isDefineCurrent(variableDeclaration.name)) {
            GuigoLang.error("Name '" + variableDeclaration.name.lexeme() + "' already defined.", GuigoErrorCode.DuplicatedName);
        }

        current.define(variableDeclaration.name.lexeme(), evaluate(variableDeclaration.expression));
        return null;
    }

    @Override
    public Object visitExpressionStatement(ExpressionStatement expressionStatement) {
        return evaluate(expressionStatement.expression);
    }
}
