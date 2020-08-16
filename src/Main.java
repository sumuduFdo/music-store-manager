import com.mongodb.*;
import javafx.application.Application;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{

    private static Scanner scn=new Scanner(System.in);
    private static Scanner scn1=new Scanner(System.in);

    private static List<String> itemList=new ArrayList<>();
    private static List<String> bought=new ArrayList<>();
    public static List<String> buyDT=new ArrayList<>();

    public static LocalDateTime current;


    static WestminsterMusicStoreManager musicManager=new WestminsterMusicStoreManager();

    private static String itemID;
    private static String title;
    private static String genre;
    private static String releaseDate;
    private static String artist;
    private static BigDecimal price;
    private static int duration;
    private static double diameter;
    private static double speed;
    private static int type;

    private static Mongo mongoC= new Mongo("localhost", 27017);
    private static DB Music=mongoC.getDB("MusicStore");
    private static DBCollection itemsCollection= Music.getCollection("MusicItems");
    private static DBCollection purchaseCollection=Music.getCollection("Purchased");
    private static DBCollection BoughtT=Music.getCollection("Purchased");
    private static Document newItem= new Document();
    private static Document soldT= new Document();
    private static BasicDBObject itemObject;
    private static boolean yearVal;
    private static boolean monthVal;
    private static boolean dayVal;
    private static boolean state;
    private static Date date=new Date();
    private static String release[];

    public static void main(String[] args) {
            int select;
        do {
            System.out.println("1.Add items");
            System.out.println("2.Remove items");
            System.out.println("3.Buy an item");
            System.out.println("4.Print list of items");
            System.out.println("5.Generate report");
            System.out.println("6.Terminate program");
            System.out.print("Enter your selection: ");
            numberValidation();
            select = scn.nextInt();
            System.out.println();

            switch(select){
                case 1:
                    addItem();
                    break;
                case 2:
                    deleteItem();
                    break;
                case 3:
                    buyItem();
                    break;
                case 4:
                    if(bought.size()==0){
                        System.out.println("No items were purchased from the system");
                    }
                    else{
                        musicManager.printList();
                    }
                    break;
                case 5:
                    musicManager.generateReport();
                    break;
                case 6:
                    System.exit(0);

            }
        }while(select!=5);
    }

    private static void addItem(){


        System.out.println("No. of spaces left in the system: "+ (1000-itemList.size()));

        if((1000-itemList.size())>0){
            System.out.println("1.Add CD");
            System.out.println("2.Add Vinyl");
            System.out.print("Select item type: ");
            numberValidation();
            type=scn.nextInt();
            type=valueCheck(type);
            System.out.print("Enter item ID: ");
            itemID=scn1.nextLine();
            System.out.print("Enter Title: ");
            title=scn1.nextLine();
            System.out.print("Enter Genre: ");
            genre=scn1.nextLine();
            while(!state){
                enterDate();
            }
            releaseDate=date.toString();
            System.out.print("Enter Artist: ");
            artist=scn1.nextLine();
            System.out.print("Enter Price: ");
            while(!scn.hasNextInt()){
                System.out.print("Please enter an integer value: ");
                scn.next();
            }
            price=scn.nextBigDecimal();
            if (type==1){
                System.out.print("Enter item duration: ");
                numberValidation();
                duration=scn.nextInt();
                CD cd=new CD(itemID, title, genre, releaseDate, artist, price,duration);
                musicManager.add(cd);
            }
            else if(type==2){
                System.out.print("Enter vinyl speed: ");
                numberValidationD();
                speed=scn.nextDouble();
                System.out.print("Enter vinyl diameter: ");
                numberValidationD();
                diameter=scn.nextDouble();
                Vinyl vn=new Vinyl(itemID, title, genre, releaseDate, artist, price, speed, diameter);
                musicManager.add(vn);
            }
            createDocument();
            itemList.add(itemID);
            musicManager.sort();
            itemsCollection.save(itemObject);
        }
        else{
            System.out.println("No spaces left in the system");
        }

    }

    private static void deleteItem(){

        if(itemList.size()==0){
            System.out.println("Sorry no items found in the program");
        }
        else{
            System.out.println("Enter 5 to display a list of all the items");
            numberValidation();
            int ans=scn.nextInt();
            if(ans==5){
                displayList();
            }
            System.out.print("Enter ID of the item to delete: ");
            String iID=scn.next();
            while (!itemList.contains(iID)){
                System.out.println("Item ID is invalid. Please enter a valid item ID");
                System.out.print("Enter ID of the item to delete: ");
                iID=scn.next();
            }
            String removed=musicManager.remove(iID);
            System.out.println("Type of the removed item: "+removed);
            itemList.remove(iID);
            System.out.println("No. of spaces left in the system: "+ (1000-itemList.size()));
            for(DBObject docFind : itemsCollection.find()){
                String boughtID=(String)docFind.get("itemID");
                if (boughtID.equals(iID)){
                    purchaseCollection.remove(docFind);
                    break;
                }
            }
            createTime();
            BoughtT.save(itemObject);
        }
    }

    private static void buyItem(){

        if(itemList.size()==0){
            System.out.println("Sorry, there are no items stored in the program");
        }
        else{
            System.out.println("Enter 5 to display a list of all the items");
            numberValidation();
            int ans=scn.nextInt();
            if(ans==5){
                displayList();
            }
            System.out.print("Enter ID of the item to buy");
            String buy=scn.next();
            while (!itemList.contains(buy)){
                System.out.println("Item ID is invalid. Please enter a valid item ID");
                buy=scn.next();
            }
            System.out.print("Enter quantity: ");
            numberValidation();
            int qty=scn.nextInt();
            qty=qtyCheck(qty);
            BigDecimal priceVal=musicManager.purchaseItems(buy);
            if(priceVal.intValue()!=0){
                System.out.println("Your total order value is: "+priceVal.intValue()*qty);
            }
            bought.add(buy);
            for(DBObject docFind : itemsCollection.find()){
                String boughtID=(String)docFind.get("itemID");
                if (boughtID.equals(buy)){
                    purchaseCollection.save(docFind);
                    break;
                }
            }
            current=LocalDateTime.now();

        }
    }


    private static void numberValidation(){
        while(!scn.hasNextInt()){
            System.out.print("Please enter an integer value: ");
            scn.next();
        }
    }

    private static void numberValidationD(){
        while(!scn.hasNextDouble()){
            System.out.print("Please enter a decimal value: ");
            scn.next();
        }
    }

    private static int valueCheck(int value){
        while(value<1 || value>2){
            System.out.print("Please enter 1 or 2: ");
            while(!scn.hasNextInt()){
                System.out.print("Please enter an integer value: ");
                scn.next();
            }
            value=scn.nextInt();
        }
        return value;
    }

    private static int qtyCheck(int value){
        while(value<0){
            System.out.print("Please enter a positive value: ");
            while(!scn.hasNextInt()){
                System.out.print("Please enter an integer value: ");
                scn.next();
            }
            value=scn.nextInt();
        }
        return value;
    }

    private static void createDocument() {
        if (type == 1) {
            newItem.put("itemID", itemID);
            newItem.put("title", title);
            newItem.put("genre", genre);
            newItem.put("release date", releaseDate);
            newItem.put("artist", artist);
            newItem.put("price", price);
            newItem.put("duartion", duration);
        } else if (type == 2) {
            newItem.put("itemID", itemID);
            newItem.put("title", title);
            newItem.put("genre", genre);
            newItem.put("release date", releaseDate);
            newItem.put("artist", artist);
            newItem.put("price", price);
            newItem.put("speed", speed);
            newItem.put("diameter", diameter);
        }
        itemObject= new BasicDBObject(newItem);
    }

    private static void createTime(){
        soldT.put("purchase time",current);
        itemObject= new BasicDBObject(soldT);
    }

    public static void displayList(){
        Application.launch(GUI.class);
    }


    public static boolean enterDate(){
        try {
            do {
                System.out.print("Enter Release-date(in the format dd/mm/yy ): ");
                String relD = scn1.nextLine();
                System.out.print("Enter Release-date(in the format dd/mm/yy ): ");
                relD = scn1.nextLine();
                release = relD.split("/");
                yearVal = (Integer.parseInt(release[2]) > 1000);
                monthVal = (Integer.parseInt(release[1]) > 0 && Integer.parseInt(release[1]) < 13);
                dayVal = (Integer.parseInt(release[0]) > 0 && Integer.parseInt(release[0]) < 31);
                if (!(yearVal && monthVal && dayVal)) {
                    System.out.println("Please enter a valid date");
                }
            } while (!(yearVal && monthVal && dayVal));
            date.setYear(Integer.parseInt(release[2]));
            date.setMonth(Integer.parseInt(release[1]));
            date.setDay(Integer.parseInt(release[0]));
            state=true;
        } catch (Exception E) {
            System.out.println("Invalid Date format");
            state=false;
        }
        return state;
    }

}
