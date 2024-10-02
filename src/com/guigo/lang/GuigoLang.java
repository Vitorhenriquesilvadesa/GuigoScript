package com.guigo.lang;

import com.guigo.lang.error.GuigoErrorCode;
import com.guigo.lang.parser.ParsedData;
import com.guigo.lang.parser.Parser;
import com.guigo.lang.parser.expression.Expression;
import com.guigo.lang.scanner.ScannedData;
import com.guigo.lang.scanner.Scanner;
import com.guigo.lang.scanner.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GuigoLang {

    public static void main(String[] args) {
        String source = extractContentFromFile(args[0]);
        Scanner scanner = new Scanner();
        ScannedData scannedData = scanner.scanTokens(source);

//        for(Token token : scannedData.tokens()) {
//            System.out.println(token.toString());
//        }

        Parser parser = new Parser();
        ParsedData parsedData = parser.parseTokens(scannedData);

        for(Expression expression : parsedData.expressions()) {
            System.out.println(expression.toString());
        }
    }

    public static String extractContentFromFile(String filepath) {
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));

            String line;

            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch(IOException e) {
            error(e.getMessage(), GuigoErrorCode.SourceFileRead);
        }

        return builder.toString();
    }

    public static void error(Token location, String message, GuigoErrorCode code) {
        System.err.println("Error at line " + location.line() + ": " + message);
        System.exit(code.ordinal());
    }

    public static void error(String message, GuigoErrorCode code) {
        System.err.println(message);
        System.exit(code.ordinal());
    }
}