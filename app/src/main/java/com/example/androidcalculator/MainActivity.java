package com.example.androidcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTextView;
    private TextView backgroundResultTextView;
    private StringBuilder expression = new StringBuilder();
    private Calculator calculator;
    private String currentOperation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        backgroundResultTextView = findViewById(R.id.backgroundResultTextView);
        calculator = new Calculator();

        setupButtons();
    }

    private void setupButtons() {
        int[] buttonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnEquals, R.id.btnClear, R.id.btnDecimal, R.id.btnBackspace, R.id.btnPercent};

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "+":
            case "-":
            case "*":
            case "/":
                handleOperation(buttonText);
                break;
            case "=":
                calculateResult(true);
                break;
            case "C":
                clearCalculator();
                break;
            case "⌫":
                handleBackspace();
                break;
            case "%":
                handlePercent();
                break;
            default:
                updateDisplay(buttonText);
                break;
        }
    }

    private void handleOperation(String operation) {
        if (!expression.toString().isEmpty() && !isLastCharOperator()) {
            if (currentOperation.isEmpty()) {
                expression.append(" ").append(operation).append(" ");
            } else {
                calculateResult(false);
                expression.append(" ").append(operation).append(" ");
            }
            currentOperation = operation;
            resultTextView.setText(expression.toString());
        }
    }

    private void calculateResult(boolean isEquals) {
        if (!expression.toString().isEmpty() && !isLastCharOperator()) {
            try {
                String[] parts = expression.toString().split(" ");
                double result = Double.parseDouble(parts[0]);
                for (int i = 1; i < parts.length; i += 2) {
                    String op = parts[i];
                    double num = Double.parseDouble(parts[i + 1]);
                    switch (op) {
                        case "+":
                            result = calculator.add(result, num);
                            break;
                        case "-":
                            result = calculator.subtract(result, num);
                            break;
                        case "*":
                            result = calculator.multiply(result, num);
                            break;
                        case "/":
                            result = calculator.divide(result, num);
                            break;
                    }
                }
                if (isEquals) {
                    expression.append(" = ").append(result);
                    resultTextView.setText(expression.toString());
                    backgroundResultTextView.setText("");
                } else {
                    expression.setLength(0);
                    expression.append(result);
                    resultTextView.setText(expression.toString());
                }
            } catch (Exception e) {
                resultTextView.setText("Error");
                backgroundResultTextView.setText("");
            }
            currentOperation = "";
        }
    }

    private void clearCalculator() {
        expression.setLength(0);
        resultTextView.setText("0");
        backgroundResultTextView.setText("");
        currentOperation = "";
    }

    private void handleBackspace() {
        if (expression.length() > 0) {
            if (expression.charAt(expression.length() - 1) == ' ') {
                expression.setLength(expression.length() - 3);
            } else {
                expression.setLength(expression.length() - 1);
            }
            if (expression.length() == 0) {
                resultTextView.setText("0");
                backgroundResultTextView.setText("");
            } else {
                resultTextView.setText(expression.toString());
                updateBackgroundResult();
            }
        }
    }

    private void handlePercent() {
        if (!expression.toString().isEmpty() && !isLastCharOperator()) {
            try {
                double value = Double.parseDouble(expression.toString());
                value /= 100;
                expression.setLength(0);
                expression.append(value);
                resultTextView.setText(expression.toString());
                updateBackgroundResult();
            } catch (NumberFormatException e) {
                resultTextView.setText("Error");
                backgroundResultTextView.setText("");
            }
        }
    }

    private void updateDisplay(String digit) {
        if (expression.toString().equals("0") && !digit.equals(".")) {
            expression.setLength(0);
        }
        expression.append(digit);
        resultTextView.setText(expression.toString());
        updateBackgroundResult();
    }

    private boolean isLastCharOperator() {
        if (expression.length() == 0) return false;
        char lastChar = expression.charAt(expression.length() - 1);
        return lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/';
    }

    private void updateBackgroundResult() {
        if (!expression.toString().isEmpty() && !isLastCharOperator()) {
            try {
                String[] parts = expression.toString().split(" ");
                if (parts.length >= 3) {
                    double result = Double.parseDouble(parts[0]);
                    for (int i = 1; i < parts.length; i += 2) {
                        String op = parts[i];
                        double num = Double.parseDouble(parts[i + 1]);
                        switch (op) {
                            case "+":
                                result = calculator.add(result, num);
                                break;
                            case "-":
                                result = calculator.subtract(result, num);
                                break;
                            case "*":
                                result = calculator.multiply(result, num);
                                break;
                            case "/":
                                result = calculator.divide(result, num);
                                break;
                        }
                    }
                    backgroundResultTextView.setText(String.valueOf(result));
                } else {
                    backgroundResultTextView.setText("");
                }
            } catch (Exception e) {
                backgroundResultTextView.setText("");
            }
        } else {
            backgroundResultTextView.setText("");
        }
    }
}