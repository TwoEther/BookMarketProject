package org.project.shop.config;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ScriptUtils {
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=euc-kr");
        response.setCharacterEncoding("euc-kr");
    }

    public static void alert(HttpServletResponse response, String alertText) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+alertText+"');</script>");
        out.flush();
    }

    public static void alertAndMovePage(HttpServletResponse response, String alertText, String next_url) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+alertText+"');location.href='"+next_url+"';</script>");
        out.flush();
    }

    public static void alertAndBackPage(HttpServletResponse response, String alertText) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+alertText+"');history.go(-1);</script>");
        out.flush();
    }
}
