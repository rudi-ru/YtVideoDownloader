package org.example;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*
Вставляем в программу ссылку на видео с Ютуб и получаем прямую ссылку
на скачивание файла.

 */

public class Main {
    public static void main(String[] args) throws IOException, YoutubeException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Вставьте URL клипа: ");
        String url = sc.nextLine();
        StringBuilder sb = new StringBuilder(url);
        String videoId = sb.substring(url.indexOf("=") + 1);

        YoutubeDownloader downloader = new YoutubeDownloader();
        RequestVideoInfo request = new RequestVideoInfo(videoId)
                .callback(new YoutubeCallback<VideoInfo>() {
                    @Override
                    public void onFinished(VideoInfo videoInfo) {
                        System.out.println("Finished parsing");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error: " + throwable.getMessage());
                    }
                })
                .async();
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data(); // will block thread
        VideoDetails details = video.details();
        System.out.println(details.title());
        System.out.println("Количество просмотров: " + details.viewCount());

        List<VideoWithAudioFormat> videoWithAudioFormats = video.videoWithAudioFormats();
        videoWithAudioFormats.forEach(it -> {
            System.out.println(it.audioQuality() + ", " + it.videoQuality() + " : " + it.url());
        });

    }
}