package com.gunawan.kuesioner.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gunawan.kuesioner.R;
import com.gunawan.kuesioner.api.ApiClient;
import com.gunawan.kuesioner.api.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private EditText etName, etEmail;
    private Button btnLanjutkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        coordinatorLayout   = findViewById(R.id.coordinatorLayout);
        etName              = findViewById(R.id.etName);
        etEmail             = findViewById(R.id.etEmail);
        btnLanjutkan        = findViewById(R.id.btnLanjutkan);
        toolbar             = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                if(name.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(
                            FormActivity.this);
                    ab.setMessage("Harap lengkapi data Anda.");
                    ab.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dlg, int sumthin) {}}).show();
                }
                else {
                    final ProgressDialog progress = ProgressDialog.show(FormActivity.this, "", "Loading...", false, false);
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    apiInterface.addUser(name, email).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progress.dismiss();
                            try {
                                String strResponse = response.body().string();
                                if(response.isSuccessful()) {
                                    JSONObject obj = new JSONObject(strResponse);
                                    int status = obj.getInt("status");
                                    if(status == 1) {
                                        Intent i = new Intent(FormActivity.this, KuesionerActivity.class);
                                        i.putExtra("email", email);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        AlertDialog.Builder ab = new AlertDialog.Builder(
                                                FormActivity.this);
                                        ab.setMessage(obj.getString("message"));
                                        ab.setNeutralButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dlg, int sumthin) {}}).show();
                                    }
                                }
                                else {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mendapatkan respon", Snackbar.LENGTH_LONG);
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
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Gagal dalam mengakses data", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            }
        });
    }

}
