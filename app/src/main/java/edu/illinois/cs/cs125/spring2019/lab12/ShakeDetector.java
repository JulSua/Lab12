package edu.illinois.cs.cs125.spring2019.lab12;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * class shake detector.
 */
public class ShakeDetector implements SensorEventListener {
    /**
     * gforce threshold of shake.
     */
    private static final float SHAKE_THRESHOLD_GFORCE = 2.5f;
    /**
     * slop time.
     */
    private static final int SHAKE_SLOPTIME_MS = 500;
    /**
     * reset count timme.
     */
    private static final int SHAKE_RESET_COUNT_TIME_MS = 2000;
    /**
     * listener.
     */
    private OnShakeListener listen;
    /**
     * time stamp.
     */
    private long shakeTimeStamp;
    /**
     * count.
     */
    private int shakeCount;

    /**
     * shake listener function.
     * @param listener = listener
     */
    public void setOnShakeListener(final OnShakeListener listener) {
        this.listen = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (listen != null) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            double gX = x / SensorManager.GRAVITY_EARTH;
            double gY = y / SensorManager.GRAVITY_EARTH;
            double gZ = z / SensorManager.GRAVITY_EARTH;

            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GFORCE) {
                final long now = System.currentTimeMillis();
                if (shakeTimeStamp + SHAKE_SLOPTIME_MS > now) {
                    return;
                }
                if (shakeTimeStamp + SHAKE_RESET_COUNT_TIME_MS < now) {
                    shakeCount = 0;
                }
                shakeTimeStamp = now;
                shakeCount++;
                listen.onShake(shakeCount);
            }
        }
    }
}
