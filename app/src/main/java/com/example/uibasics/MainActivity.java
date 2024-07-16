package com.example.uibasics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText edtTxtName, edtTxtEmail, edtTxtPassword, edtTxtPassRepeat;
    private Button btnPickImage, btnRegister;
    private TextView txtWarnName, txtWarnEmail, txtWarnPass, txtWarnPassRepeat;
    private Spinner spinnerCountry;
    private CheckBox agreementCheck;
    private View parent;
    private RadioGroup rgGen;
    private ImageView imgProfile;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews(); // Initialize all views

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPickImageIntent();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegister();
            }
        });
    }

    private void dispatchPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_CAPTURE); // Use REQUEST_IMAGE_CAPTURE
        } else {
            Toast.makeText(this, "No gallery app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            try {
                // Get selected image URI and display in ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imgProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initViews() {
        edtTxtName = findViewById(R.id.edtTxtName);
        edtTxtEmail = findViewById(R.id.edTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTextPassword);
        edtTxtPassRepeat = findViewById(R.id.edTextPassRepeat);

        btnPickImage = findViewById(R.id.btnPickImage);
        btnRegister = findViewById(R.id.btnRegister);

        txtWarnName = findViewById(R.id.txtWarnName);
        txtWarnEmail = findViewById(R.id.txtWarnEmail);
        txtWarnPass = findViewById(R.id.txtWarnPass);
        txtWarnPassRepeat = findViewById(R.id.txtWarnPassRepeat);

        spinnerCountry = findViewById(R.id.spinnerCountry);
        agreementCheck = findViewById(R.id.agreementCheck);
        rgGen = findViewById(R.id.rgGen);
        parent = findViewById(R.id.parent);

        imgProfile = findViewById(R.id.imgProfile);
    }

    private void initRegister() {
        Log.d(TAG, "initRegister: started");

        if (validateData()) {
            showSnackBar();
        }
    }

    private void showSnackBar() {
        Log.d(TAG, "showSnackBar: started");

        txtWarnName.setVisibility(View.GONE);
        txtWarnEmail.setVisibility(View.GONE);
        txtWarnPass.setVisibility(View.GONE);
        txtWarnPassRepeat.setVisibility(View.GONE);

        String name = edtTxtName.getText().toString();
        String email = edtTxtEmail.getText().toString();
        String password = edtTxtPassword.getText().toString();
        String country = spinnerCountry.getSelectedItem().toString();
        String gender = "";

        if (rgGen.getCheckedRadioButtonId() == R.id.rbMale) {
            gender = "Male";
        } else if (rgGen.getCheckedRadioButtonId() == R.id.rbFemale) {
            gender = "Female";
        } else if (rgGen.getCheckedRadioButtonId() == R.id.rbOther) {
            gender = "Other";
        } else {
            gender = "Unknown";
        }

        String snackText = "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Gender: " + gender + "\n" +
                "Country: " + country;
        Log.d(TAG, "showSnackBar: Snack Bar Text: " + snackText);

        Snackbar.make(parent, snackText, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxtName.setText("");
                        edtTxtEmail.setText("");
                        edtTxtPassword.setText("");
                        edtTxtPassRepeat.setText("");
                    }
                }).show();
    }

    private boolean validateData() {
        Log.d(TAG, "validateData: started");
        if (edtTxtName.getText().toString().isEmpty()) {
            txtWarnName.setVisibility(View.VISIBLE);
            txtWarnName.setText("Enter Your Name");
            return false;
        }

        if (edtTxtEmail.getText().toString().isEmpty()) {
            txtWarnEmail.setVisibility(View.VISIBLE);
            txtWarnEmail.setText("Enter Your Email");
            return false;
        }
        if (edtTxtPassword.getText().toString().isEmpty()) {
            txtWarnPass.setVisibility(View.VISIBLE);
            txtWarnPass.setText("Enter Your Password");
            return false;
        }
        if (edtTxtPassRepeat.getText().toString().isEmpty()) {
            txtWarnPassRepeat.setVisibility(View.VISIBLE);
            txtWarnPassRepeat.setText("Enter Your Password Again");
            return false;
        }
        if (!edtTxtPassword.getText().toString().equals(edtTxtPassRepeat.getText().toString())) {
            txtWarnPassRepeat.setVisibility(View.VISIBLE);
            txtWarnPassRepeat.setText("Password doesn't match");
            return false;
        }
        if (!agreementCheck.isChecked()) {
            Toast.makeText(this, "Please accept the agreement", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
