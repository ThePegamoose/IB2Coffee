import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.json.*;

public class DBTest {

    public String makeGETRequest(String urlName){
        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;
        try {
            URL url = new URL(urlName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                sb.append(line + '\n');
            }
            conn.disconnect();
            return sb.toString();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (ProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "";

    }

    public String parseJSON(String jsonString, String key){
        String Sensor = "";
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                Sensor += curObject.getString(key);
                if (i != (array.length()-1))
                {
                    Sensor += ", ";
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Sensor;
    }

    public String checkMax(String jsonString){
        String Location = "";
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                Location += curObject.getString("Location");
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Location;

    }

/*
    public static void main(String[] args) {
        DBTest rc = new DBTest();
        String response = rc.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Sensor_ID_C_Benedicta" );
        String maxPressure = rc.makeGETRequest("https://studev.groept.be/api/a21ib2a04/hPa_max_Benedicta/hPa" );
        rc.parseJSON(response, "ID");
        rc.checkMax(maxPressure);
    }
 */


}


