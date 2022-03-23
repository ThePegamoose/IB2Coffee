import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TempDisplay extends JDialog{

    private JLabel tempLabel;
    private JLabel heading;
    private JPanel tempPanel;

    public TempDisplay()
    {
        //super("Current Temperature");
        this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);

        tempPanel = new JPanel();
        tempLabel = new JLabel();
        heading = new JLabel();

        DBTest temperatureDB = new DBTest();
        String temperature = temperatureDB.makeGETRequest("https://studev.groept.be/api/a21ib2a04/temperature");
        int tempInt = Integer.parseInt(temperatureDB.parseJSON(temperature,"Value"));
        tempLabel.setText(temperatureDB.parseJSON(temperature,"Value")+ "°C");
        heading.setText("The current temperature of your coffee is:");

        tempLabel.setFont(tempLabel.getFont().deriveFont(64f));
        if (tempInt < 50)
        {
            tempLabel.setForeground(Color.RED);
        }
        else
            tempLabel.setForeground(Color.BLACK);


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

    }

    public static void main(String[] args) {
        TempDisplay ui= new TempDisplay();
        ui.setVisible(true);
        ui.pack();
    }
}