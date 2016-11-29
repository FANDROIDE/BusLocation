package com.example.feliciano.buslocation;

import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button Consultar,ConsultarId,Insertar,Borrar,Actualizar;

    String IP="http://192.168.43.221:3000";
    String GET=IP+"/chofer";
    String GET_BY_ID=IP+"/";
    String INSERT=IP+"/";
    String UPDATE=IP+"/";
    String DELETE=IP+"/";

    ObtenerWebService HiloConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Consultar=(Button)findViewById(R.id.Consultar);
        ConsultarId=(Button)findViewById(R.id.ConsultarID);
        Insertar=(Button)findViewById(R.id.Insertar);
        Borrar=(Button)findViewById(R.id.Borrar);
        Actualizar=(Button)findViewById(R.id.Actualizar);

        Consultar.setOnClickListener(this);
        ConsultarId.setOnClickListener(this);
        Insertar.setOnClickListener(this);
        Borrar.setOnClickListener(this);
        Actualizar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Consultar:
                HiloConexion =new ObtenerWebService();
                HiloConexion.execute(GET,"1");
                System.out.println("boton consultar");
            break;
            case R.id.ConsultarID:
                HiloConexion =new ObtenerWebService();
                HiloConexion.execute(GET_BY_ID,"2");
                System.out.println("boton consultar por id");
                break;
            case R.id.Insertar:
                HiloConexion =new ObtenerWebService();
                HiloConexion.execute(INSERT,"3");
                System.out.println("boton consultar inserat");
                break;
            case R.id.Borrar:
                HiloConexion =new ObtenerWebService();
                HiloConexion.execute(DELETE,"4");
                System.out.println("boton consultar borrar");
                break;
            case R.id.Actualizar:
                HiloConexion =new ObtenerWebService();
                HiloConexion.execute(UPDATE,"5");
                System.out.println("boton consultar actualizar");
                break;
            default:
                break;
        }
    }
    public class ObtenerWebService extends AsyncTask<String,Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            //super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena=params[0];
            URL url=null;
            String devuelve="";
            if(params[1]=="1"){
                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){

                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        /*String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                        if (resultJSON=="1"){      // hay alumnos a mostrar
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("alumnos");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve + alumnosJSON.getJSONObject(i).getString("idalumno") + " " +
                                        alumnosJSON.getJSONObject(i).getString("nombre") + " " +
                                        alumnosJSON.getJSONObject(i).getString("direccion") + "\n";

                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay alumnos";
                        }
                        */
                        JSONArray choferesJSON = respuestaJSON.getJSONArray("choferes");   // estado es el nombre del campo en el JSON
                        for(int i=0;i<choferesJSON.length();i++){
                            devuelve = devuelve + choferesJSON.getJSONObject(i).getString("idchofer") + " " +
                                    choferesJSON.getJSONObject(i).getString("nombre") + " " +
                                    choferesJSON.getJSONObject(i).getString("apellidopaterno")+" "+
                                    choferesJSON.getJSONObject(i).getString("apellidomaterno") + "\n";
                        }


                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(devuelve);

                return devuelve;


            }
            else if(params[1]=="2"){

            }
            else if(params[1]=="3"){

            }
            else if(params[1]=="4"){

            }
            else if(params[1]=="5"){

            }
            return null;
        }
    }
}
