package com.example.magdadmilbat;

import org.opencv.core.Point;

public class Ball {
    private int _radius;
    private Point _center;

    public Ball(int radius, Point center){
        _radius = radius;
        _center = new Point(new double[]{center.x, center.y});
    }

    public Point getCenter(){
        return new Point(new double[]{_center.x, _center.y});
    }

    public int getRadius(){
        return _radius;
    }
}