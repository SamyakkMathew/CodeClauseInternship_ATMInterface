import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ATMGUI extends JFrame {
    private HashMap<Integer, Data> map;
    private JTextArea outputArea;
    private JPanel accountOperationsPanel;

    public ATMGUI() {
        map = new HashMap<>();
        setTitle("ATM GUI");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = createLoginPanel();
        JPanel createAccountPanel = createAccountPanel();
        accountOperationsPanel = createAccountOperationsPanel();
        accountOperationsPanel.setVisible(false); // Initially hide the operations panel

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Create Account", createAccountPanel);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        mainPanel.add(accountOperationsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JTextField pinField = new JTextField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Enter PIN:"));
        panel.add(pinField);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pin = Integer.parseInt(pinField.getText());
                op(pin);
            }
        });

        return panel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JTextField pinField = new JTextField();
        JButton createAccountButton = new JButton("Create Account");

        panel.add(new JLabel("Enter a PIN (between 1000 and 9999):"));
        panel.add(pinField);
        panel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pinString = pinField.getText();
                try {
                    int pin = Integer.parseInt(pinString);
                    if (pin >= 1000 && pin <= 9999) {
                        op(pin);
                    } else {
                        outputArea.setText("Invalid PIN. Please enter a PIN between 1000 and 9999.");
                    }
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid PIN. Please enter a valid number.");
                }
            }
        });

        return panel;
    }

    private JPanel createAccountOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton checkBalanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit Money");
        JButton withdrawButton = new JButton("Withdraw Money");
        JButton exitButton = new JButton("Exit");

        panel.add(checkBalanceButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(exitButton);

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pin = Integer.parseInt(JOptionPane.showInputDialog("Enter PIN:"));
                checkBalance(pin);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pin = Integer.parseInt(JOptionPane.showInputDialog("Enter PIN:"));
                deposit(pin);
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pin = Integer.parseInt(JOptionPane.showInputDialog("Enter PIN:"));
                withdraw(pin);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("Exit operation selected.");
            }
        });

        return panel;
    }

    private void op(int pin) {
        if (pin >= 1000 && pin <= 9999) {
            if (map.containsKey(pin)) {
                outputArea.setText("Logging in with PIN: " + pin);
                accountOperationsPanel.setVisible(true); // Show account operations panel
            } else {
                Data account = new Data();
                account.balance = 0;
                map.put(pin, account);
                outputArea.setText("Account created with PIN: " + pin);
                accountOperationsPanel.setVisible(true); // Show account operations panel
            }
        } else {
            outputArea.setText("Invalid PIN. Please enter a PIN between 1000 and 9999.");
        }
    }

    private void checkBalance(int pin) {
        if (map.containsKey(pin)) {
            Data account = map.get(pin);
            outputArea.setText("Your current balance is: " + account.balance);
        } else {
            outputArea.setText("Invalid PIN. Please create an account with this PIN.");
        }
    }

    private void deposit(int pin) {
        if (map.containsKey(pin)) {
            Data account = map.get(pin);
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to deposit:"));
            account.balance += amount;
            outputArea.setText("Deposit successful. New balance: " + account.balance);
        } else {
            outputArea.setText("Invalid PIN. Please create an account with this PIN.");
        }
    }

    private void withdraw(int pin) {
        if (map.containsKey(pin)) {
            Data account = map.get(pin);
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to withdraw:"));
            if (amount > account.balance) {
                outputArea.setText("Insufficient balance.");
            } else {
                account.balance -= amount;
                outputArea.setText("Withdrawal successful. New balance: " + account.balance);
            }
        } else {
            outputArea.setText("Invalid PIN. Please create an account with this PIN.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ATMGUI gui = new ATMGUI();
                gui.setVisible(true);
            }
        });
    }

    class Data {
        double balance;
    }
}
