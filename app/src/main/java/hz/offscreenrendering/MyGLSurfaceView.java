package hz.offscreenrendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hz.offscreenrendering.opengles.AFilter;
import hz.offscreenrendering.opengles.GLThread;
import hz.offscreenrendering.opengles.GrayFilter;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class MyGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceHolder mHolder;
    public MyRenderer mRenderer;
    private Object mLock = new Object();
    private GLThread mThread;
    private AFilter mFilter;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setRenderer(new MyRenderer());
        mFilter = new GrayFilter();
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

    public class MyRenderer implements Renderer {

        @Override
        public void onSurfaceCreated() {
            Bitmap bmp= BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/1508689016.png");
            int textureID = createTexture(bmp);

            mFilter.create();
            mFilter.setTextureId(textureID);
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
            mFilter.setSize(width, height);
        }

        @Override
        public void onDrawFrame() {
            mFilter.draw();
        }
    }

    public interface Renderer {
        void onSurfaceCreated();

        void onSurfaceChanged(int width, int height);

        void onDrawFrame();
    }

    private int createTexture(Bitmap bmp){
        int[] texture=new int[1];
        if(bmp!=null&&!bmp.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
            return texture[0];
        }
        return 0;
    }

}
