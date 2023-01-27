package com.reyhane.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    ImageView EditImage;
    EditText title;
    EditText desc;
    Button EditImageButton;
    Button ok;
    Button cancel;

    String id,t,d;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();

        //setting up views
        SetUpViews();

        //getting and setting intent data
        GetAndSetIntent();

        //changing image
        EditImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
                    }
                    else {
                        PickImageToChange();
                    }
                }
                else {
                    PickImageToChange();
                }
            }
        });

        //edit or cancel editing
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlHelper addData = new SqlHelper(SecondActivity.this);
                addData.EditData(title.getText().toString().trim(),desc.getText().toString().trim(),R.drawable.ic_image_24, id);

                Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                SecondActivity.this.startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                SecondActivity.this.startActivity(intent);
            }
        });
    }
    //setting up views
    public void SetUpViews(){
        EditImage = findViewById(R.id.EditImage);
        title = findViewById(R.id.editTextTitle);
        desc = findViewById(R.id.editTextDescription);
        EditImageButton = findViewById(R.id.EditImageButton);
        ok = findViewById(R.id.OkEdit);
        cancel =  findViewById(R.id.CancelEdit);
    }
    //getting and setting intent data
    public void GetAndSetIntent(){
        id = getIntent().getStringExtra("id");
        t = getIntent().getStringExtra("title");
        d = getIntent().getStringExtra("description");
        img = getIntent().getIntExtra("image", 1);

        title.setText(t);
        desc.setText(d);
        EditImage.setImageResource(img);
    }

    //picking image from gallery
    public void PickImageToChange(){
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
                    PickImageToChange();
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
            EditImage.setImageURI(data.getData());
        }
    }
}