package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inneke on 9/02/2015.
 */
public class QRScannerView extends FrameLayout implements Camera.PreviewCallback
{
    private Camera camera;
    private CameraPreview preview;

    private MultiFormatReader multiFormatReader;
    private ResultHandler resultHandler;
    private boolean scanning;

    public interface ResultHandler
    {
        void handleResult(Result rawResult);
    }

    public QRScannerView(Context context)
    {
        super(context);
        this.scanning = false;
        this.setupLayout();
        this.initMultiFormatReader();
    }

    public QRScannerView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        this.scanning = false;
        this.setupLayout();
        this.initMultiFormatReader();
    }

    public void setupLayout()
    {
        this.preview = new CameraPreview(this.getContext());
        FrameLayout.LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        this.addView(this.preview, params);
    }

    private void initMultiFormatReader()
    {
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        this.multiFormatReader = new MultiFormatReader();
        this.multiFormatReader.setHints(hints);
    }

    public void startCamera()
    {
        try
        {
            this.camera = Camera.open(this.getCameraIndex());
            this.preview.setCamera(this.camera, this);
            this.preview.initCameraPreview();
        }
        catch (Exception e)
        {
            this.camera = null;
        }
    }

    private int getCameraIndex()
    {
        int cameraFront = -1;
        int cameraBack = -1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); camIdx++)
        {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                cameraFront = camIdx;
            else if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                cameraBack = camIdx;
        }

        if(cameraBack != -1)
            return cameraBack;
        else
            return cameraFront;
    }

    public void setResultHandler(ResultHandler resultHandler)
    {
        this.resultHandler = resultHandler;
    }

    public void setScanning(boolean scanning)
    {
        this.scanning = scanning;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
        int width = size.width;
        int height = size.height;

        if (this.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            int tmp = width;
            width = height;
            height = tmp;
            data = rotatedData;
        }

        Result rawResult = null;
        PlanarYUVLuminanceSource source = this.buildLuminanceSource(data, width, height);

        if (source != null)
        {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try
            {
                rawResult = this.multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re)
            {
            } catch (NullPointerException npe)
            {
            } catch (ArrayIndexOutOfBoundsException aoe)
            {
            } finally
            {
                this.multiFormatReader.reset();
            }
        }

        if (rawResult != null && this.scanning)
        {
            this.stopCamera();
            if (this.resultHandler != null)
                this.resultHandler.handleResult(rawResult);
        } else
        {
            camera.setOneShotPreviewCallback(this);
        }
    }

    public void stopCamera()
    {
        if (this.camera != null)
        {
            this.preview.stopCameraPreview();
            this.preview.setCamera(null, null);
            this.camera.release();
            this.camera = null;
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height)
    {
        Rect rect = new Rect(0, 0, width, height);
        PlanarYUVLuminanceSource source = null;

        try
        {
            source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
        }
        catch (Exception e)
        {
        }

        return source;
    }
}
