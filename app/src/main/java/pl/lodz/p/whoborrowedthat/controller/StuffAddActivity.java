package pl.lodz.p.whoborrowedthat.controller;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.whoborrowedthat.R;
import pl.lodz.p.whoborrowedthat.features.DatePickerFragment;
import pl.lodz.p.whoborrowedthat.helper.SharedPrefHelper;
import pl.lodz.p.whoborrowedthat.model.Stuff;
import pl.lodz.p.whoborrowedthat.model.User;
import pl.lodz.p.whoborrowedthat.model.UserRelation;
import pl.lodz.p.whoborrowedthat.service.ApiManager;
import pl.lodz.p.whoborrowedthat.viewmodel.UserRelationViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StuffAddActivity extends AppBaseActivity {
    private TextView rentalDateTextView;
    private TextView estimatedReturnDateTextView;
    private EditText nameEditText;
    private Stuff stuff;
    private SimpleDateFormat format;
    private Spinner friendsSpinner;
    private Button addButton;
    private Button makePhotoButton;
    private Button fromGalleryButton;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff_add);

        rentalDateTextView = findViewById(R.id.rentalDateTextView);
        estimatedReturnDateTextView = findViewById(R.id.returnDateTextView);
        format = new SimpleDateFormat("dd.MM.YYYY", getResources().getConfiguration().locale);
        stuff = new Stuff();
        rentalDateTextView.setText(format.format(stuff.getRentalDate()));
        estimatedReturnDateTextView.setText(format.format(stuff.getEstimatedReturnDate()));

        nameEditText = findViewById(R.id.nameEditText);
        friendsSpinner = findViewById(R.id.friendsSpinner);

        final List<UserSelection> list = new ArrayList<>();

        UserRelationViewModel userRelationViewModel = ViewModelProviders.of(this).get(UserRelationViewModel.class);
        userRelationViewModel.getRelations().observe(this, new Observer<List<UserRelation>>() {
            @Override
            public void onChanged(@Nullable List<UserRelation> relations) {
                User user = SharedPrefHelper.getUserFormSP(getApplication());
                for(UserRelation ur : relations) {
                    User relatedUser;
                    relatedUser = ur.getRelatedUser();
                    if (user.getId() == relatedUser.getId()) {
                        relatedUser = ur.getRelatingUser();
                    }
                    list.add(new UserSelection((int)relatedUser.getId(), relatedUser.getUsername()));
                }
                ArrayAdapter<UserSelection> dataAdapter = new ArrayAdapter<UserSelection>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                friendsSpinner.setAdapter(dataAdapter);
            }
        });



        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = nameEditText.getText().toString();
                UserSelection selectedUser = (UserSelection) friendsSpinner.getSelectedItem();
                if (itemName != null && selectedUser != null && stuff != null) {
                    stuff.setName(itemName);
                    stuff.getBorrower().setId(selectedUser.getId());

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ApiManager.getInstance().addBorrows(SharedPrefHelper.getUserFormSP(getApplication()), stuff, new Callback<Object>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                            Toast.makeText(StuffAddActivity.this,
                                    "Borrow added!"
                                    , Toast.LENGTH_LONG).show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            finish();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                            Log.d("ERROR", t.getMessage());
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                } else {
                    Toast.makeText(StuffAddActivity.this,
                            "Fill all fields"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        makePhotoButton = findViewById(R.id.makePhotoButton);
        fromGalleryButton = findViewById(R.id.fromGalleryButton);

        fromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        makePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        });
    }

    public void showDatePickerDialogForRentalDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment)newFragment).setFragmentSetup(rentalDateTextView, stuff.getRentalDate());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogForReturnDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment)newFragment).setFragmentSetup(estimatedReturnDateTextView, stuff.getEstimatedReturnDate());
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private class UserSelection {
        int id;
        String displayedName;

        UserSelection(int id, String displayedName) {
            this.id = id;
            this.displayedName = displayedName;
        }

        @NonNull
        @Override
        public String toString() {
            return displayedName;
        }

        int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDisplayedName() {
            return displayedName;
        }

        public void setDisplayedName(String displayedName) {
            this.displayedName = displayedName;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    Toast.makeText(StuffAddActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    //imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(StuffAddActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Log.d("size of bitmap", String.valueOf(thumbnail.getByteCount()));
            //imageview.setImageBitmap(thumbnail);
            //saveImage(thumbnail);
            Toast.makeText(StuffAddActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
