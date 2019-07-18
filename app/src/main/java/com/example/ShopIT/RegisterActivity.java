package com.example.ShopIT;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.example.ShopIT.Fragment.SignInFragment;
import com.example.ShopIT.Fragment.SignUpFragment;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
     public static boolean onResetPasswordFragment = false;

     public static boolean setSignUpFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout = findViewById(R.id.register_framelayout);

        if(setSignUpFragment){
            setSignUpFragment = false;
            setDefaultFragment(new SignUpFragment());
        }else {

            setDefaultFragment(new SignInFragment());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
             SignUpFragment.disableCloseBtn = false;
             SignInFragment.disableCloseBtn = false;
             if(onResetPasswordFragment){
               onResetPasswordFragment = false;
               setFragment(new SignInFragment());
               return false;
           }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
