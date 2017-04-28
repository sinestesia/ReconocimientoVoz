package es.pamp.reconocimientovoz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    // Para transformar texto a voz se puede usar TextToSpeech.

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    Button btnvoz;
    TextView txtresultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnvoz = (Button) findViewById(R.id.btnvoz);
        txtresultado = (TextView) findViewById(R.id.txtresultado);

        PackageManager pm = getPackageManager();
        //LLAMAMOS A LA ACTIVIDAD QUE SE ENCARGA DE
        // GESTIONAR EL MICROFONO
        List<ResolveInfo> actividadmicro = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (actividadmicro.size() != 0) {
            btnvoz.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { capturarVoz();
                }
            });
        }else{
            txtresultado.setText("NO HAY MICROFONO ACTIVO");
        }
    }
    private void capturarVoz(){
        //CREAMOS UN INTENT PARA PODER LANZAR EL MICROFONO
        // DEL TELEFONO, ENVIANDO EL LENGUAJE DEL PROPIO
        // MOVIL
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //TEXTO OPCIONAL PARA MOSTRAR AL USUARIO CUANDO SE LE PIDE QUE HABLE.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
        //LANZAMOS UN HILO QUE SE ENCARGARÁ DE RECUPERAR LA
        // RESPUESTA CUANDO HAYAMOS TERMINADO DE HABLAR
        // E IRÁ AL MÉTODO ONACTIVITYRESULT
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ALGUNOS TELEFONOS PIDEN CLAVE DE ACCESO PARA
        // PODER COMUNICARSE CON EL MICRO,
        // DEPENDE DE CADA USUARIO Y DE LA
        // CONFIGURACIÓN DEL TELEFONO
        // TAMBIÉN PREGUNTAMOS SI EL RESULTADO ES OK
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            //CAPTURAMOS LAS POSIBLESCOINCIDENCIAS
            // QUE EL MICROFONO NOS HA PROPORCIONADO
            ArrayList<String> palabras = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //Y AHORA SIMPLEMENTE TENDREMOS QUE MOSTRAR
            // LOS RESULTADOS OBTENIDOS DENTRODE
            // NUESTRO CONTROL TEXTVIEW
            String resultado = "";
            for(int i = 0; i < palabras.size(); i++){
                resultado += palabras.get(i) + " ";
                //en cada palabras guarda una versión diferente
            }
            txtresultado.setText(palabras.get(0)); }
        }
}


