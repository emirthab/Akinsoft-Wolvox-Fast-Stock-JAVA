import com.mysql.cj.protocol.Resultset;
import org.json.JSONException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SQLoperations {
    public static SQLquery query = new SQLquery();

    public static Integer getTotal() {return total;}
    public static Integer getBlkodu() {return blkodu;}
    public static String getStokadi() {return stokadi;}
    public static String getBarkodu() {return barkodu;}
    public static String getBirimi() {return birimi;}
    public static String getStokkodu() {return stokkodu;}

    public static Integer total,blkodu = null;
    public static String stokadi,barkodu,birimi,stokkodu = null;

    public static void stokmiktar(String barkod) throws JSONException, SQLException, IOException {
        ResultSet sorgu1 = query.BarkodSorgu(barkod);
        int ztotal = 0;
        while(sorgu1.next()){
            stokkodu = sorgu1.getString("STOKKODU");
            birimi = sorgu1.getString("BIRIMI");
            blkodu = sorgu1.getInt("BLKODU");
            stokadi = sorgu1.getString("STOK_ADI");
            barkodu = sorgu1.getString("BARKODU");
            int tutarturu = sorgu1.getInt("TUTAR_TURU");
            int miktari = sorgu1.getInt("MIKTARI");
            if (tutarturu == 0) {
                ztotal -= miktari;
            }else{
                ztotal += miktari;
            }
        }
        System.out.println("Stok İsmi = "+stokadi+"\nBLkodu = "+blkodu+"\nBarkodu = "+barkodu+"\nElde Kalan = "+ztotal);
        total = ztotal;
    }
    public static void StokHRinsert(String barkod,int Wstokmiktar) throws IOException, SQLException, JSONException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String CurrentTime = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").format(timestamp);
        int changeAMOUNT = Wstokmiktar - total;
        if (changeAMOUNT<0){
            changeAMOUNT = changeAMOUNT*-1;
            query.Update_lastBL(LastBlHR()+1);
            query.HRyazdir(LastBlHR()+1,blkodu,0,CurrentTime,changeAMOUNT,birimi,null,changeAMOUNT);
            System.out.println(changeAMOUNT+" çıkarılıyor...");
            total -= changeAMOUNT;
        }else if(changeAMOUNT>0){
            query.Update_lastBL(LastBlHR()+1);
            query.HRyazdir(LastBlHR()+1,blkodu,1,CurrentTime,changeAMOUNT,birimi,changeAMOUNT,null);
            System.out.println(changeAMOUNT+" ekleniyor...");
            total += changeAMOUNT;
        }

    }
    public static int LastBlHR() throws JSONException, SQLException, IOException {
        ResultSet sorgu1 = query.BLKODsorgu();
        int lastbl = 0;
        while(sorgu1.next()){
            lastbl = sorgu1.getInt(1);
        }
        return lastbl;
    }
    public static void resetVariables(){
        stokkodu = null;
        birimi = null;
        blkodu = null;
        stokadi = null;
        barkodu = null;
        total = null;
    }
}
