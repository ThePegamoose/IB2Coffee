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
    private javax.swing.Timer timer2;
    private String timerTime;

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
        timerDisplay.setText("00:00");


        //timer2.start();



        /*
        Object[] options = {"OK",
                "Set New Alarm",
                "Cancel"};

         */


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
                DB setReset = new DB();
                Object result = JOptionPane.showInputDialog(null, "In how many minutes would you like your coffee?", "Timer", JOptionPane.PLAIN_MESSAGE,null, fixedTimerArray, fixedTimerArray[0]);
                if(result!=null){
                    setReset.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                    setReset.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + result );
                        timerTime = String.valueOf(result);
                        countdown(String.valueOf(result));
                        timer1.start();
                    //run();
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
                DB addNew = new DB();
                Object time = JOptionPane.showInputDialog(null, "New Timer");
                if(time!=null) {
                    if (Arrays.asList(fixedTimerArray).contains(time)) {
                        JOptionPane.showMessageDialog(null, "This timer already exists");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + time);
                            timerTime = String.valueOf(time);
                            countdown(String.valueOf(time));
                            timer1.start();

                    } else {
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/newTimer/" + time);
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                        addNew.makeGETRequest("https://studev.groept.be/api/a21ib2a04/setTimer/" + time);
                        JOptionPane.showMessageDialog(null, "Timer added successfully");
                            timerTime = String.valueOf(time);
                            countdown(String.valueOf(time));
                            timer1.start();

                    }
                }
                //run();

            };
        });

        JButton stop = new JButton("Stop Timer");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DB reset = new DB();
                reset.makeGETRequest("https://studev.groept.be/api/a21ib2a04/clearActiveTimer");
                if(timer1!=null) {
                    timer1.stop();
                    timerDisplay.setText("00:00");

                }
            }

        });

        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        newTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        stop.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerDisplay.setFont(timerDisplay.getFont().deriveFont(64f));

        timerPanel.setOpaque(true);
        timerPanel.setBackground(new Color(187, 250, 192));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        timerPanel.setPreferredSize(new Dimension(300, 150));

        // Object selectionObject = JOptionPane.showOptionDialog(null, "In how many minutes would you like your coffee?", "Timer", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, timerArray);
        timerPanel.add(timerDisplay);
        timerPanel.add(chooseTimer);
        timerPanel.add(newTimer);
        timerPanel.add(stop);
        this.setContentPane(timerPanel);
    }

/*
    public void run() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("ServoCompiled\\timer.py");
            process.getOutputStream();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

 */

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
                if (minute < 0 && (second <= 59 && second > 51)) {
                    timerDisplay.setText("00:00");
                }
                else if (minute < 0){
                    minute = Integer.valueOf(time);
                    second = 0;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timerDisplay.setText(ddMinute + ":" + ddSecond);
                    //timer1.stop();
                    /*
                    timerDisplay.setText("Making coffee every" + time + "minutes");
                    public void countdown(String time) {
                        DecimalFormat dFormat = new DecimalFormat("00");
                        minute = Integer.valueOf(time);
                        second = 0;
                        timer1 = new javax.swing.Timer(1000, new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                            }
                        });
                    //timer2.start();
                    /*
                    DB continuity = new DB();
                    String cont = continuity.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getSetting/continuous");
                    String start = continuity.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getSetting/coffeeStart");
                    if (cont.equals("1") && start.equals("1")) {

                        timer1.stop();
                        countdown(time);


                        minute = Integer.valueOf(time);
                        ddMinute = dFormat.format(Integer.valueOf(time) - 1);
                        timerDisplay.setText(ddMinute + ":" + ddSecond);


                    }
                    //else if ()
                    if (){
                        timer1.stop();
                        timer1.start();
                    }



                    TempDisplay temp = new TempDisplay();
                    temp.setSize(new Dimension(300, 150));
                    temp.setVisible(true);

                     */

                }
            }
        });
/*
        timer2 = new javax.swing.Timer(minute*60000+5000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                timer2.stop();
                countdown(time);
            }
        });

 */
    }


    public static void main(String[] args)
        {
        CountTimer ui= new CountTimer();
        ui.setVisible(true);
        ui.pack();
        }
}


