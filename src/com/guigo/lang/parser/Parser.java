package com.guigo.lang.parser;

import com.guigo.lang.GuigoLang;
import com.guigo.lang.error.GuigoErrorCode;
import com.guigo.lang.parser.expression.*;
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

        List<Expression> expressions = new ArrayList<>();
        scannedData = data;

        while(!isAtEnd()) {
            Expression expression = expression();
            expressions.add(expression);
        }

        return new ParsedData(expressions);
    }

    private Expression expression() {
        return term();
    }

    private Expression term() {
        Expression expression = factor();

        while(match(Plus, Minus)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression factor() {

        Expression expression = unary();

        while(match(Star, Slash)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression unary() {

        if(match(Minus, Mark)) {
            Token operator = previous();
            Expression left = unary();

            return new UnaryExpression(operator, left);
        }

        return literal();
    }

    private Expression literal() {
        if(match(String, Number, True, False, Null)) {
            return new LiteralExpression(previous());
        }

        if(match(LeftParen)) {
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
        if(isAtEnd()) return false;

        for(TokenType t : type) {
            if(peek().type() == t) {
                advance();
                return true;
            }
        }

        return false;
    }

    private void advance() {
        current++;
    }

    private boolean check(TokenType type) {
        return peek().type() == type;
    }

    private Token peek() {
        return scannedData.tokens().get(current);
    }

    private boolean isAtEnd() {
        return peek().type() == EOF;
    }
}
