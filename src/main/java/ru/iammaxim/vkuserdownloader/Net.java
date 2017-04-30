package ru.iammaxim.vkuserdownloader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Net {
    private static final String version = "v=5.52";

    //example: messages.getDialogs, true, count=1, offset=0
    public static String processRequest(String groupAndName, boolean useAccessToken, String... keysAndValues) throws IOException {
        StringBuilder sb = new StringBuilder("https://api.vk.com/method/");
        sb.append(groupAndName);
        String url = sb.toString();
        sb.delete(0, sb.length());
        sb.append(version);
        if (useAccessToken) {
            sb.append("&access_token=").append(Main.getAccessToken());
        }
        for (String s : keysAndValues) {
            sb.append("&").append(s.replace(" ", "%20"));
        }
        String params = sb.toString();
        sb.delete(0, sb.length());

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Length", "" + params.getBytes().length);
        connection.getOutputStream().write(params.getBytes());
        connection.connect();
        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNext()) {
            sb.append(scanner.nextLine());
        }

        return sb.toString();
    }

    public static void writeResponseToOS(OutputStream os, String groupAndName, boolean useAccessToken, String... keysAndValues) throws IOException {
        StringBuilder sb = new StringBuilder("https://api.vk.com/method/");
        sb.append(groupAndName);
        String url = sb.toString();
        sb.delete(0, sb.length());
        sb.append(version);
        if (useAccessToken) {
            sb.append("&access_token=").append(Main.getAccessToken());
        }
        for (String s : keysAndValues) {
            sb.append("&").append(s.replace(" ", "%20"));
        }
        String params = sb.toString();
        sb.delete(0, sb.length());

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Length", "" + params.getBytes().length);
        connection.getOutputStream().write(params.getBytes());
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            os.write((s + '\n').getBytes());
        }
        reader.close();
    }
}
