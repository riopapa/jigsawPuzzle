package biz.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class VibratePhone {
    public VibratePhone(Context context) {

        VibratorManager vibratorManager;
        Vibrator vibrator = null;
        VibrationEffect vibrationEffect = null;
        final long[] vibPattern = {70, 70, 70, 70};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                vibratorManager =
                        (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
                vibrationEffect = VibrationEffect.createWaveform(vibPattern, -1);
        }
        vibrator.cancel();
        vibrator.vibrate(vibrationEffect);
    }
}
