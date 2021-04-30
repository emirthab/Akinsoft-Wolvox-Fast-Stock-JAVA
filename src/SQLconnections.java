import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLconnections {

    public JSONObject host_properties() throws IOException, JSONException {
        String line;
        BufferedReader Br = new BufferedReader(new FileReader("properties.json"));
        StringBuilder Sb = new StringBuilder();
        while ((line = Br.readLine()) != null){
            Sb.append(line + '\n');
        }
        JSONObject obj = new JSONObject(Sb.toString());
        return obj;
    }
    public Connection Connector_Akinsoft() throws SQLException, JSONException, IOException {
        String host = host_properties().getString("akinsoft_host");
        String user = host_properties().getString("akinsoft_user");;
        String pass = host_properties().getString("akinsoft_pass");;
        Connection con = DriverManager.getConnection(host,user,pass);
        return con;
    }

}
