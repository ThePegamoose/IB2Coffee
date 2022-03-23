import javax.swing.*;

public class Coffeesetting extends JDialog{
    private JPanel panel1;
    private JLabel heatingTime;
    private JLabel coffeeTime;
    private JTextField heatTimeData;
    private JTextField coffeetimeData;

    private JButton btn_update;
    private JTextField cupsizeData;
    private JLabel cupsize;


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
        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String heatTimeData_input = heatTimeData.getText();
                setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_heatingtime/"+heatTimeData_input);

                String coffeeTimeData_input = coffeetimeData.getText();
                setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_coffeetime/"+coffeeTimeData_input);

                String cupsizeData_input = cupsizeData.getText();
                setting.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_cupsize/"+cupsizeData_input);

            }
        });

    }
    public static void main(String[] args) {
        Coffeesetting  settings = new Coffeesetting( "Settings");
        settings.setVisible(true);
        //settings.setSize(300,300);
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
}
