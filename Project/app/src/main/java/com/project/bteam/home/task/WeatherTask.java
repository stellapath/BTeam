package com.project.bteam.home.task;

import android.os.AsyncTask;

import com.project.bteam.home.model.Weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherTask extends AsyncTask<String, Void, ArrayList<Weather>> {
    private static final String TAG = "WeatherTask";

    private String zone;

    public WeatherTask(String zone) {
        this.zone = zone;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... urls) {
        URL url;
        Document document = null;
        ArrayList<Weather> list = new ArrayList<>();

        try {
            url = new URL("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=" + zone);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(url.openStream()));
            document.getDocumentElement().normalize();

            NodeList itemNodeList = document.getElementsByTagName("item");
            Node itemNode = itemNodeList.item(0);
            Element itemElement = (Element) itemNode;

            // 지역 City
            NodeList cityNodeList = itemElement.getElementsByTagName("category");
            String city = cityNodeList.item(0).getChildNodes().item(0).getNodeValue();

            NodeList dataNodeList = document.getElementsByTagName("data");
            for (int i = 0; i < dataNodeList.getLength(); i++) {
                Node node = dataNodeList.item(i);
                Element element = (Element) node;

                // 시간 hour
                NodeList hourNodeList = element.getElementsByTagName("hour");
                String hour = hourNodeList.item(0).getChildNodes().item(0).getNodeValue();

                // 오늘 내일 모레 day
                NodeList dayNodeList = element.getElementsByTagName("day");
                String day = dayNodeList.item(0).getChildNodes().item(0).getNodeValue();

                // 온도 Temperature
                NodeList tempNodeList = element.getElementsByTagName("temp");
                String temp = tempNodeList.item(0).getChildNodes().item(0).getNodeValue() + "˚";

                // 상태 current
                NodeList currNodeList = element.getElementsByTagName("wfKor");
                String current = currNodeList.item(0).getChildNodes().item(0).getNodeValue();

                list.add(new Weather(hour, day, temp, current, city));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
