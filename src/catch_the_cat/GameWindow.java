package catch_the_cat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameWindow extends JFrame{

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image cat;
    private static Image cat2;
    private static Image cat3;
    private static Image cat7;
    private static Image cat8;
    private static Image catRandom;
    private static float cat_left = 200;
    private static float cat_top  = -200;
    private static float cat_v = 150;
    private static int score = 0;
    private static Image restart;
    private static boolean is_restart = false;
    private static int restart_left = 850; // кнопка рестарта
    private static int restart_top  = 450;
    private static boolean is_lose = false; // флаг поражения

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.jpg"));
        cat = ImageIO.read(GameWindow.class.getResourceAsStream("9.png"));
        cat2 = ImageIO.read(GameWindow.class.getResourceAsStream("2.png"));
        cat3 = ImageIO.read(GameWindow.class.getResourceAsStream("3.png"));
        cat7 = ImageIO.read(GameWindow.class.getResourceAsStream("7.png"));
        cat8 = ImageIO.read(GameWindow.class.getResourceAsStream("8.png"));
        restart = ImageIO.read(GameWindow.class.getResourceAsStream("restart.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // при закрытие окна, программа завершится
        game_window.setLocation(200, 100); // где начинается окно
        game_window.setSize(1085,737); // размеры окна
        game_window.setResizable(false); // запрещает менять размеры окна
        last_frame_time = System.nanoTime();
        ArrayList<Image> ourCats = new ArrayList<Image>();
        ourCats.add(cat);
        ourCats.add(cat2);
        ourCats.add(cat3);
        ourCats.add(cat7);
        ourCats.add(cat8);
        catRandom = ourCats.get(0);
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float cat_right = cat_left + cat.getWidth(null);
                float cat_bottom = cat_top + cat.getHeight(null);
                float restart_right = restart_left + restart.getWidth(null);
                float restart_bottom = restart_top + restart.getHeight(null);
                boolean is_cat = x >= cat_left && x <= cat_right && y >= cat_top && y <= cat_bottom;
                boolean is_restart = x >= restart_left && x <= restart_right && y >= restart_top && y <= restart_bottom;
                if(is_cat){
                    cat_top = -200;
                    cat_left = (int) (Math.random() * (game_field.getWidth() - cat.getWidth(null)));
                    cat_v += 50;
                    score++;
                    game_window.setTitle("Score: "+score);
//                    Random rand = new Random();                         // объект класса рандом, чтобы подставлять в значение рандоминдекс,по которому будем получать объект из списка
//                    int randomIndex = rand.nextInt(ourCats.size());     // хотя можно было через Math.random()
//                    catRandom = ourCats.get(randomIndex);               // получаем случайного кота
                    catRandom = ourCats.get((int) (Math.random() * ourCats.size())); // получаем случайный cat
                }
                if(is_restart && is_lose){  // условие прожатия кнопки рестарта и флага поражения
                    cat_top = -200;
                    cat_left = (int) (Math.random() * (game_field.getWidth() - cat.getWidth(null)));
                    cat_v = 150;
                    score = 0;
                    game_window.setTitle("Score: "+score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true); // показывает окно, потому что по умолчанию оно скрыто
    }

    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        is_lose = false;
        cat_top = cat_top + cat_v * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(catRandom, (int) cat_left,(int) cat_top,null);
        if(cat_top > game_window.getHeight()) {
            g.drawImage(game_over, -70, 0, null);
            is_lose = true;
            g.drawImage(restart,restart_left,restart_top, null);
        }
    }

    private static class GameField extends JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
