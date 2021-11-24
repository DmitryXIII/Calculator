package com.ineedyourcode.calculator.presenter;

import com.ineedyourcode.calculator.view.CalculatorView;

import java.util.HashMap;

public class CalculatorPresenter {
    private Calculator calculator;
    private CalculatorView view;
    private HashMap<String, String> mapTextValues = new HashMap<>();
    private HashMap<String, Integer> mapVisibilityValues = new HashMap<>();
    private Double argOne = 0.0;
    private Double argTwo = null;
    private Double result = null;
    private ArithmeticOperation previousOperation = null;
    private boolean isDotLast = false; // режим ввода цифр после запятой
    private boolean isFirstAfterDot = false; // вводимая цифра - первая после запятой
    private boolean isOperandLast = false; // режим, когда последней нажат операнд
    private boolean isEnterLast = false; // режим, когда последней нажата клавиша "="

    public CalculatorPresenter(CalculatorView view, Calculator calculator) {
        this.view = view;
        this.calculator = calculator;
    }

    public void onDigitsPressed(int digit) {
        if (isEnterLast) {
            clearAll();
            argOne = argOne * 10 + digit;
            view.showResults(showResult(argOne), setVisibility());
        } else if (previousOperation != null) {
            isOperandLast = false;
            if (!isDotLast) {
                argTwo = argTwo * 10 + digit;
            } else {
                argTwo = typeAfterDot(argTwo, digit);
            }
            view.showResults(showResult(argTwo), setVisibility());
        } else {
            isOperandLast = false;
            if (!isDotLast) {
                argOne = argOne * 10 + digit;
            } else {
                argOne = typeAfterDot(argOne, digit);
            }
            view.showResults(showResult(argOne), setVisibility());

        }
    }

    public void onArithmeticOperandsPressed(ArithmeticOperation operation) {
        if (isEnterLast) {
            isOperandLast = true;
            isEnterLast = false;
            argOne = result;
            argTwo = 0.0;
            view.showResults(showResult(result), setVisibility());
        }

        if (isOperandLast) {
            previousOperation = operation;
            view.showResults(showResult(argOne), setVisibility());
        }

        if (argTwo != null && !isOperandLast) {
            isOperandLast = true;
            isDotLast = false;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            if (argTwo == 0 && previousOperation == ArithmeticOperation.DIVISION) {
                view.showResults("деление на ноль");
                return;
            } else {
                argOne = result;
                argTwo = 0.0;
                view.showResults(showResult(result), setVisibility());
            }
        } else if (!isOperandLast) {
            isOperandLast = true;
            isDotLast = false;
            argTwo = 0.0;
            previousOperation = operation;
            view.showResults(showResult(argOne), setVisibility());
        }
        previousOperation = operation;
    }

    public void onEnterPressed() {
        if (isEnterLast) {
            isDotLast = false;
            isOperandLast = false;
            argOne = result;
            result = calculator.arithmeticOperation(result, argTwo, previousOperation);
            view.showResults(showResult(result), setVisibility());
        } else if (argTwo != null) {
            isDotLast = false;
            isOperandLast = false;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            isEnterLast = true;
            if (argTwo == 0 && previousOperation == ArithmeticOperation.DIVISION) {
                view.showResults("деление на ноль");
                isEnterLast = false;
            } else {
                view.showResults(showResult(result), setVisibility());
            }
        }
    }

    public void onClearPressed() {
        if (argTwo != null && !isEnterLast && !isOperandLast) {
            argTwo = 0.0;
            isDotLast = false;
            isFirstAfterDot = false;
            view.showResults(showResult(argTwo), setVisibility());
        } else if (!isEnterLast && !isOperandLast) {
            argOne = 0.0;
            isDotLast = false;
            isFirstAfterDot = false;
            view.showResults(showResult(argOne), setVisibility());
        } else if (isEnterLast) {
            clearAll();
        }
    }

    public void onDotPressed() {
        if (!isEnterLast && !isOperandLast) {
            isDotLast = true;
            isFirstAfterDot = true;
        }
    }

    // метод ввода цифр после запятой
    private double typeAfterDot(Double arg, int digit) {
        if (isFirstAfterDot) {
            arg = arg + digit / 10.0;
            isFirstAfterDot = false;
        } else {
            String value = arg + String.valueOf(digit);
            arg = Double.parseDouble(value);
        }
        return arg;
    }

    public void onClearLongPressed() {
        clearAll();
    }

    private void clearAll() {
        isEnterLast = false;
        isDotLast = false;
        isOperandLast = false;
        isFirstAfterDot = false;
        previousOperation = null;
        argOne = 0.0;
        argTwo = null;
        result = null;
        view.showResults(showResult(0.0), setVisibility());
    }

    public HashMap<String, String> showResult(Double doubleValue) {
        mapTextValues.put("txtDisplay", showLongOrDouble(doubleValue));
        mapTextValues.put("txtArgOne", showLongOrDouble(argOne));

        if (isEnterLast) {
            mapTextValues.put("txtEnter", "=");
            mapVisibilityValues.put("txtEnter", 1);
        } else {
            mapVisibilityValues.put("txtEnter", 0);
        }

        if (previousOperation != null) {
            switch (previousOperation) {
                case PLUS:
                    mapTextValues.put("txtOperand", "+");
                    break;
                case MINUS:
                    mapTextValues.put("txtOperand", "-");
                    break;
                case MULTIPLY:
                    mapTextValues.put("txtOperand", "*");
                    break;
                case DIVISION:
                    mapTextValues.put("txtOperand", "/");
                    break;
            }
            mapVisibilityValues.put("txtOperand", 1);
        } else {
            mapVisibilityValues.put("txtOperand", 0);
        }

        if (argTwo != null) {
            mapVisibilityValues.put("txtArgTwo", 1);
        } else {
            mapVisibilityValues.put("txtArgTwo", 0);
        }
        if (argTwo == null) {
            mapTextValues.put("txtArgTwo", "");
        } else {
            mapTextValues.put("txtArgTwo", showLongOrDouble(argTwo));
        }
        return mapTextValues;
    }

    private HashMap<String, Integer> setVisibility() {
        return mapVisibilityValues;
    }

    private String showLongOrDouble(Double doubleValue) {
        long longValue;
        double dValue = doubleValue;
        if (doubleValue == null) {
            return String.valueOf(0);
        } else if (doubleValue % 1 == 0) {
            longValue = (long) dValue;
            return String.valueOf(longValue);
        } else {
            return String.valueOf(doubleValue);
        }
    }
}