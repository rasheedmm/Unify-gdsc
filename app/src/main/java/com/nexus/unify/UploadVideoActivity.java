package com.nexus.unify;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nexus.unify.ModelClasses.Posts;


import com.nexus.unify.databinding.ActivityUploadVideoBinding;
import com.tylersuehr.socialtextview.SocialTextView;
import com.varunjohn1990.iosdialogs4android.IOSDialog;


import java.io.File;
import java.io.InputStream;
import java.util.HashMap;



public class UploadVideoActivity extends AppCompatActivity implements SocialTextView.OnLinkClickListener {

    ActivityUploadVideoBinding binding;

    Posts posts;
    long time;
    InputStream stream;
    String myUrl = "", tag;
    boolean on = false;

    Uri videouri;
    EditText ed_title,ed_desc,ed_price;


    ImageView send;
    lottiedialogfragment lottie;
    StorageTask uploadTask;
    StorageReference storageReference;
    ProgressDialog progressDialog;
Button bt_select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());





        posts = new Posts();
        storageReference = FirebaseStorage.getInstance().getReference("Videos");
        ed_title = findViewById(R.id.ed_title);
        ed_desc = findViewById(R.id.ed_desc);
        ed_price = findViewById(R.id.ed_price);

        send = findViewById(R.id.imageView12);

      bt_select= findViewById(R.id.btn_select);





        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/* video/*");
                startActivityForResult(pickIntent, 500);
            }
        });


        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.start();

                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = binding.videoView.getWidth() / (float) binding.videoView.getHeight();
                float scale = videoRatio / screenRatio;
                if (scale >= 1f) {
                    binding.videoView.setScaleX(scale);
                } else {
                    binding.videoView.setScaleY(1f / scale);
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_title.getText().toString().equals("")||ed_desc.getText().toString().equals("")||ed_price.getText().toString().equals("")) {
                    Toast.makeText(UploadVideoActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                } else {
                    new IOSDialog.Builder(getApplicationContext())
                            .title("Confirm?")              // String or String Resource ID
                            .message("Confirm Post?")  // String or String Resource ID
                            .positiveButtonText("Yeah, sure")  // String or String Resource ID
                            .negativeButtonText("Cancel")   // String or String Resource ID
                            .positiveClickListener(new IOSDialog.Listener() {
                                @Override
                                public void onClick(IOSDialog iosDialog) {
                                    iosDialog.dismiss();
                                    progressDialog = new ProgressDialog(UploadVideoActivity.this);
                                    progressDialog.setTitle("Uploading...");
                                    progressDialog.show();
                                    uploadImage();
                                }


                            }).negativeClickListener(new IOSDialog.Listener() {
                                @Override
                                public void onClick(IOSDialog iosDialog) {
                                    iosDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build()
                            .show();
                }
            }
        });


    }





    @Override
    public void onLinkClicked(int linkType, String matchedText) {
        Toast.makeText(this, matchedText, Toast.LENGTH_SHORT).show();
    }

    private String getFileExtensiion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        if (videouri != null) {

            BasicAWSCredentials credentials = new BasicAWSCredentials("AKIA2DL53HZB3AMX27U6", "5PdNwxQAFWlnvXJZzetvclFPw1bg4RzIO30mtABN");
            AmazonS3Client s3 = new AmazonS3Client(credentials);
            s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));


            TransferUtility transferUtility = new TransferUtility(s3, UploadVideoActivity.this);
            File actualImage2 = new File(videouri.getPath());
            time = System.currentTimeMillis();
            TransferObserver observer = transferUtility.upload(
                    "amplify-trinity-master-latticeapp-164128-deployment",
                    time
                            + "." + getFileExtensiion(videouri),
                    actualImage2
            );

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {

                    if (state.COMPLETED.equals(observer.getState())) {
                        myUrl = "https://amplify-trinity-master-latticeapp-164128-deployment.s3.ap-south-1.amazonaws.com/" + time
                                + "." + getFileExtensiion(videouri);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postsnew");
                        String postid = reference.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        if (on == true) {
                            tag = "on";

                        } else {
                            tag = "off";
                        }
                        hashMap.put("anms", tag);
                        hashMap.put("type", "video");
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("url", myUrl);
                        hashMap.put("text", ed_title.getText().toString());
                        hashMap.put("price", ed_price.getText().toString());
                        hashMap.put("desc", ed_desc.getText().toString());
                        reference.child(postid).setValue(hashMap);


                        // Video uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(UploadVideoActivity.this, HomeActivity.class));

                    } else if (state.FAILED.equals(observer.getState())) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    // show the progress bar
                    double progress = (100.0 * bytesCurrent / bytesTotal);
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }

                @Override
                public void onError(int id, Exception ex) {

                    Toast.makeText(UploadVideoActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
            } else if (selectedMediaUri.toString().contains("video")) {
                binding.videoView.setVideoURI(selectedMediaUri);
                videouri = selectedMediaUri;
            }
        }}
    }


