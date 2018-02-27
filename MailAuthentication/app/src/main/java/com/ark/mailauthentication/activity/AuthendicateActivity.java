package com.ark.mailauthentication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ark.mailauthentication.R;
import com.ark.mailauthentication.fragment.LoginFragment;
import com.ark.mailauthentication.util.AdvancedSharedPreference;
import com.ark.mailauthentication.util.AppConstants;
import com.ark.mailauthentication.util.Utils;

public class AuthendicateActivity extends AppCompatActivity implements View.OnClickListener {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authendicate);

        fragmentManager = getSupportFragmentManager();

        AdvancedSharedPreference advPreference = new AdvancedSharedPreference(this);


        /**
         * If savedinstnacestate is null then replace login fragment
         */
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(), Utils.LOGIN_FRAGMENT).commit();
        }

        /**
         * On close icon click finish activity
         */
        findViewById(R.id.close_activity).setOnClickListener(this);

        if (advPreference.getBoolean(AppConstants.REMINED_ME)) {
            openHomeActivity();
        }
    }

    public void openHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close_activity) {
            finish();
        }
    }


    /**
     * Replace Login Fragment with animation
     */
    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new LoginFragment(),
                        Utils.LOGIN_FRAGMENT).commit();
    }

    @Override
    public void onBackPressed() {
        // Find the tag of signup and forgot password fragment
        Fragment signUpFragment = fragmentManager
                .findFragmentByTag(Utils.SIGN_UP_FRAGMENT);
        Fragment forgotPasswordFragment = fragmentManager
                .findFragmentByTag(Utils.FORGOT_PASS_FRAGMENT);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (signUpFragment != null)
            replaceLoginFragment();
        else if (forgotPasswordFragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

}
