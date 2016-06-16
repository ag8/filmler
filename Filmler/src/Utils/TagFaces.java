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

import java.util.ArrayList;
import java.util.List;

public class TagFaces extends Application {
	public static int faceNum = 0;
	public static List<Face> faces = new ArrayList<>(); // List of untagged faces
	public static List<Person> people = new ArrayList<>();  // List of people

	FlowPane tagPane;
	FlowPane picPane;

	Image photo;
	static Stage s;

	public static List<Person> run(List<Face> facePool) {
		faces = facePool;
		launch();
		return people;
	}

	@Override
	/**
	 * Initialize the stage and open the first tagging window
	 */
	public void start(Stage primaryStage) {
		s = primaryStage;
		getName(faces.get(0)); // Open the first window explicitly. The following windows are opened after tagging.
	}



	/**
	 * Ask user to tag a face with a name
	 * @param face the face to be tagged
	 */
	private void getName(Face face) {
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

		System.out.println("Face file: " + face.croppedFile.toURI().toString());
		photo = new Image(face.croppedFile.toURI().toString(), 300, 300, false, true);
		ImageView maskView = new ImageView(photo);
		final Circle mask = new Circle(150, 150, 150);
		maskView.setClip(mask);

		nameSubmit.setOnAction(e -> new TagFaces().handleClick(nameField.getText()));

		tagPane.getChildren().addAll(textLabel, nameField, nameSubmit);
		picPane.getChildren().addAll(directions, maskView);

		componentLayout.setBottom(tagPane);
		componentLayout.setTop(picPane);
		Scene appScene = new Scene(componentLayout, 400, 500);

		//Add the Scene to the Stage
		s.setScene(appScene);
		s.setMinWidth(400);
		s.show();
	}

	private void handleClick(String name) {
		if (!name.isEmpty()) {
			// Create a list of people containing the names of everyone who was tagged
			boolean personInList = false;
			for (Person p : people) {
				if (p.getName().equalsIgnoreCase(name)) {
					// Person tagged in the photo was found in list, so add the photo URL to the person.
					p.addFace(faces.get(faceNum));
					personInList = true;
					break;
				}
			}
			if (!personInList) {
				// Person tagged in the photo was not found in the list, so make a new person.
				Person p = new Person(name);
				p.addFace(faces.get(faceNum));
				people.add(p);
			}
			System.out.println("Photo Number: " + faceNum + ", Name: " + name + ", Face: " + faces.get(faceNum) + ".");
		}
		s.close();
		faces.remove(faceNum++);
		if (faceNum < faces.size()) {
			getName(faces.get(faceNum));
		} else {
			System.out.println("All done!");
		}
	}
}