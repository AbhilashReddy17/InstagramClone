package app.com.abhi.android.instagramclone.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.com.abhi.android.instagramclone.R;
import app.com.abhi.android.instagramclone.UI.InstagramHomeActivity;

public class MainActivity extends AppCompatActivity {

    //getting references

    TextView memailText,mPasswordText,mLogin,mSignup;

    //firebaseAuth

    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthstatelistener;

    //progress dialog
    ProgressDialog mprogressdialog;

    @Override
    protected void onResume() {
        super.onResume();
        mauth.addAuthStateListener(mauthstatelistener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mauth.removeAuthStateListener(mauthstatelistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureViews();

        mauthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    movetoHome();
                    mprogressdialog.dismiss();
                }
                else{
                    mprogressdialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login not successful", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mauth.addAuthStateListener(mauthstatelistener);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memailText.getText().toString().trim();
                String password = mPasswordText.getText().toString().trim();

                loginUser(email,password);
            }
        });

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loginUser(String email,String password) {
        mprogressdialog.setTitle("");
        mprogressdialog.setMessage("Please wait while we try to log you in");
        mprogressdialog.show();
        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)) {
            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mprogressdialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        movetoHome();
                    } else {
                        mprogressdialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else
        {
            Toast.makeText(this, "emailID or password is missing please check", Toast.LENGTH_SHORT).show();
        }

    }

    private void movetoHome() {
        Intent intent = new Intent(MainActivity.this,InstagramHomeActivity.class);
        startActivity(intent);
    }

    private void configureViews() {
        memailText = (TextView) findViewById(R.id.email_text_login);
        mPasswordText = (TextView) findViewById(R.id.password_text_login);
        mLogin= (TextView) findViewById(R.id.login_text_button);
        mSignup = (TextView) findViewById(R.id.signup_text_button);
mprogressdialog = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();

    }
}
