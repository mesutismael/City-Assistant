package smartassist.appreciate.be.smartassist.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.lang.ref.WeakReference;

/**
 * Created by Inneke De Clippel on 9/02/2016.
 */
public class QrCodeAsyncTask extends AsyncTask<Void, Void, Bitmap>
{
    private String message;
    private int size;
    private WeakReference<QrCodeAsyncTaskListener> listener;

    public QrCodeAsyncTask(String message, int size)
    {
        this.message = message;
        this.size = size;
    }

    public void setListener(QrCodeAsyncTaskListener listener)
    {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        try
        {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(this.message, BarcodeFormat.QR_CODE, this.size, this.size);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int colorPixels = 0xff5ac2e6;
            int colorBackground = 0xffffffff;

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++)
            {
                int offset = y * width;
                for (int x = 0; x < width; x++)
                {
                    pixels[offset + x] = bitMatrix.get(x, y) ? colorPixels : colorBackground;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }
        catch (WriterException e)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        super.onPostExecute(bitmap);

        if (this.listener != null && this.listener.get() != null)
            this.listener.get().onQrCodeCreated(bitmap);
    }

    public interface QrCodeAsyncTaskListener
    {
        void onQrCodeCreated(Bitmap qrCode);
    }
}
