package com.gwyve;

/**
 * Created by Administrator on 2016/5/4.
 */
public class HtmlUtil {
    public static  String addUploadHtml(String uri){
        String uploadHtml = "<form action=\"";
        uploadHtml += uri;
        uploadHtml += "\" enctype=\"multipart/form-data\" method=\"post\">\n" +
                "      File: <input type=\"file\" name=\"datafile\" size=\"40\"><br>\n" +
                "    <input type=\"submit\">\n" +
                "   </form>";
        return  uploadHtml;
    }
}
