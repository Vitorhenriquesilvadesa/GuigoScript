package com.guigo.lang.parser;

import com.guigo.lang.parser.expression.Expression;
import com.guigo.lang.parser.statement.Statement;

import java.util.List;

public record ParsedData(List<Statement> expressions) {
}
