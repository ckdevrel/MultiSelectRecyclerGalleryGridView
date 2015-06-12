package com.recyclegridview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAlbumAdapter extends
        RecyclerView.Adapter<GalleryAlbumAdapter.ViewHolder> {

    private ArrayList<AlbumsModel> mGalleryImagesList;
    private Context mContext;
    static OnItemClickListener mItemClickListener;



    public GalleryAlbumAdapter (Context context, ArrayList<AlbumsModel> galleryImagesList) {
        this.mGalleryImagesList = galleryImagesList;
        this.mContext = context;

    }

    // Create new views
    @Override
    public GalleryAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_gallery_albums, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.tvName.setText(mGalleryImagesList.get(position).getFolderName ());

        Log.i ("--->folderpath",mGalleryImagesList.get (position).getFolderImagePath ());


        Glide.with (mContext)
                .load("file://"+mGalleryImagesList.get (position).getFolderImagePath ())
                .centerCrop()
                .placeholder(R.drawable.image_loading)
                .crossFade()
                .into(viewHolder.imgAlbum);
//        viewHolder.tvEmailId.setText(stList.get(position).getEmailId());


    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return mGalleryImagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvName;
        public ImageView imgAlbum;
        public AlbumsModel singleItem;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);

            imgAlbum = (ImageView) itemLayoutView.findViewById (R.id.img_album);
            itemLayoutView.setOnClickListener(this);

        }

        @Override
        public void onClick (View view) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition ());
            }
        }
    }
    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void  SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    // method to access in activity after updating selection
    public ArrayList<AlbumsModel> getGalleryImagesList() {
        return mGalleryImagesList;
    }

}