package com.example.mydorm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    //define attributes
    Context context;
    ArrayList<String> downloadUrl2FB;
    public ArrayList<String> urlList;
    public ArrayList<String> headerList;
    public ArrayList<Long> priceList;
    ArrayList<String> addressListFB;
    ArrayList<String> ownerListFB;
    ArrayList<String> descListFB;
    ArrayList<String> docIDFB;

    //Feed adapter constructor
    public FeedAdapter(Context context, ArrayList<String> headerList, ArrayList<String> urlList, ArrayList<Long> priceList,
                       ArrayList<String> addressListFB, ArrayList<String> downloadUrl2FB,
                       ArrayList<String> descListFB, ArrayList<String> ownerListFB, ArrayList<String> docIDFB) {
        this.context = context;
        this.headerList = headerList;
        this.urlList = urlList;
        this.priceList = priceList;
        this.addressListFB = addressListFB;
        this.descListFB = descListFB;
        this.ownerListFB = ownerListFB;
        this.downloadUrl2FB = downloadUrl2FB;
        this.docIDFB = docIDFB;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feed_row, parent, false);
        return new FeedViewHolder(view);
    }

    //Ad Details
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.adsHeader.setText(headerList.get(position));
        holder.priceTxt.setText(priceList.get(position).toString());
        Picasso.get().load(urlList.get(position))
                .into(holder.adsPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("address", addressListFB.get(position));
                intent.putExtra("price", priceList.get(position).toString());
                intent.putExtra("photo1", urlList.get(position));
                intent.putExtra("photo2", downloadUrl2FB.get(position));
                intent.putExtra("head", headerList.get(position));
                intent.putExtra("desc", descListFB.get(position));
                intent.putExtra("owner", ownerListFB.get(position));
                intent.putExtra("docId", docIDFB.get(position));
                System.out.println(downloadUrl2FB.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }
    //Show Ad on Dashboard
    public class FeedViewHolder extends RecyclerView.ViewHolder {

        ImageView adsPhoto;
        TextView adsHeader, priceTxt;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            adsPhoto = itemView.findViewById(R.id.adsPhoto);
            adsHeader = itemView.findViewById(R.id.adsHeader);
            priceTxt = itemView.findViewById(R.id.price_row);

        }
    }


}

