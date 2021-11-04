package com.example.authenticationapp.DriverInterface;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.authenticationapp.MainActivity;
import com.example.authenticationapp.OwnerInterface.editProfile;
import com.example.authenticationapp.OwnerInterface.feedback;
import com.example.authenticationapp.R;
import com.example.authenticationapp.UserLogin.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class AccountFragment2 extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    StorageReference mStorageRef, mStorageReference;
    ImageView imageView;
    Button btn1;
    ListView listView;
    TextView edit;
    public Uri imguri;
    public String phone, address, email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");


        DocumentReference documentReference4 = fStore.collection("users").document(userID);
        documentReference4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    phone = documentSnapshot.getString("Phone");
                    email = documentSnapshot.getString("Email");

                    String[] parkingList = {"Phone number : " + phone, "Email : " + email,"Help Centre", "Feedback","Switch Account Type", "Sign Out"};

                    listView = (ListView) view.findViewById(R.id.parkingLV);

                    ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_list_item_1, parkingList
                    );
                    listView.setAdapter(listviewAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            Object item = listView.getItemAtPosition(position);
                            Toast.makeText(getActivity(), "You selected : " + item, Toast.LENGTH_SHORT).show();

                            if (position == 2){

                            }
                            else if (position == 3){
                                feedback fb = new feedback();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fb);
                                fragmentTransaction.commit();
                            }

                            else if (position == 4) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            } else if (position == 5) {
                                new AlertDialog.Builder(getActivity())
                                        .setCancelable(false)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Sign Out")
                                        .setMessage("Are you sure you want sign out of Parking Boy?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseAuth.getInstance().signOut();//logout
                                                startActivity(new Intent(getActivity(), Login.class));
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        }
                    });

                    edit = (TextView)getView().findViewById(R.id.editTV);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editProfile editProfile = new editProfile();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, editProfile);
                            fragmentTransaction.commit();

                        }
                    });

                }


            }

        });


        mStorageReference = FirebaseStorage.getInstance().getReference().child("Images/"+userID+".ProfileAvatar");

        try {
            final File localFile = File.createTempFile(""+userID,".Lot Images");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }

        TextView textView = (TextView) view.findViewById(R.id.accountName);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = fAuth.getCurrentUser().getUid();
                Fileuploader();
                btn1.setVisibility(View.GONE);

            }
        };

        btn1 = (Button) view.findViewById(R.id.saveBtn);
        btn1.setOnClickListener(listener);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fn = documentSnapshot.getString("FullName");
                    textView.setText("" + fn);

                }

            }
        });

        imageView = (ImageView) view.findViewById(R.id.accountAvatar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });




        return view;

    }

    private void Fileuploader() {
        StorageReference Ref = mStorageRef.child(userID + ".ProfileAvatar");
        userID = fAuth.getCurrentUser().getUid();
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Profile Avatar updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Image upload error", Toast.LENGTH_LONG).show();
                    }
                });


    }
    public void Filechooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                imguri = data.getData();
                imageView.setImageURI(imguri);
                btn1.setVisibility(View.VISIBLE);
            }
        }
    }

}
