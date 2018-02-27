package com.ark.mailauthentication.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.mailauthentication.R;

/**
 * Created by noufal on 23/2/18.
 */

public class ErrorToastTemplate {
    public void showToast(Context context, View view, String error) {
        /**
         * Layout Inflater for inflating custom view
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        /**
         * inflate the layout over view
         */
        View layout = inflater.inflate(R.layout.toast_template, (ViewGroup) view.findViewById(R.id.toast_root));


        /**
         * Get TextView id and set error
         */
        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(error);
        /**
         *  Get Toast Context
         *  Set Toast gravity and Fill Horizoontal
         *  Set Duration
         *  Set Custom View over toast
         *  Finally show toast
         */
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();


    }
}
