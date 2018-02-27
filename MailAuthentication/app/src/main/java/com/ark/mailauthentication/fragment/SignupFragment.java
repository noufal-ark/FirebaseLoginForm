package com.ark.mailauthentication.fragment;


import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.activity.AuthendicateActivity;
import com.ark.mailauthentication.model.UserDetail;
import com.ark.mailauthentication.util.AppConstants;
import com.ark.mailauthentication.util.ErrorToastTemplate;
import com.ark.mailauthentication.util.SuccessToastTemplate;
import com.ark.mailauthentication.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {
    private static View rootView;
    private static EditText fullName;
    private static EditText emailId;
    private static EditText mobileNumber;
    private static EditText location;
    private static EditText password;
    private static EditText confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox termsConditions;
    private static LinearLayout signupLayout;


    private static Animation shakeAnimation;

    private FirebaseAuth mAuth;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);
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
        termsConditions = rootView.findViewById(R.id.terms_conditions);

        signupLayout = rootView.findViewById(R.id.signUpLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            termsConditions.setTextColor(csl);
        } catch (Exception e) {
            //Do nothing
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
                new AuthendicateActivity().replaceLoginFragment();
                break;
            default://Do nothing
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
        Pattern p = Pattern.compile(Utils.REG_EX);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0) {

            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "All fields are required.");
            signupLayout.startAnimation(shakeAnimation);
        }
        // Check if email id valid or not
        else if (!m.find()) {

            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Your Email Id is Invalid.");
            emailId.startAnimation(shakeAnimation);

        }// Make sure user should check Mobile Number
        else if (mobileNumber.length() > 9) {

            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Your Phone Number Is Invalid.");
            mobileNumber.startAnimation(shakeAnimation);
        }
        // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword)) {

            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Both password doesn't match.");
            password.startAnimation(shakeAnimation);
            confirmPassword.startAnimation(shakeAnimation);
        }

        // Make sure user should check Terms and Conditions checkbox
        else if (!termsConditions.isChecked()) {

            new ErrorToastTemplate().showToast(getActivity(), rootView,
                    "Please select Terms and Conditions.");
            termsConditions.startAnimation(shakeAnimation);
        }
        // Else do signup or do your stuff
        else
            signupUser(getFullName, getEmailId, getMobileNumber, getLocation, getPassword);
    }

    /**
     * Sign up using firbase
     *
     * @param getFullName     full name of user
     * @param getEmailId      email id of user
     * @param getMobileNumber mobile no of user
     * @param getLocation     location of user
     * @param getPassword     password of user
     */
    private void signupUser(final String getFullName, final String getEmailId, final String getMobileNumber, final String getLocation, final String getPassword) {
        mAuth.createUserWithEmailAndPassword(getEmailId, getPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            new ErrorToastTemplate().showToast(getActivity(), rootView, task.getException().getMessage());
                            signupLayout.startAnimation(shakeAnimation);
                        }

                    }

                    private void updateUI(FirebaseUser user) {
                        user.sendEmailVerification();
                        addNewUser(user);
                        new SuccessToastTemplate().showToast(getActivity(), rootView, "Account created successfully");
                        replaceLoginFragment(getEmailId, getPassword);
                    }

                    private void replaceLoginFragment(String emailId, String password) {
                        Bundle args = new Bundle();
                        args.putString(AppConstants.USER_EMAIL, emailId);
                        args.putString(AppConstants.USER_PASS, password);
                        LoginFragment loginFragment = new LoginFragment();
                        loginFragment.setArguments(args);
                        getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                                .replace(R.id.frameContainer, loginFragment,
                                        Utils.LOGIN_FRAGMENT).commit();
                    }

                    private void addNewUser(FirebaseUser user) {
                        String userId = user.getUid();
                        UserDetail detail = new UserDetail(getFullName, getMobileNumber, getLocation);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child(AppConstants.USER_DETAILS).child(userId).setValue(detail);
                    }
                });
    }


}
