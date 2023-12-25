package org.stick;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;


public class Controller {

    @FXML
    Rectangle startRect, secondRect;
    @FXML
     ImageView mario;
    @FXML
     Label score, score1, highscore;
    @FXML
    Rectangle stick;
    @FXML
     AnchorPane rootPane;

    int highScore = 0;
    Rectangle currentPillar = null;
    Rectangle rect1 = null, rect2 = null, oldRect = null, rect3 = null, oldrect2 = null;

    double xpos = 1;
    double oldxpos;
    double pilOldxpos;
    double pilXpos = 218;
    double pilwidth = 80;

    Integer scoreCount=0;
    private boolean isMousePressed = false;
    private AnimationTimer lengthenTimer;
    private final double lengtheningSpeed = 2.0;
    private Stage stage;
    boolean gameEnd = false;
    long pressTime;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRootPane(AnchorPane rootPane) {
        this.rootPane = rootPane;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML
    public void replay(ActionEvent actionEvent) throws IOException {
        Stage stage2 = Main.window;
        Parent game = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/stick.fxml"))));
        Scene scene = new Scene(game);
        if (stage2 != null) {
            stage2.setScene(scene);
        } else {
            System.out.println("Empty stage");
        }
    }

    @FXML
    public void quit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void updateScore(ActionEvent actionEvent) {
        score.setText(scoreCount.toString());
    }

    public void updateScore2(ActionEvent actionEvent) {
        score1.setText(scoreCount.toString());
    }

    public void updateHighScore(ActionEvent actionEvent) {
        highScore();
        highscore.setText(String.valueOf(highScore));
    }

    public void start(ActionEvent actionEvent) throws IOException {
        Parent game = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/stick.fxml"))));
        Scene scene = new Scene(game);
        if (stage != null) {
            stage.setScene(scene);
        } else {
            System.out.println("Empty stage");
        }
    }

    public void flipMario(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F) {
            mario.setScaleY(mario.getScaleY() * -1);
            if (mario.getScaleY() == -1) {
                mario.setLayoutY(310);
            } else {
                mario.setLayoutY(250);
            }
        }
    }
    public void pause(ActionEvent actionEvent) throws IOException {
        Stage stage2 = Main.window;
        Parent game = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/pause.fxml"))));
        Scene scene = new Scene(game);
        if (stage2 != null) {
            stage2.setScene(scene);
        } else {
            System.out.println("Empty stage");
        }
    }

    public void resume(ActionEvent actionEvent) throws IOException {
        Stage stage2 = Main.window;
        Parent game = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/stick.fxml"))));
        Scene scene = new Scene(game);
        if (stage2 != null) {
            stage2.setScene(scene);
            updateScore(null);
        } else {
            System.out.println("Empty stage");
        }
    }

    public void endgame() throws IOException {
        Stage stage1 = (Stage) rootPane.getScene().getWindow();
        Parent game1 = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/scene2.fxml"))));
        Scene scene1 = new Scene(game1);
        if (stage1 != null) {
            stage1.setScene(scene1);
            updateScore2(null);
            updateHighScore(null);
        } else {
            System.out.println("Empty stage");
        }
    }

    public void checkStickLanding() throws IOException, InterruptedException {
        if (stick.getLayoutX()+stick.getHeight() > pilXpos && stick.getLayoutX()+stick.getHeight() < pilXpos+pilwidth) {
            animateCharacter();
            scoreCount++;
            updateScore(null);
        } else {
            gameEnd = true;
            animatefall();
            System.out.println("Game Over");
        }
    }

    public void game(javafx.scene.input.MouseEvent event) {

        pressTime = System.currentTimeMillis();
        isMousePressed = true;
        lengthenTimer.start();
    }

    public void initialize() {

        lengthenTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                stick.getTransforms().clear();
                if (isMousePressed) {
                    stick.setLayoutY(stick.getLayoutY() - lengtheningSpeed);
                    stick.setHeight(stick.getHeight() + lengtheningSpeed);
                }
            }
        };
    }

    public void stopLengthening(MouseEvent mouseEvent) {
        if (isMousePressed) {
            isMousePressed = false;
            lengthenTimer.stop();
            rotateStick(stick);
        }
    }

    private void rotateStick(Rectangle stick) {
        Rotate rotate = new Rotate();
        rotate.setPivotX(stick.getX());
        rotate.setPivotY(stick.getY() + stick.getHeight());
        stick.getTransforms().add(rotate);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (rotate.getAngle() < 90) {
                    rotate.setAngle(rotate.getAngle() + 1);
                } else {
                    stop();
                    try {
                        checkStickLanding();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();
    }

    public void animatefall(){
        ImageView imageView = mario;
        imageView.setTranslateX(xpos);
        imageView.setTranslateY(0);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(2), imageView);
        translateTransition1.setToX(stick.getHeight());
        translateTransition1.setToY(0);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(2), imageView);
        translateTransition2.setToX(stick.getHeight());
        translateTransition2.setToY(500);

        translateTransition1.setOnFinished(event -> {
            translateTransition2.play();
        });

        translateTransition2.setOnFinished(event -> {
            stick.setHeight(0);
            try {
                endgame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        translateTransition1.play();
    }

    public void animateCharacter() {
        ImageView imageView = mario;
        oldxpos = xpos;
        imageView.setTranslateX(xpos);
        imageView.setTranslateY(0);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), imageView);
        if(xpos == 1){
            xpos = 220;
        }
        else{
            xpos+=pilXpos-oldxpos;
        }
        translateTransition.setToX(stick.getHeight());
        System.out.println("stick.getLayoutX() = "+stick.getLayoutX()+pilwidth);
        translateTransition.setToY(0);
        translateTransition.setCycleCount(1);
        translateTransition.setOnFinished(event -> {
            System.out.println("stick_old -- > "+ stick.getLayoutX());
            stick.relocate(stick.getLayoutX()+stick.getHeight()-1,305);
            System.out.println("stick -- > "+ stick.getLayoutX());
            System.out.println(pilXpos);
            System.out.println(pilwidth);
            stick.setHeight(0);
            stick.relocate(pilXpos+pilwidth-3,306);
            pilOldxpos = pilXpos;
            Rectangle r1 = generatePillar();
            changeScene(r1);
            initialize();
        });
        translateTransition.play();
    }

    public Rectangle generatePillar() {
        Random random = new Random();

        double pillarWidth = random.nextDouble() * (100-50) + 60;
        double pillarHeight = 275;

        Rectangle pillar = new Rectangle(pillarWidth, pillarHeight);
        pillar.setFill(Color.BLACK);

        double gap =  pillarWidth;
        double newX = rootPane.getChildren().isEmpty()
                ? 0
                : rootPane.getChildren().get(rootPane.getChildren().size() - 1).getLayoutX() + 2*gap;

        pillar.setLayoutX(newX);
        pillar.setLayoutY(305);
        pilXpos = pillar.getLayoutX();
        pilwidth = pillar.getWidth();
        rootPane.getChildren().add(pillar);
        return pillar;
    }

    public void highScore(){
        if (scoreCount > highScore){
            highScore = scoreCount;
        }
    }

    public void changeScene(Rectangle rect) {
        double xx = 0;
        rect1 = rect;

        if(scoreCount == 1){
            xx = -218;
            oldRect = rect1;
            rect2 = secondRect;
            rect3 = startRect;
        } else if (scoreCount == 2){
            xx = -rootPane.getChildren().get(rootPane.getChildren().size() - 1).getLayoutX() + 2*pilwidth;
            oldrect2 = oldRect;
            rect2 = oldRect;
            rect3 = secondRect;
            oldRect = rect1;
        } else {
            xx = -rootPane.getChildren().get(rootPane.getChildren().size() - 1).getLayoutX() + 2*pilwidth;
            rect2 = oldRect;
            rect3 = oldrect2;
            oldrect2 = oldRect;
            oldRect = rect1;
        }
        ImageView imageView = mario;
        TranslateTransition transitionRect = createTranslateTransition(rect1, xx, 0);
        TranslateTransition transitionStartRect = createTranslateTransition(rect3, xx-20, 0);
        TranslateTransition transitionSecondRect = createTranslateTransition(rect2, xx, 0);
        TranslateTransition transitionStick = createTranslateTransition(stick, xx, 0);
        TranslateTransition transitionMario = createTranslateTransition(imageView, 0, 0);
        ParallelTransition parallelTransition = new ParallelTransition(
                transitionRect,
                transitionStartRect,
                transitionSecondRect,
                transitionMario,
                transitionStick
        );

        double finalXx = xx;
        parallelTransition.setOnFinished(event -> {
            xpos += finalXx;
            System.out.println("pillar's x pos is "+pilOldxpos);
            pilOldxpos+= finalXx;
            System.out.println("pillar's x new pos is "+pilOldxpos);
            oldxpos += finalXx;
            System.out.println("stick_old -- > "+ stick.getLayoutX());
            System.out.println("stick -- > "+ stick.getLayoutX());
            System.out.println("Stick height: "+stick.getHeight());
            System.out.println(pilXpos);
            System.out.println(pilwidth);
            stick.setHeight(0);
        });
        parallelTransition.play();
    }

    private TranslateTransition createTranslateTransition(Rectangle rect, double toX, double toY) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), rect);
        transition.setToX(toX);
        transition.setToY(toY);
        return transition;
    }

    private TranslateTransition createTranslateTransition(ImageView img, double toX, double toY) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), img);
        transition.setToX(toX);
        transition.setToY(toY);
        return transition;
    }
}
