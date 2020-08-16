import com.mongodb.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Report extends Application {


    private static MongoClient mongoC= new MongoClient("localhost", 27017);
    private static DB Music=mongoC.getDB("MusicStore");
    private static DBCollection purchaseCollection=Music.getCollection("Purchased");
    private static DBCollection BoughtT=Music.getCollection("BoughtT");

    private static ObservableList<Sell> sold= FXCollections.observableArrayList();
    private static ArrayList<String> time= new ArrayList<>();

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("List of items");

        TableView tableReport;

        TableColumn<ItemVal, String> reportID= new TableColumn<>("Item ID");
        reportID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        TableColumn<ItemVal, String> reportTitle= new TableColumn<>("The title");
        reportTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<ItemVal, String> reportPrice= new TableColumn<>("Price");
        reportPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<ItemVal, String> sellingTime= new TableColumn<>("Price");
        sellingTime.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableReport= new TableView();
        tableReport.setItems(getSold());
        tableReport.getColumns().addAll(reportID,reportTitle,reportPrice,sellingTime);

        VBox display=new VBox();
        display.setPadding(new Insets(15,15,15,15));

        AnchorPane paneR=new AnchorPane();
        paneR.getChildren().add(display);

        Scene scene1 = new Scene(paneR);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    private ObservableList<Sell> getSold(){
        DBCursor finder=BoughtT.find();
        DBCursor findP=purchaseCollection.find();


        while(finder.hasNext()){
            DBObject date=finder.next();
            String dateTime=(String)date.get("purchase time");
            time.add(dateTime);
        }

        int count=0;

        while(findP.hasNext()){
            DBObject item=findP.next();
            String id=(String)item.get("itemID");
            String titleV=(String)item.get("title");
            String priceVal=item.get("price").toString();
            String dateT=time.get(count);
            Sell itemS=new Sell(id, titleV,priceVal,dateT);
            sold.add(itemS);
            count++;
        }
        return sold;
    }



}

