package com.guigo.lang.parser;

import com.guigo.lang.GuigoLang;
import com.guigo.lang.error.GuigoErrorCode;
import com.guigo.lang.parser.declaration.VariableDeclaration;
import com.guigo.lang.parser.expression.*;
import com.guigo.lang.parser.statement.*;
import com.guigo.lang.scanner.ScannedData;
import com.guigo.lang.scanner.Token;
import com.guigo.lang.scanner.TokenType;

import java.util.ArrayList;
import java.util.List;

import static com.guigo.lang.scanner.TokenType.*;

public class Parser {

    private int current;
    private ScannedData scannedData;

    public ParsedData parseTokens(ScannedData data) {

        List<Statement> statements = new ArrayList<>();
        scannedData = data;

        while (!isAtEnd()) {
            Statement statement = statement();
            statements.add(statement);
        }

        return new ParsedData(statements);
    }

    private Statement variableDeclaration() {
        consume(Identifier, "Expect identifier after 'var'.");
        Token name = previous();
        consume(Equal, "Expect '=' after variable name.");

        Expression expression = expression();

        consume(Semicolon, "Expect ';' after expression.");

        return new VariableDeclaration(name, expression);
    }

    private Statement statement() {
        if (match(Print)) return printStatement();
        if (match(If)) return ifStatement();
        if (match(LeftBrace)) return bodyStatement();
        if (match(While)) return whileStatement();
        if(match(Var)) return variableDeclaration();

        return expressionStatement();
    }

    private Statement expressionStatement() {
        Expression expression = expression();
        return new ExpressionStatement(expression);
    }

    private Statement whileStatement() {
        consume(LeftParen, "Expect '(' after while.");
        Expression expression = expression();
        consume(RightParen, "Expect ')' after while expression.");
        Statement statement = statement();

        return new WhileStatement(expression, statement);
    }

    private Statement bodyStatement() {
        List<Statement> statements = new ArrayList<>();
        while (!match(RightBrace)) {
            Statement statement = statement();
            statements.add(statement);
        }
        return new BodyStatement(statements);
    }

    private Statement ifStatement() {
        consume(LeftParen, "Expect '(' after 'if'.");
        Expression expression = expression();
        consume(RightParen, "Expect ')' after 'if' condition.");
        Statement thenBranch = statement();
        Statement elseBranch = null;

        if (match(Else)) {
            elseBranch = statement();
        }

        return new IfStatement(expression, thenBranch, elseBranch);
    }

    private Statement printStatement() {
        consume(LeftParen, "Expect '(' after 'print'.");
        Token paren = previous();
        Expression expression = expression();
        consume(RightParen, "Expect ')' after expression, but found '" + previous().type().toString() + "'.");
        consume(Semicolon, "Expect ';' after ')'.");
        return new PrintStatement(paren, expression);

    }

    private Expression expression() {
        return assignment();
    }

    private Expression assignment() {

        Expression expression = ternary();

        if(match(Equal)) {
            Expression value = assignment();
            Token name = ((VariableExpression)expression).name;

            consume(Semicolon, "Expect ';' after variable initializer.");

            return new AssignmentExpression(name, value);
        }

        return expression;
    }

    private Expression ternary() {
        Expression expression = and();

        if (match(Question)) {
            Expression trueBranch = ternary();
            consume(Colon, "Expect ':' after ternary true branch.");
            Expression falseBranch = ternary();
            expression = new TernaryExpression(expression, trueBranch, falseBranch);
        }

        return expression;
    }

    private Expression and() {
        Expression expression = or();

        while (match(And)) {
            Token operator = previous();
            Expression right = or();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression or() {
        Expression expression = equality();

        while (match(Or)) {
            Token operator = previous();
            Expression right = expression();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression equality() {
        Expression expression = comparison();

        while (match(EqualEqual, NotEqual)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression comparison() {
        Expression expression = term();

        if (match(Less, LessEqual, Greater, GreaterEqual)) {
            Token operator = previous();
            Expression right = term();
            return new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression term() {
        Expression expression = factor();

        while (match(Plus, Minus)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression factor() {

        Expression expression = unary();

        while (match(Star, Slash)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression unary() {

        if (match(Minus, Mark)) {
            Token operator = previous();
            Expression left = unary();

            return new UnaryExpression(operator, left);
        }

        return literal();
    }

    private Expression literal() {
        if (match(String, Number, True, False, Null)) {
            return new LiteralExpression(previous());
        }

        if(match(Identifier)) {
            return new VariableExpression(previous());
        }

        if (match(LeftParen)) {
            Token paren = previous();
            Expression expression = expression();
            consume(RightParen, "Expect ')' after group expression.");

            return new GroupExpression(paren, expression);
        }

        // Unreachable
        GuigoLang.error(previous(), "Invalid expression.", GuigoErrorCode.InvalidExpression);
        return null;
    }

    private void consume(TokenType type, String message) {
        if (!match(type)) {
            GuigoLang.error(previous(), message, GuigoErrorCode.InvalidExpression);
        }
    }

    private Token previous() {
        return scannedData.tokens().get(current - 1);
    }

    private boolean match(TokenType... type) {
        if (isAtEnd()) return false;

        for (TokenType t : type) {
            if (peek().type() == t) {
                advance();
                return true;
            }
        }

        return false;
    }

    private void advance() {
        current++;
    }

    private Token peek() {
        return scannedData.tokens().get(current);
    }

    private boolean isAtEnd() {
        return peek().type() == EOF;
    }
}
