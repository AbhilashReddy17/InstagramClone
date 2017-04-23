package app.com.abhi.android.instagramclone.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.abhi.android.instagramclone.R;


public class InstagramPost extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static InstagramPost newInstance() {
        InstagramPost fragment = new InstagramPost();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram_post, container, false);
    }

}
