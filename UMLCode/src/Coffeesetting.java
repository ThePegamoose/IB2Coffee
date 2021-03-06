import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
        //DBSettings setting = new DBSettings();
        DB db = new DB();
        String h_time = db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_heatingTime");
        heatTimeData.setText(db.parseJSON(h_time,"Value"));

        String c_time = db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_coffeeTime");
        coffeetimeData.setText(db.parseJSON(c_time,"Value"));

        String c_size = db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/Settings_Cupsize");
        cupsizeData.setText(db.parseJSON(c_size,"Value"));
        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String heatTimeData_input = heatTimeData.getText();
                db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_heatingtime/"+heatTimeData_input);

                String coffeeTimeData_input = coffeetimeData.getText();
                db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_coffeetime/"+coffeeTimeData_input);

                String cupsizeData_input = cupsizeData.getText();
                db.makeGETRequest("https://studev.groept.be/api/a21ib2a04/update_cupsize/"+cupsizeData_input);

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
