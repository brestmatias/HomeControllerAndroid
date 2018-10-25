package com.example.mati.homecontroller;

import java.util.*;
import java.text.DecimalFormat;
import java.util.BitSet;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


public class MainActivity extends AppCompatActivity {

    TextView txtView;
    TextView txtTemp;
    TextView txtTempDht;
    TextView txtPress;
    TextView txtHum;
    Button buttonOpenGate;
    Button buttonCloseGate;
    ToggleButton toggleButtonS1;
    ToggleButton toggleButtonS2;
    ToggleButton toggleButtonS3;
    ToggleButton toggleButtonS4;
    ToggleButton toggleButtonS5;
    ToggleButton toggleButtonS6;
    ToggleButton toggleButtonS7;
    ToggleButton toggleButtonS8;



    BarChart chartPressure;
    PieChart chartRain;

    String dirStation="http://192.168.1.150/";
    String dirApiProxy="https://monitorv1.herokuapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = (TextView) findViewById(R.id.txtView);
        buttonOpenGate = (Button) findViewById(R.id.buttonOpenGate);
        buttonCloseGate = (Button) findViewById(R.id.buttonCloseGate);
        txtTemp=(TextView) findViewById(R.id.txtTemp);
        txtTempDht=(TextView) findViewById(R.id.txtTempDht);
        txtPress=(TextView) findViewById(R.id.txtPress);
        txtHum=(TextView) findViewById(R.id.txtHum);

        toggleButtonS1 =(ToggleButton) findViewById(R.id.toggleButtonS1);
        toggleButtonS2 =(ToggleButton) findViewById(R.id.toggleButtonS2);
        toggleButtonS3 =(ToggleButton) findViewById(R.id.toggleButtonS3);
        toggleButtonS4 =(ToggleButton) findViewById(R.id.toggleButtonS4);
        toggleButtonS5 =(ToggleButton) findViewById(R.id.toggleButtonS5);
        toggleButtonS6 =(ToggleButton) findViewById(R.id.toggleButtonS6);
        toggleButtonS7 =(ToggleButton) findViewById(R.id.toggleButtonS7);
        toggleButtonS8 =(ToggleButton) findViewById(R.id.toggleButtonS8);

        buttonOpenGate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableInterface();
                openGate();
            }
        });

        buttonCloseGate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableInterface();
                closeGate();
            }
        });

        toggleButtonS1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(1,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(2,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(3,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(4,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(5,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS6.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(6,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS7.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(7,((ToggleButton)v));
                    }
                }
        );
        toggleButtonS8.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSalidaRequest(8,((ToggleButton)v));
                    }
                }
        );


        initPressureGraph();
        initRainGraph();
        getStatusStation();
        getHistoricMetereologic();
    }


    @Override
    protected void onResume(){
        super.onResume();

        getHistoricMetereologic();
        getStatusStation();
    }


    private void initPressureGraph(){
        chartPressure = findViewById(R.id.chartPressure);
        chartPressure.setNoDataText("Gato");
        //chartPressure.setFitBars(true); // make the x-axis fit exactly all bars
        chartPressure.animateY(3000, Easing.EasingOption.EaseOutBack);
        chartPressure.setDrawGridBackground(false);

        IAxisValueFormatter formatter= new IAxisValueFormatter() {
            String labels[]=new String[]{"-24h","-12h","-10h","-8h","-6h","-4h","-2h","-1h","-0h"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int) value];
            }
        };

        XAxis xAxis=chartPressure.getXAxis();
        xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);
        //xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM );
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);


        chartPressure.getLegend().setEnabled(false);
        chartPressure.getAxisLeft().setEnabled(false);
        chartPressure.getAxisRight().setEnabled(false);
        chartPressure.setDrawValueAboveBar(true);
        chartPressure.getDescription().setEnabled(false);

    }
    private  void initRainGraph(){
        chartRain=(PieChart) findViewById(R.id.chartRain);
        chartRain.setDrawEntryLabels(false);
        chartRain.getDescription().setEnabled(false);
        chartRain.getLegend().setEnabled(false);
        chartRain.setDrawSlicesUnderHole(false);
        chartRain.animateY(3000, Easing.EasingOption.EaseOutBack);

    }

    private void getStatusStation(){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url =dirStation+"salidas";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    int status=response.getInt("salida"); //100794
                    BitSet bits=BitSet.valueOf(new long[]{status});
                    toggleButtonS1.setChecked(bits.get(0));
                    toggleButtonS2.setChecked(bits.get(1));
                    toggleButtonS3.setChecked(bits.get(2));
                    toggleButtonS4.setChecked(bits.get(3));
                    toggleButtonS5.setChecked(bits.get(4));
                    toggleButtonS6.setChecked(bits.get(5));
                    toggleButtonS7.setChecked(bits.get(6));
                    toggleButtonS8.setChecked(bits.get(7));

                    getStatusMetereologics();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    private void sendSalidaRequest(int salida, ToggleButton t){
        disableInterface();
        RequestQueue queue = Volley.newRequestQueue(t.getContext());
        String url ="http://192.168.1.150/salida?s"+salida+"="+(t.isChecked()?"1":"0");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    private void openGate(){

        txtView.setText("Abriendo porton");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.150/porton?action=1";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if(status.contentEquals("OK"))
                        txtView.setText("Comando Recibido Correctamente");
                    else
                        txtView.setText("Error Abriendo");

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }

    private void closeGate(){

        txtView.setText("Cerrando porton");
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.150/porton?action=0";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if(status.contentEquals("OK"))
                        txtView.setText("Comando Recibido Correctamente");
                    else
                        txtView.setText("Error Cerrando");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }

    private void getStatusMetereologics(){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =dirStation+"sensors";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    double bmpPress=response.getDouble("bmpPress"); //100794
                    String bmpTemp=response.getString("bmpTemp"); //25.8
                    String dhtHum =response.getString("dhtHum");//39.5
                    String dhtTemp =response.getString("dhtTemp");//26.3
                    double rainVal =response.getDouble("rainVal");//1024


                    txtTemp.setText(bmpTemp+ (char) 0x00B0 );
                    txtTempDht.setText(dhtTemp+ (char) 0x00B0 );
                    txtHum.setText(dhtHum+"%");
                    DecimalFormat df = new DecimalFormat("###.##");
                    txtPress.setText( df.format(bmpPress/100));
                    //rain chart

                    int rainRate= (int)(100*(1-(rainVal/1024)));
                    //chartRain.setUsePercentValues(true);
                    List<PieEntry> yvalues = new ArrayList<PieEntry>();
                    yvalues.add(new PieEntry ( rainRate));
                    yvalues.add(new PieEntry ( 100-rainRate));
                    PieDataSet dataSet = new PieDataSet( yvalues,"");
                    dataSet.setDrawValues(false);
                    dataSet.setColors(new int[]{Color.BLUE,Color.WHITE});
                    PieData data = new PieData(dataSet);
                    chartRain.setData(data);
                    chartRain.setCenterText(String.valueOf(rainRate)+"%");
                    chartRain.invalidate();


                    txtView.setText(rainVal+" "+rainRate);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }
    private void getHistoricMetereologic(){

        final List<BarEntry> entries = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url =dirApiProxy+"stationStatistics?stationId=M0001";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray historicObj=response.getJSONArray("historicPress");
                    int pos[]={0,1,2,4,6,8,10,12,23};
                    for(int i = 0; i<pos.length; i++){
                        float val=0;
                        if(!historicObj.isNull(pos[i])){
                            JSONObject obj= historicObj.getJSONObject(pos[i]);
                            val=(float) (obj.isNull("val")?0:obj.getDouble("val"));
                            val-=100000;
                        }
                        entries.add(new BarEntry(((pos.length-1)-i), val)); //Graficar del ultimo al primer

                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset
                    dataSet.setDrawValues(false);

                    BarData data = new BarData(dataSet);

                    chartPressure.setData(data);
                    chartPressure.invalidate(); // refresh

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                enableInterface();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                txtView.setText(error.toString());
                enableInterface();
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }


    private void disableInterface(){
        buttonCloseGate.setEnabled(false);
        buttonOpenGate.setEnabled(false);
        toggleButtonS1.setEnabled(false);
        toggleButtonS2.setEnabled(false);
        toggleButtonS3.setEnabled(false);
        toggleButtonS4.setEnabled(false);
        toggleButtonS5.setEnabled(false);
        toggleButtonS6.setEnabled(false);
        toggleButtonS7.setEnabled(false);
        toggleButtonS8.setEnabled(false);

    }
    private void enableInterface(){
        buttonCloseGate.setEnabled(true);
        buttonOpenGate.setEnabled(true);
        toggleButtonS1.setEnabled(true);
        toggleButtonS2.setEnabled(true);
        toggleButtonS3.setEnabled(true);
        toggleButtonS4.setEnabled(true);
        toggleButtonS5.setEnabled(true);
        toggleButtonS6.setEnabled(true);
        toggleButtonS7.setEnabled(true);
        toggleButtonS8.setEnabled(true);

    }
}
