package hz.offscreenrendering.opengles;

import android.opengl.EGL14;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import hz.offscreenrendering.MyGLSurfaceView;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class EGLHelper {

    private EGL10 mEgl;
    private EGLDisplay mEglDisplay;
    private EGLSurface mEglSurface;
    private EGLContext mEglContext;
    private MyGLSurfaceView mSurfaceView;

    public EGLHelper(MyGLSurfaceView sufaceView) {
        this.mSurfaceView = sufaceView;
    }

    public void initEGLContext() {

        mEgl = (EGL10) EGLContext.getEGL();
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        int[] version = new int[2];    //主版本号和副版本号,不关心版本号可传入null
        mEgl.eglInitialize(mEglDisplay, version);

        int[] attributes = new int[] {
                EGL10.EGL_RED_SIZE,   8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE,  8,
                EGL10.EGL_ALPHA_SIZE, 8,// if you need the alpha channel
                EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL10.EGL_SURFACE_TYPE,
                EGL10.EGL_WINDOW_BIT,
                EGL10.EGL_DEPTH_SIZE, 16,// if you need the depth buffer
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        mEgl.eglChooseConfig(mEglDisplay, attributes, configs, 1, num_config);

        mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, configs[0], mSurfaceView.mHolder, null);
        int[] contextAttr = new int[] {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        mEglContext = mEgl.eglCreateContext(mEglDisplay, configs[0], EGL10.EGL_NO_CONTEXT, contextAttr);

        if (mEgl.eglGetError() != EGL10.EGL_SUCCESS) {
            Log.d("ddd", "initEGLContext: ");
        }
        mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext);
    }

    public void swap() {
        mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
    }

    public void destroySurface() {
        if (mEglSurface != null) {
            mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
            mEglSurface = null;
        }
        if (mEglContext != null) {
            mEgl.eglDestroyContext(mEglDisplay, mEglContext);
            mEglContext = null;
        }
        if (mEglDisplay != null) {
            mEgl.eglTerminate(mEglDisplay);
            mEglDisplay = null;
        }
    }
}
