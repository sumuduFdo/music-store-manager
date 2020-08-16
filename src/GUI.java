import com.mongodb.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GUI extends Application {
    private static MongoClient mongoC= new MongoClient("localhost", 27017);
    private static DB Music=mongoC.getDB("MusicStore");
    private static DBCollection itemsCollection= Music.getCollection("MusicItems");

    private static ObservableList<ItemVal> items= FXCollections.observableArrayList();

    private static TextField searchField;
    private static Button searchBtn;
    private static TextField itemField;

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("List of items");

        TableView table;

        TableColumn<ItemVal, String> ID= new TableColumn<>("Item ID");
        ID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        TableColumn<ItemVal, String> itemT= new TableColumn<>("The title");
        itemT.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<ItemVal, String> itemGen= new TableColumn<>("The Genre");
        itemGen.setCellValueFactory(new PropertyValueFactory<>("genre"));
        TableColumn<ItemVal, String> relDate= new TableColumn<>("Release Date");
        relDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        TableColumn<ItemVal, String> itemArtist= new TableColumn<>("Artist");
        itemArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        TableColumn<ItemVal, String> priceV= new TableColumn<>("Price");
        priceV.setCellValueFactory(new PropertyValueFactory<>("price"));


        table= new TableView();
        table.setItems(getItem());
        table.getColumns().addAll(ID,itemT,itemGen,relDate,itemArtist,priceV);

        HBox top=new HBox();
        HBox bottom=new HBox();
        top.setPadding(new Insets(10,10,5,5));
        Label searchLabel= new Label("Search item : ");
        Label itemTitle= new Label("Search item : ");
        searchField=new TextField();
        itemField=new TextField();
        searchField.setPromptText("Enter title to search");
        searchBtn=new Button("Search");
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                searchItem();
            }
        });

        top.getChildren().add(searchLabel);
        top.getChildren().add(searchField);
        top.getChildren().add(searchBtn);

        bottom.getChildren().add(itemTitle);
        bottom.getChildren().add(itemField);

        VBox display=new VBox();
        display.setPadding(new Insets(15,15,15,15));

        AnchorPane pane1=new AnchorPane();
        pane1.getChildren().add(display);
        display.getChildren().add(top);
        display.getChildren().add(table);
        display.getChildren().add(bottom);

        Scene scene = new Scene(pane1);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ObservableList<ItemVal> getItem(){
        DBCursor find=itemsCollection.find();
        while(find.hasNext()){
            DBObject item=find.next();
            String id=(String)item.get("itemID");
            String titleV=(String)item.get("title");
            String genreV=(String)item.get("genre");
            String releaseV=(String)item.get("releaseDate");
            String artistV=(String)item.get("artist");
            String priceVal=item.get("price").toString();
            ItemVal itemV=new ItemVal(id,titleV,genreV,releaseV,artistV,priceVal);
            items.add(itemV);
        }
        return items;
    }

    private static void searchItem(){
        String title=searchField.getText();
        for(DBObject docFind : itemsCollection.find()){
            String itemID=(String)docFind.get("itemID");
            if (itemID.equals(title)){
                String itemTitle=(String)docFind.get("title");
                itemField.setText(itemTitle);
            }
        }
    }


}
