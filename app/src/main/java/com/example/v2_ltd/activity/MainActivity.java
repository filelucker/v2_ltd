package com.example.v2_ltd.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v2_ltd.R;
import com.example.v2_ltd.api.GetService;
import com.example.v2_ltd.api.RetrofitClient;
import com.example.v2_ltd.database.AppDatabase;
import com.example.v2_ltd.database.entity.SaveData;
import com.example.v2_ltd.model.ResponseData;
import com.example.v2_ltd.sync.DataLoaderCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<ResponseData> data = null;
    LinearLayout linearLayout;
    TextView tvQ1, tvQ2, tvQ3, tvQ4, tvQ5, tvQ6;
    RadioGroup rgp;
    EditText et3, et5;
    Spinner spinner;
    Button btnSubmit;
    ImageView img;
    String hru = "", solve = "", imageData = "";
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        AppDatabase.getInstance(this);
        AutoRequestAllPermissions();

        linearLayout = (LinearLayout) findViewById(R.id.layout);
        tvQ1 = new TextView(this);
        tvQ2 = new TextView(this);
        tvQ3 = new TextView(this);
        tvQ4 = new TextView(this);
        tvQ5 = new TextView(this);
        tvQ6 = new TextView(this);
        rgp = new RadioGroup(this);
        et3 = new EditText(this);
        et5 = new EditText(this);
        img = new ImageView(this);
        btnSubmit = new Button(this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FromCard();
            }
        });

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                hru = String.valueOf(i);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hru.equals("") || et3.getText().toString().trim().equals("") || spinner.getSelectedItem().toString().equals("Select") || solve.equals("") || et5.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "অবশ্যই, * নির্দেশিত ক্ষেত্র লিখতে হবে", Toast.LENGTH_SHORT).show();
                } else {

                    hideKeyboard(MainActivity.this);

                    AppDatabase mAppDatabase = AppDatabase.getInstance(getApplicationContext());
                    SaveData data = new SaveData();
                    data.setHowAreYou(hru);
                    data.setAddress(et3.getText().toString().trim());
                    data.setWhatHappned(spinner.getSelectedItem().toString());
                    data.setSolve(solve);
                    data.setAmount(et5.getText().toString().trim());
                    data.setImgData(imageData);
                    mAppDatabase.dataDao().insert(data);

                    Toast.makeText(getApplicationContext(), "ডেটা সফলভাবে সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show();

                    hru = ""; solve = "";
                    rgp.clearCheck();
                    rgp.removeAllViews();
                    et3.setText("");
                    et5.setText("");
                    imageData = "";
                    spinner.setAdapter(null);
                    img.setImageResource(android.R.color.transparent);


                    loadData(context);

                }
            }
        });


        spinner = new Spinner(this);


        if (isNetworkConnected()) {
            loadData(this);
        } else {
            Toast.makeText(this, "ইন্টারনেট সংযোগ নেই", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void loadData(Context context) {
        loadData(context, new DataLoaderCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: " + "loadData");
                linearLayout.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 0);


                if (data != null) {

                    // Q1
                    if (data.get(0).getType().equals("multipleChoice")) {
                        String s = "";
                        if (data.get(0).isRequired()) {
                            s = " *";
                        }
                        tvQ1.setText(data.get(0).getId() + ". " + data.get(0).getQuestion() + s);
                        tvQ1.setLayoutParams(params);
                        linearLayout.addView(tvQ1);

                        int length = data.get(0).getOptionsList().size();
                        for (int i = 0; i < length; i++) {
                            RadioButton rbn = new RadioButton(context);
                            rbn.setId(View.generateViewId());
                            rbn.setText(data.get(0).getOptionsList().get(i).getValue());
                            rgp.addView(rbn);
                        }
                        rgp.setLayoutParams(params);
                        linearLayout.addView(rgp);
                    }


                    // Q2
                    if (data.get(1).getType().equals("textInput")) {
                        String s = "";
                        if (data.get(1).isRequired()) {
                            s = " *";
                        }
                        tvQ2.setText(data.get(1).getId() + ". " + data.get(1).getQuestion() + s);
                        tvQ2.setLayoutParams(params);
                        linearLayout.addView(tvQ2);

                        et3.setLayoutParams(params);
                        et3.setHint("Enter Address");
                        linearLayout.addView(et3);
                    }


                    // Q3
                    if (data.get(2).getType().equals("dropdown")) {
                        String s = "";
                        if (data.get(2).isRequired()) {
                            s = " *";
                        }
                        tvQ3.setText(data.get(2).getId() + ". " + data.get(2).getQuestion() + s);
                        tvQ3.setLayoutParams(params);
                        linearLayout.addView(tvQ3);

                        ArrayList<String> stringList = new ArrayList<String>();
                        stringList.add("Select");
                        for (int i = 0; i < data.get(2).getOptionsList().size(); i++) {
                            stringList.add(data.get(2).getOptionsList().get(i).getValue());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stringList);
                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setLayoutParams(params);
                        linearLayout.addView(spinner);
                    }


                    // Q4
                    if (data.get(3).getType().equals("checkbox")) {
                        String s = "";
                        if (data.get(3).isRequired()) {
                            s = " *";
                        }
                        tvQ4.setText(data.get(3).getId() + ". " + data.get(3).getQuestion() + s);
                        tvQ4.setLayoutParams(params);
                        linearLayout.addView(tvQ4);

                        for (int i = 0; i < data.get(3).getOptionsList().size(); i++) {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(data.get(3).getOptionsList().get(i).getValue());
                            cb.setLayoutParams(params);
                            linearLayout.addView(cb);
                            cb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (((CheckBox) v).isChecked()) {
                                        if (!solve.contains(cb.getText())) {
                                            solve = solve + ", " + cb.getText();
                                        }
                                    } else {
                                        if (solve.contains(cb.getText())) {
                                            solve.replace(cb.getText(), "");
                                        }
                                    }
                                }
                            });
                        }

                    }


                    // Q5
                    if (data.get(4).getType().equals("numberInput")) {
                        String s = "";
                        if (data.get(4).isRequired()) {
                            s = " *";
                        }
                        tvQ5.setText(data.get(4).getId() + ". " + data.get(4).getQuestion() + s);
                        params.setMargins(10, 10, 10, 0);
                        tvQ5.setLayoutParams(params);
                        linearLayout.addView(tvQ5);

                        params.setMargins(10, 10, 10, 0);
                        et5.setLayoutParams(params);
                        et5.setHint("Enter Amount");
                        et5.setInputType(InputType.TYPE_CLASS_NUMBER);
                        linearLayout.addView(et5);
                    }

                    // image upload
                    if (data.get(5).getType().equals("camera")) {
                        String s = "";
                        if (data.get(5).isRequired()) {
                            s = " *";
                        }
                        tvQ6.setText(data.get(5).getId() + ". " + data.get(5).getQuestion() + s);
                        params.setMargins(10, 10, 10, 0);
                        tvQ6.setLayoutParams(params);
                        linearLayout.addView(tvQ6);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                        layoutParams.setMargins(10, 10, 10, 0);

                        img.setLayoutParams(layoutParams);
                        img.setBackgroundResource(android.R.drawable.ic_menu_slideshow);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout.addView(img);

                    }
                    // Save Button
                    btnSubmit.setLayoutParams(params);
                    btnSubmit.setText("Save");
                    linearLayout.addView(btnSubmit);


                }


            }

            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: " + "loadData");
            }
        });
    }

    public void loadData(final Context context, final DataLoaderCallback callback) {
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("অনুগ্রহপূর্বক অপেক্ষা করুন");
        progressdialog.show();
        GetService getService = RetrofitClient.getInstance().create(GetService.class);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

        Call<List<ResponseData>> call = getService.getSurvey(timeStamp);

        call.enqueue(new Callback<List<ResponseData>>() {
            @Override
            public void onResponse(Call<List<ResponseData>> call, Response<List<ResponseData>> response) {
                progressdialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    data = response.body();
                }
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<List<ResponseData>> call, Throwable t) {
                progressdialog.dismiss();
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }


    void AutoRequestAllPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null) {
            return;
        }
        String[] permissions = info.requestedPermissions;
        boolean remained = false;
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remained = true;
            }
        }
        if (remained) {
            requestPermissions(permissions, 0);
        }
    }

//
//    public void FromCamera() {
//
//        Log.i("camera", "startCameraActivity()");
//        File file = new File();
//        Uri outputFileUri = Uri.fromFile(file);
//        Intent intent = new Intent(
//                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//        startActivityForResult(intent, 1);
//
//    }

    public void FromCard() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            img.setImageBitmap(bitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            imageData = encoded;
//            if (bitmap != null) {
//                ImageView rotate = (ImageView) findViewById(R.id.rotate);
//            }

        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
//                    onPhotoTaken();
                    break;

            }

        }

    }

    protected void onPhotoTaken() {
        // Log message
//        Log.i("SonaSys", "onPhotoTaken");
//       Boolean taken = true;
//        Boolean imgCapFlag = true;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//       Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//        image.setImageBitmap(bitmap);


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.history) {
            Intent myIntent = new Intent(this, HistoryActivity.class);
            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
