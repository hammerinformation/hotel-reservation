package com.hammerinformation.hotelreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;


    private String reservationUrl = "http://10.0.2.2:5000/reservation";
    private EditText nameEditText, surnameEditText, phoneEditText, emailEditText, adultsEditText, childrenEditText;
    private CheckBox wifiCheckBox, parkingCheckBox, breakFastCheckBox;
    private Button sendButton, checkInButton, checkOutButton;


    private  String name, surname, phone, email, adults, children, checkin, checkout;
    private  String wifi,breakfast,parking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifi="0";
        breakfast="0";
        parking="0";

        definition();
        update();
        clickButton();

    }


    private void definition() {
        //EditText
        nameEditText = findViewById(R.id.name_edit_text);
        surnameEditText = findViewById(R.id.surname_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        adultsEditText = findViewById(R.id.adults_edit_text);
        childrenEditText = findViewById(R.id.children_edit_text);

        //CheckBox
        wifiCheckBox = findViewById(R.id.wifi_check_box);
        parkingCheckBox = findViewById(R.id.parking_check_box);
        breakFastCheckBox = findViewById(R.id.breakfast_check_box);

        //Button
        sendButton = findViewById(R.id.send_button);
        checkInButton = findViewById(R.id.check_in_button);
        checkOutButton = findViewById(R.id.check_out_button);

    }

    private void clickButton() {

        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                checkin = day + "-" + (month + 1) + "-" + year;
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });


        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                checkout = day + "-" + (month + 1) + "-" + year;
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                sendData(reservationUrl);

            }
        });

    }


    private void showAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Successful");

        alert.setMessage("\n" + "Name : " + name + "\n" + "Surname : " + surname + "\n" + "Email : " + email + "\n" + "Phone : " + phone + "\n" + "Children : " + children + "\n" + "Adults : " + adults + "\n" + "Check-In : " + checkin + "\n" + "Check-Out : " + checkout + "\n");
        alert.show();
    }

    private void update() {

        name = nameEditText.getText().toString();
        surname = surnameEditText.getText().toString();
        email = emailEditText.getText().toString();
        phone = phoneEditText.getText().toString();
        adults = adultsEditText.getText().toString();
        children = childrenEditText.getText().toString();


        wifiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifi = "1";
                }
            }
        });

        breakFastCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    breakfast = "1";

                }
            }
        });

        parkingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    parking = "1";
                }
            }
        });


    }

    private void sendData(String url) {
        update();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        showAlert();
                        //show Alert here
                        //textView.setText("Response" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print("That didn't work");
                print(error.toString());
                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
            }
        }


        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("name_", name);
                params.put("surname_", surname);
                params.put("email_", email);
                params.put("phone_", phone);
                params.put("adults_", adults);
                params.put("children_", children);
                params.put("check_box_1", wifi);
                params.put("check_box_2", parking);
                params.put("check_box_3", breakfast);
                params.put("checkIn", checkin);
                params.put("checkOut", checkout);


                return params;

            }
        };

        queue.add(stringRequest);
    }


    private void print(String string) {
        System.out.println(string);


    }


}