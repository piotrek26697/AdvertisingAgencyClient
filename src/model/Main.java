package model;

import controllers.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{

    public static String screenLoginID = "login";
    public static String screenLoginFile = "/views/screenLogin.fxml";
    public static String screen2ID = "screen2";
    public static String screen2File = "/views/screen2.fxml";


    @Override
    public void start(Stage primaryStage)
    {

        ScreensController screensContainer = new ScreensController();
        screensContainer.loadScreen(Main.screenLoginID, Main.screenLoginFile);
        screensContainer.loadScreen(Main.screen2ID, Main.screen2File);

        screensContainer.setScreen(Main.screenLoginID);

        Group root = new Group();
        root.getChildren().addAll(screensContainer);
        Scene scene = new Scene(root, 1024, 720);

        primaryStage.setTitle("Advertising Agency");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
