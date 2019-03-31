package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Main;

import java.io.IOException;
import java.util.HashMap;

public class ScreensController extends AnchorPane
{
    final private int FADE_TIME = 300;

    private HashMap<String, Node> screens = new HashMap<>();

    public ScreensController()
    {
        super();
    }

    public void addScreen(String name, Node screen)
    {
        screens.put(name, screen);
    }

    public Node getScreen(String name)
    {
        return screens.get(name);
    }

    public boolean loadScreen(String name, String resources)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resources));
            Parent loadScreen = loader.load();

            ControlledScreen controlledScreen = loader.getController();
            controlledScreen.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean setScreen(String name)
    {
        if (screens != null)
        {
            DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty())  //checking if there's screen displayed
            {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(FADE_TIME), event -> {

                            getChildren().remove(0);
                            getChildren().add(0, screens.get(name));
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(FADE_TIME), new KeyValue(opacity, 1.0))
                            );
                            fadeIn.play();

                        }, new KeyValue(opacity, 0.0))
                );
                fade.play();
            } else
            {
                setOpacity(0.0);
                getChildren().add(0, screens.get(name));
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(FADE_TIME), new KeyValue(opacity, 1.1))
                );
                fadeIn.play();
            }
            return true;
        } else
        {
            System.err.println("No screens to set");
            return false;
        }
    }

    public boolean unloadScreen(String name)
    {
        if (screens.remove(name) == null)
        {
            System.err.println("Screen doesn't exist");
            return false;
        } else
            return true;
    }
}
