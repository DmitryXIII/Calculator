package com.ineedyourcode.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ineedyourcode.calculator.presenter.ArithmeticOperation;
import com.ineedyourcode.calculator.presenter.Calculator;
import com.ineedyourcode.calculator.presenter.CalculatorPresenter;
import com.ineedyourcode.calculator.view.CalculatorView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CalculatorView {

    private static final String DISPLAY = "DISPLAY";
    private static final String TXT_ARG_ONE = "TXT_ARG_ONE";
    private static final String TXT_ARG_TWO = "TXT_ARG_TWO";
    private static final String TXT_OPERAND = "TXT_OPERAND";
    private static final String TXT_ARG_TWO_VISIBILITY = "TXT_ARG_TWO_VISIBILITY";
    private static final String TXT_OPERAND_VISIBILITY = "TXT_OPERAND_VISIBILITY";
    private static final String TXT_ENTER = "TXT_ENTER";
    private static final String TXT_ENTER_VISIBILITY = "TXT_ENTER_VISIBILITY";

    private ViewGroup historyContainer;
    private TextView txtDisplay;
    private TextView txtArgOne;
    private TextView txtArgTwo;
    private TextView txtOperand;
    private TextView txtEnter;
    private CalculatorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDisplay = findViewById(R.id.txt_display);
        presenter = new CalculatorPresenter(this, new Calculator());

        HashMap<Integer, Integer> digits = new HashMap<>();
        digits.put(R.id.btn_0, 0);
        digits.put(R.id.btn_1, 1);
        digits.put(R.id.btn_2, 2);
        digits.put(R.id.btn_3, 3);
        digits.put(R.id.btn_4, 4);
        digits.put(R.id.btn_5, 5);
        digits.put(R.id.btn_6, 6);
        digits.put(R.id.btn_7, 7);
        digits.put(R.id.btn_8, 8);
        digits.put(R.id.btn_9, 9);

        View.OnClickListener digitsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDigitsPressed(digits.get(v.getId()));
            }
        };

        findViewById(R.id.btn_0).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_1).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_2).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_3).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_4).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_5).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_6).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_7).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_8).setOnClickListener(digitsClickListener);
        findViewById(R.id.btn_9).setOnClickListener(digitsClickListener);

        HashMap<Integer, ArithmeticOperation> operands = new HashMap<>();
        operands.put(R.id.btn_plus, ArithmeticOperation.PLUS);
        operands.put(R.id.btn_minus, ArithmeticOperation.MINUS);
        operands.put(R.id.btn_multiply, ArithmeticOperation.MULTIPLY);
        operands.put(R.id.btn_division, ArithmeticOperation.DIVISION);

        View.OnClickListener onArithmeticOperandsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onArithmeticOperandsPressed(operands.get(v.getId()));
            }
        };

        findViewById(R.id.btn_plus).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_minus).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_multiply).setOnClickListener(onArithmeticOperandsListener);
        findViewById(R.id.btn_division).setOnClickListener(onArithmeticOperandsListener);

        findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEnterPressed();
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {
                presenter.onClearPressed();
            }
        });

        findViewById(R.id.btn_clear).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.onClearLongPressed();
                return true;
            }
        });

        historyContainer = findViewById(R.id.layout_animated);
        txtArgOne = findViewById(R.id.txt_argOne);
        txtArgTwo = findViewById(R.id.txt_argTwo);
        txtOperand = findViewById(R.id.txt_operand);
        txtEnter = findViewById(R.id.txt_enter);

        findViewById(R.id.btn_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDotPressed();
            }
        });
    }

    @Override
    public void showResult(String value) {
        showLongOrDouble(txtDisplay, value);
    }

    @Override
    public void showHistory(Double argOne, Double argTwo, ArithmeticOperation operation, boolean isEnter) {
        if (isEnter) {
            txtEnter.setText("=");
            animatedHistory();
            txtEnter.setVisibility(View.VISIBLE);
        } else {
            animatedHistory();
            txtEnter.setVisibility(View.GONE);
        }

        if (argOne == 0.0 && argTwo == null && operation == null) {
            txtArgOne.setText(R.string.cleared);
        } else {
            showLongOrDouble(txtArgOne, String.valueOf(argOne));
        }

        if (operation != null) {
            animatedHistory();
            switch (operation) {
                case PLUS:
                    txtOperand.setText("+");
                    break;
                case MINUS:
                    txtOperand.setText("-");
                    break;
                case MULTIPLY:
                    txtOperand.setText("*");
                    break;
                case DIVISION:
                    txtOperand.setText("/");
                    break;
            }
            animatedHistory();
            txtOperand.setVisibility(View.VISIBLE);
        } else {
            animatedHistory();
            txtOperand.setVisibility(View.GONE);
        }

        if (argTwo != null && argTwo != 0.0) {
            animatedHistory();
            txtArgTwo.setVisibility(View.VISIBLE);
        } else {
            animatedHistory();
            txtArgTwo.setVisibility(View.GONE);
        }
        if (argTwo == null) {
            txtArgTwo.setText("");
        } else {
            showLongOrDouble(txtArgTwo, String.valueOf(argTwo));
        }
    }

    @Override
    public void showLongOrDouble(TextView view, String value) {
        double temp_double = Double.parseDouble(value);
        long temp_long;
        if (value == null) {
            view.setText(String.valueOf(0));
        } else if (temp_double % 1 == 0) {
            temp_long = (long) temp_double;
            view.setText(String.valueOf(temp_long));
        } else {
            view.setText(String.valueOf(value));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DISPLAY, (String) txtDisplay.getText());
        outState.putString(TXT_ARG_ONE, (String) txtArgOne.getText());
        outState.putString(TXT_ARG_TWO, (String) txtArgTwo.getText());
        outState.putInt(TXT_ARG_TWO_VISIBILITY, txtArgTwo.getVisibility() == View.VISIBLE ? 1 : 0);
        outState.putString(TXT_OPERAND, (String) txtOperand.getText());
        outState.putInt(TXT_OPERAND_VISIBILITY, txtOperand.getVisibility() == View.VISIBLE ? 1 : 0);
        outState.putString(TXT_ENTER, (String) txtEnter.getText());
        outState.putInt(TXT_ENTER_VISIBILITY, txtEnter.getVisibility() == View.VISIBLE ? 1 : 0);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtDisplay.setText(savedInstanceState.getString(DISPLAY));
        txtArgOne.setText(savedInstanceState.getString(TXT_ARG_ONE));
        txtArgTwo.setText(savedInstanceState.getString(TXT_ARG_TWO));
        txtArgTwo.setVisibility(savedInstanceState.getInt(TXT_ARG_TWO_VISIBILITY) == 1 ? View.VISIBLE : View.GONE);
        txtOperand.setText(savedInstanceState.getString(TXT_OPERAND));
        txtOperand.setVisibility(savedInstanceState.getInt(TXT_OPERAND_VISIBILITY) == 1 ? View.VISIBLE : View.GONE);
        txtEnter.setText(savedInstanceState.getString(TXT_ENTER));
        txtEnter.setVisibility(savedInstanceState.getInt(TXT_ENTER) == 1 ? View.VISIBLE : View.GONE);
    }

    private void animatedHistory() {
        ChangeBounds myTransition = new ChangeBounds();
        myTransition.setDuration(200);
        TransitionManager.go(new Scene(historyContainer), myTransition);
    }
}