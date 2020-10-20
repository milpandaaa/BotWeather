import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;


import javax.rmi.CORBA.ValueHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherService{

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String getWeather(Float lat, Float lon) throws Exception {
        String appid = "ec3fc36a556d4d2bba2bc1ab35be0144";
        String url = "http://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=hourly,alerts,minutely&units=metric&lang=ru&appid=" + appid;
        String response = executePost(url);
        return response;
    }

    public String executePost(String url) throws Exception {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        String inputLine;
        StringBuilder outLine = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            outLine.append(inputLine);
        in.close();

        return outLine.toString();
    }

    public Answer onSuccess(String response) {
        Page page = new Page();
        Answer answer = new Answer();
        try {
            page = OBJECT_MAPPER.readValue(response, Page.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования json", e);
        }
        answer.setTempCur(page.getCurrent().getTemp());
        List<String> temp = new ArrayList<>();
        page.getCurrent().getWeather().forEach(search ->{
            temp.add(search.getDescription());
        });
        answer.setDescriptionCur(temp.toString());

        Daily daily = page.getDaily().get(0);

        answer.setTempDay(daily.getTemp().getDay());
        answer.setTempMin(daily.getTemp().getMin());
        answer.setTempMax(daily.getTemp().getMax());
        answer.setTempNight(daily.getTemp().getNight());
        answer.setTempEve(daily.getTemp().getEve());
        answer.setTempMorn(daily.getTemp().getMorn());

        List<String> tempDesc = new ArrayList<>();
        daily.getWeather().forEach(search ->{
            tempDesc.add(search.getDescription());
        });

        answer.setDescriptionDay(tempDesc.toString());
        return answer;
    }
}
