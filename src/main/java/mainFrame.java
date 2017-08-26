import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class mainFrame extends JFrame {
    private JPanel mainPanel, progressPanel;
    static JPanel theMainPanel;
    private static JTextField keyWords, fileName;
    static JProgressBar progressBar;
    private Thread parseThread = new ParseThread();
    static JLabel progressBarLabel = new JLabel("0 из 0");
    private JFormattedTextField startDate, endDate;
    private MaskFormatter dateFormat;
    private static PrintStream ps;


    public mainFrame() {
        super("Gazprom Parser");

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM_HH-mm");
        String dateToday = formattedDate.format(calendar.getTime());

        theMainPanel = new JPanel(new BorderLayout());

        try {
            dateFormat = new MaskFormatter("##.##.####");
            dateFormat.setPlaceholderCharacter('_');

        } catch (ParseException e) {
            createLogStream();
            e.printStackTrace(ps);
        }

        startDate = new JFormattedTextField(dateFormat);
        endDate = new JFormattedTextField(dateFormat);

        setBounds(0, 0, 400, 240);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = mainPanel;
        container.setLayout(new GridLayout(5, 2, 10, 10));
        JLabel keyWordsLabel = new JLabel("Ключевые слова: ", SwingConstants.LEFT);
        container.add(keyWordsLabel);
        keyWords = new JTextField("", 30);
        container.add(keyWords);
        JLabel startDateLabel = new JLabel("C:", SwingConstants.LEFT);
        JLabel endDateLabel = new JLabel("По:", SwingConstants.LEFT);
        container.add(startDateLabel);
        container.add(startDate);
        container.add(endDateLabel);
        container.add(endDate);
        JLabel fileNameLabel = new JLabel("Имя файла: ", SwingConstants.LEFT);
        container.add(fileNameLabel);
        fileName = new JTextField("parser_" + dateToday, 10);
        container.add(fileName);
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        container.add(start);
        progressBar = new JProgressBar();
        progressBar.setForeground(new Color(0, 255, 0));
        start.addActionListener(new StartEventListener());
        stop.addActionListener(new StopEventListener());
        progressBar.setIndeterminate(true);


        GridBagLayout progressLayout = new GridBagLayout();
        GridBagConstraints progressConstraints = new GridBagConstraints();
        progressConstraints.anchor = GridBagConstraints.CENTER;
        progressConstraints.fill = GridBagConstraints.NONE;
        progressConstraints.gridheight = 1;
        progressConstraints.gridwidth = GridBagConstraints.REMAINDER;
        progressConstraints.insets = new Insets(10, 10, 10, 10);
        progressConstraints.weightx = 0;
        progressConstraints.weighty = 0;
        progressConstraints.gridx = GridBagConstraints.RELATIVE;
        progressConstraints.gridy = 0;
        progressLayout.setConstraints(progressBar, progressConstraints);
        progressPanel = new JPanel(progressLayout);
        progressPanel.add(progressBar);
        progressLayout.setConstraints(progressBarLabel, progressConstraints);
        progressPanel.add(progressBarLabel);
        progressConstraints.gridy = 1;
        progressLayout.setConstraints(stop, progressConstraints);
        progressPanel.add(stop);
        progressPanel.setOpaque(false);
        progressPanel.addMouseListener(new MouseAdapter() {
        });
        progressBar.setSize(350, 80);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER, -1);
        layeredPane.add(progressPanel, JLayeredPane.DEFAULT_LAYER + 50, -1);
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setSize(e.getComponent().getSize());
                progressPanel.setSize(e.getComponent().getSize());
            }
        });


        theMainPanel.add(layeredPane, BorderLayout.CENTER);
        setContentPane(theMainPanel);
        progressPanel.setVisible(false);
        setVisible(true);

    }

    static String getFileName() {
        return fileName.getText();
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBackground(new Color(-1));
        mainPanel.setEnabled(true);
        mainPanel.setToolTipText("");
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "gazprom_parser v2.2.1", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, this.$$$getFont$$$(null, -1, -1, mainPanel.getFont()), new Color(-16777216)));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    class StartEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            mainPanel.setEnabled(false);
            mainPanel.setVisible(false);
            progressPanel.setVisible(true);
            parseThread.start();
        }
    }

    class StopEventListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(mainPanel,
                    "Что-то сломалось. Сохранить промежуточный результат?\nЗаписано "
                            + Parse.numberOfLinks + " из " + Main.links.size(),
                    "Error",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Excel.createXls();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Не могу сохранить");
                    createLogStream();
                    ex.printStackTrace(ps);

                }
            }
            parseThread.interrupt();

        }
    }

    class ParseThread extends Thread {
        public void run() {
            String[] words = separateWords(keyWords.getText());
            String startDateFinal = startDate.getText();
            String endDateFinal = endDate.getText();
            for (String word : words) {
                try {
                    Main.LinkGathering(word, startDateFinal, endDateFinal);
                } catch (Exception e1) {
                    if (!Thread.interrupted()) {
                        JOptionPane.showMessageDialog(mainPanel, "Что-то сломалось", "Error", JOptionPane.PLAIN_MESSAGE);
                        createLogStream();
                        e1.printStackTrace(ps);
                        makeScreenshot(Main.driver);
                    }

                }
            }

            progressBar.setIndeterminate(false);
            progressBar.setStringPainted(true);

            try {
                Main.StartParsing();
            } catch (Exception e1) {
                if (JOptionPane.showConfirmDialog(mainPanel,
                        "Что-то сломалось. Сохранить промежуточный результат?\nЗаписано " + Parse.numberOfLinks + " из " + Main.links.size(),
                        "Error",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try {
                        Excel.createXls();
                    } catch (IOException e) {
                        createLogStream();
                        JOptionPane.showMessageDialog(mainPanel, "Не могу сохранить");
                        e.printStackTrace(ps);
                        makeScreenshot(Main.driver);
                    }
                }
                createLogStream();
                e1.printStackTrace(ps);

            }
            mainPanel.setEnabled(true);
            mainPanel.setVisible(true);
            progressPanel.setVisible(false);
        }
    }


    private String[] separateWords(String words) {
        return words.split("[,;:.!?\\s]+");
    }

    public static void main(String[] args) {
        new mainFrame();

    }

    private void createUIComponents() {

    }

    private void createLogStream() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM_HH-mm");
        String dateToday = formattedDate.format(calendar.getTime());
        try {
            ps = new PrintStream(new File("parserLog_" + dateToday + ".txt"));
        } catch (FileNotFoundException e1) {
            try {
                ps = new PrintStream("parserLogError.txt");
            } catch (FileNotFoundException e2) {
                JOptionPane.showMessageDialog(theMainPanel, "Не могу сохранить лог");
            }
        }
    }

    private void makeScreenshot(WebDriver driver) {
        File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM_HH-mm");
        String dateToday = formattedDate.format(calendar.getTime());
        try {
            FileUtils.copyFile(scr, new File("ErrorScr_" + dateToday + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}