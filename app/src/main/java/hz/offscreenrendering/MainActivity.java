package hz.offscreenrendering;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

import hz.offscreenrendering.opengles.EGLManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        EGLManager eglManager = new EGLManager(surfaceView);
    }
}
