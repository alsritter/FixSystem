package com.alsritter.proportion;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 天气权重
 *
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
public class WeatherWeight implements WeightInterface {

    private Double temp = 26.0;
    private Double humidity = 44.0;
    private RestTemplate restTemplate;

    public WeatherWeight() {
        restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory()); // 使用HttpClient，支持GZIP
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码

    }


    @Override
    public Double getWeight() {

        String url = "https://devapi.heweather.net/v7/weather/now?location={location}&key={key}";
        Map<String, Object> map = new HashMap<>();
        map.put("location", "101010100");
        map.put("key", "4298410860e442dda4620d0a3c0456bd");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, map);
        if (response.getStatusCodeValue() == 200) {
            String now = JSON.parseObject(response.getBody()).getString("now");
            // 温度
            this.temp = JSON.parseObject(now).getDouble("temp");
            // 湿度
            this.humidity = JSON.parseObject(now).getDouble("humidity");

            log.debug("当前温度:{}  湿度湿度:{}", temp, humidity);

            Double tempW = makerWeight(temp, 26.0, 5.0, 5.0);
            Double humidityW = makerWeight(humidity, 55.0, 10.0, 2.0);
            return 0.3 * tempW + 0.7 * humidityW;
        }

        return 1.0;
    }


    public Double makerWeight(Double data, Double original, Double dataFloat, Double weight) {
        if (Math.abs(data - original) > dataFloat) {
            Double r = data / original;
            if (r > 0) {
                return Math.log(r) / Math.log(weight);
            } else {
                return (ThreadLocalRandom.current().nextGaussian() / dataFloat + 0.5) * r / 500;
            }
        }

        return 1.0;
    }

}
