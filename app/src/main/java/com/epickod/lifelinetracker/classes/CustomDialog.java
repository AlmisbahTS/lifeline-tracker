package com.epickod.lifelinetracker.classes;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;

import com.epickod.lifelinetracker.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
        initDialog(context);
    }

    private void initDialog(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Remove dialog title
        setContentView(R.layout.custom_dialog_layout);  // Set custom layout

        // Make dialog background transparent
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        // Initialize the ProgressBar with the FoldingCube animation
        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        // Set dialog properties to make it non-dismissible
        setCancelable(false);  // Prevent dismissing by back button
        setCanceledOnTouchOutside(false);  // Prevent dismissing by touching outside
    }

    // Method to show the dialog
    public static void showCustomDialog(Context context) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.show();
    }

    // Method to dismiss the dialog
    public static void dismissDialog(Context context) {
        CustomDialog dialog = new CustomDialog(context);
        dialog.dismiss();
    }
}
