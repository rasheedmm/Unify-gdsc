package com.nexus.unify;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.AdapterClasses.AvatarsAdapter;
import com.nexus.unify.ModelClasses.Avatars;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import id.zelory.compressor.Compressor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddPhotosActivity extends AppCompatActivity {

    LinearLayout ll_next;
    RoundedImageView img1, img2;
    private int imageNo;
    public Uri imageUri1;
    LinearLayout ln1, ln2;
    byte[] finalimage1;
    private static String filepath;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/compressor");
    StorageReference storageReference1, storageReference2;
    StorageTask uploadTask1, uploadTask2;
    String myUri1 = "null";
    String myUri2;
    AvatarsAdapter adapter;
    FirebaseUser firebaseUser;
    lottiedialogfragment lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ll_next = findViewById(R.id.ll_next);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        ln1 = findViewById(R.id.ln_1);
        ln2 = findViewById(R.id.ln_2);

        storageReference2 = FirebaseStorage.getInstance().getReference("uploads");
        storageReference1 = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        imageUri1 = null;


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 1;
                ImagePicker.Companion.with(AddPhotosActivity.this)
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
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 2;
                showcomments();
            }
        });


        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    myUri2 = adapter.url;
                } catch (Exception e) {
                    myUri2 = "";

                }
                if (imageUri1 != null && !myUri2.equals("")) {

                    upload();
                    lottie = new lottiedialogfragment(AddPhotosActivity.this);
                    lottie.show();


                } else {
                    Toast.makeText(getApplicationContext(), "Please add one photo and avatar", Toast.LENGTH_SHORT).show();

                }








             /* Intent intent = new Intent(getApplicationContext(), IntrestSelectionActivity.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void upload() {


        if (imageUri1 != null) {


            try {
                File actualImage1 = new File(imageUri1.getPath());
                Bitmap compressedImage1 = new Compressor(AddPhotosActivity.this)
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


            final StorageReference fileRefference1 = storageReference1.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri1));
            uploadTask1 = fileRefference1.putBytes(finalimage1);
            uploadTask1.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRefference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloaduri1 = (Uri) task.getResult();
                        myUri1 = downloaduri1.toString();
                        adddata();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void adddata() {


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();


        myEdit.putString("img1", myUri1);


        myEdit.commit();


        lottie.dismiss();
        Toast.makeText(AddPhotosActivity.this, "Photos Added.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), IntrestSelectionActivity.class);
        startActivity(intent);
        finish();

    }

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {


                    switch (imageNo) {
                        case (1):
                            imageUri1 = result.getData().getData();

                            Glide.with(getApplicationContext()).load(imageUri1).into(img1);
                            break;

                    }

                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

    private void showcomments() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                AddPhotosActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(AddPhotosActivity.this)
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
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new AvatarsAdapter(this, intrestsArrayList, bottomSheetDialog, img2);
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