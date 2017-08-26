import javax.swing.*;
import java.util.TimerTask;

class TooLongLoadMessage {

    private java.util.Timer timer = new java.util.Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            JOptionPane.showMessageDialog(mainFrame.theMainPanel, "Слишком долгая загрузка.\nПопробуйте вручную обновить страницу в окне барузера либо остановите парсинг.");
        }
    };


    void startTimer() {
        timer.schedule(task, 150000);
    }

    void stopTimer() {
        timer.cancel();
    }
}
