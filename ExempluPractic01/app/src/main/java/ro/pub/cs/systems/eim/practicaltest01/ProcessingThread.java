package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Context;
import android.content.Intent;

import java.util.Date;
import java.util.Random;

/**
 * Created by irineu on 01.04.2016.
 */
public class ProcessingThread extends Thread {

    private Context context = null;
//    private int firstNumber, secondNumber;
    private double medieAritmetica, medieGeometrica;
    private boolean continueRunning;

    public ProcessingThread(Context context, int firstNumber, int secondNumber) {
//        this.firstNumber = firstNumber;
//        this.secondNumber = secondNumber;
        this.context = context;
        medieAritmetica = (firstNumber + secondNumber) / 2;
        medieGeometrica = Math.sqrt(firstNumber * secondNumber);
        continueRunning = true;
    }

    @Override
    public void run() {
        while(continueRunning) {
            this.sendMessage();
            this.sleep();
        }
    }

    public void sendMessage() {
        Intent intent = new Intent();

        intent.setAction(Constants.actionTypes[new Random().nextInt(Constants.actionTypes.length)]);
        intent.putExtra("message", new Date().toString() + " " + medieAritmetica + " " + medieGeometrica);

        context.sendBroadcast(intent);
    }

    public void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        continueRunning = false;
    }
}
