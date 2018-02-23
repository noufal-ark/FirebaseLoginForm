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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
public class SignupFragment extends Fragment implements View.OnClickListener {
    private static View rootView;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static LinearLayout signupLayout;


    private static Animation shakeAnimation;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews();
        setListeners();
        return rootView;
    }

    /**
     * Initialize all views
     */
    private void initViews() {

        fullName = rootView.findViewById(R.id.fullName);
        emailId = rootView.findViewById(R.id.userEmailId);
        mobileNumber = rootView.findViewById(R.id.mobileNumber);
        location = rootView.findViewById(R.id.location);
        password = rootView.findViewById(R.id.password);
        confirmPassword = rootView.findViewById(R.id.confirmPassword);
        signUpButton = rootView.findViewById(R.id.signUpBtn);
        login = rootView.findViewById(R.id.already_user);
        terms_conditions = rootView.findViewById(R.id.terms_conditions);

        signupLayout = rootView.findViewById(R.id.signUpLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }

    /**
     * Check Validation Method
     */
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0) {

            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "All fields are required.");
            signupLayout.startAnimation(shakeAnimation);
        }
        // Check if email id valid or not
        else if (!m.find()) {

            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "Your Email Id is Invalid.");
            emailId.startAnimation(shakeAnimation);

        }
        // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword)) {

            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "Both password doesn't match.");
            password.startAnimation(shakeAnimation);
            confirmPassword.startAnimation(shakeAnimation);
        }

        // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked()) {

            new ToastTemplate().show_Toast(getActivity(), rootView,
                    "Please select Terms and Conditions.");
            terms_conditions.startAnimation(shakeAnimation);
        }
        // Else do signup or do your stuff
        else
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
    }
}
