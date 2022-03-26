import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;


public class CountTimer extends JDialog{

    DecimalFormat dFormat = new DecimalFormat("00");
    int minute = 0;
    int second = 0;

    private JButton chooseTimer;
    private JButton newTimer;
    private JLabel timerDisplay;
    private JPanel timerPanel;
    private javax.swing.Timer timer1;

    public CountTimer()
    {
        //super("Timer");
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timerPanel = new JPanel();
        newTimer = new JButton();
        newTimer.setText("Create new timer");
        chooseTimer = new JButton();
        chooseTimer.setText("Choose an existing timer");
        timerDisplay = new JLabel();
        timerDisplay.setText("-");

        Object[] options = {"OK",
                "Set New Alarm",
                "Cancel"};


        DB timerDB = new DB();
        String timer = timerDB.makeGETRequest("https://studev.groept.be/api/a21ib2a04/timerAll");
        String timerVal = timerDB.parseJSON(timer,"timerValue");


        String[] timerArray = timerVal.split("[,]", 0);

        String[] fixedTimerArray = new String[timerArray.length];
        for (int i = 0; i < timerArray.length; i++)
        {
            String time = timerArray[i].replaceAll(" ", "");
            fixedTimerArray[i] = time;
        }




        chooseTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timer1 != null) {
                    timer1.stop();
                }
                timerDisplay.setText("00:00");
                DBTest setReset = new DBTest();
                Object result = JOptionPane.showInputDialog(null, "In how many minutes would you like your coffee?", "Timer", JOptionPane.PLAIN_MESSAGE,null, fixedTimerArray, fixedTimerArray[0]);
                if(result!=null){
                    setReset.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                    setReset.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + result );
                    countdown(String.valueOf(result));
                    timer1.start();
                    run();
                }
            };
        });

        newTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timer1 != null) {
                    timer1.stop();
                }
                timerDisplay.setText("00:00");
                DBTest addNew = new DBTest();
                Object time = JOptionPane.showInputDialog(null, "New Timer");
                if(time!=null) {
                    if (Arrays.asList(fixedTimerArray).contains(time)) {
                        JOptionPane.showMessageDialog(null, "This timer already exists");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + time);
                        countdown(String.valueOf(time));
                        timer1.start();
                    } else {
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/newTimer/" + time);
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + time);
                        JOptionPane.showMessageDialog(null, "Timer added successfully");
                        countdown(String.valueOf(time));
                        timer1.start();
                    }
                }
                run();

            };
        });

        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        newTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerDisplay.setFont(timerDisplay.getFont().deriveFont(64f));

        timerPanel.setOpaque(true);
        timerPanel.setBackground(new Color(187, 250, 192));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        timerPanel.setPreferredSize(new Dimension(300, 150));

        // Object selectionObject = JOptionPane.showOptionDialog(null, "In how many minutes would you like your coffee?", "Timer", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, timerArray);
        timerPanel.add(timerDisplay);
        timerPanel.add(chooseTimer);
        timerPanel.add(newTimer);
        this.setContentPane(timerPanel);
    }


    public void run() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ServoCompiled\\timer.py");
            process.getOutputStream();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void countdown(String time) {
        DecimalFormat dFormat = new DecimalFormat("00");
        minute = Integer.valueOf(time);
        second = 0;
        timer1 = new javax.swing.Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                second--;
                String ddSecond = dFormat.format(second);
                String ddMinute = dFormat.format(minute);
                timerDisplay.setText(ddMinute + ":" + ddSecond);

                if (second == -1) {
                    second = 59;
                    minute--;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timerDisplay.setText(ddMinute + ":" + ddSecond);
                }
                if (minute == 0 && second == 0) {
                    timer1.stop();
                    TempDisplay temp = new TempDisplay();
                    temp.setSize(new Dimension(300, 150));
                    temp.setVisible(true);
                }
            }
        });
    }


    public void resetTimer(Timer timer)
    {
        timer.stop();
        timer.restart();
    }


    public static void main(String[] args)
        {
        CountTimer ui= new CountTimer();
        ui.setVisible(true);
        ui.pack();
        }
}


