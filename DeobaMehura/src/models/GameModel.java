package models;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import menus.*;
import menus.Menu;
import objects.*;
import objects.weapons.Weapon;
import org.json.JSONObject;


import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double SCENE_SCALE_FACTOR = 0.7;
    private float sceneWidth = (float) (screenSize.getWidth() * SCENE_SCALE_FACTOR);
    private float sceneHeight = (float) (screenSize.getHeight() * SCENE_SCALE_FACTOR);
    private float sceneOffsetWidth  = sceneWidth / 5.6f;
    private float sceneOffsetHeight = sceneHeight / 3.78f;
    private float sceneX;
    private float sceneY;
    private float stageWidth;
    private float stageHeight;

    {
        System.out.println("Visina: " + sceneHeight);
        System.out.println("Sirina: " + sceneWidth);
    }

    private static GameModel thisInstance = null;

    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private Color[] colors = new Color[]{Color.RED, Color.CYAN, Color.MAGENTA, Color.GREEN, Color.BLUE , Color.rgb(255,0,127),
                                            Color.rgb(255,110,199), Color.rgb(96,80,220), Color.rgb(64,224,208),
                                                Color.rgb(57,255,20), Color.rgb(0,127,255), Color.rgb(191,0,255)};

    private int indexColor = 0;

    private Player player;
    private Weapon weapon;
    private boolean gameLost;
    private boolean gameWon;
    private Group root;

    private long poeni = 0;

    private int hit = 0;
    private double hitX,hity;
    private CopyOnWriteArrayList<Dollar> dolari = new CopyOnWriteArrayList<>();

    private int comboHit = 0;

    private int numOfLifes;

    private int imamStit = 0;
    private CopyOnWriteArrayList<Shield> stitovi = new CopyOnWriteArrayList<>();

    private int imamSat = 0;
    private CopyOnWriteArrayList<Watch> satovi = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<Bunker> bunkeri = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<SlowedDown> usporeniSnimci = new CopyOnWriteArrayList<>();

    private Weapon bunkerWeapon;

    private String playerName = "";


    public  enum  mappingMenuNames {mainMenu, playerNameMenu, rankListMenu, optionsMenu, commandsMenu, aboutMenu,
                                                pauseMenu, viewConfigMenu, loadConfigMenu};
    private  Menu[] menus = new Menu[mappingMenuNames.values().length];

    private User[]  users = new User[10];

    private int currentLevel = 0;
    private long currentPoints = 0;

    private Point2D[][] ballsPerLevels = {
            {new Point2D(sceneWidth / 2, sceneHeight / 2)},
//            {new Point2D(sceneWidth / 4, sceneHeight / 3), new Point2D(sceneWidth - sceneWidth/4, sceneHeight/3)},
            {new Point2D(sceneWidth / 3, sceneHeight / 3), new Point2D(sceneWidth - sceneWidth/4, sceneHeight/3)},
//            {new Point2D(sceneWidth / 5, sceneHeight / 3), new Point2D(sceneWidth / 2, sceneHeight/3),
//                    new Point2D(sceneWidth - sceneWidth/5, sceneHeight/3)},
//            {new Point2D(sceneWidth / 4, sceneHeight / 3), new Point2D(sceneWidth / 2, sceneHeight/3),
//                    new Point2D(sceneWidth - sceneWidth/4, sceneHeight/3)},
            {new Point2D(sceneWidth / 4, sceneHeight / 3), new Point2D(sceneWidth / 2, sceneHeight/3),
                    new Point2D(sceneWidth - sceneWidth/4, sceneHeight/3)}
    };

    private Integer[][] ballsDirectionsPerLevel = {
            {0},
//            {0, 1},
            {0, 1},
//            {0,-1, 1},
//            {0 ,0 ,0},
            {0 ,0 ,0}
    };

    private Integer[][] ballNumOfDivisionsPerLevel = {
            {0},
//            {1, 1},
            {1, 1},
//            {1 ,0 ,1},
//            {2, 1, 0},
            {2 ,1, 0}
    };

    private Point2D[][] barriersPerLevels = {
            {},
//            {},
            {new Point2D(sceneWidth/2, 0)},
//            {},
//            {new Point2D(sceneWidth/3, 0), new Point2D(2*sceneWidth/3, 0)},
            {new Point2D(sceneWidth/3, 0), new Point2D(2*sceneWidth/3, 0)}
    };

    private CopyOnWriteArrayList<Barrier> barriers = new CopyOnWriteArrayList<>();

    private int lastLevel = 0;

    private  String[] configParsNames = {"Num Of Lifes", "Bubble Size", "Time", "Bubbles Crashing Points", "Bonus Posibillity", "Bubbles SpeedX", "Fade Bubbles Posibillity",
                                                    "Time Bonus", "Bunker Duration", "SlowedDown Duration", "Dolar Bonus", "Player SpeedX",
                                                        "Shield Time", "Blinking Time", "Crashing Smallest Bubble"};

    private  Double[] defaultValuesParameters = {5., 1., 60., 5., 1/6., 1., 0.05, 10., 10., 10., 2., 1., 10., 5., 10.};

    private  Double[] minValuesParameters = {2., 0.75, 45., 3., 0., 0.5, 0., 5., 5., 5., 1.5, 0.5, 5., 2.5, 5.};

    private  Double[] maxValuesParameters = {5., 1.25, 75., 8., 0.5, 1.5, 0.3, 15., 15., 15., 2.5, 1.5, 15., 10., 15.};

    private  JSONObject jsonObject;

    private ArrayList<User> allUsers = new ArrayList<>();

    private long lastPoints = 0;

    private CopyOnWriteArrayList<Heart> srca = new CopyOnWriteArrayList<>();

    public static GameModel getInstance() {
        if (thisInstance == null) {
            thisInstance = new GameModel();
            thisInstance.makeMenus();
        }
        return thisInstance;
    }

    private  void makeMenus() {
       menus[mappingMenuNames.mainMenu.ordinal()] = new MainMenu();
       menus[mappingMenuNames.playerNameMenu.ordinal()] = new InputPlayerNameMenu();
       menus[mappingMenuNames.rankListMenu.ordinal()] = new RankListMenu();
       menus[mappingMenuNames.optionsMenu.ordinal()] = new OptionsMenu();
       menus[mappingMenuNames.commandsMenu.ordinal()] = new CommandsMenu();
       menus[mappingMenuNames.aboutMenu.ordinal()] = new AboutMenu();
       menus[mappingMenuNames.pauseMenu.ordinal()] = new PauseMenu();
       menus[mappingMenuNames.viewConfigMenu.ordinal()] = new ViewConfigMenu();
       menus[mappingMenuNames.loadConfigMenu.ordinal()] = new LoadConfigMenu();
    }

    public float getSceneOffsetWidth() {
        return sceneOffsetWidth;
    }

    public float getSceneOffsetHeight() {
        return sceneOffsetHeight;
    }

    public int getImamSat() {
        return imamSat;
    }

    public void setImamSat(int imamSat) {
        this.imamSat = imamSat;
    }

    public CopyOnWriteArrayList<Watch> getSatovi() {
        return satovi;
    }

    public int getImamStit() {
        return imamStit;
    }

    public void setImamStit(int imamStit) {
        this.imamStit = imamStit;
    }

    public CopyOnWriteArrayList<Shield> getStitovi() {
        return stitovi;
    }

    public int getNumOfLifes() {
        return numOfLifes;
    }

    public void setNumOfLifes(int numOfLifes) {
        this.numOfLifes = numOfLifes;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public double getHitX() {
        return hitX;
    }

    public void setHitX(double hitX) {
        this.hitX = hitX;
    }

    public double getHity() {
        return hity;
    }

    public void setHity(double hity) {
        this.hity = hity;
    }

    public int getComboHit() {
        return comboHit;
    }

    public void setComboHit(int comboHit) {
        this.comboHit = comboHit;
    }



    public CopyOnWriteArrayList<Dollar> getDolari() {
        return dolari;
    }

    public long getPoeni() {
        return poeni;
    }

    public void setPoeni(long poeni) {
        this.poeni = poeni;
    }

    public Color[] getColors() {
        return colors;
    }

    public float getSceneWidth() {
        return sceneWidth;
    }

    public float getSceneHeight() {
        return sceneHeight;
    }

    public double getScreenWidth() {
        return screenSize.getWidth();
    }

    public double getScreenHeight() {
        return screenSize.getHeight();
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public int getIndexColor() {
        return indexColor;
    }

    public void setIndexColor(int indexColor) {
        this.indexColor = indexColor;
    }

    public CopyOnWriteArrayList<Bunker> getBunkeri() {
        return bunkeri;
    }

    public CopyOnWriteArrayList<SlowedDown> getUsporeniSnimci() {
        return usporeniSnimci;
    }

    public Weapon getBunkerWeapon() {
        return bunkerWeapon;
    }

    public void setBunkerWeapon(Weapon bunkerWeapon) {
        this.bunkerWeapon = bunkerWeapon;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public  Menu[] getMenus() {
        return menus;
    }


    public void addUser(User u){
        int i = 0;
        int index = -1;

        for (i = 0 ; i < users.length; i++){
            if (users[i] == null)
                break;
            if(users[i].getName().equals(u.getName())){
                if(users[i].getScores()>u.getScores()){
                    return;
                }else{
                    index = i;
                    break;
                }
            }
        }

        if(index>-1){
            for(i=index; i< (users.length - 1); i++){
                users[i]=users[i+1];
            }

            users[users.length-1]=null;
        }

        for (i = 0 ; i < users.length; i++){
            if (users[i] == null)
                break;
            if (users[i].getScores() < u.getScores())
                break;
        }

        if ( i >= users.length)
            return;

        User[] pom = new User[10];

        int ind = 0;

        for(int j = 0; j < users.length; j++){
            if(j == i){
                pom[j] = u;
            }else{
                pom[j] = users[ind++];
            }
        }

        for (int m = 0 ; m < users.length; m++){
            users[m] = pom[m];
        }
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Point2D[][] getBallsPerLevels() {
        return ballsPerLevels;
    }

    public void setBallsPerLevels(Point2D[][] ballsPerLevels) {
        this.ballsPerLevels = ballsPerLevels;
    }

    public Point2D[][] getBarriersPerLevels() {
        return barriersPerLevels;
    }

    public void setBarriersPerLevels(Point2D[][] barriersPerLevels) {
        this.barriersPerLevels = barriersPerLevels;
    }

    public CopyOnWriteArrayList<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(CopyOnWriteArrayList<Barrier> barriers) {
        this.barriers = barriers;
    }

    public Integer[][] getBallsDirectionsPerLevel() {
        return ballsDirectionsPerLevel;
    }

    public Integer[][] getBallNumOfDivisionsPerLevel() {
        return ballNumOfDivisionsPerLevel;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
        ((MainMenu) GameModel.getInstance().getMenus()[mappingMenuNames.mainMenu.ordinal()]).changeContinueOptionLevel();
    }

    public String getPlayerName() {
        return playerName;
    }

    public  String[] getConfigParsNames() {
        return configParsNames;
    }

    public  Double[] getDefaultValuesParameters() {
        return defaultValuesParameters;
    }

    public  Double[] getMinValuesParameters() {
        return minValuesParameters;
    }

    public  Double[] getMaxValuesParameters() {
        return maxValuesParameters;
    }

    public  JSONObject getJsonObject() {
        return jsonObject;
    }

    public  void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    public long getLastPoints() {
        return lastPoints;
    }

    public void setLastPoints(long lastPoints) {
        this.lastPoints = lastPoints;
    }

    public long getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(long currentPoints) {
        this.currentPoints = currentPoints;
    }

    public CopyOnWriteArrayList<Heart> getSrca() {
        return srca;
    }

    public float getSceneX() {
        return sceneX;
    }

    public void setSceneX(float sceneX) {
        this.sceneX = sceneX;
    }

    public float getSceneY() {
        return sceneY;
    }

    public void setSceneY(float sceneY) {
        this.sceneY = sceneY;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

    public float getStageWidth() {
        return stageWidth;
    }

    public void setStageWidth(float stageWidth) {
        this.stageWidth = stageWidth;
    }

    public float getStageHeight() {
        return stageHeight;
    }

    public void setStageHeight(float stageHeight) {
        this.stageHeight = stageHeight;
    }
}
