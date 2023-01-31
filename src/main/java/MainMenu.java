import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class MainMenu {
    private JButton openFilebtn;
    private JTextField filepath;
    private JPanel mainPanel;
    private JButton uploadBtn;
    private JButton extractBtn;
    private JTextArea enTextArea;
    private JTextArea chiTextarea;
    private JCheckBox extractEnCheckBox;
    private JCheckBox extractChnCheckBox;
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainMenu");
        frame.setContentPane(new MainMenu().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainMenu() {
        openFilebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile(filter, filepath);
            }

        });
        extractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(filepath.getText());
                extractText(filepath.getText());
            }
        });
    }

    public static void selectFile(FileNameExtensionFilter filter, JTextField filepath) {
        JFileChooser jc = new JFileChooser();
        jc.setFileFilter(filter);
        int saveDialog = jc.showSaveDialog(null);

        if (saveDialog == JFileChooser.APPROVE_OPTION) {
            filepath.setText(jc.getSelectedFile().getAbsolutePath());
        }
    }

    public String extractText(String filepath) {
        File image = new File(filepath);
        Tesseract tess4j = new Tesseract();
        tess4j.setDatapath("/home/alxx/Apps/tess4j-traindata");

        try {
            tess4j.setTessVariable("user_defined_dpi", "200");
            if(extractEnCheckBox.isSelected()) {
                String result = tess4j.doOCR(image);
                enTextArea.setText(cleanString("en", result));
            }

            if(extractChnCheckBox.isSelected()) {
                tess4j.setLanguage("chi_sim");
                String result2 = tess4j.doOCR(image);
                chiTextarea.setText(cleanString("chi", result2));
            }

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        return "";
    }

    public static String cleanString(String lang, String text) {
        if(lang.equals("en")) {
            text = text.replaceAll("[^a-zA-Z0-9]", " ");
        } else {
            text = text.replaceAll("[a-zA-Z0-9]", " ");
        }

        return text;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

