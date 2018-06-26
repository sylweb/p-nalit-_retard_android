package com.sylweb.penaliteretard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class PickDateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button okButton;
    private Button cancelButton;
    private DatePicker myPicker;

    private int dateType;
    private Date dateToChange;
    private Date dateToKeep;
    private String priceToKeep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        Intent intent = getIntent();
        this.dateToChange = (Date) intent.getExtras().getSerializable("CURRENT_DATE");
        this.dateToKeep = (Date) intent.getExtras().getSerializable("DATE_TO_KEEP");
        this.priceToKeep = intent.getStringExtra("PRICE_TO_KEEP");
        this.dateType = intent.getIntExtra("DATE_TYPE", 0);

        this.myPicker = findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.dateToChange);
        this.myPicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        this.okButton = findViewById(R.id.buttonOK);
        this.okButton.setOnClickListener(this);
        this.cancelButton = findViewById(R.id.buttonCancel);
        this.cancelButton.setOnClickListener(this);

    }

    public Date getSelectedDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.myPicker.getYear());
        cal.set(Calendar.MONTH, this.myPicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.myPicker.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public void onClick(View v) {
        if(v == this.cancelButton) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("DATE_TYPE", this.dateType);
            intent.putExtra("DATE_CHANGED", this.dateToChange);
            intent.putExtra("DATE_TO_KEEP", this.dateToKeep);
            intent.putExtra("PRICE_TO_KEEP", this.priceToKeep);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("DATE_TYPE", this.dateType);
            intent.putExtra("DATE_CHANGED", this.getSelectedDate());
            intent.putExtra("DATE_TO_KEEP", this.dateToKeep);
            intent.putExtra("PRICE_TO_KEEP", this.priceToKeep);

            startActivity(intent);
        }
    }
}
