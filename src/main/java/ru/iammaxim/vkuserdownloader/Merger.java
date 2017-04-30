package ru.iammaxim.vkuserdownloader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by maxim on 4/30/17 at 7:34 AM.
 */
public class Merger {
    private VKUserDownloader vk;

    public Merger(VKUserDownloader vk) {
        this.vk = vk;
    }

    public void merge() throws IOException {
        System.out.println("Started merging " + vk.thread_count + " files");

        File out_file = new File("merged.txt");
        if (!out_file.exists())
            out_file.createNewFile();
        FileOutputStream fos = new FileOutputStream(out_file);

        MergeObject[] ins = new MergeObject[vk.thread_count];
        int good = vk.thread_count;

        for (int i = 0; i < vk.thread_count; i++) {
            ins[i] = new MergeObject(new File("filtered/" + i + ".txt"));
        }

        while (good > 0) {
            int min_id = Integer.MAX_VALUE, min_id_in = Integer.MAX_VALUE;

            for (int i = 0; i < vk.thread_count; i++) {
                MergeObject o = ins[i];
                if (o == null)
                    continue;

                if (o.ready()) {
                    if (o.last_id < min_id) {
                        min_id = o.last_id;
                        min_id_in = i;
                    }
                } else {
                    //remove this MergeObject
                    good--;
                    ins[i].scanner.close();
                    ins[i] = null;
                }
            }

            if (good > 0) { //check if good ins still exist
                fos.write((ins[min_id_in].last_line + "\n").getBytes());
                ins[min_id_in].readLine();
            }
        }

        fos.close();
        System.out.println("Completed merging " + vk.thread_count + " files");
    }

    class MergeObject {
        File in_file;
        Scanner scanner;
        String last_line;
        JSONObject last_object;
        int last_id;

        MergeObject(File in_file) throws FileNotFoundException {
            this.in_file = in_file;
            this.scanner = new Scanner(in_file);
            if (ready())
                readLine();
        }

        boolean ready() {
            return scanner.hasNext();
        }

        void readLine() {
            last_line = scanner.nextLine();
            last_object = new JSONObject(last_line);
            last_id = last_object.getInt("id");
        }
    }
}
