package cc.catman.coder.workbench.core.value.providers.http;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.report.ReportMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HttpValueProvider extends AbstractValueProvider {

    private HttpValueProviderArgs args;
    @Override
    public Optional<Object> run(ValueProviderContext context) {

//        OkHttpClient okHttpClient=new OkHttpClient().newBuilder()
//                .addInterceptor(new RequestLoggingInterceptor())
//                .addInterceptor(new HandshakeLoggingInterceptor())
//                .addInterceptor(new ResponseLoggingInterceptor())
//                .build();
//
//        Headers headers=Headers.of();
//
//        args.getHeaders().forEach((header,values)->{
//            values.forEach(value->headers.newBuilder().add(header,value));
//        });
//        Request request=new Request.Builder()
//                .url(args.getUrl())
//                .method(args.getMethod(), Optional.ofNullable(args.getBody()).map(body->RequestBody.create(body.getBytes())).orElse(null))
//                .build();
//
//        Call call = okHttpClient.newCall(request);
//        Response res = call.execute();

        context.breakPoint();
       ;
        HttpRequest request = null;
        HttpResponse<String> response = null;
        try {
            HttpClient client=HttpClient.newHttpClient();
            HttpRequest.Builder requestBuild = HttpRequest.newBuilder()
                    .uri(URI.create(args.getUrl()))
                    .timeout(args.getSettings().timeout() == 0 ? null : Duration.ofMillis(args.getSettings().timeout()))
                    .method(args.getMethod(), Optional.ofNullable(args.getBody()).map(HttpRequest.BodyPublishers::ofString).orElse(HttpRequest.BodyPublishers.noBody()));
            args.getHeaders().forEach((header,values)->{
                if (CollectionUtils.isEmpty(values)) {
                    requestBuild.header(header, "");
                }else {
                    values.forEach(value->requestBuild.header(header, value));
                }
            });

             request = requestBuild
                    .build();

            // 读取body数据
            context.breakPoint("after-build-request", Map.of("request",request));
            long start = System.currentTimeMillis();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            long end = System.currentTimeMillis();

            // end-start: 才是真正的请求时间,但依然有java框架的开销
            HttpResponse<String> finalResponse = response;
            context.report(()-> ReportMessage.builder()
                    .sourceType("http")
                    .batchId(context.getBatchId())
                    .eventKind(HttpEventKind.HTTP_REQUEST_DONE.name())
                    .data(Map.of("request",Map.of("url",args.getUrl()
                                    ,"method", finalResponse.request().method()
                                    ,"headers", finalResponse.request().headers().map()
                                    ,"body",Optional.ofNullable(args.getBody()).orElse("")
                                    ,"timeout", finalResponse.request().timeout().map(java.time.Duration::toMillis).orElse(0L)
                                    ,"version", finalResponse.request().version())
                            ,"response",Map.of("statusCode", finalResponse.statusCode()
                                    ,"body", finalResponse.body()
                                    ,"headers", finalResponse.headers().map()
                                    ,"version", finalResponse.version()
                                    ,"sslSession", finalResponse.sslSession().map(SSLSession::toString).orElse("")
                                    ,"uri", finalResponse.uri().toString()
                            ),"ssl",summarySSL(finalResponse.sslSession())
                            ,"duration",end-start))
                    .build());


            HttpValueProviderResult res = HttpValueProviderResult.builder()
                    .statusCode(response.statusCode())
                    .body(response.body())
                    .headers(response.headers().map())
                    .build();

            context.breakPoint("beforeReturn",
                    Map.of("response",response
                            ,"statusCode",response.statusCode()
                            ,"body",response.body()
                            ,"headers",response.headers().map())
            );


            return Optional.ofNullable(res);
        } catch (Exception e){
            // 一旦发生异常,则直接返回,但在此之前需要上报异常信息
            context.report(()-> ReportMessage.builder()
                    .sourceType("http")
                    .batchId(context.getBatchId())
                    .eventKind(HttpEventKind.HTTP_REQUEST_FAILED.name())
                    .data(Map.of(
                            "error",Map.of("message",e.getMessage()
                                    ,"stackTrace",Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray())))
                    .build());
            throw new RuntimeException(e);
        }
    }

    private Object summarySSL(Optional<SSLSession> sslSession){
        return sslSession
                .map(ssl->{
                    String protocol = ssl.getProtocol();
                    String cipherSuite = ssl.getCipherSuite();
                    long creationTime = ssl.getCreationTime();
                    long lastAccessedTime = ssl.getLastAccessedTime();
                    Certificate[] localCertificates = ssl.getLocalCertificates();
                    Certificate[] peerCertificates = new Certificate[0];
                    try {
                        peerCertificates = ssl.getPeerCertificates();
                    } catch (SSLPeerUnverifiedException ignored) {
                    }
                    String peerHost = ssl.getPeerHost();
                    int peerPort = ssl.getPeerPort();
                    return Map.of("protocol",protocol
                            ,"cipherSuite",cipherSuite
                            ,"creationTime",creationTime
                            ,"lastAccessedTime",lastAccessedTime
                            ,"localCertificates",summaryCertificate(localCertificates)
                            ,"peerCertificates",summaryCertificate(peerCertificates)
                            ,"peerHost",peerHost
                            ,"peerPort",peerPort
                    );
                })
                .orElse(Collections.emptyMap());
    }

    public Object summaryCertificate(Certificate[] certificates){
        if (certificates==null){
            return Collections.emptyList();
        }
            return Arrays.stream(certificates).map(c -> {
                PublicKey publicKey = c.getPublicKey();
                String algorithm = publicKey.getAlgorithm();
                String format = publicKey.getFormat();

                try {
                    return Map.of("type", c.getType()
                            , "algorithm", algorithm
                            , "format", format
                            , "encoded", c.getEncoded()
                            ,"publicEncoded",publicKey.getEncoded()
                    );
                } catch (CertificateEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    // 请求日志拦截器
    private static class RequestLoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            System.out.println("Request: " + request.toString());
            return chain.proceed(request);
        }
    }

    // 握手信息日志拦截器
    private static class HandshakeLoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            System.out.println("Handshake: " + response.handshake());
            return response;
        }
    }

    // 响应日志拦截器
    private static class ResponseLoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            System.out.println("Response: " + response.toString());
            return response;
        }
    }
}
