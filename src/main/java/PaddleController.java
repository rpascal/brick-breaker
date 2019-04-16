enum Decision {
    MOVE_LEFT,
    MOVE_RIGHT,
    DO_NOTHING
}

public class PaddleController {

    private Ball ball;
    private Paddle paddle;
    private Board board;

    int ballXMiddle;
    int ballYMiddle;
    int paddleXMiddle;
    int paddleYMiddle;

    int acceleration = 1;

    public PaddleController(Ball ball, Paddle paddle, Board board) {
        this.ball = ball;
        this.paddle = paddle;
        this.board = board;
    }

    public void MoveTowardBall() {
        ballXMiddle = ball.getXMiddle();
        ballYMiddle = ball.getYMiddle();
        paddleXMiddle = paddle.getXMiddle();
        paddleYMiddle = paddle.getYMiddle();

        Decision decision = makeDecision();

        String logMessage = "BP: (" + ballXMiddle + ", " + ballYMiddle + ")"
                + " PP: (" + paddleXMiddle + ", " + paddleYMiddle + ")"
                + " Decision: " + (decision)
                + "\n";

        board.logMessages.add(logMessage);

        switch (decision) {
            case MOVE_LEFT:
                paddle.stepLeft(acceleration);
                break;
            case MOVE_RIGHT:
                paddle.stepRight(acceleration);
                break;
            case DO_NOTHING:
                // Do nothing
                break;
        }

    }

    private Decision makeDecision() {
        int smallBuffer = Constants.PADDLE_WIDTH / 10;
        Range paddleRange = new Range(paddle.x + smallBuffer, paddle.x + paddle.getWidth() - smallBuffer);

        for (Item item : board.items){
            if(item.isGood()){

            }else{

            }
            // Do stuff with items
        }

        if (paddleRange.contains(ballXMiddle)) {
            return Decision.DO_NOTHING;
        }

        if (ballXMiddle < paddleXMiddle) {
            return Decision.MOVE_LEFT;
        }

        if (ballXMiddle > paddleXMiddle) {
            return Decision.MOVE_RIGHT;
        }

        return Decision.DO_NOTHING;
    }

}

class Range {
    private int low;
    private int high;

    public Range(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public boolean contains(int number) {
        return (number >= low && number <= high);
    }
}
