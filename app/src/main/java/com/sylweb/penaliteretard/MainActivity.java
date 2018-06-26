package com.sylweb.penaliteretard;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView selectEndDateButton;
    private ImageView selectDeliveryDateButton;
    private EditText priceEditText;
    private TextView endDateTextView;
    private TextView deliveryDateTextView;
    private TextView resultTextView;
    private Button calculateButton;

    private Date endDate;
    private Date deliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.selectDeliveryDateButton = findViewById(R.id.buttonDateLivraison);
        this.selectDeliveryDateButton.setOnClickListener(this);
        this.selectEndDateButton = findViewById(R.id.buttonDateFin);
        this.selectEndDateButton.setOnClickListener(this);
        this.priceEditText = findViewById(R.id.editTextPrix);
        this.endDateTextView = findViewById(R.id.textViewDateFin);
        this.deliveryDateTextView = findViewById(R.id.textViewDateLivraison);
        this.resultTextView = findViewById(R.id.textViewResult);
        this.resultTextView.setText("");
        this.calculateButton = findViewById(R.id.buttonCalculer);
        this.calculateButton.setOnClickListener(this);

        Intent intent = getIntent();
        int returnedType = 0;
        Date dateKept = null;
        Date dateChanged = null;
        String priceToKeep = null;
        if(intent.getExtras() != null) {
            dateChanged = (Date) intent.getExtras().getSerializable("DATE_CHANGED");
            dateKept = (Date) intent.getExtras().getSerializable("DATE_TO_KEEP");
            priceToKeep = intent.getStringExtra("PRICE_TO_KEEP");
            returnedType = (int) intent.getIntExtra("DATE_TYPE", 0);
        }

        if(returnedType != 0) {
            if(returnedType == 1) {
                this.endDate = dateChanged;
                this.deliveryDate = dateKept;
            }
            else if(returnedType == 2) {
                this.deliveryDate = dateChanged;
                this.endDate = dateKept;
            }
        }

        if(this.endDate == null) this.endDate = new Date(1497477600000L);
        if(this.deliveryDate==null) this.deliveryDate = new Date(1531260000000L);
        if(this.priceEditText.getText().toString().equals("") && priceToKeep == null) this.priceEditText.setText("188000");
        else this.priceEditText.setText(priceToKeep);


        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        this.endDateTextView.setText(simpleDate.format(this.endDate));
        this.deliveryDateTextView.setText(simpleDate.format(this.deliveryDate));
    }

    @Override
    public void onClick(View v) {
        if(v == selectEndDateButton) {
            Intent intent = new Intent(this,PickDateActivity.class);
            intent.putExtra("DATE_TYPE", 1);
            intent.putExtra("CURRENT_DATE", this.endDate);
            intent.putExtra("DATE_TO_KEEP", this.deliveryDate);
            intent.putExtra("PRICE_TO_KEEP", this.priceEditText.getText().toString());
            startActivity(intent);
        }
        else if(v == this.selectDeliveryDateButton) {
            Intent intent = new Intent(this,PickDateActivity.class);
            intent.putExtra("DATE_TYPE", 2);
            intent.putExtra("CURRENT_DATE", this.deliveryDate);
            intent.putExtra("DATE_TO_KEEP", this.endDate);
            intent.putExtra("PRICE_TO_KEEP", this.priceEditText.getText().toString());
            startActivity(intent);
        }
        else if(v == this.calculateButton) {
            long diff = deliveryDate.getTime() - endDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            float penalties = days * Long.valueOf(this.priceEditText.getText().toString()) / 3000;
            String penaltiesText = String.format("%.2f €", penalties);
            this.resultTextView.setText(""+days+" jours de retard soit "+penaltiesText);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putSerializable("END_DATE", this.endDate);
        savedInstanceState.putSerializable("DELIVERY_DATE", this.deliveryDate);
        savedInstanceState.putString("PRICE", this.priceEditText.getText().toString());
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        this.endDate = (Date) savedInstanceState.getSerializable("END_DATE");
        this.deliveryDate = (Date) savedInstanceState.getSerializable("DELIVERY_DATE");
        this.priceEditText.setText(savedInstanceState.getString("PRICE"));
    }
}
