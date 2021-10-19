package com.example.authenticationapp.OwnerInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifyListing2 extends AppCompatActivity {

    RadioButton mMonday, mTuesday, mWednesday, mThursday, mFriday, mSaturday, mSunday;
    String userID;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ArrayList mOperationalDays = new ArrayList();
    Button mfinishBtn;
    EditText mStarting, mEnd;
    Button mlandUpload, mvalidId, mlotImgs;
    ImageView img;
    StorageReference mStorageRef;

    public Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_listing2);

        mMonday = findViewById(R.id.mondayRd);
        mTuesday = findViewById(R.id.tuesdayRd);
        mWednesday = findViewById(R.id.wednesdayRd);
        mThursday = findViewById(R.id.thursdayRd);
        mFriday = findViewById(R.id.fridayRd);
        mSaturday = findViewById(R.id.saturdayRd);
        mSunday = findViewById(R.id.sundayRd);

        mStarting = findViewById(R.id.startingTime);
        mEnd = findViewById(R.id.endTime);

        img = (ImageView) findViewById(R.id.imgview);
        mlandUpload = findViewById(R.id.landuploadBtn);
        mvalidId = findViewById(R.id.validuploadBtn);
        mlotImgs = findViewById(R.id.imgsuploadBtn);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        mfinishBtn = findViewById(R.id.finishBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMonday.isChecked())
                    mOperationalDays.add("M");
                else
                    mOperationalDays.remove("M");
            }
        });
        mTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTuesday.isChecked())
                    mOperationalDays.add("T");
                else
                    mOperationalDays.remove("T");
            }
        });
        mWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWednesday.isChecked())
                    mOperationalDays.add("W");
                else
                    mOperationalDays.remove("W");
            }
        });
        mThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mThursday.isChecked())
                    mOperationalDays.add("TH");
                else
                    mOperationalDays.remove("TH");
            }
        });
        mFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFriday.isChecked())
                    mOperationalDays.add("F");
                else
                    mOperationalDays.remove("F");
            }
        });
        mSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSaturday.isChecked())
                    mOperationalDays.add("S");
                else
                    mOperationalDays.remove("S");
            }
        });
        mSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSunday.isChecked())
                    mOperationalDays.add("SU");
                else
                    mOperationalDays.remove("SU");
            }
        });

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("users").document(userID);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String start = documentSnapshot.getString("Starting Time");
                    mStarting.setText(""+start);
                    String end = documentSnapshot.getString("End Time");
                    mEnd.setText(""+end);

                }
            }
        });

        mfinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList operationalDays = mOperationalDays;
                String startingTime = mStarting.getText().toString();
                String endTime = mEnd.getText().toString();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference userRef = fStore.collection("users").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("Operational Days", operationalDays);
                user.put("Starting Time", startingTime);
                user.put("End Time", endTime);
                Fileuploader();


                userRef.set(user, SetOptions.merge());

                startActivity(new Intent(getApplicationContext(), Owner.class));


            }
        });

        mlotImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });
    }
    private String getExtension(Uri uri)

    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader()

    {
        StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri)+"."+userID+".LandTitle");
        userID = fAuth.getCurrentUser().getUid();
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ModifyListing2.this, "Listing information has been updated",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ModifyListing2.this, "Image upload error",Toast.LENGTH_LONG).show();
                    }
                });




    }
    private void Filechooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData() !=null)
        {
            imguri=data.getData();
            img.setImageURI(imguri);
        }
    }
}