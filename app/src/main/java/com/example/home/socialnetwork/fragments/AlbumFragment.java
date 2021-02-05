package com.example.home.socialnetwork.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.home.socialnetwork.Constants;
import com.example.home.socialnetwork.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AlbumFragment extends AbsListViewBaseFragment {

    public static final int INDEX = 0;
    String [] images,names;
    List<String> id_album;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
        listView = (GridView) rootView.findViewById(R.id.grid);
        List<String> image = getArguments().getStringArrayList(Constants.Extra.URL);
        images = new String[image.size()];
        images=image.toArray(images);
        List<String> name = getArguments().getStringArrayList(Constants.Extra.NAME_OF_ALBUM);
        names = new String[name.size()];
        names=name.toArray(names);
        id_album = getArguments().getStringArrayList(Constants.Extra.ALBUM_ID);
        ((GridView) listView).setAdapter(new AlbumFragment.ImageAdapter(getActivity(),images,names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startGridActivity(id_album.get(position),getArguments().getString(Constants.Extra.SOCIAL));
            }
        });
        return rootView;
    }

    private static class ImageAdapter extends BaseAdapter {

        private String[] IMAGE_URLS,NAMES;

        private LayoutInflater inflater;

        private DisplayImageOptions options;

        ImageAdapter(Context context,String [] images,String[]names) {
            inflater = LayoutInflater.from(context);
            this.IMAGE_URLS=images;
            this.NAMES=names;

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return IMAGE_URLS.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final AlbumFragment.ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_album_grid, parent, false);
                holder = new AlbumFragment.ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.item_image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.item_progress);
                holder.textView = (TextView) view.findViewById(R.id.item_text);
                view.setTag(holder);
            } else {
                holder = (AlbumFragment.ViewHolder) view.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(IMAGE_URLS[position], holder.imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });
            holder.textView.setText(NAMES[position]);
            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView textView;
    }
}
