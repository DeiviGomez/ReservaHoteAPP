package com.example.cp_presentacionapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

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

import com.example.cp_presentacionapp.ConsultaCliente.DatePickerFragment;
import com.example.cp_presentacionapp.ConsultaCliente.DatePickerFragment2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConsultaAdministrador extends FragmentActivity {
	
	//private EditText mEditfecha1,mEditfecha2;
	private ListView lsitahabitacionesConsultaAdminist;
	
	static EditText DateEdit,DateEdit2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta_administrador);
		
		DateEdit = (EditText) findViewById(R.id.editfecha1);
		
		DateEdit2 = (EditText) findViewById(R.id.editfecha2);
		
		//mEditfecha1 = (EditText) findViewById(R.id.editfecha1);
		//mEditfecha2 = (EditText) findViewById(R.id.editfecha2);
		lsitahabitacionesConsultaAdminist = (ListView)findViewById(R.id.lvHabitacionesConsAd);
		
		
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
		
        final Thread myThread= new Thread(new Runnable(){
        	  String respuesta;
              JSONArray json2;
              
            @Override
            public void run(){
                HttpClient httpclient = new DefaultHttpClient();
                 String fechainicio = DateEdit.getText().toString();
                 String fechafin = DateEdit2.getText().toString();             
                HttpGet del = new HttpGet("http://13.68.210.51:8080/cp_presentacionREST/restlistarHabOcupadas?diaEntrada="+fechainicio
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
                                final ArrayList<String> lista = new ArrayList<String>();
                             
                    			 for(int i=0;i<json2.length();i++){
                    				 
                    				JSONObject jsonObject = json2.getJSONObject(i);
                    			
                    				JSONObject datoshabitacion = jsonObject.getJSONObject("habitacion");    
                    				  String codigo=datoshabitacion.getString("codigo");
                    				
                    				JSONObject datotipohabitacion = datoshabitacion.getJSONObject("tipoHabitacion"); 
                    				  String costoxdia=datotipohabitacion.getString("costoxdia");
                    				  String nombre=datotipohabitacion.getString("nombre");
                    				  
                    				JSONObject datoreserva = jsonObject.getJSONObject("reserva"); 
                    				 String fechaInicio=datoreserva.getString("fechaInicio");
                   				     String fechaFIin=datoreserva.getString("fechafinal");
                   				    
                   				    JSONObject datopersona = datoreserva.getJSONObject("persona"); 
                   				      String pellidomaterno=datopersona.getString("apellidomaterno");
                   				      String pellidopaterno=datopersona.getString("apellidopaterno");
                   				      String nombrepersona=datopersona.getString("nombre");

                   				    lista.add("            - Habitaciones Ocupada -        "+                  				    		
                   				    		"Fecha de Reserva:                                       "+
                   				    		fechaInicio+" al "+ fechaFIin+ "              "+
                   				    		nombre+"                       Codigo: "+ codigo+
                   				    		"                                              Datos Cliente: "+
                   				    		nombrepersona+ " "+ pellidopaterno+ " "+ pellidomaterno
                   				    		);
                    			}	
                    			 
                    	
                                 final Thread myThread= new Thread(new Runnable(){
                         	        	  String respuesta2;
                         	              JSONArray json1;
                         	              
                         	            @Override
                         	            public void run(){
                         	                HttpClient httpclient = new DefaultHttpClient();

                         	      		    String fechainicio2 = DateEdit.getText().toString();
                                            String fechafin2 = DateEdit2.getText().toString();  
                                  
                                         
                         	               HttpGet del2 = new HttpGet("http://13.68.210.51:8080/cp_presentacionREST/restlistarHabDisponibles?diaEntrada="+fechainicio2
                         	                		+"&diaSalida="+fechafin2);

                         	                del2.setHeader("content-type", "application/json");
                         	                try {
                         	                    HttpResponse resp2 = httpclient.execute(del2);
                         	                    respuesta2 = EntityUtils.toString(resp2.getEntity());
                         	                    json1 = new JSONArray(respuesta2);
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
                         	                            if(json1!=null){
                         	                            	
                         	                               ArrayList<String> listadis = new ArrayList<String>();
                         	                    			 for(int i=0;i<json1.length();i++){
                         	                    				 
                         	                    				JSONObject jsonObject = json1.getJSONObject(i);	                    				    
                         	                    				String codigo=jsonObject.getString("codigo");
                         	                    				
                         	                    				JSONObject datotipohabitacion = jsonObject.getJSONObject("tipoHabitacion");             				
                         	                    				String nombre=datotipohabitacion.getString("nombre");
                         	                    				String costoxdia=datotipohabitacion.getString("costoxdia");	
                         	                    				
                         	                    				
                         	                    				listadis.add("            ** Habitaciones Disponibles **        "+  
                         	                    							"Habitacion: "+nombre+" Codigo: "+ codigo+ " Costo Dia: "+costoxdia);	                    					                    			
                         	                    			}	
                         	                    			lista.addAll(listadis);
                         	                    			 
                         	                               	ArrayAdapter<String>adaptador  = new ArrayAdapter<String>(ConsultaAdministrador.this, android.R.layout.simple_list_item_1, lista);                   
                                            				lsitahabitacionesConsultaAdminist.setAdapter(adaptador);   			 
                         	                    		}	    
                         	                                else{
                         	                                Toast.makeText(ConsultaAdministrador.this, "Error al cargar los datos...", Toast.LENGTH_SHORT).show();
                         	                            }
                         	                        } catch (Exception e) {
                         	                            e.printStackTrace();
                         	                        }
                         	                    }
                         	                });
                         	            }
                         	        });
                         	        myThread.start(); 
                                }else{
                                Toast.makeText(ConsultaAdministrador.this, "Error! Verifique si las fechas son correctas...", Toast.LENGTH_SHORT).show();
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consulta_administrador, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
