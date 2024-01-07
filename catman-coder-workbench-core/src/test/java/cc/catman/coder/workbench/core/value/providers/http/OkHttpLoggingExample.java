package cc.catman.coder.workbench.core.value.providers.http;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

public class OkHttpLoggingExample {

    public static void main(String[] args) {
        // Enable SSL debugging
        System.setProperty("javax.net.debug", "ssl");

        // Create an OkHttpClient instance
        OkHttpClient client = getOkHttpClient();

        // Build a request
        Request request = new Request.Builder()
                .url("https://www.example.com")
                .build();

        // Execute the request
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static OkHttpClient getOkHttpClient() {
        try {
            // Create an OkHttpClient with a custom TrustManager
            TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Enable TLS
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

            // Create an OkHttpClient that uses the custom SSLContext
            return new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                    .hostnameVerifier((hostname, session) -> true) // Ignore hostname verification for debugging
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create OkHttpClient", e);
        }
    }
}
