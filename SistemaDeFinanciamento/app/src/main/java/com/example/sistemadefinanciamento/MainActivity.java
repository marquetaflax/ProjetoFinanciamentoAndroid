package com.example.sistemadefinanciamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RadioButton checkedRadioButton;
    private boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addRadioButtonListener();
        addButtonListener();
    }

    public void addRadioButtonListener() {
        ((RadioGroup) findViewById(R.id.rdgOpcoes)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                isChecked = checkedRadioButton.isChecked();
            }
        });
    }

    public void addButtonListener() {
        ((Button) findViewById(R.id.btnContinue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(((EditText) findViewById(R.id.edtNome)).getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Insira o seu nome", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    Toast.makeText(getApplicationContext(), "Selecione um financiamento", Toast.LENGTH_SHORT).show();
                } else {
                 goToNextActivity();
                }
            }
        });
    }

    public void goToNextActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("user_name", ((EditText) findViewById(R.id.edtNome)).getText().toString());
        if (checkedRadioButton.getText().equals("Veículo")) {
            Intent intent = new Intent(getApplicationContext(), VehicleActivity.class);
            intent.putExtra("user_name", bundle);
            startActivity(intent);
        } else if (checkedRadioButton.getText().equals("Imóvel")) {
            Intent intent = new Intent(getApplicationContext(), RealtyActivity.class);
            intent.putExtra("user_name", bundle);
            startActivity(intent);
        }
    }
}