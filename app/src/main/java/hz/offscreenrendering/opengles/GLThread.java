package hz.offscreenrendering.opengles;

import hz.offscreenrendering.MyGLSurfaceView;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class GLThread extends Thread {

    private Object mLock = new Object();
    private EGLHelper mEglHelper;
    private boolean mHasSurface = false;
    public int mWidth;
    public int mHeight;
    private MyGLSurfaceView mMyGLSurfaceView;

    public GLThread(MyGLSurfaceView myGLSurfaceView) {
        this.mMyGLSurfaceView = myGLSurfaceView;
    }

    @Override
    public void run() {
        try {
            doThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doThread() throws InterruptedException {
        mEglHelper = new EGLHelper(mMyGLSurfaceView);
        MyGLSurfaceView view = mMyGLSurfaceView;
        // 确保有了Surface
        synchronized (mLock) {
            if (!mHasSurface) {
                mLock.wait();
            }
        }
        mEglHelper.initEGLContext();
        /*----实际上是在一个循环里面针对不同的事件(onSurfaceCreated/onSurfaceChanged/onDrawFrame/terminal)执行不同的回调----*/
        /*-----所有情况都执行完后，wait，当某个事件发生后标志置位，然后调用notify----*/
        view.mRenderer.onSurfaceCreated();
        // 也要确保在surfaceChanged之后执行
        view.mRenderer.onSurfaceChanged(mWidth, mHeight);
        view.mRenderer.onDrawFrame();
        mEglHelper.swap();
    }

    public void surfaceCreated() {
    }

    public void surfaceChanged(int w, int h) {
        mWidth = w;
        mHeight = h;
        synchronized (mLock) {
            mHasSurface = true;
            mLock.notifyAll();
        }
    }

    public void surfaceDestroyed() {
        synchronized (mLock) {
            mHasSurface = false;
            mLock.notifyAll();
        }
    }
}
