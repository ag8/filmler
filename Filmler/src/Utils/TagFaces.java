package Utils;

import javafx.application.Application;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagFaces extends Application {
	final static String urlBase = System.getProperty("user.dir") + "/images/";
	public static Map<String, String> mp = new HashMap<>(); // Map from photo URLs to names
	public static int photoNum = 0;
	public static List<String> urls = new ArrayList<>();    // List of photo URLs

	FlowPane tagPane;
	FlowPane picPane;

	Image photo;
	ImageView maskView;
	static Stage s;

	public static Map<String, String> run(String args[]) {
		launch(args);
		return mp;
	}

	@Override
	/**
	 * Create a list of photo URLs
	 */
	public void start(Stage primaryStage) {
		System.out.println(urlBase);
		boolean exists = true;
		for (int i = 0; exists; i++) {
			File f = new File(urlBase + "image" + i + ".jpg");
			System.out.println(f.getAbsolutePath());
			exists = f.exists();
			if (exists) urls.add("image" + i + ".jpg");
		}
		s = primaryStage;
		getName(urls.get(0)); // Open the first window explicitly. The following windows are opened after tagging.
	}



	/**
	 * Get name
	 * @param url the filepath of the photo
	 */
	private void getName(String url) {
		/* Window setup */
		s = new Stage();
		s.setTitle("Tag Photos");
		BorderPane componentLayout = new BorderPane();
		componentLayout.setPadding(new Insets(20));
		tagPane = new FlowPane();
		picPane = new FlowPane();
		tagPane.setHgap(20);
		picPane.setVgap(20);
		picPane.setHgap(40);
		tagPane.setColumnHalignment(HPos.CENTER);
		picPane.setColumnHalignment(HPos.CENTER);

		Text directions = new Text("Who is this?");
		directions.setFont(new Font(24));

		Label textLabel = new Label("Enter name:");
		textLabel.setFont(new Font(16));
		final TextField nameField = new TextField();
		nameField.setMinWidth(180);
		nameField.setPromptText("Derik Kauffman");

		Button nameSubmit = new Button("Tag");
		nameSubmit.setDefaultButton(true);

		photo = new Image("file://" + urlBase + url, 300, 300, false, false);
		ImageView maskView = new ImageView(photo);
		final Circle mask = new Circle(150, 150, 150);
		maskView.setClip(mask);

		nameSubmit.setOnAction(e -> new TagFaces().handleClick(nameField.getText()));

		tagPane.getChildren().add(textLabel);
		tagPane.getChildren().add(nameField);
		tagPane.getChildren().add(nameSubmit);
		picPane.getChildren().add(directions);
		picPane.getChildren().add(maskView);

		componentLayout.setBottom(tagPane);
		componentLayout.setTop(picPane);
		Scene appScene = new Scene(componentLayout, 400, 500);

		//Add the Scene to the Stage
		s.setScene(appScene);
		s.setMinWidth(400);
		s.show();
	}

	private void test() {
		System.out.println("Works!");
	}

	private void handleClick(String name) {
		if (!name.isEmpty()) {
			mp.put(urls.get(photoNum), name);
			System.out.println("Photo Number: " + photoNum + ", Name: " + name + ".");
			System.out.println(mp);
		}
		s.close();
		photoNum++;
		if (photoNum < urls.size()) {
			getName(urls.get(photoNum));
		} else {
			System.out.println("All done!");
		}
	}
}