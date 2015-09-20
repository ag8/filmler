package Utils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.gandkco.filmler.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagFaces extends Application {
    final String urlBase = "file:///" + System.getProperty("user.dir") + "\\images\\";
    public static Map<String, String> mp = new HashMap<>();
    public static int photoNum = 0;
    public static List<String> urls = new ArrayList<>();

    FlowPane tagPane;
    FlowPane picPane;

    Image photo;
    ImageView maskView;
    static int mainLineOfficianCountingReplacement3S = 0;
    static Stage s;

    public static void run(String args[]) {
        launch(args);
    }

    @Override

    public void start(Stage primaryStage) {
        int i = 0;

        while (true) {
            ////System.out.println(i);
            File f = new File("C:\\Users\\Irochka\\Documents\\GitHub\\filmler\\Filmler\\images\\image" + i + ".jpg");
            ////System.out.println(f.getAbsolutePath());
            boolean cR = f.exists();
            ////System.out.println(cR);
            if (!cR) {
                break;
            }
            urls.add("image" + i + ".jpg");
            i++;
        }

        ////System.out.println("Length: " + urls.size());

        s = primaryStage;
        domainLineOfficianCountingReplacement3S(mainLineOfficianCountingReplacement3S);
    }

    private void handleClick(String text) {
        mp.put(urls.get(photoNum), text);
        System.out.println("Name: " + text + ".");
        System.out.println(mp);
        photoNum++;
        if (mainLineOfficianCountingReplacement3S < urls.size()) {
            //System.out.println("THE mainLineOfficianCountingReplacement3S IS " + mainLineOfficianCountingReplacement3S + "!!!");
            photo = new Image(urlBase + urls.get(photoNum), 300, 300, false, false);
            maskView = new ImageView(photo);
            //System.out.println("s:" + s);
            s.close();
            new TagFaces().domainLineOfficianCountingReplacement3S(mainLineOfficianCountingReplacement3S);
        } else {
            s.close();
            //System.out.println("All done!");
            Main.imagesAndPeople = mp;
        }
    }

    private void domainLineOfficianCountingReplacement3S(int photoNum) {
        mainLineOfficianCountingReplacement3S++;
        //System.out.println("s3:" + s);
        s = new Stage();
        //System.out.println("s2:" + s);
        s.setTitle("Tag Photos");
        BorderPane componentLayout = new BorderPane();
        componentLayout.setPadding(new Insets(20));

        tagPane = new FlowPane();
        picPane = new FlowPane();
        tagPane.setHgap(20);
        tagPane.setColumnHalignment(HPos.CENTER);
//picPane.setColumnHalignment(HPos.CENTER);

        Text directions = new Text("Who is this?");
        directions.setFont(new Font(24));

        Label textLabel = new Label("Enter name:");
        textLabel.setFont(new Font(16));
        final TextField nameField = new TextField();
        nameField.setMinWidth(180);
        nameField.setPromptText("Derik Kauffman");

        Button nameSubmit = new Button("Tag");
        nameSubmit.setDefaultButton(true);

        photo = new Image(urlBase + urls.get(photoNum), 300, 300, false, false);
        ImageView maskView = new ImageView(photo);
//maskView.setX(-20);
//maskView.setY(-20);
//final Circle mask = new Circle(200, 220, 150);
//maskView.setClip(mask);


        nameSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
// Button click stuff here
                new TagFaces().handleClick(nameField.getText());

            }
        });

        tagPane.getChildren().add(textLabel);
        tagPane.getChildren().add(nameField);
        tagPane.getChildren().add(nameSubmit);
        picPane.getChildren().add(maskView);

        componentLayout.setBottom(tagPane);
        componentLayout.setTop(directions);
        componentLayout.getChildren().add(picPane);
        Scene appScene = new Scene(componentLayout, 400, 500);

//Add the Scene to the Stage
        s.setScene(appScene);
        s.setMinWidth(400);
        s.show();
    }
}