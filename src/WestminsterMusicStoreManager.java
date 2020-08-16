
import com.mongodb.*;
import javafx.application.Application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WestminsterMusicStoreManager implements StoreManager {

    private ArrayList<MusicItem> storeItem=new ArrayList<>();
    private static final int MAX_COUNT=1000;
    private List<MusicItem> boughtItem=new ArrayList<>();
    private static String findItem;
    private static BigDecimal findPrice;

    private static Mongo mongoC= new Mongo("localhost", 27017);
    private static DB Music=mongoC.getDB("MusicStore");
    private static DBCollection itemsCollection= Music.getCollection("MusicItems");
    private static DBCollection purchaseCollection=Music.getCollection("Purchased");

    @Override
    public void add(MusicItem item) {
        if(storeItem.size()<MAX_COUNT)
            storeItem.add(item);
        else
            throw new IllegalArgumentException("Music store is full. There are no spaces left for new items"); //custom exception must be used
    }

    @Override
    public void sort() {
        Collections.sort(storeItem);
    }

    @Override
    public boolean delete(MusicItem item) {
        storeItem.remove(item);
        return storeItem.remove(item);
    }

    @Override
    public void printList() {
            System.out.println("List of CDs");
            for(MusicItem item : storeItem){
                if(item instanceof CD)
                    System.out.println((CD)item);
            }
            System.out.println("List of Vinyl");
            for(MusicItem item : storeItem){
                if(item instanceof Vinyl)
                    System.out.println((Vinyl)item);
            }
    }


    @Override
    public void buy(MusicItem item) {
        boughtItem.add(item);
    }

    @Override
    public void generateReport() {
        Application.launch(Report.class);
    }


    public String remove(String value){
        for (MusicItem item1 : storeItem){
            String find=item1.getItemID();
            if (value==find){
                boolean delState=delete(item1);
                if(delState==true){
                    if (item1 instanceof CD){
                        findItem="CD";
                    }
                    else{
                        findItem="Vinyl";
                    }
                    break;
                }
            }

        }
        return findItem;
    }

    public BigDecimal purchaseItems(String ID){
        if(storeItem.size()==0){
            System.out.println("Sorry, there are no items found in the system");
            findPrice=new BigDecimal(0);
        }
        else{
            for(MusicItem item : storeItem){
                String find=item.getItemID();
                if (ID==find){
                    findPrice=item.getPrice();
                    buy(item);
                }
            }
        }
        return findPrice;
    }
}

