public class PaddleController {

    private Ball ball;
    private Paddle paddle;
    private Board board;

    public PaddleController(Ball ball, Paddle paddle, Board board)
    {
        this.ball = ball;
        this.paddle = paddle;
        this.board = board;
    }

    public void MoveTowardBall()
    {
        int ballXMiddle = ball.getXMiddle();
        int ballYMiddle = ball.getYMiddle();
        int paddleXMiddle = paddle.getXMiddle();
        int paddleYMiddle = paddle.getYMiddle();

        boolean goLeft = ballXMiddle < paddleXMiddle;

        String logMessage = "BP: ("+ballXMiddle+", "+ballYMiddle+")"
                + " PP: ("+paddleXMiddle+", "+paddleYMiddle+")"
                + " Decision: " + (goLeft ? "Go Left":"Go Right")
                + "\n";

        board.logMessages.add(logMessage);

        if (goLeft)
        {
            paddle.stepLeft();
        }
        else if (ballXMiddle > paddleXMiddle)
        {
            paddle.stepRight();
        }

        else if (ballXMiddle == paddleXMiddle)
        {

        }

    }






}
