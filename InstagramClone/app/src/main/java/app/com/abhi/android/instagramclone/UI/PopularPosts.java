package app.com.abhi.android.instagramclone.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.abhi.android.instagramclone.R;

public class PopularPosts extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static PopularPosts newInstance() {
        PopularPosts fragment = new PopularPosts();
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
        return inflater.inflate(R.layout.fragment_popular_posts, container, false);
    }
}
