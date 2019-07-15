package quizapp.fandroid.vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String VK_API_BASE_URL = "https://api.vk.com/";
    private static final String VK_USERS_GET = "/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_VERSION = "v";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String PARAM_ACCESS_TOKEN_CONST="65178b5665178b5665178b5645657c225f6651765178b56383e702f795ef5420d634aa6";

    public static URL generateURL(String userIds) {
        Uri builUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userIds)
                .appendQueryParameter(PARAM_VERSION, "5.8")
                .appendQueryParameter(ACCESS_TOKEN, PARAM_ACCESS_TOKEN_CONST)
                .build();

        URL url = null;
        try {
            url = new URL(builUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            //   \\A  это разделителль говорт, что мы поток будет получать целой строкой
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
            // это исключение срабатывает , когда нет интернета
        } catch (UnknownHostException е){
            return null;
        }

        finally {
            urlConnection.disconnect();
        }


    }
}
