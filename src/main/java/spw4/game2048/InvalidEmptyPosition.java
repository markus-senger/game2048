package spw4.game2048;

public final class InvalidEmptyPosition extends RuntimeException {
    InvalidEmptyPosition(String message) {
        super(message);
    }
}
