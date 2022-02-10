package com.example.quanlynhanvien;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.quanlynhanvien.model.NhanVien;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ThongTinNhanVienActivity extends AppCompatActivity {
    EditText  txtTen,txtMa,txtLuong,txtPB,txtSDT,txtGt;
    ImageView imgNV;
    NhanVien chon;
    Spinner spinner;
    ArrayList<String> dsPB;
    Button btnLuu, btnThem, btnAdd;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "qlnv.db";
    private int REQUEST_IMAGE = 10;
    int requestCode = 113, resultCode = 115;
    String manv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_nhan_vien);
        addControls();
        readDataPhongBan();
        getData();

        addEvents();
    }
    private void TakePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);

    }

    private byte[] chuyenImgViewSangByteArray(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytesData = stream.toByteArray();
            stream.close();
            return bytesData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                Uri imgUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgNV.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readDataPhongBan() {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from PhongBan", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String tenpb = cursor.getString(1);
            dsPB.add(tenpb);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dsPB);
        spinner.setAdapter(adapter);
    }

    private int setSpiner(String tenpb) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(tenpb)) {
                return i;
            }
        }
        return 0;
    }

    private Boolean checkID(String manv){
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from NhanVien", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String ma = cursor1.getString(0);
            if (ma.equals(manv))
                return true;
        }
        return false;
    }

    private String selectMaPB(String tenpb) {
        String manv = "";
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor1 = database.rawQuery("select * from PhongBan", null);
        for (int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToPosition(i);
            String ten = cursor1.getString(1);
            if (ten.equals(tenpb))
                manv = cursor1.getString(0);
        }
        return manv;
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chon == null) {
                    chon = new NhanVien();
                }
                if (chon.getManv().equals(txtMa.getText().toString()) != true) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinNhanVienActivity.this);
                    builder.setMessage(R.string.txtChange);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    android.app.AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    chon.setTennv(txtTen.getText().toString());
                    chon.setManv(txtMa.getText().toString());
                    String text = spinner.getSelectedItem().toString();
                    String mapb = selectMaPB(text) ;
                    chon.setPhongban(mapb);
                    byte[] anh = chuyenImgViewSangByteArray(imgNV);
                    chon.setAnh(anh);
                    chon.setGioiTinh(txtGt.getText().toString());
                    chon.setSdt(txtSDT.getText().toString());
                    chon.setLuong(Integer.parseInt(txtLuong.getText().toString()));


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

                chon.setTennv(txtTen.getText().toString());
                chon.setManv(txtMa.getText().toString());
                String text = spinner.getSelectedItem().toString();
                String mapb = selectMaPB(text) ;
                chon.setPhongban(mapb);
                byte[] anh = chuyenImgViewSangByteArray(imgNV);
                chon.setAnh(anh);
                chon.setGioiTinh(txtGt.getText().toString());
                chon.setSdt(txtSDT.getText().toString());
                chon.setLuong(Integer.parseInt(txtLuong.getText().toString()));
                /*if(chon.getPhongban()== "KD")
                        chon.setLuong(4000000/2 + 10000000/4 );
                else if(chon.getPhongban()=="NS")
                    chon.setLuong(4000000*4/10  + 5000000/10);
                else if(chon.getPhongban()=="KT")
                    chon.setLuong(4000000*3/4 + 3000000 );
                else chon.setLuong(4000000 + 500000);*/

                if(checkID(chon.getManv())){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ThongTinNhanVienActivity.this);
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


    private void getData() {
        Intent intent = getIntent();
        if (intent.hasExtra("CHON")) {
            chon = (NhanVien) intent.getSerializableExtra("CHON");
            if (chon != null) {
                txtTen.setText(chon.getTennv());
                txtMa.setText(chon.getManv());
//                txtLop.setText(chon.getLop());
                String phongban = chon.getPhongban();
                spinner.setSelection(setSpiner(phongban));
                txtSDT.setText(chon.getSdt());
                txtGt.setText(chon.getGioiTinh());
                txtLuong.setText(chon.getLuong() + "");
                Bitmap img = BitmapFactory.decodeByteArray(chon.getAnh(), 0, chon.getAnh().length);
                imgNV.setImageBitmap(img);
            } else {
                resetView();
            }
        } else {
            resetView();
        }
    }

    private void resetView() {
        txtTen.setText("");
        txtMa.setText("");
        txtPB.setText("");
        txtSDT.setText("");
        txtGt.setText("");
        txtLuong.setText("");
        imgNV.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

    private void addControls() {
        imgNV = findViewById(R.id.imgNhanVien);
        txtTen = findViewById(R.id.txtTenNV);
        txtMa = findViewById(R.id.txtMsnv);
//        txtLop = findViewById(R.id.txtLop);
        txtLuong = findViewById(R.id.txtLuong);
        txtSDT = findViewById(R.id.txtSDT);
        txtGt = findViewById(R.id.txtGioiTinh);
        btnLuu = findViewById(R.id.btnLuuNv);
        btnThem = findViewById(R.id.btnThemNv);
        btnAdd = findViewById(R.id.btnAdd);
        spinner = findViewById(R.id.spinnerPhongBan);
        dsPB = new ArrayList<>();
        manv = txtMa.getText().toString();
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
                Intent intent = new Intent(ThongTinNhanVienActivity.this, DanhSachPhongBanActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuDSNV:
                Intent intent1 = new Intent(ThongTinNhanVienActivity.this, DanhSachNhanVienActivity.class);
                startActivity(intent1);
                break;
            case R.id.mnuExit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}