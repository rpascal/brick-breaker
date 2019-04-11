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
        int ball_x = ball.getX();
        int ball_y = ball.getY();

        int paddle_x = paddle.getX();
        int paddle_y = paddle.getY();

        if (ball_x < paddle_x)
        {
            System.out.println("step left");
            paddle.stepLeft();
        }

        else if (ball_x > paddle_x)
        {
            System.out.println("step right");
            paddle.stepRight();
        }

        else if (ball_x == paddle_x)
        {

        }




    }




}
