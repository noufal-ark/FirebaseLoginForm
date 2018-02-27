package com.ark.mailauthentication.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.activity.HomeActivity;
import com.ark.mailauthentication.util.AdvancedSharedPreference;
import com.ark.mailauthentication.util.AppConstants;
import com.ark.mailauthentication.util.ErrorToastTemplate;
import com.ark.mailauthentication.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static View rootView;
    private static FragmentManager fragmentManager;

    private static EditText emailid;
    private static EditText password;
    private static Button loginButton;
    private static TextView forgotPassword;
    private static TextView signUp;
    private static CheckBox showHidePassword;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private FirebaseAuth mAuth;
    private AdvancedSharedPreference advPreference;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        advPreference = new AdvancedSharedPreference(getContext());
        initViews();
        initFirebase();
        setBundle();
        setListeners();
        return rootView;
    }

    private void setBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            emailid.setText(bundle.getString(AppConstants.USER_EMAIL));
            password.setText(bundle.getString(AppConstants.USER_PASS));
        }
    }


    /**
     * Initialize firebase database
     */
    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Initiate Views
     */
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        emailid = rootView.findViewById(R.id.login_emailid);
        password = rootView.findViewById(R.id.login_password);
        loginButton = rootView.findViewById(R.id.loginBtn);
        forgotPassword = rootView.findViewById(R.id.forgot_password);
        signUp = rootView.findViewById(R.id.createAccount);
        showHidePassword = rootView.findViewById(R.id.show_hide_password);
        loginLayout = rootView.findViewById(R.id.login_layout);

        /**
         * Load ShakeAnimation
         */
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        /**
         * Setting text selector over textviews
         */
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);

            forgotPassword.setTextColor(csl);
            showHidePassword.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
            // Do nothing
        }

    }

    /**
     * set Listeners
     */
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        showHidePassword.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new ForgotPasswordFragment(), Utils.FORGOT_PASS_FRAGMENT).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignupFragment(), Utils.SIGN_UP_FRAGMENT).commit();
                break;
            default://Do nothing
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If it is check then show password else hide
        // password
        if (isChecked) {

            showHidePassword.setText(R.string.hide_pwd);// change
            // checkbox
            // text

            password.setInputType(InputType.TYPE_CLASS_TEXT);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
        } else {
            showHidePassword.setText(R.string.show_pwd);// change
            // checkbox
            // text

            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password
        }
    }


    /**
     * Check Validation before login
     */
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check pattern for email id
        Pattern p = Pattern.compile(Utils.REG_EX);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new ErrorToastTemplate().showToast(getActivity(), rootView, "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find()) {
            emailid.startAnimation(shakeAnimation);
            new ErrorToastTemplate().showToast(getActivity(), rootView, "Your Email Id is Invalid.");
        }
        // Else do login and do your stuff
        else
            loginUser(getEmailId, getPassword);

    }

    private void loginUser(String getEmailId, String getPassword) {
        mAuth.signInWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        advPreference.putBoolean(AppConstants.REMINED_ME, true);
                        openHomeActivity();
                    } else {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    new ErrorToastTemplate().showToast(getActivity(), rootView, "Please verify your mail id");

                                } else {
                                    new ErrorToastTemplate().showToast(getActivity(), rootView, task.getException().getMessage());
                                }
                            }
                        });
                    }

                } else {
                    new ErrorToastTemplate().showToast(getActivity(), rootView, task.getException().getMessage());
                }
            }
        });
    }

    private void openHomeActivity() {
        getActivity().finish();
        startActivity(new Intent(getContext(), HomeActivity.class));
    }
}
