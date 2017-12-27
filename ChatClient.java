import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
 
public class ChatClient extends JFrame implements Runnable{
 
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;
    Scanner sockIn;
    PrintWriter sockOut;
     
    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        if(args.length==0)
        {
            System.out.println("Name is mandatory: java ChatClient avatar_name");
            System.exit(1);
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatClient client = new ChatClient(args[0]);
                    client.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    /**
     * Create the frame.
     */
    public ChatClient(final String avatar) {
        Socket socket=null;
        try {
            socket=new Socket("localhost", 4444);
            sockIn = new Scanner(socket.getInputStream());
            sockOut = new PrintWriter(socket.getOutputStream());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        this.setTitle(avatar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 200, 450, 314);
        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setOpaque(true);
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.BLUE); 
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(13, 12, 421, 235);
        contentPane.add(textArea);
         
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sockOut.println(avatar+": "+textField.getText());
                sockOut.flush();
                textField.setText("");
            }
        });
        textField.setBounds(9, 252, 430, 0);
        contentPane.add(textField);
        textField.setColumns(10);
         
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(13, 12, 421, 235);
        contentPane.add(scrollPane);
        new Thread(this).start();
    }
 
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true)
            while(sockIn.hasNextLine())
                textArea.setText(textArea.getText()+"\n"+sockIn.nextLine());
    }
}
