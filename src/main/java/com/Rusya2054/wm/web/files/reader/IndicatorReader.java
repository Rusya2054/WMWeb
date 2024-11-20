package com.Rusya2054.wm.web.files.reader;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Rusya2054
 */
public final class IndicatorReader {
    public static List<String> readIndicatorsFile(MultipartFile f){
        List<String> strings = new ArrayList<>(500000);
        try (InputStream is = f.getInputStream();){
            Reader r = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(r);
            String line = "";
            while (line != null){
                line = br.readLine();
                strings.add(line);
            }
        } catch (IOException exception){
        }
        return strings;
    }

    public static List<String> readIndicatorsFile(MultipartFile f, int nRows){
        List<String> strings = new ArrayList<>(nRows);
        try(InputStream is = f.getInputStream();){
            Reader r = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(r);
            String line = br.readLine();
            strings.add(line);
            int counter = 0;
            while (line != null){
                if (counter >= nRows){
                    break;
                }
                line = br.readLine();
                strings.add(br.readLine());
                counter ++;
            }
        } catch (IOException exception){
        }
        return strings;
    }

    public static List<String> readIndicatorsFile(byte[] f){
        String[] lines = {};
        try {
            String string = new String(f, "UTF-8");
            lines = string.split("\n");

        } catch (IOException ioException){

        }
        return new ArrayList<>(Arrays.asList(lines));
    }
}