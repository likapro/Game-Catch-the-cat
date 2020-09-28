package catch_the_cat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame{

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image cat;
    private static float cat_left = 200;
    private static float cat_top  = -200;
    private static float cat_v = 150;
    private static int score = 0;

    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.jpg"));
        cat = ImageIO.read(GameWindow.class.getResourceAsStream("9.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // при закрытие окна, программа завершится
        game_window.setLocation(200, 100); // где начинается окно
        game_window.setSize(1085,737); // размеры окна
        game_window.setResizable(false); // запрещает менять размеры окна
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float cat_right = cat_left + cat.getWidth(null);
                float cat_bottom = cat_top + cat.getHeight(null);
                boolean is_cat = x >= cat_left && x <= cat_right && y >= cat_top && y <= cat_bottom;
                if(is_cat){
                    cat_top = -200;
                    cat_left = (int) (Math.random() * (game_field.getWidth() - cat.getWidth(null)));
                    cat_v += 50;
                    score++;
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

        cat_top = cat_top + cat_v * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(cat, (int) cat_left,(int) cat_top,null);
        if(cat_top > game_window.getHeight()) g.drawImage(game_over, -70, 0, null);
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