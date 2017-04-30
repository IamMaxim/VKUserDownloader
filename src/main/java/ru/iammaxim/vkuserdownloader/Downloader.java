package ru.iammaxim.vkuserdownloader;

import org.json.JSONException;
import ru.iammaxim.Tasker.TaskController;
import ru.iammaxim.Tasker.TaskThread;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by maxim on 4/30/17 at 7:38 AM.
 */
public class Downloader {
    private VKUserDownloader vk;
    public boolean needToStop = false;

    public Downloader(VKUserDownloader vk) {
        this.vk = vk;
    }

    public void download() throws FileNotFoundException {
        AtomicInteger downloaded = new AtomicInteger(vk.startDownloadFrom);
        TaskController controller = new TaskController(vk.thread_count, 2);

        //start logger thread
        new Thread(() -> {
            int last_downloaded = downloaded.get();
            while (true) {
//                System.out.print("\33[2K\r> Downloaded " + downloaded + " users; speed: " + (downloaded.get() - last_downloaded) + " users/sec");
                System.out.println("> Downloaded " + downloaded + " users; speed: " + (downloaded.get() - last_downloaded) + " users/sec");
                last_downloaded = downloaded.get();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (controller.getWorkingThreads() == 0) {
                    System.out.println("\nAll threads stopped. Stopped at " + downloaded.get() + " user");
                    controller.stop();
                    return;
                }
            }
        }).start();

        //init output files
        FileOutputStream fos[] = new FileOutputStream[128];
        for (int i = 0; i < vk.thread_count; i++) {
            fos[i] = new FileOutputStream(i + ".txt");
        }

        //start stopper thread
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.equals("stop")) {
                    System.out.println("\nstopping...");
                    needToStop = true;
                }
            }
        }).start();

        for (int i = vk.startDownloadFrom; i < 500000000; i += 1000) {
            if (needToStop) {
                controller.stop();
                break;
            }

            StringBuilder ids = new StringBuilder();
            for (int j = i; j < i + 1000; j++) {
                ids.append(j);
                if (j < i + 999) ids.append(',');
            }

            controller.addTask(() -> {
                try {
                    Net.writeResponseToOS(fos[((TaskThread) Thread.currentThread()).index], "users.get", false, "fields=photo_max_orig,sex,bdate,last_seen,city", "user_ids=" + ids);
                    fos[((TaskThread) Thread.currentThread()).index].write('\n');
                    downloaded.addAndGet(1000);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
