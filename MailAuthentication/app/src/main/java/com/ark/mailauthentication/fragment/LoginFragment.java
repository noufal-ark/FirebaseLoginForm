package com.ark.mailauthentication.fragment;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
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
import android.widget.Toast;

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.util.ToastTemplate;
import com.ark.mailauthentication.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static View rootView;
    private static FragmentManager fragmentManager;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews();
        setListeners();
        return rootView;
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
        show_hide_password = rootView.findViewById(R.id.show_hide_password);
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
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    /**
     * set Listeners
     */
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        show_hide_password.setOnCheckedChangeListener(this);
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
                        .replace(R.id.frameContainer, new ForgotPasswordFragment(), Utils.ForgotPasswordFragment).commit();
                break;
            case R.id.createAccount:

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new SignupFragment(), Utils.SignUpFragment).commit();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If it is check then show password else hide
        // password
        if (isChecked) {

            show_hide_password.setText(R.string.hide_pwd);// change
            // checkbox
            // text

            password.setInputType(InputType.TYPE_CLASS_TEXT);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
        } else {
            show_hide_password.setText(R.string.show_pwd);// change
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
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);
            new ToastTemplate().show_Toast(getActivity(), rootView,"Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find()){
            emailid.startAnimation(shakeAnimation);
            new ToastTemplate().show_Toast(getActivity(), rootView,"Your Email Id is Invalid.");
        }
            // Else do login and do your stuff
        else
            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
                    .show();

    }
}
