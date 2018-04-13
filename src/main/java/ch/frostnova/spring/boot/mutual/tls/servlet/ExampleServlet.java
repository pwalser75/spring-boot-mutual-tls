package ch.frostnova.spring.boot.mutual.tls.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Example servlet
 */
@WebServlet(
        description = "Info Servlet",
        urlPatterns = {"/info"}
)
public class ExampleServlet extends HttpServlet {

    private final List<String> KNOWN_ROLES = Arrays.asList("Test-User", "Admin");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html");

        PrintWriter writer = new PrintWriter(resp.getWriter());

        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>Example Servlet</title>");
        writer.write("</head>");
        writer.write("<style>");
        writer.write("@import url('https://fonts.googleapis.com/css?family=Lato:300,400,700,900');");
        writer.write("* { font-family:Lato, Calibri, sans-serif; color: #444455; }");
        writer.write("body { padding: 20px; }");
        writer.write("h1, h2, h3 { font-weight: 900; }");
        writer.write("h1 { margin-top:0; }");
        writer.write("h4 { margin-bottom:0; }");
        writer.write("b { font-weight: 900; }");
        writer.write("</style>");
        writer.write("<body>");
        writer.write("<h4>Example Servlet</h4>");

        Principal userPrincipal = req.getUserPrincipal();
        String userName = userPrincipal != null ? userPrincipal.getName() : "unknown visitor";
        writer.write("<h1>Welcome, " + userName + ".</h1>");

        if (userPrincipal != null) {
            writer.write("<h3>Authorization (roles):</h3>");
            writer.write("<ul>");
            KNOWN_ROLES.stream().filter(req::isUserInRole).forEach(role -> writer.write("<li>" + role + "</li>"));
            writer.write("</ul>");

        }
        req.isUserInRole("Test-User");

        writer.write("<h3>Information</h3>");
        writer.write("<ul>");
        writeListEntry(writer, "RequestURL", req.getRequestURL() + "");
        writeListEntry(writer, "RequestURI", req.getRequestURI());
        writeListEntry(writer, "AuthType", req.getAuthType());
        writeListEntry(writer, "Method", req.getMethod());
        writeListEntry(writer, "RequestedSessionId", req.getRequestedSessionId());
        writeListEntry(writer, "ServletPath", req.getServletPath());
        writeListEntry(writer, "Locale", req.getLocale() + "");
        writeListEntry(writer, "RemoteAddr", req.getRemoteAddr());
        writer.write("</ul>");

        writer.write("<h3>Headers</h3>");
        writer.write("<ul>");
        Enumeration<String> headers = req.getHeaderNames();
        if (!headers.hasMoreElements()) {
            writer.write("none");
        }
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            writeListEntry(writer, header, req.getHeader(header));
        }
        writer.write("</ul>");

        writer.write("<h3>Request Parameters</h3>");
        writer.write("<ul>");
        Enumeration<String> parameters = req.getParameterNames();
        if (!parameters.hasMoreElements()) {
            writer.write("none");
        }
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            writeListEntry(writer, parameter, req.getParameter(parameter));
        }
        writer.write("</ul>");

        writer.write("<h3>Cookies</h3>");
        writer.write("<ul>");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                writeListEntry(writer, cookie.getName(), cookie.getValue());
            }
        }
        writer.write("</ul>");

        writer.write("<h3>Web Services</h3>");
        writer.write("<ul>");
        writer.write("<li><a href=\"/api/weather/forecast\" target=\"_new\">Weather forecast</a></li>");
        writer.write("</ul>");

        writer.write("</body>");
        writer.write("</html>");

        writer.flush();
    }

    private void writeListEntry(Writer writer, String label, String text) throws IOException {
        writer.write("<li><b>" + label + "</b>: " + text + "</li>");
    }
}
