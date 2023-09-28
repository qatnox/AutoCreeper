package Main;

import Additional.RoundedBorder;
import Interfaces.ACEvents;
import Interfaces.UI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AutoClicker extends JFrame implements ACEvents, UI {

    private static JLabel status;
    private static JTextArea logger;
    private static JLabel notify;
    private static JScrollPane loggerScroll;
    private static volatile JButton start;
    private static volatile JButton stop;
    private static JLabel labelSpeed;
    private static JTextField speedClick;
    private static final Border blackline = BorderFactory.createLineBorder(Color.black);
    private static volatile Integer speed = 0;
    private static ThClick threadClicker;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    static final Object lock = new Object();
    private final float version = 1.1f;
    @Override
    public void start(ActionEvent e) {
        threadClicker = new ThClick();

        try {
            speed = (int) (Double.parseDouble(speedClick.getText()) * 1000);
        } catch (NumberFormatException nme){
            Date dateClick = new Date();
            logger.append("["+ formatter.format(dateClick) + "] " + "Incorrect speed is entered!\n");
        }

        if(speed == 0){
            speedClick.setText("5");

            Date dateDefault = new Date();
            logger.append("["+ formatter.format(dateDefault) + "] " +
                    "Default speed set to 5 seconds!\n" +
                    "======================================\n");
        }

        if (speed != null) {
            if (speed > 0) {
                threadClicker.start();
                Date dateSpeedEnt = new Date();
                logger.append("["+ formatter.format(dateSpeedEnt) + "] " + "The speed set to " + Double.parseDouble(speedClick.getText()) + " seconds" + "\n");
                if (!threadClicker.isInterrupted()) {
                    Date dateStart = new Date();
                    logger.append("[" + formatter.format(dateStart) + "] " + "Started working\n");
                }
            }
        }
    }
    @Override
    public void stop(ActionEvent e) {
        try {
            threadClicker.interrupt();
        } catch (Exception ex) {
            logger.append("AutoCreeper is already stopped!\n");
        }

        status.setText("AutoCreeper IS STOPPED!");
        start.setEnabled(true);
        stop.setEnabled(false);
    }

    public static class ThClick extends Thread {

        @Override
        public void run() {

            stop.setEnabled(true);
            start.setEnabled(false);
            status.setText("AutoCreeper IS ACTIVE!");
            status.setForeground(Color.green);
            Robot robot;

            try {
                robot = new Robot();
            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }
            while (!Thread.currentThread().isInterrupted()) {
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                try {
                    synchronized (lock) {
                        lock.wait(speed);
                    }
                } catch (InterruptedException ex) {
                    Date date = new Date();
                    logger.append("["+ formatter.format(date) + "] " + "Stopped working" +
                            "\n" +
                            "======================================\n");
                    Thread.currentThread().interrupt();
                }
            }
            status.setText("AutoCreeper IS STOPPED!");
            status.setForeground(Color.red);
        }
    }

    public void createStatus(){
        status = new JLabel("AutoCreeper IS STOPPED!");
        status.setHorizontalTextPosition(SwingConstants.CENTER);
        status.setVerticalTextPosition(SwingConstants.CENTER);
        status.setFont(new Font("Century Gothic", Font.BOLD, 20));
        status.setBounds(50, 115, 250, 50);
    }

    public void createLabelSpeed() {
        labelSpeed = new JLabel("Enter the speed of clicks (in sec)");
        labelSpeed.setBounds(50, 160, 250, 70);

        labelSpeed.setHorizontalTextPosition(SwingConstants.CENTER);
        labelSpeed.setVerticalTextPosition(SwingConstants.CENTER);
        labelSpeed.setFont(new Font("Century Gothic", Font.PLAIN, 15));
    }

    public void createNotify(){
        notify = new JLabel("When changing speed - restart (STOP/START)!");
        notify.setFont(new Font("Century Gothic", Font.BOLD, 12));
        notify.setBounds(30, 205, 270, 60);
    }

    public void createStartButton(){
        start = new JButton("START");
        start.setBounds(30, 250, 120, 40);
        start.setFont(new Font("Century Gothic", Font.BOLD, 15));
        start.setFocusable(false);

        start.setBorder(new RoundedBorder(10));
        start.setBorderPainted(true);
        start.setContentAreaFilled(false);

        start.addActionListener(this::start);
    }

    public void createStopButton(){
        stop = new JButton("STOP");
        stop.setBounds(180, 250, 120, 40);
        stop.setFont(new Font("Century Gothic", Font.BOLD, 15));
        stop.setFocusable(false);

        stop.setBorder(new RoundedBorder(10));
        stop.setBorderPainted(true);
        stop.setContentAreaFilled(false);

        stop.addActionListener(this::stop);
    }

    public void createSpeedClick(){
        speedClick = new JTextField();
        speedClick.setBounds(130, 165, 70, 20);
        speedClick.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        speedClick.setBorder(blackline);
    }

    public void createLogger(){
        logger = new JTextArea();
        logger.append("- - - - - - - - - - [AutoCreeper] - v. " + version + " - - - - - - - - - -\n");
        logger.setEditable(false);
        logger.setFont(new Font("Century Gothic", Font.PLAIN, 12));
    }

    public void createLoggerScroll(){
        loggerScroll = new JScrollPane(logger);
        loggerScroll.setBounds(30, 5, 270, 120);
        loggerScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(loggerScroll);
    }

    @Override
    public void addComponents(){
        add(notify);
        add(loggerScroll);
        add(labelSpeed);
        add(speedClick);
        add(status);
        add(start);
        add(stop);
    }

    @Override
    public void setColors(){
        speedClick.setBackground(Color.white);
        logger.setBackground(Color.darkGray);

        labelSpeed.setForeground(Color.white);
        status.setForeground(Color.white);
        logger.setForeground(Color.white);
        notify.setForeground(Color.red);
        start.setForeground(Color.white);
        stop.setForeground(Color.white);

        getContentPane().setBackground(Color.darkGray);
        getContentPane().setBackground(Color.darkGray);
    }

    @Override
    public void configGUI(){
        createStatus();
        createLabelSpeed();
        createNotify();
        createStartButton();
        createStopButton();
        createSpeedClick();
        createLogger();
        createLoggerScroll();
    }

    @Override
    public void initMainGUI(){

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 350);
        setResizable(false);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("AutoCreeper");
        ImageIcon ico = new ImageIcon("D:/JetBrains/IntelliJ_prj/AutoCreeper/ac_ico.png");
        setIconImage(ico.getImage());
    }

}
