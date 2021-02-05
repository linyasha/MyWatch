package com.example.home.socialnetwork;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.example.home.socialnetwork.fragments.AlbumFragment;
import com.example.home.socialnetwork.fragments.GridFragment;
import com.example.home.socialnetwork.fragments.ImagePagerFragment;
import java.util.ArrayList;

public class SimpleImageActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
        Fragment fr;
        String tag;
        switch (frIndex) {
            default:
            case AlbumFragment.INDEX:
                tag = AlbumFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new AlbumFragment();
                }
                fr.setArguments(getIntent().getExtras());
                break;
            case GridFragment.INDEX:
                tag = GridFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new GridFragment();
                }
                fr.setArguments(choseSocialNetwork());
                break;
            case ImagePagerFragment.INDEX:
                tag = ImagePagerFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if (fr == null) {
                    fr = new ImagePagerFragment();
                    fr.setArguments(getIntent().getExtras());
                }
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
    }

    Bundle choseSocialNetwork() {
        String socialnetwork = getIntent().getStringExtra(Constants.Extra.SOCIAL);
        Bundle bundle1 = new Bundle();
        if (socialnetwork.equals(Constants.Extra.FB)) {
            FbWorkClass fbWorkClass = new FbWorkClass(getApplicationContext(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
            fbWorkClass.getFBAlbumPhoto(getIntent().getStringExtra(Constants.Extra.IMAGE_POSITION));
            bundle1.putStringArrayList(Constants.Extra.PHOTO_LINK, fbWorkClass.getUrl());
            bundle1.putStringArrayList(Constants.Extra.NAME_OF_PHOTO, fbWorkClass.getName());
            bundle1.putStringArrayList(Constants.Extra.CREATED_TIME, fbWorkClass.getId_album());
            return bundle1;
        } else if (socialnetwork.equals(Constants.Extra.VK)) {
            VkWorkClass vkWorkClass = new VkWorkClass(new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
            vkWorkClass.getVkAlbumPhoto(getIntent().getStringExtra(Constants.Extra.IMAGE_POSITION));
            bundle1.putStringArrayList(Constants.Extra.PHOTO_LINK, vkWorkClass.getUrl());
            bundle1.putStringArrayList(Constants.Extra.NAME_OF_PHOTO, vkWorkClass.getName());
            bundle1.putStringArrayList(Constants.Extra.CREATED_TIME, vkWorkClass.getId_album());
            return bundle1;
        }
        return null;
    }
}