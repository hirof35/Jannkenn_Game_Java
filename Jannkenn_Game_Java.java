package ultimateJankenGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class UltimateJankenGame extends JFrame implements ActionListener {
    private JLabel infoLabel, scoreLabel, playerImg, cpuImg;
    private JButton[] buttons = new JButton[3];
    private String[] hands = {"グー", "チョキ", "パー"};
    private ImageIcon[] icons = new ImageIcon[3]; // 小さくした画像用
    
    private int winCount = 0, loseCount = 0, drawCount = 0;
    private final int WIN_GOAL = 5;
    
    private Timer shuffleTimer;
    private int shuffleTicks = 0;
    private int selectedHand;
    private Random random = new Random();
    private Clip currentClip;

    public UltimateJankenGame() {
        setupWindow();
        loadResources();
        initUI();
        setupTimer();
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("Java Janken Ultimate Edition");
        setSize(450, 450); // ウィンドウサイズを少しコンパクトに
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
    }

    private void loadResources() {
        for (int i = 0; i < 3; i++) {
            try {
                ImageIcon icon = new ImageIcon(hands[i] + ".png");
                // ★ 改良点: 画像サイズを 80x80 に縮小
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icons[i] = new ImageIcon(img);
            } catch (Exception e) {
                System.out.println("画像読み込み失敗: " + hands[i]);
            }
        }
    }

    private void initUI() {
        // スコアとメッセージ
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        scoreLabel = new JLabel("READY?", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel = new JLabel("先に5勝で優勝！", JLabel.CENTER);
        infoLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
        topPanel.add(scoreLabel);
        topPanel.add(infoLabel);
        add(topPanel, BorderLayout.NORTH);

        // 対戦エリア
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 周囲に余白を追加

        playerImg = new JLabel("YOU", JLabel.CENTER);
        cpuImg = new JLabel("CPU", JLabel.CENTER);
        
        // テキストを画像の下に配置
        playerImg.setVerticalTextPosition(JLabel.BOTTOM);
        playerImg.setHorizontalTextPosition(JLabel.CENTER);
        cpuImg.setVerticalTextPosition(JLabel.BOTTOM);
        cpuImg.setHorizontalTextPosition(JLabel.CENTER);
        
        // フォントサイズ調整
        playerImg.setFont(new Font("Arial", Font.PLAIN, 12));
        cpuImg.setFont(new Font("Arial", Font.PLAIN, 12));

        centerPanel.add(playerImg);
        centerPanel.add(cpuImg);
        add(centerPanel, BorderLayout.CENTER);

        // 操作ボタン
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            buttons[i] = new JButton(hands[i]);
            if (icons[i] != null) {
                buttons[i].setIcon(icons[i]); // 小さいアイコンをセット
            }
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(this);
            // ボタン自体も少しスリムに
            buttons[i].setMargin(new Insets(5, 5, 5, 5));
            btnPanel.add(buttons[i]);
        }
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void setupTimer() {
        shuffleTimer = new Timer(80, e -> {
            int tmp = random.nextInt(3);
            if (icons[tmp] != null) cpuImg.setIcon(icons[tmp]);
            cpuImg.setText("考え中...");
            shuffleTicks++;
            if (shuffleTicks >= 12) finishTurn();
        });
    }

    private void playSound(String file) {
        stopSound();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
            currentClip = AudioSystem.getClip();
            currentClip.open(ais);
            currentClip.start();
        } catch (Exception e) {}
    }

    private void stopSound() {
        if (currentClip != null) {
            if (currentClip.isRunning()) currentClip.stop();
            currentClip.close();
            currentClip = null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        playSound("click.wav");
        selectedHand = Integer.parseInt(e.getActionCommand());
        playerImg.setIcon(icons[selectedHand]);
        playerImg.setText("あなた: " + hands[selectedHand]);
        
        for (JButton b : buttons) b.setEnabled(false);
        shuffleTicks = 0;
        shuffleTimer.start();
    }

    private void finishTurn() {
        shuffleTimer.stop();
        int cpuHand = random.nextInt(3);
        cpuImg.setIcon(icons[cpuHand]);
        cpuImg.setText("CPU: " + hands[cpuHand]);

        int res = (selectedHand - cpuHand + 3) % 3;
        if (res == 0) {
            infoLabel.setText("あいこ！");
            getContentPane().setBackground(Color.YELLOW);
            drawCount++;
        } else if (res == 2) {
            winCount++;
            infoLabel.setText("勝ち！");
            getContentPane().setBackground(Color.PINK);
            playSound("win.wav");
        } else {
            loseCount++;
            infoLabel.setText("負け...");
            getContentPane().setBackground(new Color(135, 206, 250));
            playSound("lose.wav");
        }
        
        scoreLabel.setText(String.format("【 %d 勝 - %d 敗 - %d 分 】", winCount, loseCount, drawCount));
        
        if (winCount >= WIN_GOAL || loseCount >= WIN_GOAL) {
            showResult();
        } else {
            for (JButton b : buttons) b.setEnabled(true);
        }
    }

    private void showResult() {
        stopSound();
        String msg = (winCount >= WIN_GOAL) ? "完全優勝！おめでとう！" : "CPUの完全優勝...";
        JOptionPane.showMessageDialog(this, msg);
        
        winCount = 0; loseCount = 0; drawCount = 0;
        scoreLabel.setText("READY?");
        infoLabel.setText("先に5勝で優勝！");
        getContentPane().setBackground(Color.WHITE);
        playerImg.setIcon(null);
        playerImg.setText("YOU");
        cpuImg.setIcon(null);
        cpuImg.setText("CPU");
        for (JButton b : buttons) b.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UltimateJankenGame());
    }
}
