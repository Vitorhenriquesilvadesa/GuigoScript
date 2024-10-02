package com.guigo.lang.scanner;

import com.guigo.lang.GuigoLang;
import com.guigo.lang.error.GuigoErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guigo.lang.scanner.TokenType.*;

public class Scanner {

    int line;
    int start;
    int current;
    String source;
    List<Token> tokens;

    private static final Map<String, TokenType> symbols;
    private static final Map<String, Boolean> booleans;


    static {
        symbols = new HashMap<>();
        symbols.put("source", Source);
        symbols.put("target", Target);
        symbols.put("kernel", Kernel);
        symbols.put("return", Return);
        symbols.put("true", True);
        symbols.put("false", False);
        symbols.put("null", Null);
        symbols.put("or", Or);
        symbols.put("and", And);
        symbols.put("print", Print);
        symbols.put("if", If);
        symbols.put("else", Else);
        symbols.put("while", While);
        symbols.put("var", Var);
        
        booleans = new HashMap<>();
        booleans.put("true", true);
        booleans.put("false", false);
    }

    public ScannedData scanTokens(String source) {
        this.source = source;
        tokens = new ArrayList<>();
        line = 1;

        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        makeToken(TokenType.EOF);

        return new ScannedData(tokens);
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            case '\n':
                line++;
                break;

            case ' ':
            case '\t':
            case '\r':
                break;

            case '>':
                if (match('=')) {
                    makeToken(TokenType.GreaterEqual, ">=");
                } else {
                    makeToken(TokenType.Greater, ">");
                }

                break;

            case '<':
                if (match('=')) {
                    makeToken(LessEqual, "<=");
                } else {
                    makeToken(TokenType.Less, "<");
                }

                break;

            case '=':
                if (match('=')) {
                    makeToken(EqualEqual, "==");
                } else {
                    makeToken(TokenType.Equal, "=");
                }

                break;

            case '(':
                makeToken(LeftParen, "(");
                break;

            case ')':
                makeToken(RightParen, ")");
                break;

            case '}':
                makeToken(RightBrace, "}");
                break;


            case '{':
                makeToken(LeftBrace, "{");
                break;


            case '.':
                makeToken(Dot, ".");
                break;

            case ':':
                makeToken(Colon, ".");
                break;

            case '?':
                makeToken(Question, "?");
                break;


            case ',':
                makeToken(Comma, ",");
                break;


            case ';':
                makeToken(Semicolon, ";");
                break;

            case '!':
                if(match('=')) {
                    makeToken(NotEqual, "!=");
                } else {
                    makeToken(Mark, "!");
                }
                break;

            case '-':
                makeToken(Minus, "-");
                break;

            case '+':
                makeToken(Plus, "+");
                break;

            case '*':
                makeToken(Star, "*");
                break;

            case '"':
                string();
                break;

            case '/':
                if(match('/')) {
                    comment();
                    break;
                } else {
                    makeToken(Slash, "/");
                }

            default:
                if (isDigit(c)) {
                    number();
                    break;
                }

                if (isAlpha(c)) {
                    identifier();
                }

        }
    }

    private void comment() {
        while(!check('\n')) advance();
    }

    private void identifier() {

        while (isAlpha(peek()) || check('_')) {
            advance();
        }

        String name = source.substring(start, current);

        if(booleans.containsKey(name)) {
            makeToken(symbols.getOrDefault(name, TokenType.Identifier), name, booleans.get(name));
            return;
        }

        makeToken(symbols.getOrDefault(name, TokenType.Identifier), name);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (check('.')) {
            do {
                advance();
            } while (isDigit(peek()));
        }

        String lexeme = source.substring(start, current);
        Double number = Double.parseDouble(lexeme);
        makeToken(TokenType.Number, lexeme, number);
    }

    private void string() {

        do {
            advance();
        } while (!check('"') && !isAtEnd());

        if (isAtEnd()) {
            GuigoLang.error("Unterminated string at line " + line + ".", GuigoErrorCode.UnterminatedString);
        }

        advance();

        String literal = source.substring(start + 1, current - 1);
        makeToken(TokenType.String, literal, literal);
    }

    private boolean isAlpha(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    private boolean isAlphanumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean match(char c) {
        if (isAtEnd()) return false;
        if (!check(c)) return false;

        advance();
        return true;
    }

    private boolean check(char c) {
        if (isAtEnd()) return false;
        return peek() == c;
    }

    private char peek() {
        return source.charAt(current);
    }

    private void makeToken(TokenType type) {
        makeToken(type, null, null);
    }

    private void makeToken(TokenType type, Object literal) {
        String lexeme = source.substring(start, current);
        makeToken(type, lexeme, literal);
    }

    private void makeToken(TokenType type, String lexeme, Object literal) {
        tokens.add(new Token(line, type, lexeme, literal));
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
