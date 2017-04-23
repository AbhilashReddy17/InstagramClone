package app.com.abhi.android.instagramclone.UserProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class SignUpActivity extends AppCompatActivity {

    TextView memailTextview,mPasswordTextview,msignupButton;

    //Firebase Authentication ID

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthstateListener;

    //progress dialog
    ProgressDialog mprogressdialog;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAuth.addAuthStateListener(mAuthstateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthstateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        configureViews();

        mAuthstateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    moveToHome();
                }
            }
        };
mAuth.addAuthStateListener(mAuthstateListener);

        msignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mprogressdialog.setTitle("create Account");
                    mprogressdialog.setMessage("please wait while we create your account..");
                    mprogressdialog.show();
                }
                createAccount();
            }
        });

    }

    private void createAccount() {
        Log.d("signup Activity","entered creatAccount()");
        String email = memailTextview.getText().toString().trim();
        String password = mPasswordTextview.getText().toString().trim();
Log.d("email & password signup",email+"\n"+password);
        if(! TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password))
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    mprogressdialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Account succefully created", Toast.LENGTH_SHORT).show();
                    moveToHome();
                }
                else{
                    mprogressdialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void moveToHome() {
        Intent intent = new Intent(SignUpActivity.this,InstagramHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void configureViews() {
        mprogressdialog = new ProgressDialog(this);
        msignupButton = (TextView) findViewById(R.id.signup_button_signup);
        memailTextview = (TextView) findViewById(R.id.email_text_signup);
        mPasswordTextview = (TextView) findViewById(R.id.password_text_signup);
        mAuth = FirebaseAuth.getInstance();

    }
}
