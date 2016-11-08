package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by Inneke on 9/02/2015.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private Camera camera;
    private Handler autoFocusHandler;
    private boolean previewing = true;
    private boolean surfaceCreated = false;
    private Camera.PreviewCallback previewCallback;

    public CameraPreview(Context context)
    {
        super(context);
    }

    public CameraPreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setCamera(Camera camera, Camera.PreviewCallback previewCallback)
    {
        this.camera = camera;
        this.previewCallback = previewCallback;
        this.autoFocusHandler = new Handler();
    }

    public void initCameraPreview()
    {
        if (this.camera != null)
        {
            this.getHolder().addCallback(this);
            if (this.previewing)
                requestLayout();
            else
                showCameraPreview();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        this.surfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3)
    {
        if (surfaceHolder.getSurface() == null)
        {
            return;
        }
        this.stopCameraPreview();
        this.showCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        this.surfaceCreated = false;
        this.stopCameraPreview();
    }

    public void showCameraPreview()
    {
        if (this.camera != null)
        {
            try
            {
                this.previewing = true;
                this.setupCameraParameters();
                this.camera.setPreviewDisplay(getHolder());
                this.camera.setDisplayOrientation(getDisplayOrientation());
                this.camera.setOneShotPreviewCallback(this.previewCallback);
                this.camera.startPreview();
                this.camera.autoFocus(this.autoFocusCB);
            } catch (Exception e)
            {
            }
        }
    }

    public void stopCameraPreview()
    {
        if (this.camera != null)
        {
            try
            {
                this.previewing = false;
                this.camera.cancelAutoFocus();
                this.camera.setOneShotPreviewCallback(null);
                this.camera.stopPreview();
            } catch (Exception e)
            {
            }
        }
    }

    public void setupCameraParameters()
    {
        Camera.Size optimalSize = this.getOptimalPreviewSize();
        Camera.Parameters parameters = this.camera.getParameters();
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        this.camera.setParameters(parameters);
    }

    public int getDisplayOrientation()
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else
        {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private Camera.Size getOptimalPreviewSize()
    {
        if (this.camera == null)
            return null;

        List<Camera.Size> sizes = this.camera.getParameters().getSupportedPreviewSizes();
        if (sizes == null)
            return null;

        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenResolution = new Point();
        display.getSize(screenResolution);
        int screenWidth = screenResolution.x;
        int screenHeight = screenResolution.y;
        double screenRatio = (double) screenWidth / screenHeight;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes)
        {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - screenRatio) <= 0.1 && Math.abs(size.height - screenHeight) < minDiff)
            {
                optimalSize = size;
                minDiff = Math.abs(size.height - screenHeight);
            }
        }

        if (optimalSize == null)
        {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes)
            {
                if (Math.abs(size.height - screenHeight) < minDiff)
                {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - screenHeight);
                }
            }
        }

        return optimalSize;
    }

    private Runnable doAutoFocus = new Runnable()
    {
        public void run()
        {
            if (CameraPreview.this.camera != null && CameraPreview.this.previewing && CameraPreview.this.surfaceCreated)
            {
                CameraPreview.this.camera.autoFocus(CameraPreview.this.autoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback()
    {
        public void onAutoFocus(boolean success, Camera camera)
        {
            CameraPreview.this.autoFocusHandler.postDelayed(CameraPreview.this.doAutoFocus, 1000);
        }
    };

    private double getPreviewRatio()
    {
        Camera.Size optimalSize = this.getOptimalPreviewSize();
        if(optimalSize == null)
            return 0;
        double width = optimalSize.width;
        double height = optimalSize.height;
        return height / width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        double ratio = this.getPreviewRatio();
        if(ratio != 0)
        {
            double width = MeasureSpec.getSize(widthMeasureSpec);
            double height = MeasureSpec.getSize(heightMeasureSpec);

            if(width * ratio < height)
                height = width * ratio;
            else
                width = height / ratio;

            this.setMeasuredDimension((int) width, (int) height);
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
