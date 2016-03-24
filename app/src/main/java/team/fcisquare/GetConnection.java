package team.fcisquare;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Andrew on 3/22/2016.
 */
public class GetConnection extends AsyncTask<String, String, String> implements Connection {
    private HashMap<String, String> data;
    private ConnectionListener listener;

    public GetConnection(HashMap<String, String> data, ConnectionListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        byte[] result = null;
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet();
        try {
            // set up get data
            String dataString = ""; // handle data, as ?id=1
            Iterator<String> it = data.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next(), value = data.get(key);
                dataString += "?" + key + "=" + value;
            }

            get.setURI(new URI(params[0] + dataString)); // set URI to params[0] + dataString

            HttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
        }
        return str;
    }
    @Override
    public void onPostExecute(String result){
        listener.getResult(result);
    }

}
