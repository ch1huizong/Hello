package Http;

import static android.graphics.BitmapFactory.decodeFile;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils {

    public static String sendUrl(String url) {
        StringBuffer buffer = new StringBuffer();

        try {
            URL u = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();

            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String str = null;
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static Bitmap loadImage(String url) {
        Bitmap bm = null;

        try {
            URL u = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();

            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            InputStream in = conn.getInputStream();

            String fileName = String.valueOf(System.currentTimeMillis());
            FileOutputStream out = null;
            File fileDownload = null;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File parent = Environment.getExternalStorageDirectory();
                fileDownload = new File(parent, fileName);
                out = new FileOutputStream(fileDownload);
            }

            byte[] bytes = new byte[2 * 1024];
            int lens;
            if (out != null) {
                while ((lens = in.read(bytes)) != -1) {
                    out.write(bytes, 0, lens);
                }
                bm = decodeFile(fileDownload.getAbsolutePath());
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (ProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
