package com.bteam.project.home.task;

import android.os.AsyncTask;
import android.util.Log;

import com.bteam.project.home.model.Weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherTask extends AsyncTask<String, Void, Weather> {
    private static final String TAG = "WeatherTask";

    private String zone;

    public WeatherTask(String zone) {
        this.zone = zone;
    }

    @Override
    protected Weather doInBackground(String... urls) {
        URL url;
        Document document = null;
        String temp = null, current = null, city = null;
        try {
            url = new URL("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=" + zone);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(url.openStream()));
            document.getDocumentElement().normalize();

            NodeList itemNodeList = document.getElementsByTagName("item");
            for (int i = 0; i < itemNodeList.getLength(); i++) {
                Node node = itemNodeList.item(i);
                Element element = (Element) node;

                // Temperature
                NodeList tempNodeList = element.getElementsByTagName("temp");
                temp = tempNodeList.item(0).getChildNodes().item(0).getNodeValue() + "˚";
                Log.e(TAG, "onPostExecute: " +  temp);

                // Current
                NodeList currNodeList = element.getElementsByTagName("wfKor");
                current = currNodeList.item(0).getChildNodes().item(0).getNodeValue();
                Log.e(TAG, "onPostExecute: " + current);

                // City
                NodeList cityNodeList = element.getElementsByTagName("category");
                city = cityNodeList.item(0).getChildNodes().item(0).getNodeValue();
                Log.e(TAG, "onPostExecute: " +  city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Weather(temp, current, city);
    }

}
