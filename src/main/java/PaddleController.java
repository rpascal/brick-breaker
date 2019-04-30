import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

enum Decision {
    MOVE_LEFT,
    MOVE_RIGHT,
    DO_NOTHING
}

class PredictionValues {
    Decision decision;
    int yDist;
    int xDist;
    boolean isTravelingUp;
    boolean isBall;
    boolean isGood;

    public int getValue() {
        if(xDist > yDist && !isBall){
            return 0; // Cant make it
        }

        System.out.println("y distance: " + yDist + " x distance: " + xDist);


        int p = 0;

        if(isBall){
            p += 50;
        }

        if(isTravelingUp){
            p -= 45;
        }

        int heightOfWindow = Constants.WINDOW_HEIGHT;

        p += heightOfWindow - yDist;

        // figure out logic here => HIGHER = better
        return p;
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
        paddleXMiddle = paddle.getXMiddle();
        paddleYMiddle = paddle.getYMiddle();

        Decision decision = makeDecision();

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

            Decision d = Decision.DO_NOTHING;
            if(item.isGood()){
                if (paddleRange.contains(item.getXMiddle())) {
                    d = Decision.DO_NOTHING;
                }

                if (item.getXMiddle() < paddleXMiddle) {
                    d =  Decision.MOVE_LEFT;
                }

                if (item.getXMiddle() > paddleXMiddle) {
                    d =  Decision.MOVE_RIGHT;
                }
            }else{
                if (paddleRange.contains(item.getXMiddle())) {
                    d = Decision.MOVE_RIGHT;
                }

                if (item.getXMiddle() < paddleXMiddle) {
                    d =  Decision.MOVE_RIGHT;
                }

                if (item.getXMiddle() > paddleXMiddle) {
                    d =  Decision.MOVE_LEFT;
                }
            }


            PredictionValues prediction = new PredictionValues();
            prediction.isGood = item.isGood();
            prediction.xDist = absDiff(item.getXMiddle(), paddleXMiddle);
            prediction.yDist = absDiff(item.getYMiddle(), paddleYMiddle);
            prediction.isBall = false;
            prediction.isTravelingUp = false;
            prediction.decision = d;

            chooses.add(prediction);

        }

        for (Ball ball : balls) {
            int ballXMiddle = ball.getXMiddle();
            int ballYMiddle = ball.getYMiddle();

            Decision d = Decision.DO_NOTHING;
            if (paddleRange.contains(ballXMiddle)) {
                d = Decision.DO_NOTHING;
            }

            if (ballXMiddle < paddleXMiddle) {
                d =  Decision.MOVE_LEFT;
            }

            if (ballXMiddle > paddleXMiddle) {
                d =  Decision.MOVE_RIGHT;
            }

            PredictionValues prediction = new PredictionValues();
            prediction.isGood = false;
            prediction.xDist = absDiff(ballXMiddle, paddleXMiddle);
            prediction.yDist = absDiff(ballYMiddle, paddleYMiddle);
            prediction.isBall = true;
            prediction.isTravelingUp = ball.getY() < 0;
            prediction.decision = d;

            chooses.add(prediction);
        }


        PredictionValues prediction = chooses.stream().max(Comparator.comparing(PredictionValues::getValue)).get();


        System.out.println("Decision is: " + prediction.decision);

        return  prediction.decision;

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
