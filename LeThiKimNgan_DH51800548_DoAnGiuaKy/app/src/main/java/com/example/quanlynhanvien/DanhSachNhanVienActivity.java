package com.example.quanlynhanvien;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.quanlynhanvien.adapter.NhanVienAdapter;
import com.example.quanlynhanvien.model.NhanVien;

import java.util.ArrayList;

public class DanhSachNhanVienActivity extends AppCompatActivity {
    ListView lvNv;
    ArrayList<NhanVien> dsNV;
    NhanVienAdapter adapter;
    NhanVien chon;
    int requestCode = 113, resultCode = 115;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "qlnv.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhan_vien);
        addControls();
        addEvents();

        docDSNv();
    }
    private void docDSNv() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from NhanVien", null);

        adapter.clear();
        while (cursor.moveToNext()) {
            String manv = cursor.getString(0);
            String tennv = cursor.getString(1);
            String sdt = cursor.getString(2);
            int luong = cursor.getInt(3);
            byte[] anh = cursor.getBlob(4);
            String gioitinh = cursor.getString(5);
            String phongban = selectTenPB(cursor.getString(6));
            dsNV.add(new NhanVien(tennv, manv,luong, sdt, gioitinh, anh, phongban));

        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private String selectTenPB(String manv) {
        String tenpb= "";
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
        lvNv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachNhanVienActivity.this);
                    builder.setMessage(R.string.txtDelet);
                    builder.setPositiveButton(R.string.txtCancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NhanVien nv = dsNV.get(position);
                            SQLiteDatabase database = openOrCreateDatabase(
                                    DB_NAME,
                                    MODE_PRIVATE,
                                    null
                            );
                            int delete = database.delete(
                                    "NhanVien",
                                    "manv=?",
                                    new String[]{nv.getManv()}
                            );
                            database.close();
                            docDSNv();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                return true;
            }
        });
        lvNv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < dsNV.size()) {
                    Intent intent = new Intent(DanhSachNhanVienActivity.this, ThongTinNhanVienActivity.class);
                    chon = dsNV.get(position);
                    intent.putExtra("CHON", chon);
                    startActivityForResult(intent, requestCode);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (resultCode == this.resultCode) {
                if (data.hasExtra("TRA")) {
                    NhanVien nv = (NhanVien) data.getSerializableExtra("TRA");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("manv", nv.getManv());
                    row.put("tennv", nv.getTennv());
                    row.put("sdt", nv.getSdt());
                    row.put("luong", nv.getLuong());
                    row.put("anh", nv.getAnh());
                    row.put("gioitinh", nv.getGioiTinh());
                    row.put("phongban", nv.getPhongban());
                    long update = database.update(
                            "NhanVien",
                            row,
                            "manv=?",
                            new String[]{nv.getManv()}
                    );
                    database.close();
                    docDSNv();
                } else if (data.hasExtra("THEM")) {
                    NhanVien nv = (NhanVien) data.getSerializableExtra("THEM");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("manv", nv.getManv());

                    row.put("tennv", nv.getTennv());
                    row.put("sdt", nv.getSdt());
                    row.put("luong", nv.getLuong());
                    row.put("anh", nv.getAnh());
                    row.put("gioitinh", nv.getGioiTinh());
                    row.put("phongban", nv.getPhongban());
                    long insert = database.insert(
                            "NhanVien",
                            null,
                            row
                    );
                    database.close();
                    docDSNv();
                }
            }
        }
    }
//        chon = null;
//    }

    private void addControls() {
        lvNv = findViewById(R.id.lvNhanVien);
        dsNV = new ArrayList<>();
        adapter = new NhanVienAdapter(
                DanhSachNhanVienActivity.this,
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
                Intent intent = new Intent(DanhSachNhanVienActivity.this, DanhSachPhongBanActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuDSNV:
                Intent intent1 = new Intent(DanhSachNhanVienActivity.this, DanhSachNhanVienActivity.class);
                startActivity(intent1);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}