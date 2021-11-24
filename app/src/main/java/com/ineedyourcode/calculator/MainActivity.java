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
    private HashMap<String, TextView> mapTxtViews = new HashMap<>();
    private CalculatorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new CalculatorPresenter(this, new Calculator());
        txtDisplay = findViewById(R.id.txt_display);

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
    public void showResult(HashMap<String, String> mapTextValues, HashMap<String, Integer> mapVisibilityValues) {
        txtDisplay.setText(mapTextValues.get("txtDisplay"));
        txtArgOne.setText(mapTextValues.get("txtArgOne"));
        txtArgTwo.setText(mapTextValues.get("txtArgTwo"));
        txtOperand.setText(mapTextValues.get("txtOperand"));
        txtEnter.setText(mapTextValues.get("txtEnter"));

        animatedHistory();
        txtArgTwo.setVisibility(mapVisibilityValues.get("txtArgTwo") == 1 ? View.VISIBLE : View.GONE);
        animatedHistory();
        txtOperand.setVisibility(mapVisibilityValues.get("txtOperand") == 1 ? View.VISIBLE : View.GONE);
        animatedHistory();
        txtEnter.setVisibility(mapVisibilityValues.get("txtEnter") == 1 ? View.VISIBLE : View.GONE);
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