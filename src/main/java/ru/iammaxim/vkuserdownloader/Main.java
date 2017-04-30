package ru.iammaxim.vkuserdownloader;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Main {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

    public static void main(String[] args) throws IOException {
        VKUserDownloader vk = new VKUserDownloader();
        vk.thread_count = 32;
        vk.run(VKUserDownloader.Mode.FILTER_MERGED);
        vk.run(VKUserDownloader.Mode.FORMAT);
    }

    public static String getAccessToken() {
        return "";
    }
}
