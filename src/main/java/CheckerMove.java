public class CheckerMove {
    int startRow, startCol, endRow, endCol;

    public CheckerMove(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }


    @Override
    public String toString() {
        return "CheckerMove{" +
                "startRow=" + startRow +
                ", startCol=" + startCol +
                ", endRow=" + endRow +
                ", endCol=" + endCol +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckerMove that = (CheckerMove) o;

        if (startRow != that.startRow) return false;
        if (startCol != that.startCol) return false;
        if (endRow != that.endRow) return false;
        return endCol == that.endCol;
    }

    @Override
    public int hashCode() {
        int result = startRow;
        result = 31 * result + startCol;
        result = 31 * result + endRow;
        result = 31 * result + endCol;
        return result;
    }
}