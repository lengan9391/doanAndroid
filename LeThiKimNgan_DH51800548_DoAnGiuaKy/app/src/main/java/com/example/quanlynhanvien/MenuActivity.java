package com.example.quanlynhanvien;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button btnDsNv,btnDsPhongBan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        addControls();
        addEvents();
    }
    private void addEvents() {
        btnDsNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MenuActivity.this,
                        DanhSachNhanVienActivity.class
                );
                startActivity(intent);
            }
        });
        btnDsPhongBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MenuActivity.this,
                        DanhSachPhongBanActivity.class
                );
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnDsPhongBan = findViewById(R.id.btnDsPhongBan);
        btnDsNv = findViewById(R.id.btnDsNV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuDSPB:
                Intent intent = new Intent(MenuActivity.this, DanhSachPhongBanActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuDSNV:
                Intent intent1 = new Intent(MenuActivity.this, DanhSachNhanVienActivity.class);
                startActivity(intent1);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}