package com.example.musicapp.listsong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.ImageCacheHelper;
import com.example.musicapp.R;

import java.util.ArrayList;

public class ListSongRecyclerAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ListSongRecyclerAdaper";
    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private ImageCacheHelper mImageCacheHelper;
    public MultiClickAdapterListener mListener;



    public ListSongRecyclerAdaper(Context context, ArrayList<SongModel> listSong, MultiClickAdapterListener listener) {
        mContext = context;
        mListSong = listSong;
        mImageCacheHelper = new ImageCacheHelper(R.mipmap.music_128_foreground);
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song, viewGroup, false);
//            ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
            return new HolderSongRecyclerView(view, mListener);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_circle, viewGroup, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HolderSongRecyclerView) {
            showSongItem((HolderSongRecyclerView) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoading((LoadingViewHolder) viewHolder, i);
        }

    }

    private void showSongItem(HolderSongRecyclerView viewHolder, int position) {
        SongModel songModel = mListSong.get(position);
        viewHolder.bindContent(songModel);
//        viewHolder.btnOptionSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: OPTION MENU CLICK " + v.getId());
////                Toast.makeText(MainActivity.getMainActivity(),"CLICK OPTION MENU",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void showLoading(LoadingViewHolder viewHolder, int position) {

    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolderRecycler viewHolderRecycler, int i) {
//        SongModel songModel = mListSong.get(i);
//        viewHolderRecycler.bindContent(songModel);
//    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
//        Log.d(TAG, "onViewRecycled: ");

    }

    @Override
    public int getItemViewType(int position) {
        return mListSong.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mListSong == null ? 0 : mListSong.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    private class ViewHolderRecycler extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//
//        TextView titleSong;
//        TextView artist;
//        TextView duration;
//        ImageView imageView;
//        ImageButton btnOptionSong;
//
//        CardView layoutItemSong;
//
//        ViewHolderRecycler(@NonNull View itemView, MultiClickAdapterListener listenerCustom) {
//            super(itemView);
//
//            this.titleSong = itemView.findViewById(R.id.txtTitle);
////            this.album=album;
//            this.artist = itemView.findViewById(R.id.txtArtist);
//            this.imageView = itemView.findViewById(R.id.imgSong);
//            this.duration = itemView.findViewById(R.id.txtDuration);
//            this.btnOptionSong = itemView.findViewById(R.id.btnOptionSong);
//            layoutItemSong = itemView.findViewById(R.id.layoutItemSong);
//            btnOptionSong.setTag("btnOptionSong");
//            titleSong.setTag("txtTitleSOng");
////            this.duration.setVisibility(View.GONE);
////            this.imageView.setVisibility(View.VISIBLE);
//
//            btnOptionSong.setOnClickListener(this);
//            layoutItemSong.setOnClickListener(this);
//            layoutItemSong.setOnLongClickListener(this);
//
//        }
//
//        @SuppressLint("SetTextI18n")
//        void bindContent(SongModel songModel) {
//            Log.d(TAG, "bindContent: BIND CONTENT");
////            this.btnOptionSong.setTag("btnOptionSong");
//            this.titleSong.setText(songModel.getTitle());
//            this.artist.setText(songModel.getArtist() + "_" + songModel.getAlbumId());
//            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
////CACHE
//            final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
//            if (bitmap != null) {
//                this.imageView.setImageBitmap(bitmap);
//            } else {
//                mImageCacheHelper.loadAlbumArt(imageView, songModel);
////                loadAlbumArt(this.imageView, songModel);
//            }
//
////\CACHE
//
////GLIDE
////            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
////            mediaMetadataRetriever.setDataSource(songModel.getPath());
////            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
////            Glide
////                    .with(mContext)
////                    .load(mediaMetadataRetriever.getEmbeddedPicture())
//////                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//////                    .dontTransform()
////                    .override(68, 68)
////                    .thumbnail(1.0f)
////                    .into(new SimpleTarget<Drawable>() {
////                        @Override
////                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
////                            imageView.setImageDrawable(resource);
////                            imageView.setDrawingCacheEnabled(true);
////
////                        }
////                    });
////\GLIDE
////PICASSO
////            Uri sArtworkUri = Uri.parse("content://media//external/audio/albumart");
////            Uri imageUri = Uri.withAppendedPath(sArtworkUri, String.valueOf(songModel.getAlbumId()));
////            Bitmap bitmap = null;
////            try {
////                bitmap = BitmapFactory.decodeStream(MainActivity.getMainActivity().getContentResolver().openInputStream(imageUri));
////
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            Log.d(TAG, "bindContent: BITMAP URL"+ (bitmap != null ? bitmap.getByteCount() : 0));
////            imageView.setImageBitmap(bitmap);
////            imageView.setImageURI(imageUri);
////            Log.d(TAG, "bindContent: URI IMAGE" + imageUri.toString());
////            Picasso.get().load(imageUri).into(imageView);
//
////\PICASSO
////BITMAP WITH ASYNC TASK
////            if (cancelPotentialWork(songModel.getPath(), imageView)) {
////                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
////                final AsyncDrawable asyncDrawable = new AsyncDrawable(null, task);
////                imageView.setImageDrawable(asyncDrawable);
////                task.execute(songModel.getPath());
////            }
//
////BITMAP WITH ASYNC TASK
////BITMAP CACHE
////            Bitmap bitmap = CacheHelper.Instance().getBitmapFromMemCache(songModel.getPath());
////            if (bitmap != null){
////                imageView.setImageBitmap(bitmap);
////            }
////            else{
////                CacheHelper.Instance().addBitmapToMemoryCache(songModel.getPath());
////                imageView.setImageResource(R.mipmap.music_file_128);
////            }
//
//
////            }
//
////            new BitmapWorkerTask(imageView).execute(songModel.getPath());
//
////            if (this.imageView.getDrawable() == null) {
////            ParamImageThread paramImageThread = new ParamImageThread(this.imageView, songModel.getPath());
////            new loadImageFromStorage().execute(paramImageThread);
////            }
//
//
//        }
//
//
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.btnOptionSong) {
//                Log.d(TAG, "onClick: CLICK OPTION MENU");
//                mListener.optionMenuClick(v, getAdapterPosition());
//            } else {
//                Log.d(TAG, "onClick: ITEM SONG");
//                mListener.layoutItemClick(v, getAdapterPosition());
//            }
////            listenerRef.get().onPositionClicked(getAdapterPosition());
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            Log.d(TAG, "onLongClick: " + v.getId());
//            mListener.layoutItemLongClick(v, getAdapterPosition());
////            listenerRef.get().onLongClicked(getAdapterPosition());
//            return true;
//        }
//    }


//    private class LoadingViewHolder extends RecyclerView.ViewHolder {
//        ProgressBar progressBar;
//
//        LoadingViewHolder(@NonNull View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progressBarCircle);
//        }
//    }


//    private static class AsyncDrawable extends BitmapDrawable {
//        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;
//
//        AsyncDrawable(Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
////            super(resources, bitmap);
//            bitmapWorkerTaskWeakReference = new WeakReference<>(bitmapWorkerTask);
//
//        }
//
//        BitmapWorkerTask getBitmapWorkerTask() {
//            return bitmapWorkerTaskWeakReference.get();
//        }
//
//    }


}
