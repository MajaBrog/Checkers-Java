import org.junit.Test;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardTestSuite {
    @Test
    public void testMove1() {
        Board board = new Board();
        board.setFigure(2, 0, CheckerType.BLACK);
        System.out.println(board);
        //board.makeMove(2, 0, 1, 1);
        System.out.println(board);
    }

    @Test
    public void testSetFigures() {
        Board board = new Board();
        board.setFigures();
        System.out.println(board);
    }

    @Test
    public void testGetPossibleMovesForWhiteChecker() {
        Board board = new Board();
        board.setFigure(3, 4, CheckerType.WHITE);
        board.setFigure(2, 3, CheckerType.BLACK);
        board.setFigure(2, 5, CheckerType.BLACK);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col) != CheckerType.BLACK && board.getFigure(row, col) != CheckerType.WHITE){
                    board.setFigure(row, col, CheckerType.NONE);
                }
            }
        }
        System.out.println(board);
        Map<String, List<CheckerMove>> possibleMoves = board.getPossibleMoves(3, 4, CheckerType.WHITE);
        for (String typeOfMove : possibleMoves.keySet()) {
            System.out.println("Move: " + typeOfMove); //Optional for better understanding
            for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                System.out.println(checkerMove.toString());
            }
        }
    }

    @Test
    public void testGetPossibleMovesForBlackChecker() {
        Board board = new Board();
        board.setFigure(2, 1, CheckerType.BLACK);
        board.setFigure(3, 0, CheckerType.WHITE);
        board.setFigure(3, 2, CheckerType.WHITE);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col) != CheckerType.BLACK && board.getFigure(row, col) != CheckerType.WHITE){
                    board.setFigure(row, col, CheckerType.NONE);
                }
            }
        }
        System.out.println(board);
        Map<String, List<CheckerMove>> possibleMoves = board.getPossibleMoves(2, 1, CheckerType.BLACK);
        for (String typeOfMove : possibleMoves.keySet()) {
            System.out.println("Move: " + typeOfMove); //Optional for better understanding
            for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                System.out.println(checkerMove.toString());
            }
        }
    }
    @Test
    public void testGetPossibleMovesForBlackKing() {
        Board board = new Board();
        board.setFigure(3, 3, CheckerType.BLACK_KING);
        board.setFigure(4, 2, CheckerType.WHITE);
        board.setFigure(4, 4, CheckerType.WHITE);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col)==null)
                    board.setFigure(row, col, CheckerType.NONE);
            }
        }
        System.out.println(board);
        Map<String, List<CheckerMove>> possibleMoves = board.getPossibleMoves(3, 3, CheckerType.BLACK_KING);
        for (String typeOfMove : possibleMoves.keySet()) {
            System.out.println("Move: " + typeOfMove); //Optional for better understanding
            for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                System.out.println(checkerMove.toString());
            }
        }
    }
    @Test
    public void testGetPossibleMovesForWhiteKing() {
        Board board = new Board();
        board.setFigure(3, 3, CheckerType.WHITE_KING);
        board.setFigure(4, 2, CheckerType.BLACK);
        board.setFigure(4, 4, CheckerType.BLACK);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col)==null)
                    board.setFigure(row, col, CheckerType.NONE);
            }
        }
        System.out.println(board);
        Map<String, List<CheckerMove>> possibleMoves = board.getPossibleMoves(3, 3, CheckerType.WHITE_KING);
        for (String typeOfMove : possibleMoves.keySet()) {
            System.out.println("Move: " + typeOfMove); //Optional for better understanding
            for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                System.out.println(checkerMove.toString());
            }
        }
    }

    @Test
    public void testCheckIfMoveIsPossible() {
        Board board = new Board();
        board.setFigure(2, 1, CheckerType.BLACK);
        //board.setFigure(3, 0, CheckerType.WHITE);
        board.setFigure(3, 2, CheckerType.WHITE);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col) != CheckerType.BLACK && board.getFigure(row, col) != CheckerType.WHITE){
                    board.setFigure(row, col, CheckerType.NONE);
                }
            }
        }
        boolean movePossible=board.checkIfMoveIsPossible(CheckerType.BLACK,2,1,3,0);
        System.out.println("Move possible: "+movePossible);

    }
    @Test
    public void testMakeMoveV2() {
        Board board = new Board();
        board.setFigure(6, 6, CheckerType.BLACK);
        board.setFigure(3, 0, CheckerType.WHITE);
        board.setFigure(3, 2, CheckerType.WHITE);
        System.out.println(board);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col) != CheckerType.BLACK && board.getFigure(row, col) != CheckerType.WHITE){
                    board.setFigure(row, col, CheckerType.NONE);
                }
            }
        }
        board.makeMove(6,6,7,7);
        System.out.println(board);

    }
    @Test
    public void testMakeMoveKing() {
        Board board = new Board();
        board.setFigure(4, 1, CheckerType.BLACK_KING);
        board.setFigure(3, 0, CheckerType.WHITE);
        board.setFigure(3, 2, CheckerType.WHITE);
        System.out.println(board);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getFigure(row, col) != CheckerType.BLACK && board.getFigure(row, col) != CheckerType.WHITE){
                    board.setFigure(row, col, CheckerType.NONE);
                }
            }
        }
        board.makeMove(4,1,2,3);
        System.out.println(board);

    }

}