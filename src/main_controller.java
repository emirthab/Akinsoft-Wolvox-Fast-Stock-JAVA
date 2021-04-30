import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class main_controller extends JPanel implements Initializable {
    static SQLconnections con = new SQLconnections();

    @FXML
    public AnchorPane anchor_main_page;
    public TextField input_barkod_main_page;
    public Button button_ok_page_main;
    public AnchorPane anchor_product_page;
    public Text stok_adi;
    public Text stok_kodu;
    public Text CurrentTotalAmount;
    public Text barkodu;
    public Text bl_kodu;
    public TextField input_amount;
    public ImageView back_icon;
    public Text birim;
    public Button button_ok_product_page;
    public TextField ip_adress;
    public TextField file_path;
    public TextField username;
    public TextField password;
    public TextField evrak_no;
    public ImageView icon_back_2;
    public AnchorPane anchor_page_settings;
    public ImageView settings_icon;

    public void initialize(URL arg0, ResourceBundle arg1) {
        button_ok_page_main.setOnMouseClicked((event) -> {
            actionMainPage();
        });
        button_ok_product_page.setOnMouseClicked((event) -> {
            actionProductPage();
        });

        back_icon.setOnMouseClicked((event) -> {
            SQLoperations.resetVariables();
            input_barkod_main_page.setStyle("-fx-text-fill: black;");
            anchor_product_page.setVisible(false);
            anchor_main_page.setVisible(true);
        });
        icon_back_2.setOnMouseClicked((event) -> {
            anchor_page_settings.setVisible(false);
            anchor_main_page.setVisible(true);
            try {
                saveLoginProperties();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });
        settings_icon.setOnMouseClicked((event) -> {
            anchor_page_settings.setVisible(true);
            anchor_main_page.setVisible(false);
            try {
                listStringsLogin();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        input_barkod_main_page.setOnKeyPressed((event) -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ENTER) {
                actionMainPage();
            }
        });
        input_amount.setOnKeyPressed((event) -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ENTER) {
                actionProductPage();
            }
        });
    }

    public void actionMainPage() {
        try {
            SQLoperations.stokmiktar(input_barkod_main_page.getText());
            birim.setText(SQLoperations.getBirimi());
            stok_adi.setText("Stok Adı: " + SQLoperations.getStokadi());
            stok_kodu.setText("Stok Kodu: " + SQLoperations.getStokkodu());
            CurrentTotalAmount.setText("Eldeki Miktar: " + SQLoperations.getTotal());
            barkodu.setText("Barkodu: " + SQLoperations.getBarkodu());
            bl_kodu.setText("Bilgi Kodu: " + SQLoperations.getBlkodu());
            if (SQLoperations.getBarkodu() != null) {
                anchor_product_page.setVisible(true);
                anchor_main_page.setVisible(false);
            } else {
                input_barkod_main_page.setStyle("-fx-text-fill: red;");
                input_barkod_main_page.setText("Bir Hata Oluştu Veya Ürün Bulunamadı");
            }
        } catch (JSONException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public void actionProductPage(){
        if (input_amount.getText() != null) {
            try {
                SQLoperations.StokHRinsert(SQLoperations.getBarkodu(), Integer.parseInt(input_amount.getText()));
                CurrentTotalAmount.setText("Eldeki Miktar : "+String.valueOf(SQLoperations.getTotal()));
            } catch (JSONException | SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void saveLoginProperties() throws JSONException, IOException {
        JSONObject WRTobj = new JSONObject();
        String sb_host = "jdbc:firebirdsql://"+ip_adress.getText()+"/"+file_path.getText()+"?charSet=utf-8";
        String sb_user = username.getText();
        String sb_password = password.getText();
        String sb_evrakno = evrak_no.getText();
        WRTobj.put("akinsoft_host",sb_host);
        WRTobj.put("akinsoft_user",sb_user);
        WRTobj.put("akinsoft_pass",sb_password);
        WRTobj.put("evrak_no",sb_evrakno);
        FileWriter writer = new FileWriter("properties.json");
        writer.write(WRTobj.toString());
        writer.close();
    }
    public void listStringsLogin() throws IOException, JSONException {
        String s_ip = "";
        String s_path = "";
        String s_user = "";
        String s_password = "";
        String s_evrakno = "";
        String prop_ip = con.host_properties().getString("akinsoft_host");
        boolean stop1 = false;
        boolean stop2 = false;
        boolean SecondIsOpen = false;
        for(int i = 19; i < prop_ip.length(); i++) {
            char c = prop_ip.charAt(i);
            if (c == '/') {
                stop1=true;
            }
            if (c == '?'){
                stop2 = true;
            }
            if (!stop1) {
                s_ip += c;
            }else if(SecondIsOpen && !stop2){
                s_path += c;
            }else {
                SecondIsOpen = true;
            }
        }
        s_user = con.host_properties().getString("akinsoft_user");
        s_password = con.host_properties().getString("akinsoft_pass");
        s_evrakno = con.host_properties().getString("evrak_no");
        ip_adress.setText(s_ip);
        file_path.setText(s_path);
        evrak_no.setText(s_evrakno);
        username.setText(s_user);
        password.setText(s_password);
    }
}
//8693245574102