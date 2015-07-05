package com.example.tictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class Board extends View{

	int myHeight;
	int myWidth;

	// to draw on
	Bitmap myBitmap;
	Canvas myCanvas;
	Paint myPaint;

	boolean drawX = true;

	public Board(Context context) {
		super(context);
		myPaint = new Paint();
		myPaint.setColor(Color.BLACK);
		myPaint.setStyle(Paint.Style.STROKE);
	}

	// make size adjustable for any screen
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		myHeight = View.MeasureSpec.getSize(heightMeasureSpec);
		myWidth = View.MeasureSpec.getSize(widthMeasureSpec);

		setMeasuredDimension(myWidth, myHeight);

		myBitmap = Bitmap.createBitmap(myWidth, myHeight, Bitmap.Config.ARGB_8888);
		// make drawable
		myCanvas = new Canvas(myBitmap);

		calculateLinePlacement();
		drawBoard();

	}

	int[][] XOpositions = new int[3][3];

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(myBitmap, 0, 0, myPaint);
	}


	Point[] horizLine1;
	Point[] horizLine2;
	Point[] vertLine1;
	Point[] vertLine2;

	private LogicalBoard myLogicBoard;

	private void calculateLinePlacement() {
		int splitHeight = myHeight / 3;
		int splitWidth = myWidth / 3;

		horizLine1 = new Point[2];
		Point p1 = new Point(0, splitHeight);
		Point p2 = new Point(myWidth, splitHeight);
		horizLine1[0] = p1;
		horizLine1[1] = p2;

		horizLine2 = new Point[2];
		p1 = new Point(0, 2 * splitHeight);
		p2 = new Point(myWidth, 2 * splitHeight);
		horizLine2[0] = p1;
		horizLine2[1] = p2;

		vertLine1 = new Point[2];
		p1 = new Point(splitWidth, 0);
		p2 = new Point(splitWidth, myHeight);
		vertLine1[0] = p1;
		vertLine1[1] = p2;

		vertLine2 = new Point[2];
		p1 = new Point(2  *splitWidth, 0);
		p2 = new Point(2 * splitWidth, myHeight);
		vertLine2[0] = p1;
		vertLine2[1] = p2;

		myLogicBoard = new LogicalBoard(splitWidth, splitHeight);
	}

	private void drawBoard() {
		myCanvas.drawLine(horizLine1[0].x, horizLine1[0].y, horizLine1[1].x, horizLine1[1].y, myPaint);
		myCanvas.drawLine(horizLine2[0].x, horizLine2[0].y, horizLine2[1].x, horizLine2[1].y, myPaint);
		myCanvas.drawLine(vertLine1[0].x, vertLine1[0].y, vertLine1[1].x, vertLine1[1].y, myPaint);
		myCanvas.drawLine(vertLine2[0].x, vertLine2[0].y, vertLine2[1].x, vertLine2[1].y, myPaint);

		invalidate();

	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		RectF position = myLogicBoard.getPositionToFill(me.getX(), me.getY());
		// checking existence of position
		if(position != null) {
			// draw x or o
			if(drawX) {
				// draw X w/ 2 lines
				myCanvas.drawLine(position.left, position.top, position.right, position.bottom, myPaint);
				myCanvas.drawLine(position.right, position.top, position.left, position.bottom, myPaint);
				XOpositions[(int) (me.getX() / (myWidth / 3))][(int) (me.getY() / (myHeight / 3))] = 1;
			} else {
				// draw o
				myCanvas.drawOval(position, myPaint);
				XOpositions[(int) (me.getX() / (myWidth / 3))][(int) (me.getY() / (myHeight / 3))] = -1;
			}
			drawX = !drawX;
			
			Paint textPaint = new Paint();
			textPaint.setStyle(Paint.Style.STROKE);
			textPaint.setColor(Color.RED);
			textPaint.setTextSize(30F);
			textPaint.setStrokeWidth(3);
			
			
			invalidate();
			if(checkWin() == 'x') {
				myCanvas.drawColor(Color.WHITE);
				myCanvas.drawText("Game over! X wins!", myWidth / 2 - 100, myHeight / 2, textPaint);
			} else if(checkWin() == 'o') {
				myCanvas.drawColor(Color.WHITE);
				myCanvas.drawText("Game over! Y wins!", myWidth / 2 - 100, myHeight / 2, textPaint);
			} 
		}

		return true;
	}

	private char checkWin() {
		int total;
		// loop thru XOpositions, check for win

		// column win
		for(int r = 0; r < 3; r++) {
			total = XOpositions[r][0] + XOpositions[r][1] + XOpositions[r][2];
			if(total == 3) {
				// X wins
				return 'x';
			} else if(total == -3) {
				// O wins
				return 'o';
			} 
		}

		// row win
		for(int c = 0; c < 3; c++) {
			total = XOpositions[0][c] + XOpositions[1][c] + XOpositions[2][c];
			if(total == 3) {
				// X wins
				return 'x';
			} else if(total == -3) {
				// O wins
				return 'o';
			} 

		}

		// diag 1 win
		total = XOpositions[0][0] + XOpositions[1][1] + XOpositions[2][2];
		if(total == 3) {
			// X wins
			return 'X';
		} else if(total == -3) {
			// O wins
			return 'O';
		} 
		
		// diag 2 win
		total = XOpositions[2][0] + XOpositions[1][1] + XOpositions[0][2];
		if(total == 3) {
			// X wins
			return 'X';
		} else if(total == -3) {
			// O wins
			return 'O';
		}
		
		return 'n';


	}

}

