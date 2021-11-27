package com.ineedyourcode.calculator.presenter;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.ineedyourcode.calculator.view.CalculatorView;

import java.util.HashMap;

public class CalculatorPresenter {
    private static final String KEY_STATE = "KEY_STATE";

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

    public void onSaveState(Bundle bundle) {
        bundle.putParcelable(KEY_STATE, new State(mapTextValues, mapVisibilityValues, argOne, argTwo, result, previousOperation, isDotLast, isFirstAfterDot, isOperandLast, isEnterLast));
    }

    public void restoreState(Bundle bundle) {
        State state = bundle.getParcelable(KEY_STATE);
        mapTextValues = state.mapTextValues;
        mapVisibilityValues = state.mapVisibilityValues;
        argOne = state.argOne;
        argTwo = state.argTwo;
        result = state.result;
        previousOperation = state.previousOperation;
        isDotLast = state.isDotLast;
        isFirstAfterDot = state.isFirstAfterDot;
        isOperandLast = state.isOperandLast;
        isEnterLast = state.isEnterLast;
    }

    public void onDigitsPressed(int digit) {
        if (isEnterLast) {
            clearAll();
            argOne = argOne * 10 + digit;
            view.showResult(sendResultToMain(argOne), setVisibility());
        } else if (previousOperation != null) {
            isOperandLast = false;
            if (!isDotLast) {
                argTwo = argTwo * 10 + digit;
            } else {
                argTwo = typeAfterDot(argTwo, digit);
            }
            view.showResult(sendResultToMain(argTwo), setVisibility());
        } else {
            isOperandLast = false;
            if (!isDotLast) {
                argOne = argOne * 10 + digit;
            } else {
                argOne = typeAfterDot(argOne, digit);
            }
            view.showResult(sendResultToMain(argOne), setVisibility());

        }
    }

    public void onArithmeticOperandsPressed(ArithmeticOperation operation) {
        if (isEnterLast) {
            isOperandLast = true;
            isEnterLast = false;
            argOne = result;
            argTwo = 0.0;
            view.showResult(sendResultToMain(result), setVisibility());
        }

        if (isOperandLast) {
            previousOperation = operation;
            view.showResult(sendResultToMain(argOne), setVisibility());
        }

        if (argTwo != null && !isOperandLast) {
            isOperandLast = true;
            isDotLast = false;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            if (argTwo == 0 && previousOperation == ArithmeticOperation.DIVISION) {
                view.showResult(sendResultToMain("деление на ноль"), setVisibility());
                return;
            } else {
                argOne = result;
                argTwo = 0.0;
                view.showResult(sendResultToMain(result), setVisibility());
            }
        } else if (!isOperandLast) {
            isOperandLast = true;
            isDotLast = false;
            argTwo = 0.0;
            previousOperation = operation;
            view.showResult(sendResultToMain(argOne), setVisibility());
        }
        previousOperation = operation;
    }

    public void onEnterPressed() {
        if (isEnterLast) {
            isDotLast = false;
            isOperandLast = false;
            argOne = result;
            result = calculator.arithmeticOperation(result, argTwo, previousOperation);
            view.showResult(sendResultToMain(result), setVisibility());
        } else if (argTwo != null) {
            isDotLast = false;
            isOperandLast = false;
            result = calculator.arithmeticOperation(argOne, argTwo, previousOperation);
            isEnterLast = true;
            if (argTwo == 0 && previousOperation == ArithmeticOperation.DIVISION) {
                view.showResult(sendResultToMain("деление на ноль"), setVisibility());
                isEnterLast = false;
            } else {
                view.showResult(sendResultToMain(result), setVisibility());
            }
        }
    }

    public void onClearPressed() {
        if (argTwo != null && !isEnterLast && !isOperandLast) {
            argTwo = 0.0;
            isDotLast = false;
            isFirstAfterDot = false;
            view.showResult(sendResultToMain(argTwo), setVisibility());
        } else if (!isEnterLast && !isOperandLast) {
            argOne = 0.0;
            isDotLast = false;
            isFirstAfterDot = false;
            view.showResult(sendResultToMain(argOne), setVisibility());
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
        view.showResult(sendResultToMain(0.0), setVisibility());
    }

    private HashMap<String, String> sendResultToMain(String message) {
        mapTextValues.put("txtDisplay", message);
        return mapTextValues;
    }


    private HashMap<String, String> sendResultToMain(Double doubleValue) {
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

    static class State implements Parcelable {
        private HashMap<String, String> mapTextValues = new HashMap<>();
        private HashMap<String, Integer> mapVisibilityValues = new HashMap<>();
        private Double argOne;
        private Double argTwo;
        private Double result;
        private ArithmeticOperation previousOperation;
        private boolean isDotLast;
        private boolean isFirstAfterDot;
        private boolean isOperandLast;
        private boolean isEnterLast;

        public State(HashMap<String, String> mapTextValues, HashMap<String, Integer> mapVisibilityValues, Double argOne, Double argTwo, Double result, ArithmeticOperation previousOperation, boolean isDotLast, boolean isFirstAfterDot, boolean isOperandLast, boolean isEnterLast) {
            this.mapTextValues = mapTextValues;
            this.mapVisibilityValues = mapVisibilityValues;
            this.argOne = argOne;
            this.argTwo = argTwo;
            this.result = result;
            this.previousOperation = previousOperation;
            this.isDotLast = isDotLast;
            this.isFirstAfterDot = isFirstAfterDot;
            this.isOperandLast = isOperandLast;
            this.isEnterLast = isEnterLast;
        }

        protected State(Parcel in) {
            if (in.readByte() == 0) {
                argOne = null;
            } else {
                argOne = in.readDouble();
            }
            if (in.readByte() == 0) {
                argTwo = null;
            } else {
                argTwo = in.readDouble();
            }
            if (in.readByte() == 0) {
                result = null;
            } else {
                result = in.readDouble();
            }
            isDotLast = in.readByte() != 0;
            isFirstAfterDot = in.readByte() != 0;
            isOperandLast = in.readByte() != 0;
            isEnterLast = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (argOne == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(argOne);
            }
            if (argTwo == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(argTwo);
            }
            if (result == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(result);
            }
            dest.writeByte((byte) (isDotLast ? 1 : 0));
            dest.writeByte((byte) (isFirstAfterDot ? 1 : 0));
            dest.writeByte((byte) (isOperandLast ? 1 : 0));
            dest.writeByte((byte) (isEnterLast ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return new State(in);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };
    }
}