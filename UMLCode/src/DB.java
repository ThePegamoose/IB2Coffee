import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DB {

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

    public  void waterPopup(){

        JOptionPane.showMessageDialog(null,"Not enough water");
    }

    public void temperaturePopUp(){

        JOptionPane.showMessageDialog(null,"Coffee getting cold");
    }

    public String parseJSON(String jsonString){
        String var ="";

        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject curObject = array.getJSONObject(i);
                var += curObject.getString("Value");
                int Val=Integer.parseInt(var);
                if(Val == 0){
                   //System.out.println("The waterlevel is  " + curObject.getString("Value") );
                    waterPopup();
                }
                if (Val== 30){
                    temperaturePopUp();
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return var;
    }


    public static void main(String[] args) {
        DB rc = new DB();
        String response = rc.makeGETRequest("https://studev.groept.be/api/a21ib2a04/waterlevel" );
        rc.parseJSON(response);
        System.out.println(rc.parseJSON(response));
    }
}


