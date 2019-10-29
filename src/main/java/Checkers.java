import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Checkers extends Application {

    public Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER_RIGHT);
        flowPane.getStylesheets().add(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Style.css")).toExternalForm());
        flowPane.setId("table");
        flowPane.heightProperty().isEqualTo(900);
        flowPane.widthProperty().isEqualTo(1100);

        Scene scene = new Scene(flowPane, 1100, 900);

        GridPane grid = addGridPane();
        VBox vBox = addVBox();

        flowPane.getChildren().addAll(grid, vBox);
        Board board = new Board();
        newGame(grid, board);

        DropShadow borderGlow = new DropShadow(20, Color.WHITE);
        DropShadow borderGlowBlack = new DropShadow(20, Color.BLACK);

        CheckBox checkBox = new CheckBox("Show Possible Moves");
        checkBox.setSelected(true);

        AtomicInteger startRowIndex = new AtomicInteger();
        AtomicInteger startColIndex = new AtomicInteger();

        grid.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Map<String, List<CheckerMove>> possibleMoves = new HashMap<>();
            CheckerType figure = null;
            for (Node node : grid.getChildren()) {

                if (node instanceof Checker) {
                    if (node.getBoundsInParent().contains(e.getX(), e.getY())) {
                        node.setEffect(borderGlow);
                        startRowIndex.set(GridPane.getRowIndex(node));
                        startColIndex.set(GridPane.getColumnIndex(node));
                        figure = board.getFigure(startRowIndex.get(), startColIndex.get());
                        if (board.checkIfRightChecker(board.getWhosTurn(), figure)) {
                            possibleMoves = board.getPossibleMoves(startRowIndex.get(), startColIndex.get(), figure);
                        } else {
                            Popup popup = new Popup();
                            Label label = new Label("It is not your checker!");
                            label.getStylesheets().add(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Style.css")).toExternalForm());
                            label.alignmentProperty().setValue(Pos.CENTER);
                            label.setMinWidth(200);
                            label.setMinHeight(100);
                            popup.getContent().add(label);
                            popup.setAutoHide(true);
                            popup.setHideOnEscape(true);
                            popup.show(primaryStage);
                            popup.anchorLocationProperty().setValue(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
                        }
                    }
                }
            }
            if (checkBox.isSelected() && board.checkIfRightChecker(board.getWhosTurn(), figure)) {
                for (String typeOfMove : possibleMoves.keySet()) {
                    for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                        int row = checkerMove.getEndRow();
                        int column = checkerMove.getEndCol();
                        Circle circle = new Circle(40, Color.TRANSPARENT);
                        circle.setStroke(Color.BLACK);
                        circle.strokeWidthProperty().set(5);
                        circle.setEffect(borderGlowBlack);
                        grid.add(circle, column, row);
                    }
                }
            }
        });

        grid.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            for (Node checker : grid.getChildren()) {

                if (checker instanceof Checker) {
                    if (checker.getBoundsInParent().contains(e.getX(), e.getY())) {
                        int endRowIndex = GridPane.getRowIndex(checker);
                        int endColIndex = GridPane.getColumnIndex(checker);
                        board.makeMove(startRowIndex.get(), startColIndex.get(), endRowIndex, endColIndex);
                    }
                }
            }
            updateBoard(grid, board);
            doComputerMove(grid, board);

        });

        Button newGameButton = new Button("NEW GAME");
        newGameButton.setOnAction(actionEvent -> {
            Alert restartConfirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to restart the game?"
            );
            Button restartButton = (Button) restartConfirmation.getDialogPane().lookupButton(
                    ButtonType.OK
            );
            restartButton.setText("YES");
            restartConfirmation.setHeaderText("Confirm Restart");
            restartConfirmation.initModality(Modality.APPLICATION_MODAL);
            restartConfirmation.initOwner(primaryStage);


            Optional<ButtonType> result = restartConfirmation.showAndWait();
            if (ButtonType.OK.equals(result.get())) {
                newGame(grid, board);
            }
        });

        Button showMovesButton = new Button("Show Movable Checkers");
        showMovesButton.setOnAction(actionEvent -> {
            Map<String, List<CheckerMove>> allPossibleMoves = board.getAllMovableCheckers(board.getWhosTurn());
            for (String typeOfMove : allPossibleMoves.keySet()) {
                for (CheckerMove checkerMove : allPossibleMoves.get(typeOfMove)) {
                    int row = checkerMove.getStartRow();
                    int column = checkerMove.getStartCol();
                    for (Node node : grid.getChildren()) {
                        if (node instanceof Checker) {
                            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                                node.setEffect(borderGlow);
                            }
                        }
                    }
                }
            }
        });

        vBox.getChildren().addAll(newGameButton, showMovesButton, checkBox);

        primaryStage.setTitle("Checkers");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(confirmCloseEventHandler);

    }

    private void doComputerMove(GridPane grid, Board board) {

        board.computerMove();
        updateBoard(grid, board);

        if (board.checkIfThereIsNoWinner() != null) {
            Alert endConfirmation;

            if (board.checkIfThereIsNoWinner() == Player.USER) endConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "You've won! DO you want to play again?");
            else endConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "You've lost the game! DO you want to play again?");
            Button newGameButton = (Button) endConfirmation.getDialogPane().lookupButton(
                    ButtonType.OK);
            Button endButton = (Button) endConfirmation.getDialogPane().lookupButton(
                    ButtonType.CANCEL);

            newGameButton.setText("YES");
            endButton.setText("NO");
            endConfirmation.setHeaderText("Confirm Restart");
            endConfirmation.initModality(Modality.APPLICATION_MODAL);
            endConfirmation.initOwner(primaryStage);

            Optional<ButtonType> result = endConfirmation.showAndWait();

            if (ButtonType.OK.equals(result.get())) newGame(grid, board);
            else Platform.exit();
        }
    }

    private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(primaryStage);


        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) event.consume();
    };

    private void newGame(GridPane grid, Board board) {

        grid.getChildren().clear();

        board.setFigures();
        board.setWhosTurn(Player.USER);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                grid.add(makeChecker(board.getFigure(row, col)), col, row);
            }
        }
    }

    private VBox addVBox() {

        VBox vBox = new VBox();

        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(800);
        vBox.setStyle("-fx-background-color: transparent;");
        vBox.setSpacing(200);

        return vBox;
    }

    private GridPane addGridPane() {

        GridPane grid = new GridPane();

        for (int n = 0; n < 8; n++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(100);
            column.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setValignment(VPos.CENTER);
            row.setPrefHeight(100);
            grid.getRowConstraints().add(row);
        }
        grid.setGridLinesVisible(false);
        grid.getStyleClass().add("board");
        return grid;
    }

    private Checker makeChecker(CheckerType checkerType) {
        return new Checker(checkerType);
    }

    private void updateBoard(GridPane grid, Board board) {
        grid.getChildren().clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                grid.add(makeChecker(board.getFigure(row, col)), col, row);
            }
        }
    }
}
