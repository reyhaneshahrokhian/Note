package com.reyhane.note;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.reyhane.note.Adaptor.NoteAdaptor;

public class AddNewNote extends AppCompatActivity {

    EditText titleText;
    EditText DescriptionText;
    Button AddNote;
    Button AddImageButton;
    ImageView AddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        getSupportActionBar().hide();

        //setting up views
        SetUpViews();

        //Adding image
        AddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
                    }
                    else {
                        PickImage();
                    }
                }
                else {
                    PickImage();
                }
            }
        });

        //Add new note to lists and sql
        AddNote.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                NoteAdaptor.titles.add(0, titleText.getText().toString().trim());
                NoteAdaptor.desc.add(0, DescriptionText.getText().toString().trim());
                NoteAdaptor.image.add(0, R.drawable.ic_image_24);

                SqlHelper addData = new SqlHelper(AddNewNote.this);
                addData.AddNotes(titleText.getText().toString().trim(), DescriptionText.getText().toString().trim(), R.drawable.ic_image_24);

                Intent intent = new Intent(AddNewNote.this,MainActivity.class);
                AddNewNote.this.startActivity(intent);
            }
        });
    }
    //setting up views
    public void SetUpViews(){
        titleText = findViewById(R.id.AddingTitle);
        DescriptionText = findViewById(R.id.AddingDescription);
        AddNote = findViewById(R.id.FinalAddButton);
        AddImageButton = findViewById(R.id.AddingImageButton);
        AddImage = findViewById(R.id.AddingImage);
    }

    //picking image from gallery
    public void PickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }
    //result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickImage();
                } else {
                    Toast.makeText(this, "Permission were denied!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1000) {
            AddImage.setImageURI(data.getData());
        }
    }
}