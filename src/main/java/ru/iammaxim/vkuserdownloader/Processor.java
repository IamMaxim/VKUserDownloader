package ru.iammaxim.vkuserdownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by maxim on 4/30/17 at 7:39 AM.
 */
public class Processor {
    private VKUserDownloader vk;

    public Processor(VKUserDownloader vk) {
        this.vk = vk;
    }

    public void process() throws IOException {
        //create output dir
        {
            File dir = new File("processed");
            if (!dir.exists())
                dir.mkdirs();
        }

        for (int i = 0; i < vk.thread_count; i++) {
            System.out.println("Started processing file " + i + ".txt");
            File in_file = new File(i + ".txt");
            File out_file = new File("processed/" + i + ".txt");
            if (!out_file.exists())
                out_file.createNewFile();

            Scanner scanner = new Scanner(new FileReader(in_file));
            FileOutputStream fos = new FileOutputStream(out_file);

            while (scanner.hasNext()) {
                try {
                    String s = scanner.nextLine();
                    if (s.isEmpty())
                        continue;
                    JSONArray arr = new JSONObject(s).getJSONArray("response");
                    arr.forEach(o -> {
                        try {
                            if (!((JSONObject) o).has("deactivated"))
                                fos.write((o.toString() + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                }
            }

            scanner.close();
            fos.close();
            System.out.println("Completed processing file " + i + ".txt");
        }
    }
}
