package menus;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;
import models.GameModel;

public class AboutMenu extends Menu {

    private Text mainMenu, informationsText;
    private double textSize, xAsis, yStart;



    public AboutMenu() {
        super(new Point2D(GameModel.getInstance().getSceneWidth() / 2 - GameModel.getInstance().getSceneWidth() / 3,
                GameModel.getInstance().getSceneHeight() / 2 - GameModel.getInstance().getSceneHeight() / 3));

        Group aboutInfo = new Group();

        textSize = ((GameModel.getInstance().getSceneWidth() + GameModel.getInstance().getSceneHeight()) / 60);


        Rectangle meniBackground = new Rectangle(GameModel.getInstance().getSceneWidth() / 1.5,
                GameModel.getInstance().getSceneHeight() / 1.5);
        meniBackground.setFill(Color.color(0, 0, 0, 0.5));
        meniBackground.setStroke(Color.rgb(244, 196, 48));
        meniBackground.setStrokeType(StrokeType.CENTERED);
        meniBackground.setStrokeWidth(10);

        String textInfo = "";
        textInfo += "Deoba Mehura v1.0\n";
        textInfo += "Diplomski rad osnovnih akademskih studija\n";
        textInfo += "\nOpis: \n2D igra sastavljena iz više nivoa sa ciljem razbijanja mehura i\nsakupljanja što većeg broja poena. "
                + "Uz zvučne i vizuelne efekte.\n";
        textInfo += "\nAutor: Zorana Janković\n";
        textInfo += "Mentor: prof. dr. Igor Tartalja\n";
        textInfo += "© Univerzitet u Beogradu - Elektrotehnički fakultet 2020\n";

        informationsText = new Text(30, 50, textInfo);

        informationsText.setFont(new Font(textSize* 0.8));
        informationsText.setFill(Color.YELLOW);
        informationsText.setStrokeWidth(1.3);
        informationsText.setStroke(Color.YELLOW);


        mainMenu = new Text(GameModel.getInstance().getSceneWidth() / 7, GameModel.getInstance().getSceneHeight() / 1.61, "START MENU");
        mainMenu.setStroke(Color.YELLOW);
        mainMenu.setFill(Color.YELLOW);
        mainMenu.setFont(Font.loadFont(Main.class.getResource("/resources/angrybirds-regular.ttf").toExternalForm(), textSize));

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameModel.getInstance().getRoot().getChildren().remove(AboutMenu.this);
                GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getMenus()[GameModel.mappingMenuNames.mainMenu.ordinal()]);
            }
        });

        formatMenuOption(mainMenu);

        aboutInfo.getChildren().addAll(meniBackground, mainMenu, informationsText);
        this.getChildren().addAll(aboutInfo);
    }

    protected void formatMenuOption(Text option) {
        option.setFill(Color.GRAY);
        option.setOnMouseEntered(e -> {
            option.setFill(Color.GRAY);
        });

        option.setOnMouseExited(e -> {
            option.setScaleX(1);
            option.setScaleY(1);
            //option.setFill(Color.YELLOW);
        });

        option.setOnMousePressed(e -> {
            option.setScaleX(0.9);
            option.setScaleY(0.9);
            option.setFill(Color.GRAY);
        });

        option.setOnMouseReleased(e -> {
            option.setScaleX(1);
            option.setScaleY(1);
        });

    }

    public void clickOnOption(){
        Event.fireEvent(mainMenu, new MouseEvent(MouseEvent.MOUSE_CLICKED,
                0, 0, 0, 0, MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true, true, true, true, null));
    }


}
