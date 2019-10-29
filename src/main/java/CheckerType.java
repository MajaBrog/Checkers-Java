import java.util.Arrays;

public enum CheckerType {
    BLACK, WHITE, BLACK_KING, WHITE_KING, NONE;

    public boolean in(CheckerType... checkerTypes) {
        return Arrays.stream(checkerTypes).anyMatch(checkerType -> checkerType == this);
    }

    public boolean notIn(CheckerType... checkerTypes) {
        return Arrays.stream(checkerTypes).noneMatch(checkerType -> checkerType == this);
    }

    public CheckerType opposite() {
        switch (this) {
            case BLACK:
                return CheckerType.WHITE;
            case WHITE:
                return CheckerType.BLACK;
        }
        return null;
    }

    public CheckerType samePlayer() {
        switch (this) {
            case BLACK:
                return CheckerType.BLACK_KING;
            case WHITE:
                return CheckerType.WHITE_KING;
            case BLACK_KING:
                return CheckerType.BLACK;
            case WHITE_KING:
                return CheckerType.WHITE;
        }
        return null;
    }
}
