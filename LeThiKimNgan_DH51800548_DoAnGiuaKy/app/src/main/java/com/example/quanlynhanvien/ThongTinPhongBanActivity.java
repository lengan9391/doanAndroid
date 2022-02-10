package com.example.quanlynhanvien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.quanlynhanvien.adapter.NhanVienAdapter;
import com.example.quanlynhanvien.model.NhanVien;
import com.example.quanlynhanvien.model.PhongBan;

import java.util.ArrayList;

public class ThongTinPhongBanActivity extends AppCompatActivity {
    EditText txtMaPB,txtTenPB;
    PhongBan chon;
    Button btnLuu,btnThem;
    String ma,ten;
    ListView lvNv;
    ArrayList<NhanVien> dsNV;
    NhanVienAdapter adapter;
    int requestCode = 113, resultCode = 115;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "qlnv.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_phong_ban);
        addControls();
        getData();
        addEvents();
        docDSNv();
    }
    private void docDSNv() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from NhanVien where phongban=?", new String[]{chon.getMapb()});
        adapter.clear();
        while (cursor.moveToNext()) {
            String manv = cursor.getString(0);
            String tennv = cursor.getString(1);
            String sdt = cursor.getString(2);
            int luong = cursor.getInt(3);
            byte[] anh = cursor.getBlob(4);
            String gioitinh = cursor.getString(5);
            String phongban = selectTenPB(cursor.getString(6));
            dsNV.add(new NhanVien(tennv, manv, luong, sdt, gioitinh, anh, phongban));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private String selectTenPB(String manv) {
        String tenpb = "";
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from PhongBan", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String mapb = cursor1.getString(0);
            if (mapb.equals(manv))
                tenpb = cursor1.getString(1);
        }
        return tenpb;
    }

    private void addEvents() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chon == null) {
                    chon = new PhongBan();
                }
                if (chon.getMapb().equals(txtMaPB.getText().toString()) != true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinPhongBanActivity.this);
                    builder.setMessage(R.string.txtChange);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    chon.setTenpb(txtTenPB.getText().toString());
                    chon.setMapb(txtMaPB.getText().toString());

                    Intent intent = getIntent();
                    intent.putExtra("TRA", chon);
                    setResult(resultCode, intent);
                    finish();

                }
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chon.setTenpb(txtTenPB.getText().toString());
                chon.setMapb(txtMaPB.getText().toString());
                if(checkID(chon.getMapb())){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinPhongBanActivity.this);
                    builder.setMessage(R.string.txtCheckID);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else {
                    Intent intent = getIntent();
                    intent.putExtra("THEM", chon);
                    setResult(resultCode, intent);
                    finish();
                }
            }
        });
    }
    private Boolean checkID(String manv){
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from PhongBan", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String mapb = cursor1.getString(0);
            if (mapb.equals(manv))
                return true;
        }
        return false;
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent.hasExtra("CHON")) {
            chon = (PhongBan) intent.getSerializableExtra("CHON");
            if (chon != null) {
                txtTenPB.setText(chon.getTenpb());
                txtMaPB.setText(chon.getMapb());
            } else {
                resetView();
            }
        } else {
            resetView();
        }
    }

    private void resetView() {
        txtMaPB.setText("");
        txtTenPB.setText("");
    }

    private void addControls() {
        txtMaPB = findViewById(R.id.txtMaPB);
        txtTenPB = findViewById(R.id.txtTenPB);
        btnLuu = findViewById(R.id.btnLuu);
        btnThem = findViewById(R.id.btnThem);
        ma = txtMaPB.getText().toString();
        ten = txtTenPB.getText().toString();

        lvNv = findViewById(R.id.lvDSnv);
        dsNV = new ArrayList<>();
        adapter = new NhanVienAdapter(
                ThongTinPhongBanActivity.this,
                R.layout.item_sinhvien,
                dsNV
        );

        lvNv.setAdapter(adapter);
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
                Intent intent = new Intent(ThongTinPhongBanActivity.this, DanhSachPhongBanActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuDSNV:
                Intent intent1 = new Intent(ThongTinPhongBanActivity.this, DanhSachNhanVienActivity.class);
                startActivity(intent1);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}