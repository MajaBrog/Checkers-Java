
import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private CheckerType[][] board;
    private Player whosTurn = Player.USER;

    public Board() {
        board = new CheckerType[8][];
        for (int n = 0; n < 8; n++)
            board[n] = new CheckerType[8];
    }

    public void setFigures() {
        int row;
        int col;
        for (row = 0; row < 8; row++) {
            for (col = 0; col < 8; col++) {
                if (row % 2 != col % 2) {
                    if (row < 3)
                        board[row][col] = CheckerType.BLACK;
                    else if (row > 4)
                        board[row][col] = CheckerType.WHITE;
                    else
                        board[row][col] = CheckerType.NONE;
                } else {
                    board[row][col] = CheckerType.NONE;
                }
            }
        }
    }

    public void setFigure(int row, int col, CheckerType checkerType) {
        board[row][col] = checkerType;
    }

    public CheckerType getFigure(int row, int col) {
        return board[row][col];
    }

    Player getWhosTurn() {
        return whosTurn;
    }

    void setWhosTurn(Player whosTurn) {
        this.whosTurn = whosTurn;
    }

    boolean checkIfRightChecker(Player whosTurn, CheckerType checkerType) {
        if (whosTurn == Player.USER) {
            return checkerType == CheckerType.BLACK || checkerType == CheckerType.BLACK_KING;
        } else {
            return checkerType == CheckerType.WHITE || checkerType == CheckerType.WHITE_KING;
        }
    }

    public void makeMove(int startRow, int startCol, int endRow, int endCol) {
        CheckerType checkerType = getFigure(startRow, startCol);
        if (checkIfRightChecker(whosTurn, checkerType) && startRow != endRow) {
            if (checkIfMoveIsPossible(checkerType, startRow, startCol, endRow, endCol)) {
                if (checkIfMoveIsAJump(checkerType, startRow, startCol, endRow, endCol)) {
                    setFigure(endRow, endCol, checkerType);
                    setFigure(startRow, startCol, CheckerType.NONE);
                    setFigure(startRow + (endRow - startRow) / 2, startCol + (endCol - startCol) / 2, CheckerType.NONE);
                } else {
                    setFigure(endRow, endCol, checkerType);
                    setFigure(startRow, startCol, CheckerType.NONE);
                    whosTurn = whosTurn.opposite();
                }
            }
            if (checkerType == CheckerType.BLACK && endRow == 7) {
                setFigure(endRow, endCol, CheckerType.BLACK_KING);
            } else if (checkerType == CheckerType.WHITE && endRow == 0) {
                setFigure(endRow, endCol, CheckerType.WHITE_KING);
            }
        }
    }

    void computerMove() {

        Random rand = new Random();
        Map<String, List<CheckerMove>> allPossibleMoves;
        CheckerMove computerMove;
        if (getWhosTurn() == Player.COMPUTER) {
            allPossibleMoves = getAllMovableCheckers(getWhosTurn());
            int endRow;
            int endCol;
            int startRow;
            int startCol;
            while ((getAllMovableCheckers(getWhosTurn()).get("Jump moves").size() > 0)) {
                computerMove = getAllMovableCheckers(getWhosTurn()).get("Jump moves").get(rand.nextInt(getAllMovableCheckers(getWhosTurn()).get("Jump moves").size()));
                startRow = computerMove.getStartRow();
                startCol = computerMove.getStartCol();
                endRow = computerMove.getEndRow();
                endCol = computerMove.getEndCol();
                makeMove(startRow, startCol, endRow, endCol);
            }

            if (allPossibleMoves.get("Normal moves").size() > 0) {
                computerMove = allPossibleMoves.get("Normal moves").get(rand.nextInt(allPossibleMoves.get("Normal moves").size()));
                startRow = computerMove.getStartRow();
                startCol = computerMove.getStartCol();
                endRow = computerMove.getEndRow();
                endCol = computerMove.getEndCol();
                makeMove(startRow, startCol, endRow, endCol);
            }
        }
    }


    boolean checkIfMoveIsPossible(CheckerType checkerType, int startRow, int startCol, int endRow, int endCol) {
        boolean movePossible = false;
        CheckerMove userMove = new CheckerMove(startRow, startCol, endRow, endCol);
        Map<String, List<CheckerMove>> possibleMoves = getPossibleMoves(startRow, startCol, checkerType);
        for (String typeOfMove : possibleMoves.keySet()) {
            for (CheckerMove checkerMove : possibleMoves.get(typeOfMove)) {
                if (checkerMove.equals(userMove)) {
                    movePossible = true;
                    break;
                }
            }
        }
        return movePossible;
    }

    private boolean checkIfMoveIsAJump(CheckerType checkerType, int startRow, int startCol, int endRow, int endCol) {
        boolean jumpMove = false;
        CheckerMove userMove = new CheckerMove(startRow, startCol, endRow, endCol);
        Map<String, List<CheckerMove>> possibleMoves = getPossibleMoves(startRow, startCol, checkerType);
        for (CheckerMove checkerMove : possibleMoves.get("Jump moves")) {
            jumpMove = checkerMove.equals(userMove);
        }
        return jumpMove;
    }

    public Map<String, List<CheckerMove>> getPossibleMoves(int startRow, int startCol, CheckerType checkerType) {

        List<CheckerMove> jumpMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                if (getFigure(row, col) == CheckerType.NONE) {
                    int rowDistance = Math.abs(row - startRow);
                    int columnDistance = Math.abs(col - startCol);
                    if (checkerType.in(CheckerType.BLACK_KING, CheckerType.WHITE_KING)) {
                        if (rowDistance == columnDistance) {
                            if (row - startRow > 0 && getFigure(row - 1, col - (col - startCol) / columnDistance).notIn(checkerType.samePlayer(), CheckerType.NONE) ||
                                    (row - startRow < 0 && getFigure(row + 1, col - (col - startCol) / columnDistance).notIn(checkerType.samePlayer(), CheckerType.NONE))) {
                                jumpMoves.add(new CheckerMove(startRow, startCol, row, col));
                            }
                        }
                    } else {
                        if (getFigure(startRow + (row - startRow) / 2, startCol + (col - startCol) / 2) == checkerType.opposite()) {
                            if (rowDistance == 2 && columnDistance == 2) {
                                jumpMoves.add(new CheckerMove(startRow, startCol, row, col));
                            }
                        }
                    }
                }

        List<CheckerMove> normalMoves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (getFigure(row, col) == CheckerType.NONE) {
                    int rowDistance = Math.abs(row - startRow);
                    int columnDistance = Math.abs(col - startCol);
                    if (checkerType == CheckerType.BLACK) {
                        if (row - startRow == 1 && columnDistance == 1) {
                            normalMoves.add(new CheckerMove(startRow, startCol, row, col));
                        }
                    } else if (checkerType == CheckerType.WHITE) {
                        if (row - startRow == -1 && columnDistance == 1) {
                            normalMoves.add(new CheckerMove(startRow, startCol, row, col));
                        }
                    } else if (checkerType.in(CheckerType.BLACK_KING, CheckerType.WHITE_KING)) {
                        if (rowDistance == columnDistance) {
                            if (row - startRow > 0 && getFigure(row - 1, col - (col - startCol) / columnDistance).in(checkerType, CheckerType.NONE) ||
                                    (row - startRow < 0 && getFigure(row + 1, col - (col - startCol) / columnDistance).in(checkerType, CheckerType.NONE))) {
                                normalMoves.add(new CheckerMove(startRow, startCol, row, col));
                            }
                        }
                    }
                }
            }
        }

        Map<String, List<CheckerMove>> possibleMoves = new HashMap<>();
        possibleMoves.put("Normal moves", normalMoves);
        possibleMoves.put("Jump moves", jumpMoves);

        return possibleMoves;
    }

    Map<String, List<CheckerMove>> getAllMovableCheckers(Player whosTurn) {
        Map<String, List<CheckerMove>> allMovableCheckers = new HashMap<>();
        List<CheckerMove> allNormalMoves = new ArrayList<>();
        List<CheckerMove> allJumpMoves = new ArrayList<>();
        allMovableCheckers.put("Normal moves", allNormalMoves);
        allMovableCheckers.put("Jump moves", allJumpMoves);
        Map<String, List<CheckerMove>> possibleMoves;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (checkIfRightChecker(whosTurn, getFigure(row, col))) {
                    possibleMoves = getPossibleMoves(row, col, getFigure(row, col));
                    if (!possibleMoves.isEmpty()) {
                        for (String typeOfMove : possibleMoves.keySet()) {
                            allMovableCheckers.get(typeOfMove).addAll(possibleMoves.get(typeOfMove));
                        }
                    }
                }
            }
        }
        return allMovableCheckers;
    }

    Player checkIfThereIsNoWinner() {
        Player winner = null;
        for (Player player : Player.values()) {
            List<CheckerMove> userMoves = getAllMovableCheckers(player).keySet().stream()
                    .flatMap(key -> getAllMovableCheckers(player).get(key).stream())
                    .collect(Collectors.toList());
            if (userMoves.size() == 0) winner = player.opposite();
        }
        return winner;
    }

    @Override
    public String toString() {
        String s = "--------------------------\n";
        for (int row = 0; row < 8; row++) {
            s += "|";
            for (int col = 0; col < 8; col++) {
                s += getFigureSymbol(getFigure(row, col)) + "|";
            }
            s += "\n";
        }
        s += "--------------------------\n";
        return s;
    }

    private String getFigureSymbol(CheckerType checkerType) {
        if (checkerType == CheckerType.WHITE)
            return "wP";
        else if (checkerType == CheckerType.BLACK)
            return "bP";
        else if (checkerType == CheckerType.WHITE_KING)
            return "wK";
        else if (checkerType == CheckerType.BLACK_KING)
            return "bK";
        else
            return "  ";
    }
}
