package com.sanousun.mdweather.bg.md.rotate;


import com.sanousun.mdweather.bg.md.MaterialWeatherView;

/**
 * Base Rotate controller.
 */

public class BaseRotateController extends MaterialWeatherView.RotateController {

    private double rotate;

    private static double ROTATE_SLOP = 0.25;

    public BaseRotateController(double rotate) {
        this.rotate = rotate;
    }

    @Override
    public void updateRotation(double rotate) {
        if (Math.abs(this.rotate - rotate) > ROTATE_SLOP) {
            this.rotate = rotate;
        }
    }

    @Override
    public double getRotate() {
        return rotate;
    }
}
