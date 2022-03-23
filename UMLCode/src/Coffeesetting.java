import javax.swing.*;

public class Coffeesetting extends JDialog{
    private JPanel panel1;
    private JLabel heatingTime;
    private JLabel coffeeTime;
    private JLabel cupSize;
    private JLabel heatTimeData;
    private JLabel coffeetimeData;
    private JLabel cupsizeData;

    public Coffeesetting(String title){
        //super("Settings");
        setContentPane(panel1);
        DBSettings setting = new DBSettings();
        String h_time = setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_heatingTime");
        heatTimeData.setText(setting.parseJSON(h_time));

        String c_time = setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_coffeeTime");
        coffeetimeData.setText(setting.parseJSON(c_time));

        String c_size = setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_Cupsize");
        cupsizeData.setText(setting.parseJSON(c_size));

    }
    public static void main(String[] args) {
        Coffeesetting  settings = new Coffeesetting( "Settings");
        settings.setVisible(true);
        //settings.setSize(300,300);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}
