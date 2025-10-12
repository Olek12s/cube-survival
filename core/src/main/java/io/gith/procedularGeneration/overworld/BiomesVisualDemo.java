package io.gith.procedularGeneration.overworld;

import io.gith.procedularGeneration.BiomeName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class BiomesVisualDemo {

    static class BiomePanel extends JPanel {
        private BufferedImage image;
        private int width, height;
        private OverworldBiomeGenerator generator;
        private double worldOffsetX = 0;
        private double worldOffsetY = 0;
        private int seed;
        private static final int CHUNK_SIZE = 8;
        private static final double MOVE_STEP = CHUNK_SIZE * 64;

        private static final Map<BiomeName, Color> BIOME_COLORS = new EnumMap<>(BiomeName.class);

        static {
            BIOME_COLORS.put(BiomeName.Ocean, Color.BLUE);
            BIOME_COLORS.put(BiomeName.Coast, Color.ORANGE);
            BIOME_COLORS.put(BiomeName.Plains, new Color(0x00FF5E));
            BIOME_COLORS.put(BiomeName.Desert, Color.YELLOW);
            BIOME_COLORS.put(BiomeName.Forest, new Color(0x12A247));
            BIOME_COLORS.put(BiomeName.Taiga, new Color(0x799583));
            BIOME_COLORS.put(BiomeName.Highlands, Color.LIGHT_GRAY);
            BIOME_COLORS.put(BiomeName.Mountains, Color.BLACK);
        }

        public BiomePanel(int width, int height, int seed) {
            this.width = width;
            this.height = height;
            this.seed = seed;
            this.generator = new OverworldBiomeGenerator(seed);
            setPreferredSize(new Dimension(width + 200, height));
            setFocusable(true);
            generateImage();
            setupKeyListener();
        }

        private void setupKeyListener() {
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT -> worldOffsetX -= MOVE_STEP;
                        case KeyEvent.VK_RIGHT -> worldOffsetX += MOVE_STEP;
                        case KeyEvent.VK_UP -> worldOffsetY -= MOVE_STEP;
                        case KeyEvent.VK_DOWN -> worldOffsetY += MOVE_STEP;
                    }
                    generateImage();
                }
            });
        }

        public void setSeed(int newSeed) {
            this.seed = newSeed;
            this.generator = new OverworldBiomeGenerator(seed);
            generateImage();
        }

        private void generateImage() {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double worldX = x + worldOffsetX;
                    double worldY = y + worldOffsetY;
                    BiomeName biomeName = generator.generateBiome(worldX, worldY);
                    Color color = BIOME_COLORS.getOrDefault(biomeName, Color.MAGENTA);
                    image.setRGB(x, y, color.getRGB());
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) g.drawImage(image, 0, 0, null);

            int legendX = width + 20;
            int legendY = 20;
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.setColor(Color.BLACK);
            g.drawString("Legenda biomów:", legendX, legendY);
            legendY += 20;

            for (Map.Entry<BiomeName, Color> entry : BIOME_COLORS.entrySet()) {
                g.setColor(entry.getValue());
                g.fillOval(legendX, legendY, 12, 12);
                g.setColor(Color.BLACK);
                g.drawString(" - " + entry.getKey().name(), legendX + 15, legendY + 12);
                legendY += 20;
            }

            g.setColor(Color.BLACK);
            g.drawString("Offset: (%.0f, %.0f)".formatted(worldOffsetX, worldOffsetY), legendX, legendY + 10);
        }
    }

    public static void main(String[] args) {
        int width = 800;
        int height = 600;
        int initialSeed = new Random().nextInt();

        JFrame frame = new JFrame("Biome World Preview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        BiomePanel panel = new BiomePanel(width, height, initialSeed);
        frame.add(panel, BorderLayout.CENTER);


        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        JTextField seedField = new JTextField(String.valueOf(initialSeed), 10);
        JButton generateButton = new JButton("Generuj");
        JButton randomButton = new JButton("Generuj (losowy seed)");

        generateButton.addActionListener(e -> {
            try {
                int seed = Integer.parseInt(seedField.getText());
                panel.setSeed(seed);
                panel.requestFocusInWindow();
            } catch (NumberFormatException ignored) {}
        });

        randomButton.addActionListener(e -> {
            int seed = new Random().nextInt();
            seedField.setText(String.valueOf(seed));
            panel.setSeed(seed);
            panel.requestFocusInWindow();
        });

        controls.add(new JLabel("Seed:"));
        controls.add(seedField);
        controls.add(generateButton);
        controls.add(randomButton);

        frame.add(controls, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }
}
