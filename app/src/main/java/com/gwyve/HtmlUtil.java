package com.gwyve;

/**
 * Created by Administrator on 2016/5/4.
 */
public class HtmlUtil {
    public static  String addUploadHtml(String uri){
        String uploadHtml = "<br><form action='";
        uploadHtml += uri;
        uploadHtml += "' method=\"post\" enctype=\"multipart/form-data\" name=\"form1\" id=\"form1\">\n" +
                "<b>Upload file  </b>\n" +
                "<br><hr>\n" +
                "<input type=\"file\" name=\"file\" id=\"file\">\n" +
                "<input type=\"submit\" name=\"button\" id=\"button\" value=\"Submit\"><hr>\n" +
                "</form>";
        return  uploadHtml;
    }
}
