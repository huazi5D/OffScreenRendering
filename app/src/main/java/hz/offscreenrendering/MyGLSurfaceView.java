package hz.offscreenrendering;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hz.offscreenrendering.opengles.GLThread;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class MyGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceHolder mHolder;
    public MyRenderer mRenderer;
    private Object mLock = new Object();
    private GLThread mThread;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setRenderer(new MyRenderer());
    }

    public void setRenderer(MyRenderer renderer) {
        mRenderer = renderer;
        mThread = new GLThread(this);
        mThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread.surfaceCreated();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        mThread.surfaceChanged(w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mThread.surfaceDestroyed();
    }

    public void stopRender() {
        // 设置停止标示，notify
    }

    public class MyRenderer implements Renderer {

        @Override
        public void onSurfaceCreated() {

        }

        @Override
        public void onSurfaceChanged(int width, int height) {

        }

        @Override
        public void onDrawFrame() {

        }
    }

    public interface Renderer {
        void onSurfaceCreated();

        void onSurfaceChanged(int width, int height);

        void onDrawFrame();
    }

}
