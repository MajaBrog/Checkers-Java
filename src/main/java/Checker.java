import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class Checker extends StackPane {

    public Checker(CheckerType type) {
        if (type != CheckerType.NONE) {
            ImageView image;
            if (type == CheckerType.BLACK) {
                image = new ImageView(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("BlackChecker.png")).toExternalForm()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                getChildren().addAll(image);
            } else if (type == CheckerType.WHITE) {
                image = new ImageView(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("WhiteChecker.png")).toExternalForm()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                getChildren().addAll(image);
            } else if (type == CheckerType.BLACK_KING) {
                image = new ImageView(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("BlackKing.png")).toExternalForm()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                getChildren().addAll(image);
            } else if (type == CheckerType.WHITE_KING) {
                image = new ImageView(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("WhiteKing.png")).toExternalForm()));
                image.setFitHeight(90);
                image.setFitWidth(90);
                getChildren().addAll(image);
            }
        }
    }
}
//public class Checker extends StackPane {
//
//    public Checker(CheckerType type) {
//        if (type != CheckerType.NONE) {
//            Circle circle = new Circle(45);
//            Text text;
//            if (type == CheckerType.BLACK) {
//                ImageView image=new ImageView(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("BrownChecker.png")).toExternalForm()));
//                image.setFitHeight(90);
//                image.setFitWidth(90);
//                getChildren().addAll(image);
//            } else if (type == CheckerType.WHITE) {
//                circle.setFill(new ImagePattern(new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResource("WhiteChecker.png")).toExternalForm())));
//                getChildren().addAll(circle);
//            } else if (type == CheckerType.BLACK_KING) {
//                circle.setFill(Color.BLACK);
//                text = new Text("K");
//                getChildren().addAll(circle, text);
//            } else if (type == CheckerType.WHITE_KING) {
//                circle.setFill(Color.WHITE);
//                text = new Text("K");
//                getChildren().addAll(circle, text);
//            }
//        }
//    }
//}
