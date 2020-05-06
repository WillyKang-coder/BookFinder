import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BookPane extends Application{
private static final String POST_PARAMS = "userName=Pankaj";
private TextArea textArea = new TextArea();

@Override// 38-60 user interface
public void start(Stage stage) throws Exception {
	GridPane root = new GridPane();
	Scene scene = new Scene(root, 600, 800);
	
	root.setHgap(8); 
	root.setVgap(8);
	root.setPadding(new Insets(15));
	
	ColumnConstraints cons1 = new ColumnConstraints(); 
	cons1.setHgrow(Priority.NEVER);
	root.getColumnConstraints().add(cons1);
	
	ColumnConstraints cons2 = new ColumnConstraints();
	cons2.setHgrow(Priority.ALWAYS);
	
	root.getColumnConstraints().addAll(cons1, cons2);
	
	RowConstraints rcons1 = new RowConstraints();
	rcons1.setVgrow(Priority.NEVER);
	
	RowConstraints rcons2 = new RowConstraints();
	rcons2.setVgrow(Priority.ALWAYS);
	
	root.getRowConstraints().addAll(rcons1, rcons2);
	// textArea.setText("hello");
	Label lbl = new Label("Info About:");
	lbl.setFont(new Font("Arial", 18));
	TextField name = new TextField();
	name.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
	textArea.setStyle("-fx-font-size: 1.5em;");
	Button goBtn = new Button("GO");
	goBtn.setStyle("-fx-font-size: 1.5em");
	goBtn.setStyle("-fx-background-color: #B9FF33; ");
	Button closeBtn = new Button("Close");
	String response = "";
	goBtn.setOnAction(new EventHandler<ActionEvent>() {
	@Override
	public void handle(ActionEvent event) {
	    try {
	sendPost("https://en.wikipedia.org/wiki/" + name.getText().replaceAll("\\s+","_"));
	//textArea.setText(response);
	} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
            }
        });

        GridPane.setHalignment(goBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(name, 1, 0, 3, 1);
        root.add(textArea, 0, 1, 4, 2);
        root.add(goBtn, 2, 3);
        // Scene scene = new Scene(root, 500, 300);

        stage.setTitle("Info Search");
        stage.setScene(scene);
        stage.show();
}
// botton get request, and import the respond back from the web server
public void runTask(String in) {
textArea.appendText(in + " \n");
    }


private void sendPost(String url) throws IOException {
StringBuffer response = new StringBuffer();
System.out.println(url);
URL obj = new URL(url);
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
con.setRequestProperty("User-Agent", "Mozilla/5.0"); // user agent from firefox

// For POST only - START
con.setDoOutput(true);
OutputStream os = con.getOutputStream();
// os.write(POST_PARAMS.getBytes());
os.flush();
os.close();
// For POST only - END
textArea.setWrapText(true);

int responseCode = con.getResponseCode();
System.out.println("GET Response Code :: " + responseCode);

if (responseCode == HttpURLConnection.HTTP_OK) { //success
BufferedReader in = new BufferedReader(new InputStreamReader(
con.getInputStream()));
String inputLine;
while ((inputLine = in.readLine()) != null) {
response.append(inputLine);
// runTask(inputLine);
// textArea.appendText(inputLine);
}
// textArea.setText(response);

// textArea.setText(response.toString().replaceAll("\\<.*?\\>", ""));
in.close();
EditorKit kit = new HTMLEditorKit();
Document doc = kit.createDefaultDocument();
doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
try {
	Reader reader = new StringReader(response.toString());
	kit.read(reader, doc, 0);
	String text = doc.getText(0, doc.getLength());
	textArea.setText(text.replace("'", ""));
   } catch (Exception e) {}
// print result
System.out.println(response.toString());
// return response.toString();
} else {
System.out.println("GET request not worked");
}
}
public static void main(String[] args){
launch(args);
}
}
