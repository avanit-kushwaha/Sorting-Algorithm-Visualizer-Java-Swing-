import javax.swing.*;
import java.awt.*;
import java.util.Random;

class SortingVisualizer extends JPanel {
    private int[] array;
    private static final int ARRAY_SIZE = 50;
    public int delay = 50;
    private JComboBox<String> algorithmSelector;
    private JSlider speedSlider;

    public SortingVisualizer() {
        array = new int[ARRAY_SIZE];
        generateArray();
    }

    public void generateArray() {
        Random rand = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = rand.nextInt(400) + 10;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth() / ARRAY_SIZE;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            g.setColor(Color.BLUE);
            g.fillRect(i * width, getHeight() - array[i], width, array[i]);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bubbleSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                    for (int j = 0; j < ARRAY_SIZE - i - 1; j++) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            repaint();
                            sleep();
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void selectionSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < ARRAY_SIZE - 1; i++) {
                    int minIdx = i;
                    for (int j = i + 1; j < ARRAY_SIZE; j++) {
                        if (array[j] < array[minIdx]) {
                            minIdx = j;
                        }
                    }
                    int temp = array[minIdx];
                    array[minIdx] = array[i];
                    array[i] = temp;
                    repaint();
                    sleep();
                }
            }
        });
        thread.start();
    }

    public void insertionSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i < ARRAY_SIZE; i++) {
                    int key = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];
                        j--;
                        repaint();
                        sleep();
                    }
                    array[j + 1] = key;
                    repaint();
                    sleep();
                }
            }
        });
        thread.start();
    }

    public void mergeSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                mergeSortHelper(0, ARRAY_SIZE - 1);
            }
        });
        thread.start();
    }

    private void mergeSortHelper(int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(left, mid);
            mergeSortHelper(mid + 1, right);
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= right) {
            temp[k++] = array[j++];
        }

        for (int m = 0; m < temp.length; m++) {
            array[left + m] = temp[m];
            repaint();
            sleep();
        }
    }

    public void quickSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                quickSortHelper(0, ARRAY_SIZE - 1);
            }
        });
        thread.start();
    }

    private void quickSortHelper(int low, int high) {
        if (low < high) {
            int pivot = partition(low, high);
            quickSortHelper(low, pivot - 1);
            quickSortHelper(pivot + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                repaint();
                sleep();
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        repaint();
        sleep();
        return i + 1;
    }

    public void countingSort() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int max = array[0];
                for (int i = 1; i < ARRAY_SIZE; i++) {
                    if (array[i] > max) {
                        max = array[i];
                    }
                }

                int[] count = new int[max + 1];
                for (int i = 0; i < ARRAY_SIZE; i++) {
                    count[array[i]]++;
                }

                int index = 0;
                for (int i = 0; i <= max; i++) {
                    while (count[i] > 0) {
                        array[index++] = i;
                        repaint();
                        sleep();
                        count[i]--;
                    }
                }
            }
        });
        thread.start();
    }

    public void setSortingAlgorithm(String algorithm) {
        if (algorithm.equals("Bubble Sort")) {
            bubbleSort();
        } else if (algorithm.equals("Selection Sort")) {
            selectionSort();
        } else if (algorithm.equals("Insertion Sort")) {
            insertionSort();
        } else if (algorithm.equals("Merge Sort")) {
            mergeSort();
        } else if (algorithm.equals("Quick Sort")) {
            quickSort();
        } else if (algorithm.equals("Counting Sort")) {
            countingSort();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sorting Visualizer");
        final SortingVisualizer panel = new SortingVisualizer();
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        String[] algorithms = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort", "Counting Sort"};
        final JComboBox<String> algorithmSelector = new JComboBox<>(algorithms);
        final JSlider speedSlider = new JSlider(10, 200, 50);
        JButton startButton = new JButton("Start Sorting");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.setSortingAlgorithm((String) algorithmSelector.getSelectedItem());
            }
        });

        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.generateArray();
            }
        });

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                panel.delay = speedSlider.getValue();
            }
        });

        controlPanel.add(new JLabel("Algorithm:"));
        controlPanel.add(algorithmSelector);
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedSlider);
        controlPanel.add(startButton);
        controlPanel.add(resetButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
