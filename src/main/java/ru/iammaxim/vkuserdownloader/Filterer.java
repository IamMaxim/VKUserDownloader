package ru.iammaxim.vkuserdownloader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by maxim on 4/30/17 at 7:39 AM.
 */
public class Filterer {
    private VKUserDownloader vk;

    public Filterer(VKUserDownloader vk) {
        this.vk = vk;
    }

    public void filter() throws IOException {
        //create output dir
        {
            File dir = new File("filtered");
            if (!dir.exists())
                dir.mkdirs();
        }

        for (int i = 0; i < vk.thread_count; i++) {
            System.out.println("Started filtering file " + i + ".txt");
            File in_file = new File("processed/" + i + ".txt");
            File out_file = new File("filtered/" + i + ".txt");
            if (!out_file.exists())
                out_file.createNewFile();

            Scanner scanner = new Scanner(new FileReader(in_file));
            FileOutputStream fos = new FileOutputStream(out_file);

            while (scanner.hasNext()) {
                try {
                    String s = scanner.nextLine();
                    if (s.isEmpty())
                        continue;
                    JSONObject user = new JSONObject(s);
                    if (!user.has("city"))
                        continue;
                    JSONObject city = user.getJSONObject("city");
                    if (city.getInt("id") != 109) //Penza
                        continue;
                    fos.write((s + '\n').getBytes());
                } catch (Exception e) {
                }
            }

            scanner.close();
            fos.close();
            System.out.println("Completed filtering file " + i + ".txt");
        }
    }

    public void filterMerged() throws IOException {
        File in_file = new File("merged.txt");
        File out_file = new File("merged_filtered.txt");
        if (!out_file.exists())
            out_file.createNewFile();

        Scanner scanner = new Scanner(in_file);
        FileOutputStream fos = new FileOutputStream(out_file);

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.isEmpty())
                continue;

            JSONObject o = new JSONObject(s);
            if (!o.has("bdate"))
                continue;

            String[] bdate = o.getString("bdate").split("\\.");
            if (bdate.length < 3)
                continue;

            if (Integer.parseInt(bdate[2]) < 1997)
                continue;

            if (!o.has("last_seen"))
                continue;

            if (o.getJSONObject("last_seen").getLong("time") < 1493337600)
                continue;

            fos.write((s + '\n').getBytes());
        }
    }

    public void filterFiltered() throws IOException {
        File in_file = new File("merged.txt");
        File out_file = new File("merged_filtered.txt");
        if (!out_file.exists())
            out_file.createNewFile();

        Scanner scanner = new Scanner(in_file);
        FileOutputStream fos = new FileOutputStream(out_file);

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            if (s.isEmpty())
                continue;

            JSONObject o = new JSONObject(s);

        }
    }
}
