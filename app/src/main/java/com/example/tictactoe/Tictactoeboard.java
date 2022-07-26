package com.example.tictactoe;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Tictactoeboard extends View {

    private final int boardcolor;
    public final int Xcolor;
    private final GameLogic game;
    public final int Ocolor;
    private final int winnercolor;
    private final Paint paint = new Paint();
    private boolean winningLine = false;
    private int cellSize = getWidth() / 3;


    public Tictactoeboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Tictactoeboard, 0, 0);
        game = new GameLogic();


        try {
            boardcolor = a.getInteger(R.styleable.Tictactoeboard_boardcolor, 0);
            Xcolor = a.getInteger(R.styleable.Tictactoeboard_Xcolor, 0);
            Ocolor = a.getInteger(R.styleable.Tictactoeboard_Ocolor, 0);
            winnercolor = a.getInteger(R.styleable.Tictactoeboard_winningcolor, 0);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int dimensions = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(dimensions, dimensions);
        cellSize = dimensions / 3;

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN){
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            if (!winningLine){
                if (game.updateGameBoard(row, col)){
                    invalidate();

                    if (game.winnerCheck()){
                        winningLine = true;
                        invalidate();
                    }

                    //updates the actual game code
                    if (game.getPlayer() % 2 == 0){
                        game.setPlayer(game.getPlayer() - 1);
                    }
                    else {
                        game.setPlayer(game.getPlayer() + 1);
                    }


                }
            }

            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawMarkers(canvas);


    }

    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardcolor);
        paint.setStrokeWidth(16);


        for (int c = 1; c < 3; c++) {
            canvas.drawLine(cellSize * c, 0, cellSize * c, canvas.getHeight(), paint);
        }

        for (int r = 1; r < 3; r++) {
            canvas.drawLine(0, cellSize * r, canvas.getWidth(), cellSize * r, paint);
        }
    }
    private void drawMarkers(Canvas canvas){
        //will draw an X or O onto the board
        for (int r=0; r<3; r++){
            for (int c=0; c<3; c++){
                if (game.getGameBoard()[r][c] != 0){
                    if (game.getGameBoard()[r][c] == 1){
                        drawX(canvas, r, c);
                    }
                    else {
                        drawO(canvas, r, c);
                    }
                }
            }
        }
    }


    private void drawX(Canvas canvas, int row, int col) {
        paint.setColor(Xcolor);
        paint.setStrokeWidth(16);

        //positive sloping line
        canvas.drawLine((float) ((col+1)*cellSize - cellSize* 0.2),
                (float) (row*cellSize + cellSize* 0.2),
                (float) (col*cellSize + cellSize* 0.2),
                (float) ((row+1)*cellSize - cellSize* 0.2),
                paint);

        //negative sloping line
        canvas.drawLine((float) (col*cellSize + cellSize* 0.2),
                (float) (row*cellSize + cellSize* 0.2),
                (float) ((col+1)*cellSize - cellSize* 0.2),
                (float) ((row+1)*cellSize - cellSize* 0.2),
                paint);

    }


    private void drawO(Canvas canvas, int row, int col) {
        paint.setColor(Ocolor);
        canvas.drawOval((float)(col*cellSize + cellSize* 0.2),
                (float)(row*cellSize + cellSize* 0.2),
                (float) ((col*cellSize+cellSize) - cellSize* 0.2),
                (float) ((row*cellSize+cellSize) - cellSize* 0.2),
                paint);
    }
    private void drawHorizontalLine(Canvas canvas, int row, int col){
        canvas.drawLine(col, row*cellSize + (float)cellSize/2,
                cellSize*3, row*cellSize + (float)cellSize/2,
                paint);
    }



    private void drawVerticalLine(Canvas canvas, int row, int col){
        canvas.drawLine(col*cellSize + (float)cellSize/2, row,
                col*cellSize + (float)cellSize/2, cellSize*3,
                paint);
    }

    private void drawDiagonalLinePos(Canvas canvas){
        canvas.drawLine(0, cellSize*3, cellSize*3, 0, paint);
    }

    private void drawDiagonalLineNeg(Canvas canvas){
        canvas.drawLine(0, 0, cellSize*3, cellSize*3, paint);
    }

    public void setUpGame(Button playAgain, Button home, TextView playerDisplay, String[] names){
        game.setPlayAgainBTN(playAgain);
        game.setHomeBTN(home);
        game.setPlayerTurn(playerDisplay);
        game.setPlayerNames(names);
    }

    public  void resetGame()
    {
        GameLogic.resetGame();
        winningLine = false;
    }

    private void drawWinningLine(Canvas canvas){
        int row = game.getWinType()[0];
        int col = game.getWinType()[1];

        switch (game.getWinType()[2]){
            case 1:
                //draw Horizontal Line
                drawHorizontalLine(canvas, row, col);
                break;
            case 2:
                //draw Vertical Line
                drawVerticalLine(canvas, row, col);
                break;
            case 3:
                //draw Diagonal Line Neg
                drawDiagonalLineNeg(canvas);
                break;
            case 4:
                //draw Diagonal Line Pos
                drawDiagonalLinePos(canvas);
                break;
        }
    }

}