package com.pradyumna.sos;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlertDialogActivity extends AppCompatActivity {

    private Button btnSOS;
    private EditText edtPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        edtPhoneNum = findViewById(R.id.edtPhoneNum);
        btnSOS = findViewById(R.id.btnSOS);

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("PhoneNumber", edtPhoneNum.getText().toString());
                startActivity(intent);
            }
        });

    }
}
