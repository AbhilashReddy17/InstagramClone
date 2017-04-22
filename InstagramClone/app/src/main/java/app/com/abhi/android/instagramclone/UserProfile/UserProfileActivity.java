package app.com.abhi.android.instagramclone.UserProfile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import app.com.abhi.android.instagramclone.InstagramHomeActivity;
import app.com.abhi.android.instagramclone.R;

public class UserProfileActivity extends AppCompatActivity {

    TextView musername, mStatus,msaveprofile;
    ImageView mprofilepicture;
    AlertDialog.Builder builder = null;
    Uri imageHoldUri = null;

    //FirebaseReference

    FirebaseAuth mauth;
    FirebaseAuth.AuthStateListener mauthstatelistener;
    DatabaseReference mdatabasereferene;
    StorageReference mstoragereference;
    private int RC_CAMERA = 1;
    private int RC_GALLERY=2;

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
        setContentView(R.layout.activity_user_profile);

        configureViews();

        mauthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    movetoActivity(InstagramHomeActivity.class);
                }
            }
        };
        mauth.addAuthStateListener(mauthstatelistener);

        mprofilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectprofilePicture();

            }
        });

        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = musername.getText().toString().trim();
                String status = mStatus.getText().toString().trim();
                mprogressdialog.setMessage("saving profile please wait");
                mprogressdialog.show();

                    if(!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(status)){

                        mdatabasereferene.child("user_name").setValue(uname);
                        mdatabasereferene.child("status").setValue(status);
                        mdatabasereferene.child("uid").setValue(mauth.getCurrentUser().getUid());

                        if(imageHoldUri != null) {

                           StorageReference storage = mstoragereference.child("user_profile_pic").child(mauth.getCurrentUser().getUid()).child(imageHoldUri.getLastPathSegment());
                            storage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri uri = taskSnapshot.getDownloadUrl();
                                    mdatabasereferene.child("profile_pic").setValue(uri.toString());
                                    mprogressdialog.dismiss();
                                    finish();
                                    movetoActivity(InstagramHomeActivity.class);
                                }
                            });


                        }
                        else{
                            mdatabasereferene.child("profile_pic").setValue(null);
                            mprogressdialog.dismiss();
                        }

                    }
                    else{
                        Toast.makeText(UserProfileActivity.this, "Please enter user name and status", Toast.LENGTH_SHORT).show();
                        mprogressdialog.dismiss();
                    }
            }
        });


    }



    private void selectprofilePicture() {

        final CharSequence[] titles = {"Choose from gallery","Take Photo","cancel"};
        builder.setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(titles[which].equals("Choose from gallery")){
                    chooseFromGallery();
                }
                else if(titles[which].equals("Take Photo")){
                    takePhoto();
                }
                else if(titles[which].equals("cancel")){
                    dialog.dismiss();
                }

            }
        });
        builder.show();

    }

    private void takePhoto() {
        Log.d("useractivity profile","entered take photo");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,RC_CAMERA);

    }

    private void chooseFromGallery() {
        Log.d("useractivity profile","entered choose from gallery");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,RC_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_CAMERA){
            getImage(data);

        }
        else if(resultCode == RESULT_OK && requestCode == RC_GALLERY ){
            getImage(data);
        }


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               imageHoldUri = result.getUri();
                mprofilepicture.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Exception error = result.getError();
            }

        }

    }

    private void getImage(Intent data) {
        Uri imageuri = data.getData();
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    private void movetoActivity(Class activityclass) {
        finish();
        Intent intent = new Intent(this,activityclass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void configureViews() {
        musername = (TextView) findViewById(R.id.username_text_profile);
        mStatus = (TextView) findViewById(R.id.status_text_userprofile);
        mprofilepicture = (ImageView) findViewById(R.id.profile_picture_user);
        msaveprofile = (TextView) findViewById(R.id.save_userprofile_button);
        builder = new AlertDialog.Builder(this);
        mauth = FirebaseAuth.getInstance();
        mdatabasereferene = FirebaseDatabase.getInstance().getReference().child("user_profiles").child(mauth.getCurrentUser().getUid());
        mstoragereference = FirebaseStorage.getInstance().getReference();
        mprogressdialog = new ProgressDialog(this);
    }
}
