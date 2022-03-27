import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TempDisplay extends JDialog{

    private JLabel tempLabel;
    private JLabel heading;
    private JPanel tempPanel;
    private Timer timer;

    public TempDisplay()
    {
        //super("Current Temperature");
        //this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);

        tempPanel = new JPanel();
        tempLabel = new JLabel();
        heading = new JLabel();

        heading.setText("The current temperature of your coffee is:");

        Timer timer = new javax.swing.Timer(2500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DB temperatureDB = new DB();
                String temperature = temperatureDB.makeGETRequest("https://studev.groept.be/api/a21ib2a04/temperature");
                int tempInt = Integer.parseInt(temperatureDB.parseJSON(temperature,"Value"));

                tempLabel.setFont(tempLabel.getFont().deriveFont(64f));
                if (tempInt < 50)
                {
                    tempLabel.setForeground(Color.RED);
                    //JOptionPane.showMessageDialog(null,"Coffee is getting cold");
                }
                else
                    tempLabel.setForeground(Color.BLACK);

                tempLabel.setText(temperatureDB.parseJSON(temperature,"Value")+ "°C");
            }
        });


        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        tempPanel.add(heading);
        tempPanel.add(tempLabel);

        tempPanel.setOpaque(true);
        tempPanel.setBackground(new Color(187, 250, 192));
        tempPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        tempPanel.setPreferredSize(new Dimension(300, 150));

        this.setContentPane(tempPanel);
        timer.start();

    }

    public String temperatureVal(){
        return tempLabel.getText();
    }

    public static void main(String[] args) {
        TempDisplay ui= new TempDisplay();
        ui.setVisible(true);
        ui.pack();
    }
}