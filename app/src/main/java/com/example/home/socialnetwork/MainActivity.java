package com.example.home.socialnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.home.socialnetwork.fragments.AlbumFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.PHOTOS};
    private CallbackManager callbackManager;
    private ImageButton vk, fb;
    private Button logoutFB, albumsFB,logoutVK, albumsVK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coose_social_network);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        vk = (ImageButton) findViewById(R.id.vk_button);
        fb = (ImageButton) findViewById(R.id.fb_button);
        albumsFB = (Button) findViewById(R.id.albumsFB);
        albumsFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFBAlbums(Profile.getCurrentProfile());
            }
        });
        albumsFB.setVisibility(View.INVISIBLE);
        logoutFB = (Button) findViewById(R.id.logoutFB);
        logoutFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                logoutFB.setVisibility(View.INVISIBLE);
                albumsFB.setVisibility(View.INVISIBLE);

            }
        });
        logoutFB.setVisibility(View.INVISIBLE);
        albumsVK = (Button) findViewById(R.id.albumsVK);
        albumsVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayVkAlbums();
            }
        });
        albumsVK.setVisibility(View.INVISIBLE);
        logoutVK = (Button) findViewById(R.id.logoutVK);
        logoutVK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
                logoutVK.setVisibility(View.INVISIBLE);
                albumsVK.setVisibility(View.INVISIBLE);

            }
        });
        logoutVK.setVisibility(View.INVISIBLE);
        vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVk();
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFB();
            }
        });
    }

    void startFB() {
        if (Profile.getCurrentProfile() == null) {
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    albumsFB.setVisibility(View.VISIBLE);
                    logoutFB.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos"));
        } else {
            albumsFB.setVisibility(View.VISIBLE);
            logoutFB.setVisibility(View.VISIBLE);
        }
    }

    void startVk() {
if (VKSdk.isLoggedIn()==false){
    VKSdk.login(MainActivity.this, scope);
} else {
    albumsVK.setVisibility(View.VISIBLE);
    logoutVK.setVisibility(View.VISIBLE);
}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                albumsVK.setVisibility(View.VISIBLE);
                logoutVK.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(VKError error) {

            }
        }))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayFBAlbums(Profile profile) {
        if (profile != null) {
            FbWorkClass photoLink = new FbWorkClass(getApplicationContext(),
                    new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>());
            photoLink.getFBAlbums();
            startActivity(Constants.Extra.FB,
                    photoLink.getName(),
                    photoLink.getUrl(),
                    photoLink.getId_album());
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    void startActivity(String socialnetwork, ArrayList<String> name, ArrayList<String> url, ArrayList<String> id_album) {
        Intent intent = new Intent(getBaseContext(), SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, AlbumFragment.INDEX);
        intent.putExtra(Constants.Extra.SOCIAL, socialnetwork);
        intent.putStringArrayListExtra(Constants.Extra.NAME_OF_ALBUM, name);
        intent.putStringArrayListExtra(Constants.Extra.URL, url);
        intent.putStringArrayListExtra(Constants.Extra.ALBUM_ID, id_album);
        startActivity(intent);
    }

    private void displayVkAlbums() {
        if (VKSdk.isLoggedIn()) {
            VkWorkClass vkWorkClass = new VkWorkClass(new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>());
            vkWorkClass.getVkAlbums();
            startActivity(Constants.Extra.VK,
                    vkWorkClass.getName(),
                    vkWorkClass.getUrl(),
                    vkWorkClass.getId_album());
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in VK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VKSdk.isLoggedIn()){
            albumsVK.setVisibility(View.VISIBLE);
            logoutVK.setVisibility(View.VISIBLE);
        }
        if (Profile.getCurrentProfile()!=null){
            albumsFB.setVisibility(View.VISIBLE);
            logoutFB.setVisibility(View.VISIBLE);
        }

    }
}

/**    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        vk_button = (Button) findViewById(R.id.vk_button);
        vk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, scope);
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (LoginButton) findViewById(R.id.loginbutton);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("user_photos");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFBAlbums(Profile.getCurrentProfile());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                displayVkAlbums();
            }

            @Override
            public void onError(VKError error) {

            }
        }))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayFBAlbums(Profile profile) {
        if (profile != null) {
            FbWorkClass photoLink = new FbWorkClass(getApplicationContext(),
                    new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>());
            photoLink.getFBAlbums();
            startActivity(Constants.Extra.FB,
                    photoLink.getName(),
                    photoLink.getUrl(),
                    photoLink.getId_album());
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayVkAlbums() {
        if (VKSdk.isLoggedIn()) {
            VkWorkClass vkWorkClass = new VkWorkClass(new ArrayList<String>(),
                    new ArrayList<String>(),
                    new ArrayList<String>());
            vkWorkClass.getVkAlbums();
            startActivity(Constants.Extra.VK,
                    vkWorkClass.getName(),
                    vkWorkClass.getUrl(),
                    vkWorkClass.getId_album());
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in VK", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void startActivity(String socialnetwork, ArrayList<String> name, ArrayList<String> url, ArrayList<String> id_album) {
        Intent intent = new Intent(getBaseContext(), SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, AlbumFragment.INDEX);
        intent.putExtra(Constants.Extra.SOCIAL, socialnetwork);
        intent.putStringArrayListExtra(Constants.Extra.NAME_OF_ALBUM, name);
        intent.putStringArrayListExtra(Constants.Extra.URL, url);
        intent.putStringArrayListExtra(Constants.Extra.ALBUM_ID, id_album);
        startActivity(intent);
    }

} **/
