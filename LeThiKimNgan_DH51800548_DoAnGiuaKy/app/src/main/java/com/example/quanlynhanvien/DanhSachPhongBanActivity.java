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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.quanlynhanvien.model.PhongBan;

import java.util.ArrayList;

public class DanhSachPhongBanActivity extends AppCompatActivity {
    ListView lvPhongBan;
    ArrayList<PhongBan> dsPhongBan;
    ArrayAdapter<PhongBan> adapterPhongBan;
    int requestCode = 113, resultCode = 115;
    PhongBan chon;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "qlnv.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong_ban);
        addControls();
        addEvents();
        docDSPhongBan();
    }
    private void docDSPhongBan() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("select * from PhongBan", null);
        adapterPhongBan.clear();
        while (cursor.moveToNext()) {
            String mapb = cursor.getString(0);
            String tenpb = cursor.getString(1);
            adapterPhongBan.add(new PhongBan(mapb, tenpb));
        }
        cursor.close();
        database.close();
        adapterPhongBan.notifyDataSetChanged();
    }


    private void addEvents() {
        lvPhongBan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DanhSachPhongBanActivity.this);
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
                            PhongBan pb = adapterPhongBan.getItem(position);
                            SQLiteDatabase database = openOrCreateDatabase(
                                    DB_NAME,
                                    MODE_PRIVATE,
                                    null
                            );
                            int delete = database.delete(
                                    "PhongBan",
                                    "mapb=?",
                                    new String[]{pb.getMapb()}
                            );
                            int deleteNV = database.delete(
                                    "NhanVien",
                                    "tenpb=?",
                                    new String[]{pb.getMapb()}
                            );
                            database.close();
                            docDSPhongBan();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
                return true;
            }
        });
        lvPhongBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < adapterPhongBan.getCount()) {
                    Intent intent = new Intent(DanhSachPhongBanActivity.this, ThongTinPhongBanActivity.class);
                    chon = adapterPhongBan.getItem(position);
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
                    PhongBan tra = (PhongBan) data.getSerializableExtra("TRA");
                    if (chon != null) {
                        chon.setMapb(tra.getMapb());
                        chon.setTenpb(tra.getTenpb());
                        SQLiteDatabase database = openOrCreateDatabase(
                                DB_NAME,
                                MODE_PRIVATE,
                                null
                        );
                        ContentValues row = new ContentValues();
                        row.put("mapb", chon.getMapb());
                        row.put("tenpb", chon.getTenpb());
                        int update = database.update(
                                "PhongBan",
                                row,
                                "mapb=?",
                                new String[]{chon.getMapb()}
                        );
                        database.close();
                        docDSPhongBan();
                    }
                } else if (data.hasExtra("THEM")) {
                    PhongBan pb = (PhongBan) data.getSerializableExtra("THEM");
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    ContentValues row = new ContentValues();
                    row.put("mapb", pb.getMapb());
                    row.put("tenpb", pb.getTenpb());
                    long insert = database.insert(
                            "PhongBan",
                            null,
                            row
                    );
                    database.close();
                    docDSPhongBan();
//                    dsPhongBan.add(l);
//                    adapterPhongBan.notifyDataSetChanged();
                }
            }
        }
        chon = null;
    }


    private void addControls() {
        lvPhongBan = findViewById(R.id.lvPhongBan);
        dsPhongBan = new ArrayList<>();
        adapterPhongBan = new ArrayAdapter<>(
                DanhSachPhongBanActivity.this,
                android.R.layout.simple_list_item_1

        );
        lvPhongBan.setAdapter(adapterPhongBan);
        chon = null;
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
                Intent intent = new Intent(DanhSachPhongBanActivity.this, DanhSachPhongBanActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuDSNV:
                Intent intent1 = new Intent(DanhSachPhongBanActivity.this, DanhSachNhanVienActivity.class);
                startActivity(intent1);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}