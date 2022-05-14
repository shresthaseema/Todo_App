package com.example.todo_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class LaunchScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {

        //Animated the components for launch screen
        LottieAnimationView lottieAnimationView = getView().findViewById(R.id.launch_id);
        lottieAnimationView.playAnimation();
        ImageView launch_background = getView().findViewById(R.id.launch_bg_id);
        launch_background.animate().translationY(-2000).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(3000);
        TextView textView_title = getView().findViewById(R.id.text_title);
        textView_title.animate().translationY(-1900).setDuration(1000).setStartDelay(3000);
        TextView textView_content = getView().findViewById(R.id.content_id);
        textView_content.animate().translationY(1900).setDuration(1000).setStartDelay(3000);
    }
}