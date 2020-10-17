package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 天气参数
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@ToString
public class WeatherData {
    private Double temp;
    private Double humidity;
}
