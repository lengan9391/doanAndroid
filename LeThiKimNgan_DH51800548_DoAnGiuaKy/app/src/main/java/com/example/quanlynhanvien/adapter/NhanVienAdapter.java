package com.example.quanlynhanvien.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlynhanvien.R;
import com.example.quanlynhanvien.model.NhanVien;

import java.util.List;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    Activity context;
    int resource;
    List<NhanVien> objects;
    public NhanVienAdapter(@NonNull Activity context, int resource, @NonNull List<NhanVien> objects) {
        super(context,resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        LayoutInflater inflater=this.context.getLayoutInflater();
        view=inflater.inflate(this.resource,null);
        ImageView imgAnh=view.findViewById(R.id.imgItemStu);
        TextView txtTen=view.findViewById(R.id.txtItemTen);
        TextView txtMa=view.findViewById(R.id.txtItemMa);
        TextView txtPhongban=view.findViewById(R.id.txtItemPhongban);
        NhanVien nv=this.objects.get(position);
        Bitmap img = BitmapFactory.decodeByteArray(nv.getAnh(),0,nv.getAnh().length);
        imgAnh.setImageBitmap(img);
        txtTen.setText(nv.getTennv());
        txtMa.setText(nv.getManv());
        txtPhongban.setText(nv.getPhongban());

        return view;
    }
}
