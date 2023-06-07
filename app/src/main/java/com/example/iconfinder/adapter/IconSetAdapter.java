package com.example.iconfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iconfinder.R;
import com.example.iconfinder.helpers.MyApplicationHelper;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;
import com.example.iconfinder.ui.IconSetDetail;

import java.util.List;

public class IconSetAdapter extends RecyclerView.Adapter<IconSetAdapter.MyViewHolder>{

    private Context context;
    private List<IconSetModel> iconSetModelList;

    public IconSetAdapter(Context context, List<IconSetModel> iconSetModelList) {
        this.context = context;
        this.iconSetModelList = iconSetModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iconset_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final IconSetModel transferModel = this.iconSetModelList.get(position);
        holder.iconSetName.setText(transferModel.getName());

        if (!this.iconSetModelList.get(position).getIcons().isEmpty()) {
            Glide.with(context).load(transferModel.getIcons().get(0).getPreviewUrl()).into(holder.iconImage1);
            if (transferModel.getIcons().get(0).isPremium()) {
                holder.isPrime1.setVisibility(View.VISIBLE);
            } else {
                holder.isPrime1.setVisibility(View.GONE);
            }
            Glide.with(context).load(transferModel.getIcons().get(1).getPreviewUrl()).into(holder.iconImage2);
            if (transferModel.getIcons().get(1).isPremium()) {
                holder.isPrime2.setVisibility(View.VISIBLE);
            } else {
                holder.isPrime2.setVisibility(View.GONE);
            }
            Glide.with(context).load(transferModel.getIcons().get(2).getPreviewUrl()).into(holder.iconImage3);
            if (transferModel.getIcons().get(2).isPremium()) {
                holder.isPrime3.setVisibility(View.VISIBLE);
            } else {
                holder.isPrime3.setVisibility(View.GONE);
            }
            Glide.with(context).load(transferModel.getIcons().get(3).getPreviewUrl()).into(holder.iconImage4);
            if (transferModel.getIcons().get(3).isPremium()) {
                holder.isPrime4.setVisibility(View.VISIBLE);
            } else {
                holder.isPrime4.setVisibility(View.GONE);
            }

            holder.mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, IconSetDetail.class);
                    ((MyApplicationHelper) context.getApplicationContext()).setIconSetModel(transferModel);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (this.iconSetModelList != null){
            return iconSetModelList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView iconSetName;
        ImageView iconImage1,iconImage2,iconImage3,iconImage4;
        ImageView isPrime1,isPrime2,isPrime3,isPrime4;
        CardView mainCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iconSetName = itemView.findViewById(R.id.iconset_name);
            iconImage1 = itemView.findViewById(R.id.icon_image_1);
            iconImage2 = itemView.findViewById(R.id.icon_image_2);
            iconImage3 = itemView.findViewById(R.id.icon_image_3);
            iconImage4 = itemView.findViewById(R.id.icon_image_4);
            isPrime1 = itemView.findViewById(R.id.prime_star_1);
            isPrime2 = itemView.findViewById(R.id.prime_star_2);
            isPrime3 = itemView.findViewById(R.id.prime_star_3);
            isPrime4 = itemView.findViewById(R.id.prime_star_4);
            mainCard = itemView.findViewById(R.id.main_card);
        }
    }
}
