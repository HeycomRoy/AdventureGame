package network.testSystem;


import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {

    private TestPanel tp;
    private TestGame level = new TestGame();
    private TestSlave slave;

    public TestFrame(TestGame l, TestSlave s) {
        super("Test game");
        setLayout(new BorderLayout());
        slave = s;
        addKeyListener(slave);
        tp = new TestPanel();
        add(tp);
        tp.reFresh(level.toCharArray());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setSize(500, 500);
    }

    public void reFresh(char[] c) {
        tp.reFresh(c);
    }

    public class TestPanel extends JPanel {

        private JLabel out[][] = new JLabel[TestGame.SIZE][TestGame.SIZE];

        public TestPanel() {
            setLayout(new GridLayout(TestGame.SIZE, TestGame.SIZE, 1, 1));
            for (int i = 0; i < TestGame.SIZE; ++ i) {
                for (int j = 0; j < TestGame.SIZE; ++ j) {
                    out[i][j] = new JLabel(Character.toString(TestGame.TILE));
                    add(out[i][j]);
                }
            }
        }

        public void reFresh(char[] in) {
            int index = 0;
            for (int i = 0; i < TestGame.SIZE; ++ i) {
                for (int j = 0; j < TestGame.SIZE; ++ j) {
                    out[i][j].setText(Character.toString(in[index ++]));
                }
            }
        }
    }
}
