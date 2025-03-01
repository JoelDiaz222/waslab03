package fib.asw;

import org.apache.hc.client5.http.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Tasca_6 {
    static final String BASE_URL = "https://mastodont.cat/api/v1/";
    static final String ACCOUNT_ID = "109862447110628983"; // ID de fib_asw
    static final String URI_FOLLOWING = BASE_URL + "accounts/" + ACCOUNT_ID + "/following";
    static final String TOKEN = ResourceBundle.getBundle("token").getString("token");
    static final String STYLE = "<style>\n" +
            "  :root {\n" +
            "    --primary-color: #1da1f2;\n" +
            "    --secondary-color: #17bf63;\n" +
            "    --background-color: #ffffff;\n" +
            "    --card-bg: #fefefe;\n" +
            "    --header-bg: #f8f9fa;\n" +
            "    --text-color: #333333;\n" +
            "    --muted-text: #6c757d;\n" +
            "    --border-radius: 8px;\n" +
            "  }\n" +
            "\n" +
            "  body {\n" +
            "    font-family: \"Helvetica Neue\", Arial, sans-serif;\n" +
            "    margin: 0;\n" +
            "    padding: 20px;\n" +
            "    background-color: var(--background-color);\n" +
            "    color: var(--text-color);\n" +
            "    line-height: 1.6;\n" +
            "  }\n" +
            "\n" +
            "  .header {\n" +
            "    background-color: var(--header-bg);\n" +
            "    padding: 20px;\n" +
            "    margin-bottom: 30px;\n" +
            "    border-radius: var(--border-radius);\n" +
            "    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\n" +
            "  }\n" +
            "\n" +
            "  .header h1 {\n" +
            "    margin: 0 0 10px;\n" +
            "    font-size: 1.8em;\n" +
            "  }\n" +
            "\n" +
            "  .account {\n" +
            "    background-color: var(--card-bg);\n" +
            "    padding: 20px;\n" +
            "    margin: 15px 0;\n" +
            "    border-radius: var(--border-radius);\n" +
            "    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);\n" +
            "  }\n" +
            "\n" +
            "  .account-avatar {\n" +
            "    width: 40px;\n" +
            "    height: 40px;\n" +
            "    border-radius: 50%;\n" +
            "    vertical-align: middle;\n" +
            "    margin-right: 10px;\n" +
            "  }\n" +
            "\n" +
            "  .tut {\n" +
            "    border-left: 4px solid var(--primary-color);\n" +
            "    padding: 15px;\n" +
            "    margin: 15px 0;\n" +
            "    background-color: #ffffff;\n" +
            "    border-radius: var(--border-radius);\n" +
            "    transition: transform 0.2s;\n" +
            "  }\n" +
            "\n" +
            "  .tut:hover {\n" +
            "    transform: translateX(5px);\n" +
            "  }\n" +
            "\n" +
            "  .timestamp {\n" +
            "    color: var(--muted-text);\n" +
            "    font-size: 0.85em;\n" +
            "  }\n" +
            "\n" +
            "  .emoji {\n" +
            "    height: 1.2em;\n" +
            "    vertical-align: middle;\n" +
            "  }\n" +
            "\n" +
            "  .reblog {\n" +
            "    border-left-color: var(--secondary-color);\n" +
            "  }\n" +
            "\n" +
            "  .error {\n" +
            "    color: #b71c1c;\n" +
            "    padding: 15px;\n" +
            "    background-color: #ffebee;\n" +
            "    border-radius: var(--border-radius);\n" +
            "    border: 1px solid #f44336;\n" +
            "  }\n" +
            "\n" +
            "    </style>\n";

    public static void main(String[] args) {
        try {
            //  Obtenir comptes seguits
            String response = Request.get(URI_FOLLOWING)
                    .addHeader("Authorization", "Bearer " + TOKEN)
                    .execute()
                    .returnContent()
                    .asString();

            final JSONArray accounts = new JSONArray(response);

            //  Inici de l'HTML
            final StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n<html lang='ca'>\n<head>\n<meta charset='UTF-8'>\n");
            html.append("<title>Darrers tuts dels comptes seguits</title>\n");

            //  Mateix estil que a l'HTML original
            html.append(STYLE);
            html.append("</head>\n<body>\n");

            final LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                    "EEEE, dd 'de' MMMM 'de' yyyy 'a les' HH:mm:ss",
                    new Locale("ca", "ES")
            );
            final String formattedNow = now.format(formatter);

            html.append("<div class=\"header\">\n" +
                    "<h1>Els cinc tuts més recents del comptes seguits per l'usuari 'fib_asw'</h1>\n" +
                    "<p>" + formattedNow + "</p>\n" +
                    "</div>\n");

            //  Processar cada compte seguit
            for (int i = 0; i < accounts.length(); i++) {
                final JSONObject account = accounts.getJSONObject(i);

                //  Extraiem camps del tut en JSON obtingut
                final String id = account.getString("id");
                final String displayName = account.optString("display_name", account.getString("username"));

                String username = account.getString("acct");
                if (!username.contains("@")) {
                    username += "@mastodont.cat";
                }

                final String avatar = account.getString("avatar");
                final int followersCount = account.getInt("followers_count");

                html.append("<div class='account'>\n");
                html.append("<h2><img src='" + avatar + "' alt='Avatar' class='account-avatar'> " + displayName + " (@" + username + ")</h2>\n");
                html.append("<p>Nombre de seguidors: " + followersCount + "</p>\n");

                //  Obtenir els 5 tuts més recents del compte
                final String uriStatuses = BASE_URL + "accounts/" + id + "/statuses?limit=5";
                response = Request.get(uriStatuses)
                        .addHeader("Authorization", "Bearer " + TOKEN)
                        .execute()
                        .returnContent()
                        .asString();

                final JSONArray tuts = new JSONArray(response);
                html.append("<div class='tuts'>\n");

                for (int j = 0; j < tuts.length(); j++) {
                    final JSONObject tut = tuts.getJSONObject(j);
                    String content = tut.getString("content");
                    final boolean isReblog = tut.has("reblog") && !tut.isNull("reblog");

                    html.append("<div class='tut" + (isReblog ? " reblog" : "") + "'>\n");

                    final String createdAtRaw = tut.getString("created_at");
                    final OffsetDateTime dateTime = OffsetDateTime.parse(createdAtRaw);
                    final String formattedDate = dateTime.atZoneSameInstant(ZoneId.of("Europe/Madrid"))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    html.append("<p class='timestamp'>" + (isReblog ? "🔁 Retut - " : "") + formattedDate + "</p>\n");

                    if (isReblog) {
                        final JSONObject reblog = tut.getJSONObject("reblog");
                        final String originalAuthor = reblog.getJSONObject("account").getString("username");
                        html.append("<p class='timestamp'><i>(Original: @" + originalAuthor + ")</i></p>\n");
                        content = reblog.getString("content");
                    }

                    html.append("<div class='content'>" + content + "</div>\n");
                    html.append("</div>\n");
                }

                html.append("</div>\n</div>\n");
            }

            html.append("</body>\n</html>");

            //  Escriure l'HTML a un fitxer
            try (final FileWriter file = new FileWriter("darrers_tuts.html")) {
                file.write(html.toString());
            }

            //  Imprimim l'HTML per la consola
            System.out.println(html);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
