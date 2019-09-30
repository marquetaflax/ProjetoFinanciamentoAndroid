package com.example.sistemadefinanciamento;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleActivity extends AppCompatActivity {

    private Button btnVCalculate;
    private RadioGroup rdgVeiculo;
    private RadioButton checkedRadioButton;
    private boolean isChecked;

    double taxaJuros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        Bundle getBundle;
        getBundle = this.getIntent().getBundleExtra("user_name");
        ((TextView) findViewById(R.id.txtVNome)).setText(getBundle.getString("user_name"));

        addRadioButtonListener();
        addButtonListener();
    }

    public void addButtonListener() {
        btnVCalculate = (Button) findViewById(R.id.btnVCalculate);
        btnVCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(((EditText) findViewById(R.id.edtVParcela)).getText().toString()) || TextUtils.isEmpty(((EditText) findViewById(R.id.edtVEntrada)).getText().toString()) ||
                    TextUtils.isEmpty(((EditText) findViewById(R.id.edtValorVeiculo)).getText().toString()) || TextUtils.isEmpty(((EditText) findViewById(R.id.edtVRenda)).getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Insira um valor em todos os campos", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    Toast.makeText(getApplicationContext(), "Selecione o financiamento de um tipo de veículo", Toast.LENGTH_SHORT).show();
                } else {
                    onCalculateVehicle();
                }
            }
        });
    }

    public void addRadioButtonListener() {
        rdgVeiculo = (RadioGroup) findViewById(R.id.rdgVeiculo);
        rdgVeiculo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                isChecked = checkedRadioButton.isChecked();
            }
        });
    }
    private void onCalculateVehicle() {
        double veiculo = Double.parseDouble(((EditText) findViewById(R.id.edtValorVeiculo)).getText().toString());
        double entrada = Double.parseDouble(((EditText) findViewById(R.id.edtVEntrada)).getText().toString());
        int parcelas = Integer.parseInt(((EditText) findViewById(R.id.edtVParcela)).getText().toString());
        double rendaMensal = Double.parseDouble(((EditText) findViewById(R.id.edtVRenda)).getText().toString());
        String userName = ((TextView) findViewById(R.id.txtVNome)).getText().toString();
        double valorASerPago;

        if (checkedRadioButton.getText().equals("Veículo novo")) {
            veiculo = veiculo*(1.05);
        }

        if (verifyIncoming(veiculo, entrada)) {
            if (verifyNetRent(((veiculo-entrada)/parcelas), rendaMensal)) {
                valorASerPago = (entrada + (((veiculo-entrada)/parcelas)*onCalculateInterestTaxes(rendaMensal))*parcelas);

                Bundle bundle = new Bundle();
                bundle.putDouble("val_produto", veiculo);
                bundle.putDouble("val_entrada", entrada);
                bundle.putDouble("val_juros", taxaJuros);
                bundle.putInt("val_parcelas", parcelas);
                bundle.putDouble("val_renda", rendaMensal);
                bundle.putDouble("result", valorASerPago);
                bundle.putString("user_name", userName);
                bundle.putString("tipo_financiamento", "veiculo");

                Intent thisIntent = new Intent(getApplicationContext(), ResultActivity.class);
                thisIntent.putExtra("bill", bundle);
                startActivity(thisIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Você não possui os requisitos mínimos para o financiamento", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Valor de entrada é inferior a 20% do valor do imóvel", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean verifyIncoming(double valorVeiculo, double entrada) {
        if (entrada < (0.2*valorVeiculo)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyNetRent(double parcela, double renda) {
        if (parcela > (0.3*renda)) {
            return false;
        } else {
            return true;
        }
    }

    public double onCalculateInterestTaxes(double rendaLiquida) {
        if (rendaLiquida <= 3500.0) {
            taxaJuros = 0.06;
            return 0.06;
        } else if (rendaLiquida > 3500.0 && rendaLiquida <= 5000.0) {
            taxaJuros = 0.05;
            return 0.05;
        } else if (rendaLiquida > 5000.0) {
            taxaJuros = 0.04;
            return 0.04;
        } else {
            taxaJuros = 0;
            return 0;
        }
    }

}
