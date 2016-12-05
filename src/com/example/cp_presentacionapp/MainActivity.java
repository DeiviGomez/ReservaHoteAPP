package com.example.cp_presentacionapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

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
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;



public class MainActivity extends Activity {

	private EditText et_usuario, et_Password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 et_usuario = (EditText)findViewById(R.id.txtUsuario);
	     et_Password = (EditText)findViewById(R.id.txtPass);    
	}

	public void VerificarAcceso(View v){
		   if(et_usuario.getText().toString().equals("")){
	            Toast.makeText(MainActivity.this, "Debe ingresar el Usuario!", Toast.LENGTH_LONG).show();
	            return;
	        }
	        if(et_Password.getText().toString().equals("")){
	            Toast.makeText(MainActivity.this, "Debe ingresar el Password!", Toast.LENGTH_LONG).show();
	            return;
	        }
     final Thread myThread= new Thread(new Runnable(){

         String respuesta;
         JSONObject json;
         @Override
         public void run(){
             HttpClient httpclient = new DefaultHttpClient();
             String Usuario = et_usuario.getText().toString();
             String password = et_Password.getText().toString();
   
             HttpGet del = new HttpGet("http://13.68.210.51:8080/cp_presentacionREST/restverificaacceso?_Usuario="+
             		Usuario+"&_Pass="+password);

             del.setHeader("content-type", "application/json");
             try {
                 HttpResponse resp = httpclient.execute(del);
                 respuesta = EntityUtils.toString(resp.getEntity());
                 json = new JSONObject(respuesta);
                 //json2 = new JSONArray(respuesta);
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
                     	if(respuesta.equals("0") || respuesta.equals("")|| respuesta==null ){
                     		 Toast.makeText(MainActivity.this, "Usuario o Passsword no valido", Toast.LENGTH_SHORT).show();
                     
                         }else{
                            final int idusuario=Integer.parseInt(respuesta.toString());
                            
                            if(respuesta.equals("1")){
                            	 Toast.makeText(MainActivity.this, "Bienvenido cliente "+ respuesta, Toast.LENGTH_SHORT).show();
                            	 Intent x = new Intent(MainActivity.this, ConsultaCliente.class);
                            	 et_usuario.setText("");
                                 et_Password.setText("");
                                 startActivity(x);
                            }else if(respuesta.equals("2")){
                            	Toast.makeText(MainActivity.this, "Bienvenido Administrador "+ respuesta, Toast.LENGTH_SHORT).show();
                            	 Intent y = new Intent(MainActivity.this, ConsultaAdministrador.class);
                            	 et_usuario.setText("");
                                 et_Password.setText("");
                                 startActivity(y);
                            }else{
                            	Toast.makeText(MainActivity.this, "No se reconoce al Usuario", Toast.LENGTH_SHORT).show();
                            }
                      
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });
         }
     });myThread.start();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
