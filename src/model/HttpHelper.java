package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper
{
    public String doPut(String url, String message, String contentType)
    {
        return send("PUT", url, message, contentType);
    }

    public String doPost(String url, String message, String contentType)
    {
        return send("POST", url, message, contentType);
    }

    public String doGet(String url)
    {
        return send("GET", url, null, null);
    }

    public String send(String method, String url, String message, String contentType)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(message != null); //true if POST or PUT
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            if (message != null)
            {
                connection.setRequestProperty("Content-Type", contentType);
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(message);
                wr.flush();
                wr.close();
            }
            return getResponse(connection);

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
