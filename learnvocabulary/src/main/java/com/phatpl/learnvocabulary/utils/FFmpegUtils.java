package com.phatpl.learnvocabulary.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FFmpegUtils {
    public static final Path temDir = Paths.get(System.getProperty("java.io.tmpdir"));
    private static final Logger log = LoggerFactory.getLogger(FFmpegUtils.class);

    public static String transcodeToM3U8(MultipartFile video) throws IOException, InterruptedException {
        video.transferTo(temDir.resolve(video.getOriginalFilename()));

        Path resultPath = Paths.get(temDir.toString(), "result");
        Files.createDirectories(resultPath);

        var commands = new ArrayList<String>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(temDir.resolve(video.getOriginalFilename()).toString());
        commands.add("-c:v");
        commands.add("libx264");
        commands.add("-c:a");
        commands.add("copy");
        commands.add("-hls_time");
        commands.add(Constant.TS_SECONDS);
        commands.add("-hls_playlist_type");
        commands.add("vod");
        commands.add("-hls_segment_filename");
        commands.add("%06d.ts");
        commands.add("index.m3u8");

        Process process = new ProcessBuilder()
                .command(commands)
                .directory(resultPath.toFile())
                .start();
        if (process.waitFor() != 0) {
            throw new RuntimeException("Có lỗi trong quá trình chuck video");
        }

        Files.delete(temDir.resolve(video.getOriginalFilename()));
        return Paths.get(temDir.toString(), "/result").toString();
    }

    public static Map<String, InputStream> ChunkVideoFile(MultipartFile video, String prefixPath) throws IOException, InterruptedException {
        String path = FFmpegUtils.transcodeToM3U8(video);

        BufferedReader br = new BufferedReader(new FileReader(path + "/index.m3u8"));
        var lines = new ArrayList<String>();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            lines.add(line);
        }
        br.close();

        for (int i = 0; i < lines.size(); ++i) {
            if (lines.get(i).endsWith(".ts")) {
                lines.set(i, prefixPath + lines.get(i));
            }
        }

        FileWriter fr = new FileWriter(path + "/index.m3u8");
        for (String line : lines) {
            fr.write(line + "\n");
        }
        fr.close();
        var map = new HashMap<String, InputStream>();
        var files = Paths.get(path).toFile().listFiles();
        if (files != null) {
            for (var file : files) {
                log.info(file.getName());
                map.put(file.getName(), (InputStream) FileUtils.openInputStream(file));
                Files.delete(Paths.get(path, file.getName()));
            }
        }
        return map;
    }


    public static boolean screenShots(String source, String file, String time) throws IOException, InterruptedException {
        var commands = new ArrayList<String>();
        commands.add("ffmpeg");
        commands.add("-i");
        commands.add(source);
        commands.add("-ss");
        commands.add(time);
        commands.add("-y");
        commands.add("-q:v");
        commands.add("1");
        commands.add("-frames:v");
        commands.add("1");
        commands.add("-f");
        commands.add("image2");
        commands.add(file);
        Process process = new ProcessBuilder(commands).start();
        return process.waitFor() == 0;
    }
}
