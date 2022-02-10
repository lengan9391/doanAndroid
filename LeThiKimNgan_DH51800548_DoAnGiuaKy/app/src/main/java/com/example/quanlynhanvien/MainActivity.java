package com.example.quanlynhanvien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    EditText txtUser,txtPass;
    Button btnLogin;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "qlnv.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        copyDB();
    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void copyDB() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                File dbDir = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbDir.exists()) dbDir.mkdir();

                InputStream is = getAssets().open(DB_NAME);
                String outputFilePath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int lenght = 0;
                while ((lenght = is.read(buffer)) > 0) {
                    os.write(buffer, 0, lenght);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = txtUser.getText().toString();
                String pass = txtPass.getText().toString();
                if (ten.equals("ngan") && pass.equals("123")) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            MenuActivity.class
                    );
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.txtAlertLogin);
                    builder.setPositiveButton(R.string.txtExit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });
    }

    private void addControls() {
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
    }
}