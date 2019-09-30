package com.example.sistemadefinanciamento;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle getBundle = this.getIntent().getBundleExtra("bill");
        ((TextView) findViewById(R.id.txtQtdParcela)).setText(String.valueOf(getBundle.getInt("val_parcelas")));
        ((TextView) findViewById(R.id.txtValEntrada)).setText(String.valueOf(getBundle.getDouble("val_entrada")));
        ((TextView) findViewById(R.id.txtValJuros)).setText(String.valueOf(getBundle.getDouble("val_juros")));
        ((TextView) findViewById(R.id.txtValorProduto)).setText(String.valueOf(getBundle.getDouble("val_produto")));
        ((TextView) findViewById(R.id.txtValRendaMensal)).setText(String.valueOf(getBundle.getDouble("val_renda")));

        ((TextView) findViewById(R.id.txtBillResult)).setText(String.valueOf(getBundle.getString("result")));
        ((TextView) findViewById(R.id.txtRsNome)).setText(String.valueOf(getBundle.getInt("user_name")));

        ImageView i = (ImageView) findViewById(R.id.imgFinanciado);
        if (getBundle.getString("tipo_financiamento").equals("imovel")) {
            i.setImageResource(R.drawable.casa);
        } else {
            i.setImageResource(R.drawable.carro);
        }
    }
}
