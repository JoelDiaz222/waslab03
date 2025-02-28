package fib.asw;

import org.apache.hc.client5.http.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class Tasca_5 {
    public static void main(String[] args) {
        final String URI_LAST_STATUS = "https://mastodont.cat/api/v1/accounts/109862447110628983/statuses?limit=1";
        final String URI_BOOST = "https://mastodont.cat/api/v1/statuses/%s/reblog";
        final String TOKEN = ResourceBundle.getBundle("token").getString("token");

        try {
            //  GET del tut més recent
            String output = Request.get(URI_LAST_STATUS)
                    .addHeader("Authorization","Bearer " + TOKEN)
                    .execute()
                    .returnContent()
                    .asString();

            final JSONArray tuts = new JSONArray(output);
            final JSONObject lastTut = tuts.getJSONObject(0);
            final String content = lastTut.getString("content");

            System.out.println("Contingut del tut més recent:");
            System.out.println(content);

            //  Fem boost al tut
            final String idLastTut = lastTut.getString("id");
            final String URL_BOOST_FORMATTED = String.format(URI_BOOST, idLastTut);

            output = Request.post(URL_BOOST_FORMATTED)
                    .addHeader("Authorization","Bearer " + TOKEN)
                    .execute()
                    .returnContent()
                    .asString();

            System.out.println("Resposta al fer boost d'aquest tut:");
            System.out.println(output);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
