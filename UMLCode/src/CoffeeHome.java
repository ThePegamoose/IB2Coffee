import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
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


    public CoffeeHome(String title) {
        super(title);
        setContentPane(panelMain);
        buttonClock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClockForm  clockForm = new ClockForm();
                clockForm.setSize(500,300);
                //  clockForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clockForm.pack();
                clockForm.setVisible(true);


            }
        });
        buttonTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CountTimer  timerForm = new CountTimer();
                timerForm.setSize(new Dimension(300,250));
                timerForm.setVisible(true);

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
    }
    public  void cupPopup(){

        JOptionPane.showMessageDialog(null,"Dont forget to place your coffee cup");
    }
    public void checkStats(){
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String date = formatter.format(d);
        DB db2 = new DB();
        String ret = db2.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getCount/" + date);
        JSONArray array = new JSONArray(ret);
        JSONObject ob = array.getJSONObject(0);
        String value = ob.getString("count");
        coffeeIntake.setText("Coffees drunk today: " + value + "  ");

        int total = Integer.parseInt(value);
        LocalDate today = LocalDate.now();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("ddMMyyyy");
        for(int i = 1; i <= 6; i++){
            String day = today.minusDays(i).format(form);
            ret = db2.makeGETRequest("https://studev.groept.be/api/a21ib2a04/getCount/" + day);
            array = new JSONArray(ret);
            if(!array.isEmpty()){
                ob = array.getJSONObject(0);
                total += Integer.parseInt(ob.getString("count"));
            }
        }
        coffeeIntake.setText("Coffees drunk today: " + value + "  \n" + "Coffees drunk this week: " + total);

    }


    public static void main(String[] args) {
        String water ="0";
        CoffeeHome  frame = new CoffeeHome( "CoffeeApp");
        frame.setVisible(true);
        frame.setSize(500,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DB db = new DB();
        db.parseJSON(db.makeGETRequest( "https://studev.groept.be/api/a21ib2a04/waterlevel"));
        db.parseJSON(db.makeGETRequest( "https://studev.groept.be/api/a21ib2a04/temperature"));




        //JPanel panelMain = new JPanel();



    }


}
