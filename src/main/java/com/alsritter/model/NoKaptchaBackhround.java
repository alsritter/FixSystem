package com.alsritter.model;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 去除验证码背景实现类
 *
 * @author alsritter
 * @version 1.0
 **/
public class NoKaptchaBackhround  extends Configurable implements BackgroundProducer {

    @Override
    public BufferedImage addBackground(BufferedImage baseImage) {
        int width = 60;
        int height = 30;
        BufferedImage imageWithBackground = new BufferedImage(width, height, 1);
        Graphics2D graph = (Graphics2D)imageWithBackground.getGraphics();
        graph.fill(new Rectangle2D.Double(0.0D, 0.0D, (double)width, (double)height));
        graph.drawImage(baseImage, 0, 0, null);
        return imageWithBackground;
    }
}
