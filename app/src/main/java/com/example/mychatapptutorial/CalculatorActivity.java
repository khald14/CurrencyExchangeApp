package com.example.mychatapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import org.mariuszgromada.math.mxparser.*;



public class CalculatorActivity extends AppCompatActivity {

    private EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        display = findViewById(R.id.input);
        display.setShowSoftInputOnFocus(false);
        display.setOnClickListener(v -> {
            if((display.getText().toString()).equals(getString(R.string.display))){
                display.setText("");
            }
        });
        findViewById(R.id.zero_btn).setOnClickListener(v -> updateText("0"));
        findViewById(R.id.one_btn).setOnClickListener(v -> updateText("1"));
        findViewById(R.id.two_btn).setOnClickListener(v -> updateText("2"));
        findViewById(R.id.three_btn).setOnClickListener(v -> updateText("3"));
        findViewById(R.id.four_btn).setOnClickListener(v -> updateText("4"));
        findViewById(R.id.five_btn).setOnClickListener(v -> updateText("5"));
        findViewById(R.id.six_btn).setOnClickListener(v -> updateText("6"));
        findViewById(R.id.seven_btn).setOnClickListener(v -> updateText("7"));
        findViewById(R.id.eight_btn).setOnClickListener(v -> updateText("8"));
        findViewById(R.id.nine_btn).setOnClickListener(v -> updateText("9"));
        findViewById(R.id.multiply_btn).setOnClickListener(v -> updateText("×"));
        findViewById(R.id.divide_btn).setOnClickListener(v -> updateText("÷"));
        findViewById(R.id.subtract_btn).setOnClickListener(v -> updateText("-"));
        findViewById(R.id.add_btn).setOnClickListener(v -> updateText("+"));
        findViewById(R.id.clear_btn).setOnClickListener(v -> display.setText(""));
        findViewById(R.id.parentheses_btn).setOnClickListener(v -> {
            int cursorPos = display.getSelectionStart();
            int openPar = 0;
            int closePar = 0;
            int textLen = display.getText().length();

            for(int i = 0 ; i < cursorPos; i++){
                if(display.getText().toString().charAt(i) == '('){
                    openPar+=1;
                }
                if(display.getText().toString().charAt(i) == ')'){
                    closePar+=1;
                }
            }
            if(openPar == closePar || display.getText().toString().charAt(textLen - 1) == '('){
                updateText("(");
            }
            else if(closePar < openPar && display.getText().toString().charAt(textLen - 1) != '('){
                updateText(")");
            }
            display.setSelection(cursorPos+1);
        });
        findViewById(R.id.percent_btn).setOnClickListener(v -> {
            updateText("%");
            equalBTN(v);
        });
        findViewById(R.id.plus_minus_btn).setOnClickListener(v -> updateText("-"));
        findViewById(R.id.point_btn).setOnClickListener(v -> updateText("."));
        findViewById(R.id.equal_btn).setOnClickListener(this::equalBTN);
        findViewById(R.id.backspace_btn).setOnClickListener(v -> {
            int cursorPos = display.getSelectionStart();
            int textLen = display.getText().length();

            if(cursorPos!= 0 && textLen !=0){
                SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
                selection.replace(cursorPos-1,cursorPos,"");
                display.setText(selection);
                display.setSelection(cursorPos-1);
            }
        });

        findViewById(R.id.CalculatorTopAppBar).setOnClickListener(v -> {
            Intent intent = new Intent(CalculatorActivity.this,HomeActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }
    private void updateText(String strToAdd){
        String oldStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String leftStr = oldStr.substring(0,cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if(getString(R.string.display).equals(display.getText().toString())){
            display.setText(strToAdd);
        }else{
            display.setText(String.format("%s%s%s",leftStr,strToAdd,rightStr));
        }
        display.setSelection(cursorPos+1);
    }
    public void equalBTN(View view){
        String userExp = display.getText().toString();
        userExp = userExp.replaceAll("÷","/");
        userExp = userExp.replaceAll("×","*");

        Expression exp = new Expression(userExp);

        String result = String.valueOf(exp.calculate());

        display.setText(result);
        display.setSelection(result.length());
    }
}