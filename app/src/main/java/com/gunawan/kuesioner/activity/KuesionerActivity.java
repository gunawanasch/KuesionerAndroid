package com.gunawan.kuesioner.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gunawan.kuesioner.R;
import com.gunawan.kuesioner.adapter.KuesionerAdapter;
import com.gunawan.kuesioner.api.ApiClient;
import com.gunawan.kuesioner.api.ApiInterface;
import com.gunawan.kuesioner.model.Answer;
import com.gunawan.kuesioner.model.Kuesioner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KuesionerActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar pbKuesioner;
    private RecyclerView rvKuesioner;
    private Button btnSubmit;
    private Toolbar toolbar;
    private KuesionerAdapter adapter;
    private ArrayList<Kuesioner> listKuesioner = new ArrayList<Kuesioner>();
    private List<Answer> listAnswer;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuesioner);
        coordinatorLayout   = findViewById(R.id.coordinatorLayout);
        pbKuesioner         = findViewById(R.id.pbKuesioner);
        rvKuesioner         = findViewById(R.id.rvKuesioner);
        btnSubmit           = findViewById(R.id.btnSubmit);
        toolbar             = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        email = i.getStringExtra("email");
        rvKuesioner.setHasFixedSize(true);
        rvKuesioner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pbKuesioner.setVisibility(View.VISIBLE);
        rvKuesioner.setVisibility(View.GONE);
        getKuesioner();
    }

    private void getKuesioner() {
        ApiInterface api = new ApiClient().getClient().create(ApiInterface.class);
        Call<ArrayList<Kuesioner>> call = api.getKuesioner();
        call.enqueue(new Callback<ArrayList<Kuesioner>>() {
            @Override
            public void onResponse(Call<ArrayList<Kuesioner>> call, Response<ArrayList<Kuesioner>> response) {
                if(response.isSuccessful()) {
                    listKuesioner = response.body();
                    listAnswer = Arrays.asList(new Answer[listKuesioner.size()]);
                    pbKuesioner.setVisibility(View.GONE);
                    rvKuesioner.setVisibility(View.VISIBLE);
                    adapter = new KuesionerAdapter(KuesionerActivity.this, listKuesioner);
                    rvKuesioner.setAdapter(adapter);
                    adapter.setOnCheckedChangeListener(new KuesionerAdapter.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChange(int position, int idTitle, int idQuestion, String value) {
                            listAnswer.set(position, new Answer(idTitle, idQuestion, value));
                        }
                    });

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<Integer> listIdTitle      = new ArrayList<>();
                            ArrayList<Integer> listIdQuestion   = new ArrayList<>();
                            ArrayList<String> listValue         = new ArrayList<>();
                            try {
                                for(int i=0; i<listAnswer.size(); i++) {
                                    listIdTitle.add(listAnswer.get(i).getIdTitle());
                                    listIdQuestion.add(listAnswer.get(i).getIdQuestion());
                                    listValue.add(listAnswer.get(i).getValue());
                                }
                                addAnswer(listIdTitle, listIdQuestion, listValue);
                            } catch (NullPointerException e) {
                                AlertDialog.Builder ab = new AlertDialog.Builder(
                                        KuesionerActivity.this);
                                ab.setMessage("Anda belum menjawab semua pertanyaan.");
                                ab.setNeutralButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dlg, int sumthin) {}}).show();
                            }

                        }
                    });
                }
                else {
                    pbKuesioner.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mendapatkan respon.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Kuesioner>> call, Throwable t) {
                pbKuesioner.setVisibility(View.GONE);
                t.printStackTrace();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mengakses data.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void addAnswer(ArrayList<Integer> listIdTitle, ArrayList<Integer> listIdQuestion, ArrayList<String> listValue) {
        final ProgressDialog progress = ProgressDialog.show(KuesionerActivity.this, "", "Loading...", false, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.addAnswer(listIdTitle, listIdQuestion, listValue, email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progress.dismiss();
                try {
                    String strResponse = response.body().string();
                    if(response.isSuccessful()) {
                        JSONObject obj = new JSONObject(strResponse);
                        int status = obj.getInt("status");
                        if(status == 1) {
                            AlertDialog.Builder ab = new AlertDialog.Builder(
                                    KuesionerActivity.this);
                            ab.setMessage(obj.getString("message"));
                            ab.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dlg, int sumthin) {}}).show();
                        }
                        else {
                            AlertDialog.Builder ab = new AlertDialog.Builder(
                                    KuesionerActivity.this);
                            ab.setMessage(obj.getString("message"));
                            ab.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dlg, int sumthin) {}}).show();
                        }
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mendapatkan respon.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mengakses data.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(KuesionerActivity.this);
        alert.setMessage("Apakah anda yakin ingin keluar?");
        alert.setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

}
