package com.pothiwala.atirek.onlinemediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;

/**
 * Created by Alm on 6/30/2016.
 */
public class timepass extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_button);

        final FABProgressCircle fabProgressCircle;
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.progress);

        ImageView floatingActionButton = (ImageView) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
            }
        });

    }
}
