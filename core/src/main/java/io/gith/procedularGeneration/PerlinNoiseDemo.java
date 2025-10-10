package io.gith.procedularGeneration;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PerlinNoiseDemo {

    static class NoisePanel extends JPanel {
        private BufferedImage image;
        private int seed;

        private int width;
        private int height;

        public NoisePanel(int width, int height) {
            this.width = width;
            this.height = height;
            this.seed = new Random().nextInt();
            generateImage();
        }

        public void generateImage() {
            float scale = 0.015f;
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            long start = System.currentTimeMillis();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double noise = 0;
                    for (int k = 0; k < 1; k++)  noise = Noise.perlinNoise(x * scale, y * scale, 2, 0.5, 2.0, seed);
                    int gray = (int) (noise * 255);
                    int rgb = new Color(gray, gray, gray).getRGB();
                    image.setRGB(x, y, rgb);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end-start);
            repaint();
        }

        public void setSeed(int seed) {
            this.seed = seed;
            generateImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }
    }

    public static void main(String[] args) {
        int width = 200;
        int height = 200;

        JFrame frame = new JFrame("Perlin Noise Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width + 20, height + 80);
        frame.setLayout(new BorderLayout());

        NoisePanel panel = new NoisePanel(width, height);
        frame.add(panel, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Odśwież");
        refreshButton.addActionListener(e -> panel.setSeed(new Random().nextInt()));
        frame.add(refreshButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
