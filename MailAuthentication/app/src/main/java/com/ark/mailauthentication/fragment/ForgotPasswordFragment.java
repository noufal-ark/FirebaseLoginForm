package com.ark.mailauthentication.fragment;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.activity.MainActivity;
import com.ark.mailauthentication.util.ToastTemplate;
import com.ark.mailauthentication.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private static View rootView;

    private static EditText emailId;
    private static TextView submit, back;

    private static Animation shakeAnimation;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initViews();
        setListeners();
        return rootView;
    }

    /**
     * Initialize view
     */
    private void initViews() {
        emailId = rootView.findViewById(R.id.registered_emailid);
        submit = rootView.findViewById(R.id.forgot_button);
        back = rootView.findViewById(R.id.backToLoginBtn);

        /**
         * Load ShakeAnimation
         */
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }
    }

    /**
     * Set Listeners over buttons
     */
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                new MainActivity().replaceLoginFragment();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }
    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "Please enter your Email Id.");
            emailId.startAnimation(shakeAnimation);
        }

        // Check if email id is valid or not
        else if (!m.find()) {
            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "Your Email Id is Invalid.");
            emailId.startAnimation(shakeAnimation);
        }
        // Else submit email id and fetch passwod or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }
}
