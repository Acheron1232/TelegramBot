package com.bot.firstbot.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class HttpC {
    public static void main(String[] args) throws IOException, InterruptedException, JSONException {
        Scanner scanner =new Scanner(System.in);
        boolean d = true;
        while (d){
            String sd =scanner.nextLine();
            if(sd.equals("stop")) d=false;
            System.out.println(chatGpT(sd));
        }

    }

    public static String chatGpT(String text) throws IOException, InterruptedException, JSONException {
        HttpClient client = HttpClient.newBuilder().
                version(HttpClient.Version.HTTP_2)
                .build();
        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("temperature", 0.5);
        data.put("max_tokens", 400);
        data.put("top_p", 0.5);
        data.put("frequency_penalty", 0.5);
        data.put("presence_penalty", 0.5);
        var request = HttpRequest.newBuilder(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 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")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(data)))
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject js = new JSONObject(response.body());
        String s = js.getJSONArray("choices").getJSONObject(0).getString("text");
        return s;
    }
}
