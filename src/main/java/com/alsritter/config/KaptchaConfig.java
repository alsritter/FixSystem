package com.alsritter.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.util.Config;

import java.util.Properties;

/**
 * 验证码这块代码全是 copy 的
 * 参考：https://juejin.im/post/6844903894661890055
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
public class KaptchaConfig {

    /**
     * =========Constant	描述	默认值==================
     * kaptcha.border	                图片边框，合法值：yes , no	yes
     * kaptcha.border.color	            边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.	black
     * kaptcha.border.thickness	        边框厚度，合法值：>0	1
     * kaptcha.image.width	            图片宽	200
     * kaptcha.image.height	            图片高	50
     * kaptcha.producer.impl	        图片实现类	com.google.code.kaptcha.impl.DefaultKaptcha
     * kaptcha.textproducer.impl	    文本实现类	com.google.code.kaptcha.text.impl.DefaultTextCreator
     * kaptcha.textproducer.char.string	文本集合，验证码值从此集合中获取	abcde2345678gfynmnpwx
     * kaptcha.textproducer.char.length	验证码长度	5
     * kaptcha.textproducer.font.names	字体	Arial, Courier
     * kaptcha.textproducer.font.size	字体大小	40px.
     * kaptcha.textproducer.font.color	字体颜色，合法值： r,g,b  或者 white,black,blue.	black
     * kaptcha.textproducer.char.space	文字间隔	2
     * kaptcha.noise.impl	            干扰实现类	com.google.code.kaptcha.impl.DefaultNoise
     * kaptcha.noise.color	            干扰颜色，合法值： r,g,b 或者 white,black,blue.	black
     * kaptcha.obscurificator.impl	    图片样式：
     * 水纹 com.google.code.kaptcha.impl.WaterRipple
     * 鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
     * 阴影 com.google.code.kaptcha.impl.ShadowGimpy	com.google.code.kaptcha.impl.WaterRipple
     * kaptcha.background.impl	        背景实现类	com.google.code.kaptcha.impl.DefaultBackground
     * kaptcha.background.clear.from	背景颜色渐变，开始颜色	light grey
     * kaptcha.background.clear.to	    背景颜色渐变，结束颜色	white
     * kaptcha.word.impl	            文字渲染器	com.google.code.kaptcha.text.impl.DefaultWordRenderer
     * kaptcha.session.key	session key	KAPTCHA_SESSION_KEY
     * kaptcha.session.date	session date	KAPTCHA_SESSION_DATE
     */


    @Bean(name="captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        //验证码是否带边框 No
        properties.setProperty("kaptcha.border", "no");
        //验证码字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        //验证码整体宽度（注意：因为自定义样式,所以需要再去修改 NoKaptchaBackhround 里的宽高）
        properties.setProperty("kaptcha.image.width", "60");
        //验证码整体高度
        properties.setProperty("kaptcha.image.height", "30");
        //文字个数
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //文字大小
        properties.setProperty("kaptcha.textproducer.font.size","20");
        //文字随机字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        //文字距离
        properties.setProperty("kaptcha.textproducer.char.space","3");
        //干扰线颜色
        properties.setProperty("kaptcha.noise.color","blue");

        // 这里添加自定义的样式
        //自定义验证码样式
        properties.setProperty("kaptcha.obscurificator.impl","com.alsritter.model.DisKaptchaCssImpl");
        //自定义验证码背景
        properties.setProperty("kaptcha.background.impl","com.alsritter.model.NoKaptchaBackhround");
        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
