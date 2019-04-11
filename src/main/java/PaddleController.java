public class PaddleController {

    private Ball ball;
    private Paddle paddle;

    public PaddleController(Ball ball, Paddle paddle)
    {
        this.ball = ball;
        this.paddle = paddle;
    }

    public void MoveTowardBall()
    {
        int ballXMiddle = ball.getXMiddle();
        int ballYMiddle = ball.getYMiddle();

        int paddleXMiddle = paddle.getXMiddle();
        int paddleYMiddle = paddle.getYMiddle();

        if (ballXMiddle < paddleXMiddle)
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
