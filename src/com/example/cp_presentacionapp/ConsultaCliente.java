package com.example.cp_presentacionapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.support.v4.app.DialogFragment;
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


public class ConsultaCliente extends Activity{

	private EditText mEditfecha1,mEditfecha2;
	private ListView lsitahabitaciones;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta_cliente);
		
		mEditfecha1 = (EditText) findViewById(R.id.editfecha1);
		mEditfecha2 = (EditText) findViewById(R.id.editfecha2);
		lsitahabitaciones = (ListView)findViewById(R.id.lvHabitaciones);
		
	}

	
	public void onClickActualizar(View v){
		
        final Thread myThread= new Thread(new Runnable(){
        	  String respuesta;
              JSONArray json2;
              
            @Override
            public void run(){
                HttpClient httpclient = new DefaultHttpClient();
                String fechainicio = mEditfecha1.getText().toString();
                String fechafin = mEditfecha2.getText().toString();
                HttpGet del = new HttpGet("http://192.168.1.226:8080/rest/restlistarHabDisponibles?diaEntrada="+fechainicio
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
