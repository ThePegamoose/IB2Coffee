import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class CoffeeHome extends JFrame{

    private JPanel panelMain;
    private JLabel setYourTimeLabel;
    public JButton buttonClock;
    public JButton buttonTimer;
    private JLabel logolabel;
    private JLabel coffeeIntake;
    private JButton btn_settings;
    private JLabel WeeklyIntake;
    private JTextArea textArea1;

    private String check;
    //private TempDisplay tempDP;


    public CoffeeHome(String title) {
        super(title);
        setContentPane(panelMain);

        DB dbMode = new DB();



        buttonClock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClockForm  clockForm = new ClockForm();
                clockForm.setSize(500,300);
                //  clockForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clockForm.pack();
                clockForm.setVisible(true);
                dbMode.makeGETRequest("https://studev.groept.be/api/a21ib2a04/UpdateSetting/1/mode");
                waterPopup();
                cupPopup();
                temperaturePopUp();


            }
        });
        buttonTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CountTimer  timerForm = new CountTimer();
                timerForm.setSize(new Dimension(300,250));
                timerForm.setVisible(true);
                dbMode.makeGETRequest("https://studev.groept.be/api/a21ib2a04/UpdateSetting/0/mode");
                waterPopup();
                cupPopup();
                temperaturePopUp();

            }
        });
        btn_settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coffeesetting  settings = new Coffeesetting("");
                settings.setSize(new Dimension(200,250));
                settings.setVisible(true);

            }
        });
        checkStats();
        
        btn_CoffeeNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Dont forget to place your coffee cup");

            }
        });
    }
    public  void cupPopup(){

        JOptionPane.showMessageDialog(null,"Dont forget to place your coffee cup");
    }

    public  void waterPopup(){
        DB db = new DB();
        String info = db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getSetting/waterStatus");
        String checkWater = db.parseJSON(info,"Value");
        //System.out.println(check);
        if (checkWater.equals("0")) {
            JOptionPane.showMessageDialog(null, "Not enough water");
        }
    }

    public void temperaturePopUp(){
        DB db = new DB();

        /*
        tempDP.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/UpdateSetting/0/coffeeFinished");
            }
        });

         */
        Timer timer = new javax.swing.Timer(2500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String info = db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getSetting/coffeeFinished");
                check = db.parseJSON(info,"Value");
                //System.out.println(check);
                if (check.equals("1")){
                    TempDisplay tempDP = new TempDisplay();
                    tempDP.setSize(new Dimension(300, 150));
                    tempDP.setVisible(true);
                    db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/UpdateSetting/0/coffeeFinished");

                }
            }
        });
        timer.start();
        /*
        int temperatureValue = Integer.parseInt(tempDP.temperatureVal());
        if (temperatureValue <50){
            JOptionPane.showMessageDialog(null,"Coffee getting cold");
        }
         */

    }

    public String checkFinished(){

        return check;

    }

    public void checkStats(){
        Date d = new Date();
        String value;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String date = formatter.format(d);
        DB db2 = new DB();
        String ret = db2.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getCount/" + date);
        JSONArray array = new JSONArray(ret);
        if(!array.isEmpty()){
            JSONObject ob = array.getJSONObject(0);
            value = ob.getString("count");
        }
        else{
            value = "0";
        }
        coffeeIntake.setText("Coffees drunk today: " + value + "  ");

        int total = Integer.parseInt(value);
        LocalDate today = LocalDate.now();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("ddMMyyyy");
        for(int i = 1; i <= 6; i++){
            String day = today.minusDays(i).format(form);
            ret = db2.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getCount/" + day);
            array = new JSONArray(ret);
            if(!array.isEmpty()){
                JSONObject ob = array.getJSONObject(0);
                total += Integer.parseInt(ob.getString("count"));
            }
        }
        //coffeeIntake.setText("Coffees drunk today: " + value + "     ");
        //WeeklyIntake.setText("Coffees drunk this week: " + total + "  ");
        Font font1 = new Font("Dialog", Font.BOLD, 12);
        //System.out.println(javax.swing.UIManager.getDefaults().getFont("Label.font"));
        textArea1.setFont(font1);
        textArea1.setText("Coffees drunk today:  "+ value + "      " + "\n" + "Coffees drunk this week: " + total + "    ");

    }




    public static void main(String[] args) {
        String water ="0";
        CoffeeHome  frame = new CoffeeHome( "CoffeeApp");
        frame.setVisible(true);
        frame.setSize(505,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DB db = new DB();
        db.parseJSON(db.makeGETRequest( "https://studev.groept.be/api/a21ib2a04/waterlevel"),"Value");
        db.parseJSON(db.makeGETRequest( "https://studev.groept.be/api/a21ib2a04/temperature"),"Value");




        //JPanel panelMain = new JPanel();



    }
}
