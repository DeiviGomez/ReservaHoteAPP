package com.example.cp_presentacionapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.widget.TimePicker;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Dialog;
import android.content.Intent;
import android.app.DatePickerDialog;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;


public class ConsultaCliente extends FragmentActivity{

	private ListView lsitahabitaciones;
	
	static EditText DateEdit,DateEdit2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta_cliente);
				
		DateEdit = (EditText) findViewById(R.id.editfecha1);
		
		DateEdit2 = (EditText) findViewById(R.id.editfecha2);
		
		lsitahabitaciones = (ListView)findViewById(R.id.lvHabitaciones);
		
		
		DateEdit.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
			 showTruitonDatePickerDialog(v);
			 }
			 });
		
		DateEdit2.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
			 showTruitonDatePickerDialog2(v);
			 }
			 });	
	}

	
	public void showTruitonDatePickerDialog(View v) {
		 DialogFragment newFragment = new DatePickerFragment();
		 newFragment.show(getSupportFragmentManager(), "Fecha Inicio");
		 }
	
	public void showTruitonDatePickerDialog2(View v) {
		 DialogFragment newFragment = new DatePickerFragment2();
		 newFragment.show(getSupportFragmentManager(), "Fecha Fin");
		 }
	
	public static class DatePickerFragment extends DialogFragment implements
	 DatePickerDialog.OnDateSetListener {
	 
		 @Override
		 public Dialog onCreateDialog(Bundle savedInstanceState) {
			 final Calendar c = Calendar.getInstance();
			 int year = c.get(Calendar.YEAR);
			 int month = c.get(Calendar.MONTH);
			 int day = c.get(Calendar.DAY_OF_MONTH);
			 return new DatePickerDialog(getActivity(), this, year, month, day);
		 }
		 
		 public void onDateSet(DatePicker view, int year, int month, int day) {
			 DateEdit.setText(day + "/" + (month + 1) + "/" + year);
		 }
	 }
	
	public static class DatePickerFragment2 extends DialogFragment implements
	 DatePickerDialog.OnDateSetListener {	 
		 @Override
		 public Dialog onCreateDialog(Bundle savedInstanceState) {
			 final Calendar c = Calendar.getInstance();
			 int year = c.get(Calendar.YEAR);
			 int month = c.get(Calendar.MONTH);
			 int day = c.get(Calendar.DAY_OF_MONTH);
			 return new DatePickerDialog(getActivity(), this, year, month, day);
		 }
		 
		 public void onDateSet(DatePicker view, int year, int month, int day) {
			 DateEdit2.setText(day + "/" + (month + 1) + "/" + year);
		 }
	 }
	
	
	public void onClickActualizar(View v){
		
		
		//Toast.makeText(ConsultaCliente.this, DateEdit.getText(), Toast.LENGTH_SHORT).show();
		//Toast.makeText(ConsultaCliente.this, DateEdit2.getText(), Toast.LENGTH_SHORT).show();
		
		
        final Thread myThread= new Thread(new Runnable(){
        	  String respuesta;
              JSONArray json2;
              
            @Override
            public void run(){
                HttpClient httpclient = new DefaultHttpClient();
                String fechainicio = DateEdit.getText().toString();
                String fechafin = DateEdit2.getText().toString();
                HttpGet del = new HttpGet("http://13.68.210.51:8080/cp_presentacionREST/restlistarHabDisponibles?diaEntrada="+fechainicio
                		+"&diaSalida="+fechafin);

                del.setHeader("content-type", "application/json");
                try {
                    HttpResponse resp = httpclient.execute(del);
                    respuesta = EntityUtils.toString(resp.getEntity());
                    json2 = new JSONArray(respuesta);
                }catch(HttpResponseException e){
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(json2!=null){	                            	
                                ArrayList<String> lista = new ArrayList<String>();
                    			 for(int i=0;i<json2.length();i++){
                    				 
                    				JSONObject jsonObject = json2.getJSONObject(i);	                    				    
                    				String codigo=jsonObject.getString("codigo");
                    				
                    				JSONObject datotipohabitacion = jsonObject.getJSONObject("tipoHabitacion");             				
                    				String nombre=datotipohabitacion.getString("nombre");
                    				String costoxdia=datotipohabitacion.getString("costoxdia");	
                    				
                    				lista.add("Habitacion: "+nombre+" Codigo: "+ codigo+ " Costo Dia: "+costoxdia);	                    					                    			
                    			}	
                    			 
                    				ArrayAdapter<String>adaptador  = new ArrayAdapter<String>(ConsultaCliente.this, android.R.layout.simple_list_item_1, lista);
                    				lsitahabitaciones.setAdapter(adaptador);
                    		
                                }else{
                                Toast.makeText(ConsultaCliente.this, "Error! Verifique si las fechas son correctas...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        myThread.start();
        
        
		
	}
	
}
