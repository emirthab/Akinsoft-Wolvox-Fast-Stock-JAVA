import org.json.JSONException;
import sun.lwawt.macosx.CSystemTray;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLquery {
    public static SQLconnections con = new SQLconnections();

    public static ResultSet BarkodSorgu(String barkod) throws IOException, SQLException, JSONException {
        String selected = "STOKHR.BLSTKODU,STOKHR.TUTAR_TURU,STOKHR.MIKTARI,STOK.BIRIMI,STOK.BARKODU,STOK.STOK_ADI,STOK.BLKODU,STOK.STOKKODU";
        String froms = "STOKHR,STOK";
        String wheres = "STOK.BLKODU = STOKHR.BLSTKODU AND STOK.BARKODU = '" + barkod + "'";
        String Query = "SELECT " + selected + " FROM " + froms + " WHERE " + wheres;
        Statement stat = con.Connector_Akinsoft().createStatement();
        ResultSet res = stat.executeQuery(Query);
        return res;
    }
    public static ResultSet BLKODsorgu() throws IOException, SQLException, JSONException {
        String selected = "MAX (BLKODU)";
        String froms = "STOKHR";
        String Query = "SELECT " + selected + " FROM " + froms;
        Statement stat = con.Connector_Akinsoft().createStatement();
        ResultSet res = stat.executeQuery(Query);
        return res;
    }
    public void HRyazdir(int blkodu, int blstkodu, int tutarturu, String tarihi, int miktar, String birim, Integer girenMik, Integer cikanMik) throws IOException, SQLException, JSONException {
        String tablename ="STOKHR";
        String values =
                blkodu+","
                        +blstkodu+","
                        +tutarturu+",'"
                        +tarihi+"','"
                        +con.host_properties().getString("evrak_no")+"'," //evrak no
                        +null+"," //aciklama
                        +miktar+","
                        +null+"," //kpb_tutarı
                        +null+"," //kpb_fiyatı
                        +null+"," //doviz tutarı
                        +null+"," //doviz fiyati
                        +null+"," //ozel kodu
                        +null+"," //carikodu
                        +0+"," //dovizkullan
                        +1+"," //kpbdvz
                        +null+"," //doviz_birimi
                        +0+"," //doviz_hes_isle
                        +null+"," //doviz_alis
                        +null+"," //doviz_satic
                        +null+"," //depo_adi
                        +miktar+"," //miktar2
                        +girenMik+"," //kpb_gmik
                        +cikanMik+"," //kpb_cmik
                        +null+"," //kpb_gtut
                        +null+"," //kpb_ctut
                        +null+"," //dvz_gmik
                        +null+"," //dvz_cmik
                        +null+"," //dvz_gtut
                        +null+"," //dvz_ctut
                        +null+"," //entegrasyon
                        +0+"," //devir
                        +0+"," //iade
                        +0+"," //silindi
                        +"'SYSDBA'"+"," //kaydeden
                        +null+"," //degistiren
                        +null+"," //degistirilme_tarihi
                        +null+"," //bldepotrs_kodu
                        +null+"," //ekbilgi1
                        +null+"," //ekbilgi2
                        +null+"," //ekbilgi3
                        +null+"," //ekbilgi4
                        +1+",'" //maliyete_ekle
                        +birim+"'," //birim_2
                        +null+"," //ana_stokkkodu
                        +null+"," //sube_kodu
                        +null+"," //gm_entegrasyon
                        +null+"," //transfer_irsaliyesi
                        +null+"," //ek_maliyet_kbp
                        +null+"," //ek_maliyet_dvz
                        +null+"," //offline_durum
                        +null+"," //blfhkodu
                        +null+",'" //source_app
                        +tarihi+"'," //kayit tarihi
                        +null; //blsayimkodu
        String Query = "INSERT INTO " + tablename + " VALUES (" + values+")";
        Statement stat = con.Connector_Akinsoft().createStatement();
        stat.executeUpdate(Query);
        System.out.println(Query);
    }
    public void Update_lastBL(int blkodu) throws IOException, SQLException, JSONException {
        Statement stat = con.Connector_Akinsoft().createStatement();
        String Query = "UPDATE GEN_IDT SET GEN_VALUE = "+blkodu+" WHERE GEN_NAME = 'STOKHR_GEN'";
        stat.executeUpdate(Query);
    }
}
