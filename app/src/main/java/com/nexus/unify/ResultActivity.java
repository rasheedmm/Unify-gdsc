package com.nexus.unify;


import static com.google.common.io.Files.getFileExtension;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.databinding.ActivityResultBinding;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.CharPolicy;


import com.nexus.unify.AdapterClasses.HasTagPresenter;
import com.nexus.unify.AdapterClasses.UserPresenter;
import com.nexus.unify.ModelClasses.Hashtags;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.ModelClasses.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class ResultActivity extends AppCompatActivity {


    @NonNull
    ActivityResultBinding binding;

    Posts posts;
    RoundedImageView img1, img2;
    private int imageNo;
    public Uri imageUri1;
    String myUrl = "", tag;
    boolean on = false;
    SwitchCompat sw;
    boolean img = true;
    byte[] finalimage1;
    StorageReference storageReference1, storageReference2;
    EditText edit;
    private Autocomplete mentionsAutocomplete, hashtagsAutocomplete;
    ArrayAdapter<String> arraylist_adapter;
    AutoCompleteTextView text_type;
    ArrayList<String> arraylist_types;
    TextView send;
    lottiedialogfragment lottie;
    StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMentionsAutocomplete();

        setupHashTagAutocomplete();


        posts = new Posts();
        storageReference1 = FirebaseStorage.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("Posts");
        edit = findViewById(R.id.titleBox);
        text_type = findViewById(R.id.txt_edu_level);
        send = findViewById(R.id.imageView12);
        sw = findViewById(R.id.switch1);
        arraylist_types = new ArrayList<>();
        arraylist_types.add("Career");
        arraylist_types.add("Events");
        arraylist_types.add("Forum");
        arraylist_types.add("Vacancies");
        arraylist_types.add("Courses");

        arraylist_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_types);
        text_type.setAdapter(arraylist_adapter);
        text_type.setThreshold(1);

        // binding.img1.setImageURI(getIntent().getData());
        binding.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 1;
                ImagePicker.Companion.with(ResultActivity.this)
                        .crop()

                        .maxResultSize(1080, 1920, false)
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                launcher.launch(it);
                            }
                        }));
            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                on = isChecked;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_type.getText().toString().equals("")) {
                    Toast.makeText(ResultActivity.this, "Please choose privacy", Toast.LENGTH_SHORT).show();

                }

               else if(edit.getText().toString().equals("")){

                    Toast.makeText(ResultActivity.this, "Post description must not be empty.", Toast.LENGTH_SHORT).show();
                }
                else {




                    new iOSDialogBuilder(ResultActivity.this)
                            .setTitle("Confirm")
                            .setSubtitle("Confirm post")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener(getString(R.string.ok),new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                 //   Toast.makeText(ResultActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    if (img == true) {
                                        lottie = new lottiedialogfragment(ResultActivity.this);
                                        lottie.show();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                                        String postid = reference.push().getKey();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("postid", postid);
                                        hashMap.put("privacy", text_type.getText().toString());
                                        if (on == true) {
                                            tag = "on";

                                        } else {
                                            tag = "off";
                                        }
                                        hashMap.put("anms", tag);
                                        hashMap.put("type", "image");
                                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        hashMap.put("url", "null");
                                        hashMap.put("text", edit.getText().toString());
                                        reference.child(postid).setValue(hashMap);
                                        lottie.dismiss();

                                        finish();
                                        startActivity(new Intent(ResultActivity.this, HomeActivity.class));
                                    } else {

                                        uploadImage();
                                    }
                                }
                            })
                            .setNegativeListener(getString(R.string.dismiss), new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .build().show();
                }
            }
        });


    }

    private void setupMentionsAutocomplete() {
        edit = findViewById(R.id.titleBox);
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('@'); // Look for @mentions
        UserPresenter userpresenter = new UserPresenter(this);
        com.otaliastudios.autocomplete.AutocompleteCallback<User> usercallback = new com.otaliastudios.autocomplete.AutocompleteCallback<User>() {
            @Override
            public boolean onPopupItemClicked(@NonNull Editable editable, @NonNull User item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getUsername();
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        mentionsAutocomplete = Autocomplete.<User>on(edit)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(userpresenter)
                .with(usercallback)
                .build();
    }

    private void setupHashTagAutocomplete() {
        edit = findViewById(R.id.titleBox);
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePolicy policy = new CharPolicy('#'); // Look for hashtags
        HasTagPresenter hasTagpresenter = new HasTagPresenter(this);
        com.otaliastudios.autocomplete.AutocompleteCallback<Hashtags> hashcallback = new com.otaliastudios.autocomplete.AutocompleteCallback<Hashtags>() {
            @Override
            public boolean onPopupItemClicked(@NonNull Editable editable, @NonNull Hashtags item) {
                // Replace query text with the full name.
                int[] range = CharPolicy.getQueryRange(editable);
                if (range == null) return false;
                int start = range[0];
                int end = range[1];
                String replacement = item.getTag() + " ";
                editable.replace(start, end, replacement);
                // This is better done with regexes and a TextWatcher, due to what happens when
                // the user clears some parts of the text. Up to you.
                editable.setSpan(new StyleSpan(Typeface.BOLD), start, start + replacement.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        hashtagsAutocomplete = Autocomplete.<Hashtags>on(edit)
                .with(elevation)
                .with(backgroundDrawable)
                .with(policy)
                .with(hasTagpresenter)
                .with(hashcallback)
                .build();
    }


    private String getFileExtensiion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        lottie = new lottiedialogfragment(ResultActivity.this);
        lottie.show();


        try {
            File actualImage1 = new File(imageUri1.getPath());
            Bitmap compressedImage1 = new Compressor(ResultActivity.this)
                    .setMaxWidth(800)
                    .setMaxHeight(800)
                    .setQuality(60)
                    .compressToBitmap(actualImage1);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            compressedImage1.compress(Bitmap.CompressFormat.JPEG, 70, baos1);
            finalimage1 = baos1.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (imageUri1 != null) {

            final StorageReference fileRefference1 = storageReference1.child(System.currentTimeMillis()
                    + "." + getFileExtension(String.valueOf(imageUri1)));
            uploadTask = fileRefference1.putBytes(finalimage1);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRefference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        String postid = reference.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("privacy", text_type.getText().toString());
                        if (on == true) {
                            tag = "on";

                        } else {
                            tag = "off";
                        }
                        hashMap.put("anms", tag);
                        hashMap.put("type", "image");
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("url", myUrl);
                        hashMap.put("text", edit.getText().toString());
                        reference.child(postid).setValue(hashMap);
                        lottie.dismiss();
                        finish();
                        startActivity(new Intent(ResultActivity.this, HomeActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {


                    switch (imageNo) {
                        case (1):
                            imageUri1 = result.getData().getData();

                            Glide.with(getApplicationContext()).load(imageUri1).into(binding.img1);

                            img = false;
                    }

                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

}