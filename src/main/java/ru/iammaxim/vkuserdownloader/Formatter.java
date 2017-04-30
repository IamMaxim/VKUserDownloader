package ru.iammaxim.vkuserdownloader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by maxim on 4/30/17 at 8:17 PM.
 */
public class Formatter {
    private static final String entry_template = "<a href=\"https://vk.com/id$ID\">$FIRST_NAME $LAST_NAME</a> $BDATE<br>\n";
    private static String html_template;

    static {
        try {
            File f = new File("html_template.html");
            Scanner scanner = new Scanner(f).useDelimiter("\\A");
            html_template = scanner.next();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public VKUserDownloader vk;

    public Formatter(VKUserDownloader vk) {
        this.vk = vk;
    }

    public void format(String filepath_in) throws IOException {
        File file_in = new File(filepath_in);
        File file_out = new File("formatted.html");
        if (!file_out.exists())
            file_out.createNewFile();

        Scanner scanner = new Scanner(file_in);
        FileOutputStream fos = new FileOutputStream(file_out);

        StringBuilder body_sb = new StringBuilder();

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            JSONObject object = new JSONObject(s);

            body_sb.append(entry_template
                    .replace("$ID", "" + object.getInt("id"))
                    .replace("$FIRST_NAME", object.getString("first_name"))
                    .replace("$LAST_NAME", object.getString("last_name"))
                    .replace("$BDATE", object.getString("bdate"))
            );
        }

        fos.write(html_template.replace("$BODY", body_sb.toString()).getBytes());
    }
}
