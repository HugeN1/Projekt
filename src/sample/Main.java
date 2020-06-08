package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class Main extends Application {

    private static final int iloscPar = 18;
    private static final int liczbaWierszy = 6;
    private Karta wybrany = null;
    private int licznikKlikniec = 2;
    static int pary = 0;

    private Parent tworzenieZawartosci() {
        Pane root = new Pane();
        root.setPrefSize(600, 600);
        char a = 'A';
        List<Karta> karty = new ArrayList<>();
        for (int i = 0; i < iloscPar; i++) {
            karty.add(new Karta(String.valueOf(a)));
            karty.add(new Karta(String.valueOf(a)));
            a++;
        }

        Collections.shuffle(karty);

        for (int i = 0; i < karty.size(); i++) {
            Karta karta = karty.get(i);
            karta.setTranslateX(100 * (i % liczbaWierszy));
            karta.setTranslateY(100 * (i / liczbaWierszy));
            root.getChildren().add(karta);
        }

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Memory PC");
        primaryStage.setScene(new Scene(tworzenieZawartosci()));
        primaryStage.show();
    }

    private class Karta extends StackPane {
        private Text tekst = new Text();

        public Karta(String wartość) {
            Rectangle border = new Rectangle(100, 100);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            tekst.setText(wartość);
            tekst.setFont(Font.font(30));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, tekst);

            setOnMouseClicked(event ->
            {
                if (jestOtwarte() || licznikKlikniec == 0)
                    return;
                licznikKlikniec--;
                if (wybrany == null) {
                    wybrany = this;
                    otwarte(() -> {
                    });
                } else {
                    otwarte(() -> {
                        if (!taSamaWartosc(wybrany)) {
                            wybrany.zamknięte();
                            this.zamknięte();
                        } else {
                            pary++;
                        }

                        wybrany = null;
                        licznikKlikniec = 2;
                    });
                }
            });

            zamknięte();
        }

        public boolean jestOtwarte() {
            return tekst.getOpacity() == 1;
        }

        public void otwarte(Runnable action) {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), tekst);
            ft.setToValue(1);
            ft.setOnFinished(event -> action.run());
            ft.play();
        }

        public void zamknięte() {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.1), tekst);
            ft.setToValue(0);
            ft.play();
        }

        public boolean taSamaWartosc(Karta inna) {

            return tekst.getText().equals(inna.tekst.getText());
        }
    }


    public static void main(String[] args) {
        wygrana();
        launch(args);
    }

    private static void wygrana() {
        if (pary == iloscPar) {
            System.out.println("wygrałeś");
        }
    }
}
