package com.guigo.lang.parser;

import com.guigo.lang.parser.expression.Expression;

import java.util.List;

public record ParsedData(List<Expression> expressions) {
}
