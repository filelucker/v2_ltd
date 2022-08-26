package com.example.v2_ltd;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v2_ltd.api.GetService;
import com.example.v2_ltd.api.RetrofitClient;
import com.example.v2_ltd.model.ResponseData;
import com.example.v2_ltd.sync.DataLoaderCallback;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<ResponseData> data = null;
    LinearLayout linearLayout;
    TextView tvQ1, tvQ2, tvQ3;
    RadioGroup rgp;
    EditText et3;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        tvQ1 = new TextView(this);
        tvQ2 = new TextView(this);
        tvQ3 = new TextView(this);
        rgp = new RadioGroup(this);
        et3 = new EditText(this);

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                if (data != null) {

                    // Q1
                    if (data.get(0).getType().equals("multipleChoice")) {
                        tvQ1.setText(data.get(0).getId() + ". " + data.get(0).getQuestion());
                        params.setMargins(10, 10, 10, 0);
                        tvQ1.setLayoutParams(params);
                        linearLayout.addView(tvQ1);

                        int length = data.get(0).getOptionsList().size();
                        for (int i = 0; i < length; i++) {
                            RadioButton rbn = new RadioButton(context);
                            rbn.setId(View.generateViewId());
                            rbn.setText(data.get(0).getOptionsList().get(i).getValue());
                            rgp.addView(rbn);
                        }
                        params.setMargins(10, 0, 10, 0);
                        rgp.setLayoutParams(params);
                        linearLayout.addView(rgp);
                    }


                    // Q2
                    if (data.get(1).getType().equals("textInput")) {
                        tvQ2.setText(data.get(1).getId() + ". " + data.get(1).getQuestion());
                        params.setMargins(10, 10, 10, 0);
                        tvQ2.setLayoutParams(params);
                        linearLayout.addView(tvQ2);

                        params.setMargins(10, 10, 10, 0);
                        et3.setLayoutParams(params);
                        et3.setHint("Enter Address");
                        linearLayout.addView(et3);
                    }


                    // Q3
                    if (data.get(2).getType().equals("dropdown")) {
                        tvQ3.setText(data.get(2).getId() + ". " + data.get(2).getQuestion());
                        params.setMargins(10, 10, 10, 0);
                        tvQ3.setLayoutParams(params);
                        linearLayout.addView(tvQ3);

                        ArrayList<String> stringList = new ArrayList<String>();
                        for (int i = 0; i < data.get(2).getOptionsList().size(); i++) {
                            stringList.add(data.get(2).getOptionsList().get(i).getValue());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String> (context,android.R.layout.simple_spinner_dropdown_item,stringList);
                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        params.setMargins(10, 10, 10, 0);
                        spinner.setLayoutParams(params);
                        linearLayout.addView(spinner);


                    }


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

}
