package com.nexus.unify;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.AdapterClasses.AvatarsAdapter;
import com.nexus.unify.ModelClasses.Avatars;
import com.nexus.unify.ModelClasses.User;


import java.util.ArrayList;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EditProfileActivity extends AppCompatActivity {
    EditText username, name, bio, anms_username;

    RoundedImageView image_profile, image_anmsprfl;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    Uri imageUri;
    StorageTask uploadTask;
    Button save;
    lottiedialogfragment lottie;
    AvatarsAdapter adapter;
    DatabaseReference reference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        username = findViewById(R.id.contact);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        anms_username = findViewById(R.id.anms_username);
        image_anmsprfl = findViewById(R.id.image_profile_anm);
        save = findViewById(R.id.btn_submit);
        name = findViewById(R.id.name);
        bio = findViewById(R.id.desc);

        image_profile = findViewById(R.id.image_profile);
        String title = "Edit Profile";
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                name.setText(user.getName());
                anms_username.setText(user.getAnmsname());
                Glide.with(getApplicationContext()).load(user.getAnmsimg()).into(image_anmsprfl);
                Glide.with(getApplicationContext()).load(user.getImg1()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image_anmsprfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showcomments();
            }
        });
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfileActivity.this)
                        .crop()
                        .crop(3, 4)
                        .maxResultSize(1080, 1920, true)
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


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                } else if (anms_username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Anonymous Id", Toast.LENGTH_SHORT).show();
                } else {
                    updateProfile(username.getText().toString(), name.getText().toString(), bio.getText().toString(), anms_username.getText().toString());
                }
            }
        });
    }

    private void updateProfile(String username, String name, String bio, String anmsusername) {
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String anmsimg = sh.getString("anmsimg", "");
        reference1 = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("username", username.toLowerCase());
        hashMap1.put("name", name);
        hashMap1.put("anmsname", anmsusername);
        hashMap1.put("bio", bio);
        hashMap1.put("anmsimg", anmsimg);
        reference1.updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        //  startActivity(new Intent(this, HomeActivity.class));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        lottie = new lottiedialogfragment(EditProfileActivity.this);
        lottie.show();
        if (imageUri != null) {
            final StorageReference fileRefference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileRefference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRefference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloaduri = (Uri) task.getResult();
                        String myUri = downloaduri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("img1", myUri);

                        reference.updateChildren(hashMap);
                        lottie.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {


                    imageUri = result.getData().getData();

                    uploadImage();
                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

    private void showcomments() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                EditProfileActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(EditProfileActivity.this)
                .inflate(R.layout.bottom_sheet_container_avatars,
                        bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        RecyclerView recyclerView;
        ArrayList<Avatars> intrestsArrayList = new ArrayList<>();
        ArrayList<String> selected_intrests = new ArrayList<String>();


        recyclerView = bottomSheetView.findViewById(R.id.recycler_view);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new AvatarsAdapter(this, intrestsArrayList, bottomSheetDialog, image_anmsprfl);
        recyclerView.setAdapter(adapter);

        createListOfData(intrestsArrayList, adapter);

    }

    private void createListOfData(ArrayList<Avatars> intrestsArrayList, AvatarsAdapter adapter) {

        intrestsArrayList = new ArrayList<>();


        for (int i = 0; i < 300; i++) {


            Avatars intrests = new Avatars();

            intrests.setName(String.valueOf(i));
            intrests.setUrl("https://api.multiavatar.com/" + i + ".png");

            intrestsArrayList.add(intrests);
        }


        adapter.setIntrests(intrestsArrayList);


    }
}