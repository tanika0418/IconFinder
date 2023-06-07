package com.example.iconfinder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iconfinder.R;
import com.example.iconfinder.custom.CustomSnacks;
import com.example.iconfinder.models.IconModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class IconListAdapter extends RecyclerView.Adapter<IconListAdapter.MyViewHolder> {

    private Context context;
    private View view;
    private List<IconModel> list;
    private CustomSnacks customSnacks;

    public IconListAdapter(Context context, List<IconModel> list,View view) {
        this.context = context;
        this.list = list;
        this.view = view;
        customSnacks = new CustomSnacks();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.icon_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IconModel tempIcon = this.list.get(position);
        Glide.with(context).load(this.list.get(position).getPreviewUrl()).into(holder.iconImage);
        if(this.list.get(position).isPremium()){
            holder.isPrime.setVisibility(View.VISIBLE);
        }
        else {
            holder.isPrime.setVisibility(View.GONE);
        }
        holder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                //---( Bottom Sheet Variables )---//
                ImageView iconImage = bottomSheetDialog.findViewById(R.id.icon_image);
                TextView textView = bottomSheetDialog.findViewById(R.id.icon_name);
                Button button = bottomSheetDialog.findViewById(R.id.download_btn);
                ProgressBar progressBar = bottomSheetDialog.findViewById(R.id.progress);

                Glide.with(context).load(tempIcon.getPreviewUrl()).into(iconImage);
                textView.setText(tempIcon.getName());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OutputStream outputStream;
                        progressBar.setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);

                        //---( Download Here )---//
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) iconImage.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();

                        File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File dir = new File(filePath.getAbsolutePath());
                        dir.mkdir();
                        File file = new File(dir,tempIcon.getName()+"_"+System.currentTimeMillis()+".png");
                        try {
                            outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                            outputStream.flush();
                            outputStream.close();
                            progressBar.setVisibility(View.GONE);
                            button.setVisibility(View.VISIBLE);
                            bottomSheetDialog.dismiss();
                            customSnacks.successSnack(view,"Saved to Downloads, Thank You for downloading this Icon!");
                        } catch (IOException e) {
                            e.printStackTrace();
                            customSnacks.failSnack(view,"Download Failed, Please try again!");
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.list != null){
            return  this.list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImage;
        ImageView isPrime;
        CardView mainCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImage = itemView.findViewById(R.id.icon_image);
            isPrime = itemView.findViewById(R.id.prime_star);
            mainCard = itemView.findViewById(R.id.main_card);
        }
    }
}
