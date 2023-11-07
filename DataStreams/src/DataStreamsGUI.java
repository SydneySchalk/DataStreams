import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;

    public DataStreamsGUI() {
        setTitle("DataStreams");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTextArea = new JTextArea(20, 40);
        filteredTextArea = new JTextArea(20, 40);
        searchField = new JTextField(20);

        JButton loadButton = new JButton("Load a File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        Path file = chooser.getSelectedFile().toPath();
                        Stream<String> lines = Files.lines(file);

                        originalTextArea.setText(lines.collect(Collectors.joining("\n")));
                        lines.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataStreamsGUI.this, "Error loading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchField.getText().trim();
                if (!searchString.isEmpty()) {
                    Stream<String> lines = Arrays.stream(originalTextArea.getText().split("\n"));
                    filteredTextArea.setText(lines.filter(line -> line.contains(searchString)).collect(Collectors.joining("\n")));
                }
            }
        });


        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(loadButton);
        inputPanel.add(quitButton);

        add(new JScrollPane(originalTextArea), BorderLayout.WEST);
        add(new JScrollPane(filteredTextArea), BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataStreamsGUI dataStreamsGUI = new DataStreamsGUI();
            dataStreamsGUI.setVisible(true);
        });
    }
}
