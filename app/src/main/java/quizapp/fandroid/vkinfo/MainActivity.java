package quizapp.fandroid.vkinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import static quizapp.fandroid.vkinfo.utils.NetworkUtils.generateURL;
import static quizapp.fandroid.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private TextView result;

    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                // отправляем и получаем запрос с сервера
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        protected void onPostExecute(String response) {
            result.setText(response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // создам сопрас на сервер
                URL generateURL = generateURL(searchField.getText().toString());
                // создаем оделный поток , что бы запрос не использовал основной поток
                new VKQueryTask().execute(generateURL);

                //result.setText(generateURL.toString());
            }
        };

        searchButton.setOnClickListener(onClickListener);
    }
}
