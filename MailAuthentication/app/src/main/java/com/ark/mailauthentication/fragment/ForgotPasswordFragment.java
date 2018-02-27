package com.ark.mailauthentication.fragment;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.activity.AuthendicateActivity;
import com.ark.mailauthentication.util.AppConstants;
import com.ark.mailauthentication.util.ErrorToastTemplate;
import com.ark.mailauthentication.util.SuccessToastTemplate;
import com.ark.mailauthentication.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private static View rootView;

    private static EditText emailId;
    private static TextView submit;
    private static TextView back;

    private static Animation shakeAnimation;

    private FirebaseAuth mAuth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initViews();
        initFirebase();
        setListeners();
        return rootView;
    }

    /**
     * Initialize firebase database
     */
    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
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
            // Do nothing
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
                new AuthendicateActivity().replaceLoginFragment();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;
            default://Do nothing

        }
    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.REG_EX);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0) {
            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Please enter your Email Id.");
            emailId.startAnimation(shakeAnimation);
        }

        // Check if email id is valid or not
        else if (!m.find()) {
            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Your Email Id is Invalid.");
            emailId.startAnimation(shakeAnimation);
        }
        // Else submit email id and fetch passwod or do your stuff
        else
            forgotPassword(getEmailId);
    }

    /**
     * To communicate with user password
     *
     * @param getEmailId
     */
    private void forgotPassword(String getEmailId) {
        final String mail = getEmailId;
        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    new SuccessToastTemplate().showToast(getActivity(), rootView, "Reset password send to " + mail);
                    replaceLoginFragment(mail);
                } else {
                    new ErrorToastTemplate().showToast(getActivity(), rootView, task.getException().getMessage());
                }
            }
        });

    }


    private void replaceLoginFragment(String emailId) {
        Bundle args = new Bundle();
        args.putString(AppConstants.USER_EMAIL, emailId);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(args);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, loginFragment,
                        Utils.LOGIN_FRAGMENT).commit();
    }
}
