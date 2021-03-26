package com.satvick.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.satvick.R;

import java.util.List;

/**
 * Created by ADMIN on 2/12/2017.
 */

public class FullImagePopUpWithZoom extends Dialog {
    //remember,here we must take context
    Context context;
    TouchImageView imageView;
    ImageView ivClose;
    int[] image;
   // List<Integer> color;
    int position;

    List<String> color;


    public FullImagePopUpWithZoom(Context context,List<String> color, int position) {
        super(context);
        this.context=context;
        this.color=color;
        this.position=position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_zoom_image_pop_up);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        imageView=(TouchImageView)findViewById(R.id.imageView);
        Picasso.with(context).load(color.get(position)).into(imageView);
        ivClose=(ImageView)findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
    }
}
