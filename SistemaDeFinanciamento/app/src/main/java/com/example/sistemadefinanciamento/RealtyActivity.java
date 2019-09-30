package com.example.sistemadefinanciamento;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RealtyActivity extends AppCompatActivity {

    private Button btnRCalculate;
    private RadioGroup rdgImovel;
    private RadioButton checkedRadioButton;
    private boolean isChecked;

    double taxaJuros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realty);

        Bundle getBundle;
        getBundle = this.getIntent().getBundleExtra("user_name");
        ((TextView) findViewById(R.id.txtRNome)).setText(getBundle.getString("user_name"));

        addRadioButtonListener();
        addButtonListener();
    }

    public void addButtonListener() {
        
        btnRCalculate = (Button) findViewById(R.id.btnRCalculate);
        btnRCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(((EditText) findViewById(R.id.edtValorImovel)).getText().toString()) || TextUtils.isEmpty(((EditText) findViewById(R.id.edtREntrada)).getText().toString()) ||
                    TextUtils.isEmpty(((EditText) findViewById(R.id.edtRParcelas)).getText().toString()) || TextUtils.isEmpty(((EditText) findViewById(R.id.edtRMensal)).getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Insira um valor em todos os campos", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    Toast.makeText(getApplicationContext(), "Selecione o financiamento de um tipo de imóvel", Toast.LENGTH_SHORT).show();
                } else {
                    onCalculateRealty();
                }
            }
        });
    }

    public void addRadioButtonListener() {
        rdgImovel = (RadioGroup) findViewById(R.id.rdgImovel);
        rdgImovel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                isChecked = checkedRadioButton.isChecked();
            }
        });
    }

    private void onCalculateRealty() {
        String userName = ((TextView) findViewById(R.id.txtRNome)).getText().toString();
        double valorASerPago;
        double imovel = Double.parseDouble(((EditText) findViewById(R.id.edtValorImovel)).getText().toString());
        double entrada = Double.parseDouble(((EditText) findViewById(R.id.edtREntrada)).getText().toString());
        int parcelas = Integer.parseInt(((EditText) findViewById(R.id.edtRParcelas)).getText().toString());
        double rendaMensal = Double.parseDouble(((EditText) findViewById(R.id.edtRMensal)).getText().toString());


        if (checkedRadioButton.getText().equals("Imóvel novo")) {
            imovel = imovel*1.05;
        } else if (checkedRadioButton.getText().equals("Imóvel usado")) {
            imovel = imovel*1.02;
        }

        if (verifyIncoming(imovel, entrada)) {
            if (verifyNetRent(((imovel-entrada)/parcelas), rendaMensal)) {

                valorASerPago = (entrada + (((imovel-entrada)/parcelas)*onCalculateInterestTaxes(rendaMensal))*parcelas);

                Bundle bundle = new Bundle();
                bundle.putDouble("val_produto", imovel);
                bundle.putDouble("val_entrada", entrada);
                bundle.putDouble("val_juros", taxaJuros);
                bundle.putInt("val_parcelas", parcelas);
                bundle.putDouble("val_renda", rendaMensal);
                bundle.putDouble("result", valorASerPago);
                bundle.putString("user_name", userName);
                bundle.putString("tipo_financiamento", "imovel");

                Intent thisIntent = new Intent(getApplicationContext(), ResultActivity.class);
                thisIntent.putExtra("bill", bundle);
                startActivity(thisIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Você não possui os requisitos mínimos para o financiamento", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Valor de entrada inferior a 20% do valor do imóvel", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean verifyIncoming(double valorImovel, double entrada) {
        if (entrada < (0.2*valorImovel)) {
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
            taxaJuros = 0.03;
            return 0.03;
        } else if (rendaLiquida > 3500.0 && rendaLiquida <= 5000.0) {
            taxaJuros = 0.025;
            return 0.025;
        } else if (rendaLiquida > 5000.0) {
            taxaJuros = 0.02;
            return 0.02;
        } else {
            taxaJuros = 0;
            return 0;
        }
    }

}
