import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GUI extends JFrame {
    private S_DES sdes;

    public GUI() {
        setTitle("S-DES 加密/解密");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建组件
        JLabel modeLabel = new JLabel("选择加密模式:");
        String[] modes = {"标准加密", "扩展加密"};
        JComboBox<String> modeComboBox = new JComboBox<>(modes);
        JButton nextButton = new JButton("下一步");

        // 设置布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(modeLabel);
        panel.add(modeComboBox);
        panel.add(nextButton);
        add(panel);

        // 初始化 S-DES 算法类
        sdes = new S_DES();

        // 下一步按钮事件
        nextButton.addActionListener(e -> {
            String selectedMode = (String) modeComboBox.getSelectedItem();
            if ("标准加密".equals(selectedMode)) {
                new StandardModeGUI(sdes).setVisible(true);
            } else {
                new ExtendedModeGUI(sdes).setVisible(true);
            }
            dispose(); // 关闭当前窗口
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}

// 标准模式界面
class StandardModeGUI extends JFrame {
    private JTextField inputField;
    private JTextField keyField;
    private JTextArea outputArea;
    private S_DES sdes;

    public StandardModeGUI(S_DES sdes) {
        this.sdes = sdes;
        setTitle("标准模式加密/解密");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建组件
        JLabel inputLabel = new JLabel("请输入(8位明文):");
        inputField = new JTextField(20);
        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        keyField = new JTextField(10);
        JButton encryptButton = new JButton("加密");
        JButton decryptButton = new JButton("解密");
        JButton switchButton = new JButton("切换模式"); // 切换模式按钮

        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);

        // 设置布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1)); // 增加一行用于切换按钮
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(switchButton); // 添加切换按钮

        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(panel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // 加密按钮事件
        encryptButton.addActionListener(e -> {
            String inputText = inputField.getText();
            String keyText = keyField.getText();

            if (validateKey(keyText) && inputText.length() == 8) {
                int[] key = parseKey(keyText);
                sdes.setKey(key);
                int[] inputArray = stringToBinaryArray(inputText);
                int[] encryptedArray = sdes.S_Encrypt(inputArray);
                outputArea.setText("密文: " + binaryArrayToString(encryptedArray));
            } else {
                outputArea.setText("无效输入或密钥，需为8位和10位二进制数");
            }
        });

        // 解密按钮事件
        decryptButton.addActionListener(e -> {
            String inputText = inputField.getText();
            String keyText = keyField.getText();

            if (validateKey(keyText) && inputText.length() == 8) {
                int[] key = parseKey(keyText);
                sdes.setKey(key);
                int[] inputArray = stringToBinaryArray(inputText);
                int[] decryptedArray = sdes.S_Decrypt(inputArray);
                outputArea.setText("明文: " + binaryArrayToString(decryptedArray));
            } else {
                outputArea.setText("无效输入或密钥，需为8位和10位二进制数");
            }
        });

        // 切换模式按钮事件
        switchButton.addActionListener(e -> {
            new GUI().setVisible(true); // 返回主界面
            dispose(); // 关闭当前窗口
        });
    }
    // 将字符串形式的输入转换为二进制数组
    private int[] stringToBinaryArray(String input) {
        int[] binaryArray = new int[8];
        for (int i = 0; i < 8; i++) {
            binaryArray[i] = (input.charAt(i) == '1') ? 1 : 0;
        }
        return binaryArray;
    }

    // 将二进制数组转换为字符串
    private String binaryArrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int bit : array) {
            sb.append(bit);
        }
        return sb.toString();
    }

    // 验证密钥是否是10位二进制
    private boolean validateKey(String keyText) {
        return keyText.matches("[01]{10}");
    }

    // 将字符串形式的二进制密钥转换为整数数组
    private int[] parseKey(String keyText) {
        int[] key = new int[10];
        for (int i = 0; i < 10; i++) {
            key[i] = keyText.charAt(i) - '0';
        }
        return key;
    }
}


// 扩展模式界面
class ExtendedModeGUI extends JFrame {
    private JTextField inputField;
    private JTextField keyField;
    private JTextArea outputArea;
    private S_DES sdes;

    public ExtendedModeGUI(S_DES sdes) {
        this.sdes = sdes;
        setTitle("扩展模式加密/解密");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建组件
        JLabel inputLabel = new JLabel("请输入(明文/密文):");
        inputField = new JTextField(20);
        JLabel keyLabel = new JLabel("密钥 (10位二进制):");
        keyField = new JTextField(10);
        JButton encryptButton = new JButton("加密");
        JButton decryptButton = new JButton("解密");
        JButton switchButton = new JButton("切换模式"); // 切换模式按钮

        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);

        // 设置布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1)); // 增加一行用于切换按钮
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(keyLabel);
        panel.add(keyField);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(switchButton); // 添加切换按钮

        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(panel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // 加密按钮事件
        encryptButton.addActionListener(e -> {
            String inputText = inputField.getText();
            String keyText = keyField.getText();

            if (validateKey(keyText)) {
                int[] key = parseKey(keyText);
                sdes.setKey(key);
                String encryptedText = sdes.Encrypt(inputText);
                outputArea.setText("密文: " + encryptedText);
            } else {
                outputArea.setText("无效密钥，需为10位二进制数");
            }
        });

        // 解密按钮事件
        decryptButton.addActionListener(e -> {
            String inputText = inputField.getText();
            String keyText = keyField.getText();

            if (validateKey(keyText)) {
                int[] key = parseKey(keyText);
                sdes.setKey(key);
                String decryptedText = sdes.Decrypt(inputText);
                outputArea.setText("明文: " + decryptedText);
            } else {
                outputArea.setText("无效密钥，需为10位二进制数");
            }
        });

        // 切换模式按钮事件
        switchButton.addActionListener(e -> {
            new GUI().setVisible(true); // 返回主界面
            dispose(); // 关闭当前窗口
        });
    }

    // 验证密钥是否是10位二进制
    private boolean validateKey(String keyText) {
        return keyText.matches("[01]{10}");
    }

    // 将字符串形式的二进制密钥转换为整数数组
    private int[] parseKey(String keyText) {
        int[] key = new int[10];
        for (int i = 0; i < 10; i++) {
            key[i] = keyText.charAt(i) - '0';
        }
        return key;
    }
}
