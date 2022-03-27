import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.*;
import javax.swing.*;


public class ClockForm extends JDialog {

    private JButton chooseAlarm;
    private JButton newAlarm;
    private JLabel clock;
    private JLabel activeAlarms;
    private JButton DisableAlarm;
    private JPanel clockPanel;
    private JList alarmList;
    private JComboBox Day;
    private JComboBox Hour;
    private JComboBox Minute;
    private String[] HourArray= new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    private String[] DayArray= new String[]{"Monday","Tuesday","Wednesday", "Thursday", "Friday","Saturday","Sunday"};
    private String[] MinuteArray= new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",};
    public ClockForm() {
        //super("Alarm");
        //this.setDefaultCloseOperation(3);
        this.clockPanel = new JPanel();
        this.newAlarm = new JButton();
        this.newAlarm.setText("Create new alarm");
        this.chooseAlarm = new JButton();
        this.chooseAlarm.setText("Choose an existing Alarm");
        this.alarmList=new JList();
        this.activeAlarms=new JLabel("active alarms");
        this.DisableAlarm= new JButton("turn off alarm");
        this.Minute= new JComboBox(MinuteArray);
        this.Hour= new JComboBox(HourArray);
        this.Day= new JComboBox(DayArray);
        Object[] var10000 = new Object[]{"OK", "Set New Alarm", "Cancel"};
        DB AlarmDB = new DB();
        String ActiveAlarm=AlarmDB.makeGETRequest(   "http://mysql.studev.groept.be/api/a21ib2a04/getActiveAlarm");
        String ActiveAlarmVal = AlarmDB.parseJSON(ActiveAlarm, "time");
        String[] ActiveAlarmArray = ActiveAlarmVal.split("[,]", 0);
        String Alarm = AlarmDB.makeGETRequest(   "http://mysql.studev.groept.be/api/a21ib2a04/getAllAlarm");
        String AlarmVal = AlarmDB.parseJSON(Alarm, "time");
        String[] alarmArray = AlarmVal.split("[,]", 0);
        this.alarmList=new JList(ActiveAlarmArray);
        String[] fixedAlarmArray = new String[alarmArray.length];
        for (int i = 0; i < alarmArray.length; i++)
        {
            String time = alarmArray[i].replaceAll(" ", "").replaceAll(":","");
            fixedAlarmArray[i] = time;
        }

        newAlarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                disableAlarm();
                DBTest setReset = new DBTest();
                String SetHour=(String)Hour.getSelectedItem();
                String SetDay=(String)Day.getSelectedItem();
                String SetMinute=(String)Minute.getSelectedItem();
                String time=SetDay+SetHour+SetMinute;
                System.out.println(time);
                setReset.makeGETRequest("http://mysql.studev.groept.be/api/a21ib2a04/newAlarm/"+time);



            }

        });

        chooseAlarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                disableAlarm();
                DBTest setReset = new DBTest();
                Object result = JOptionPane.showInputDialog(null, "Select your alarm", "Alarm", JOptionPane.PLAIN_MESSAGE,null, fixedAlarmArray, fixedAlarmArray[0]);
                if(result!=null){
                    setReset.makeGETRequest("http://mysql.studev.groept.be/api/a21ib2a04/setAlarm/"+result);

                }
            };
        });
        DisableAlarm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                disableAlarm();

            };
        });

        this.clockPanel.setLayout(new BoxLayout(this.clockPanel, 1));
        this.newAlarm.setAlignmentX(0.5F);
        this.chooseAlarm.setAlignmentX(0.5F);
        this.activeAlarms.setAlignmentX(0.5F);
        this.DisableAlarm.setAlignmentX(0.5F);
        this.clockPanel.setOpaque(true);
        this.clockPanel.setBackground(new Color(187, 250, 192));
        this.clockPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.clockPanel.setPreferredSize(new Dimension(300, 300));
        this.clockPanel.add(this.activeAlarms);
        this.clockPanel.add(this.alarmList);
        this.clockPanel.add(this.chooseAlarm);
        this.clockPanel.add(this.newAlarm);
        this.clockPanel.add(this.Day);
        this.clockPanel.add(this.Hour);
        this.clockPanel.add(this.Minute);
        this.clockPanel.add(this.DisableAlarm);
        this.setContentPane(this.clockPanel);
    }
    public void disableAlarm()
    {
        DBTest setReset = new DBTest();
        setReset.makeGETRequest("http://mysql.studev.groept.be/api/a21ib2a04/disableAlarm");
    }


    public static void main(String[] args) {
        ClockForm ui = new ClockForm();
        ui.setVisible(true);
        ui.pack();
    }
}


