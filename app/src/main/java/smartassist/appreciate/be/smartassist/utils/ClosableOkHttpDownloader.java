package smartassist.appreciate.be.smartassist.utils;

import android.net.Uri;

import com.coolerfall.download.Downloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.coolerfall.download.Utils.getFilenameFromHeader;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Inneke De Clippel on 2/08/2016.
 *
 * Copy of OkHttpDownloader. Changes:
 * - line 119: throw an IOException instead of a DownloadException because that one is protected.
 * - lines 67, 88, 112: close the body instead of the response because response.close() does not exist.
 */
public class ClosableOkHttpDownloader implements Downloader
{
    private final OkHttpClient client;
    private Response response;
    private final AtomicInteger redirectionCount = new AtomicInteger();

    private static OkHttpClient defaultOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(20 * 1000, MILLISECONDS)
                .readTimeout(25 * 1000, MILLISECONDS)
                .writeTimeout(20 * 1000, MILLISECONDS)
                .build();
    }

    /**
     * Create an instance using a default {@link OkHttpClient}.
     *
     * @return {@link ClosableOkHttpDownloader}
     */
    public static ClosableOkHttpDownloader create() {
        return new ClosableOkHttpDownloader(null);
    }

    /**
     * Create an instance using a {@code client}.
     *
     * @return {@link ClosableOkHttpDownloader}
     */
    public static ClosableOkHttpDownloader create(OkHttpClient client) {
        return new ClosableOkHttpDownloader(client);
    }

    private ClosableOkHttpDownloader(OkHttpClient client) {
        this.client = client == null ? defaultOkHttpClient() : client;
    }

    @Override public String detectFilename(Uri uri) throws IOException
    {
        redirectionCount.set(5);
        Response response = innerRequest(client, uri, 0);
        String url = response.request().url().toString();
        String contentDisposition = response.header("Content-Disposition");
        if(response.body() != null)
        {
            response.body().close();
        }
        return getFilenameFromHeader(url, contentDisposition);
    }

    @Override public int start(Uri uri, long breakpoint) throws IOException {
        redirectionCount.set(5);
        response = innerRequest(client, uri, breakpoint);
        return response.code();
    }

    @Override public long contentLength() {
        return response == null ? -1 : response.body().contentLength();
    }

    @Override public InputStream byteStream() {
        return response == null ? null : response.body().byteStream();
    }

    @Override public void close() {
        if (response != null && response.body() != null) {
            response.body().close();
        }
    }

    @Override public Downloader copy() {
        return create(client.newBuilder().build());
    }

    Response innerRequest(OkHttpClient client, Uri uri, long breakpoint) throws IOException {
        Request.Builder builder = new Request.Builder().url(uri.toString());
        if (breakpoint > 0) {
            builder.header("Accept-Encoding", "identity")
                    .header("Range", "bytes=" + breakpoint + "-")
                    .build();
        }
        Response response = client.newCall(builder.build()).execute();
        int statusCode = response.code();
        switch (statusCode) {
            case 301:
            case 302:
            case 303:
            case 307:
                if(response.body() != null)
                {
                    response.body().close();
                }
                if (redirectionCount.decrementAndGet() >= 0) {
				/* take redirect url and call start recursively */
                    String redirectUrl = response.header("Location");
                    return innerRequest(client, Uri.parse(redirectUrl), breakpoint);
                } else {
                    throw new IOException("redirects too many times");
                }
        }

        return response;
    }
}
