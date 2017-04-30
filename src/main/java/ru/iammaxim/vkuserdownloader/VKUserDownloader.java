package ru.iammaxim.vkuserdownloader;

import java.io.IOException;

/**
 * Created by maxim on 4/30/17 at 7:17 AM.
 */
public class VKUserDownloader {
    //change this to start downloading from specified user ID
    public int startDownloadFrom = 0;
    //amount of working threads
    public int thread_count;

    public void run(Mode mode) throws IOException {
        switch (mode) {
            case DOWNLOAD:
                new Downloader(this).download();
                break;
            case PROCESS:
                new Processor(this).process();
                break;
            case FILTER:
                new Filterer(this).filter();
                break;
            case MERGE:
                new Merger(this).merge();
                break;
            case FILTER_MERGED:
                new Filterer(this).filterMerged();
                break;
            case FILTER_FILTERED:
                new Filterer(this).filterFiltered();
                break;
            case FORMAT:
                new Formatter(this).format("merged_filtered.txt");
                break;
        }
    }

    enum Mode {
        DOWNLOAD,
        PROCESS,
        FILTER,
        MERGE,
        FILTER_MERGED,
        FILTER_FILTERED,
        FORMAT
    }
}
