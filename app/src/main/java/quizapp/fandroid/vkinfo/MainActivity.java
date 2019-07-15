package quizapp.fandroid.vkinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static quizapp.fandroid.vkinfo.utils.NetworkUtils.generateURL;
import static quizapp.fandroid.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private TextView result;
    // переменная отвечает , если у нас возник ошибки с соединением с вк
    private TextView errorMessage;
    //индикатор
    private ProgressBar loadingIndicator;

    //этот метод вызываеться, если от ВК пришел ответ ,
    // результат мы отображаем на экран, а текст сошибкой мы скрываем
    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    // этот метод вызываеться, если от ВК не пришел ответ ,
    //  текст сошибкой мы отображаем, а результат мы будем скрывать с экрана
    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }


    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            // запускаем индикатор загрузки
            loadingIndicator.setVisibility(View.VISIBLE);
        }

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
            //распарсим строки json
            String firstName = null;
            String lastName = null;
            String resultingString ="";

            // мы проверяем , получили мы сообщение от сервера
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    // берем response
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");


                     for (int i = 0; i < jsonArray.length(); i++) {

                        // берем i элемент , и пополучаем все эго элементы
                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        // получаем firstName
                        firstName = userInfo.getString("first_name");
                        // получаем lastName
                        lastName = userInfo.getString("last_name");
                        // собираем строку для вывода на экрна
                        resultingString += "Имя: " + firstName + "\n" + "Фамилия: " + lastName+"\n\n";
                    }
                    // выодим результат
                    result.setText(resultingString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // выводим область срезультатом
                showResultTextView();

            } else {
                // выводим область сошибкой
                showErrorTextView();
            }
            // индикатор делаем не видемым
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        // результат
        result = findViewById(R.id.tv_result);
        // ошибка
        errorMessage = findViewById(R.id.tv_error_message);
        // индикатор
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

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
