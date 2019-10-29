public enum Player {
    USER, COMPUTER;

    public Player opposite() {
        switch (this) {
            case USER:
                return Player.COMPUTER;
            case COMPUTER:
                return Player.USER;
            default:
                throw new IllegalStateException("This should never happen");
        }
    }
}
