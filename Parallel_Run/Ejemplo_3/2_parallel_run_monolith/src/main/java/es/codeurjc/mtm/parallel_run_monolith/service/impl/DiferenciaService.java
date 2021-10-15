package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import com.lordofthejars.diferencia.api.DiferenciaConfiguration;
import com.lordofthejars.diferencia.core.Diferencia;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;

@Slf4j
@Service
public class DiferenciaService {

    private final OkHttpClient client = new OkHttpClient();

    @Value("${user-notification-ms.host}")
    private String USER_NOTIFICATION_MS_HOST;

    @Value("${user-notification-ms.port}")
    private int USER_NOTIFICATION_MS_PORT;


    public Boolean diferenciaCore(Long id) throws IOException {
        final DiferenciaConfiguration.Builder configurationBuilder =
                new DiferenciaConfiguration
                        .Builder("http://now.httpbin.org", "http://now.httpbin.org")
                        .withNoiseDetection(true);

        Diferencia diferencia = new Diferencia(configurationBuilder);

        diferencia.start();

        final String diferenciaUrl = diferencia.getDiferenciaUrl();
        final Response response = sendRequest(diferenciaUrl, "/");

        diferencia.close();

        Boolean result = response.code() == HttpURLConnection.HTTP_OK;
        log.info("The diferencia result with compare oldCode/newCode is "+result);
        return result;
    }

    private okhttp3.Response sendRequest(String diferenciaUrl, String path) throws IOException {

        final Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(diferenciaUrl + path)
                .build();

        return client.newCall(request).execute();
    }

}

