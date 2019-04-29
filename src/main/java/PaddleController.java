import java.util.ArrayList;
import java.util.List;

enum Decision {
    MOVE_LEFT,
    MOVE_RIGHT,
    DO_NOTHING
}

class PredictionValues {
    Decision decision;
    int yToPaddle;
    int xToPaddle;
    boolean isBall;
    boolean isGood;

    public int getValue() {
        return 0;
    }

}

public class PaddleController {

    private List<Ball> balls;
    private Paddle paddle;
    private Board board;

    //    int ballXMiddle;
//    int ballYMiddle;
    int paddleXMiddle;
    int paddleYMiddle;

    int acceleration = 2;

    public PaddleController(List<Ball> balls, Paddle paddle, Board board) {
        this.balls = balls;
        this.paddle = paddle;
        this.board = board;
    }

    public void MoveTowardBall() {
//        ballXMiddle = ball.getXMiddle();
//        ballYMiddle = ball.getYMiddle();
        paddleXMiddle = paddle.getXMiddle();
        paddleYMiddle = paddle.getYMiddle();

        Decision decision = makeDecision();

//        "BP: (" + ballXMiddle + ", " + ballYMiddle + ")"
//                +

//        String logMessage = " PP: (" + paddleXMiddle + ", " + paddleYMiddle + ")"
//                + " Decision: " + (decision)
//                + "\n";
//
//        board.logMessages.add(logMessage);

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

    private int absDiff(int a, int b) {
        return Math.abs(a - b);
    }

    private Decision makeDecision() {
        int smallBuffer = paddle.getWidth() / 5;
        Range paddleRange = new Range(paddle.x + smallBuffer, paddle.x + paddle.getWidth() - smallBuffer);

        List<PredictionValues> chooses = new ArrayList<>();

        for (Item item : board.items) {
//            if(item.isGood()){
//
//            }else{
//
//            }
            PredictionValues prediction = new PredictionValues();
            prediction.isGood = item.isGood();
            prediction.xToPaddle = absDiff(item.getXMiddle(), paddleXMiddle);
            prediction.yToPaddle = absDiff(item.getYMiddle(), paddleYMiddle);
            prediction.isBall = false;

            chooses.add(prediction);

            // Do stuff with items
        }

        for (Ball ball : balls) {
            int ballXMiddle = ball.getXMiddle();
            int ballYMiddle = ball.getYMiddle();


            PredictionValues prediction = new PredictionValues();
            prediction.isGood = true;
            prediction.xToPaddle = absDiff(ballXMiddle, paddleXMiddle);
            prediction.yToPaddle = absDiff(ballYMiddle, paddleYMiddle);
            prediction.isBall = true;

            chooses.add(prediction);


        }




//
//        if (paddleRange.contains(ballXMiddle)) {
//            return Decision.DO_NOTHING;
//        }
//
//        if (ballXMiddle < paddleXMiddle) {
//            return Decision.MOVE_LEFT;
//        }
//
//        if (ballXMiddle > paddleXMiddle) {
//            return Decision.MOVE_RIGHT;
//        }


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
