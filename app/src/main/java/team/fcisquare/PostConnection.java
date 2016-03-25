package team.fcisquare;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author Andrew Albert
 * @version 1.0
 * @since 24/3/2016
 *
 * This class is used for POST connection requests
 *
 */
public class PostConnection extends Connection {
    private HashMap<String, String> data;
    private ConnectionListener listener;

    /**
     * ust an instructor, get hashmap of values and listener interface
     *
     * @param data
     * @param listener
     */
    public PostConnection(HashMap<String, String> data, ConnectionListener listener) {
        this.data = data;
        this.listener = listener;
    }

    /**
     * Method used to connect to server
     *
     * @param params
     * @return result of the service
     */
    @Override
    protected String doInBackground(String... params) {
        byte[] result = null;
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
        try {
            // set up post data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<>();
            Iterator<String> it = data.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, data.get(key)));
            }

            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));

            HttpResponse response = client.execute(post);
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
    /**
     * Method to return result to the listener
     *
     * @param result
     */
    @Override
    public void onPostExecute(String result){
        listener.getResult(result);
    }

}
