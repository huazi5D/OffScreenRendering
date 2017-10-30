package hz.offscreenrendering.opengles;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by Administrator on 2017-10-30.
 */

public class EGLManager {

    class EGLHelper {

        private EGL10 mEgl;
        private EGLDisplay mEglDisplay;

        public void initEGLContext() {

            mEgl = (EGL10) EGLContext.getEGL();
            mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

            int[] version = new int[2];    //主版本号和副版本号,不关心版本号可传入null
            mEgl.eglInitialize(mEglDisplay, version);

//            mEgl.eglChooseConfig(mEglDisplay, )
        }
    }
}
