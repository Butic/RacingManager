package racingmanager;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Класс для сохранения и загрузки данных приложения из файлов.
 */
public class FileManager {
	
	 /**
     * Сохраняет данные таблицы гонщиков в CSV-файл.
     *
     * @param parent главное окно приложения
     * @param model модель таблицы гонщиков
     */
    public void save(JFrame parent, DefaultTableModel model) {

        // Создание диалогового окна выбора файла для сохранения.
        FileDialog saveDialog = new FileDialog(
            parent,
            "Сохранение данных",
            FileDialog.SAVE
        );

        // Предлагаем имя файла по умолчанию.
        saveDialog.setFile("racers.csv");

        // Показываем окно выбора файла.
        saveDialog.setVisible(true);

        // Если пользователь нажал "Отмена", прекращаем выполнение метода.
        if (saveDialog.getFile() == null) {
            return;
        }

        // Формируем полный путь к выбранному файлу.
        String fileName =
            saveDialog.getDirectory() + saveDialog.getFile();

        try {

            // Создаем буферизованный поток записи в файл.
            BufferedWriter writer =
                new BufferedWriter(
                    new FileWriter(fileName)
                );

            // Проходим по всем строкам таблицы.
            for (int i = 0; i < model.getRowCount(); i++) {

                String racer =
                    model.getValueAt(i, 0).toString();

                String team =
                    model.getValueAt(i, 1).toString();

                String points =
                    model.getValueAt(i, 2).toString();

                // Записываем одну строку CSV.
                writer.write(
                    racer + ";" + team + ";" + points
                );

                // Переходим на следующую строку файла.
                writer.newLine();
            }

            // Закрываем поток после завершения записи.
            writer.close();

            JOptionPane.showMessageDialog(
                parent,
                "Данные успешно сохранены"
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                parent,
                "Ошибка при сохранении файла"
            );

            ex.printStackTrace();
        }
    }
    
    /**
     * Загружает данные гонщиков из CSV-файла в таблицу.
     *
     * @param parent главное окно приложения
     * @param model модель таблицы гонщиков
     */
    public void load(JFrame parent, DefaultTableModel model) {

        // Создание диалогового окна выбора файла для загрузки.
        FileDialog loadDialog = new FileDialog(
            parent,
            "Загрузка данных",
            FileDialog.LOAD
        );

        // Показываем только CSV-файлы.
        loadDialog.setFile("*.csv");

        // Показываем окно выбора файла.
        loadDialog.setVisible(true);

        // Если пользователь нажал "Отмена", прекращаем выполнение метода.
        if (loadDialog.getFile() == null) {
            return;
        }

        // Формируем полный путь к выбранному файлу.
        String fileName =
            loadDialog.getDirectory() + loadDialog.getFile();

        try {

            // Создаем поток чтения из файла.
            BufferedReader reader =
                new BufferedReader(
                    new FileReader(fileName)
                );

            // Удаляем старые данные из таблицы.
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }

            String line;

            // Читаем файл построчно до его окончания.
            while ((line = reader.readLine()) != null) {

                // Разделяем строку CSV по символу ";".
                String[] data = line.split(";");

                // Проверяем, что в строке находятся три значения.
                if (data.length == 3) {

                    String racer = data[0];
                    String team = data[1];
                    String points = data[2];

                    // Добавляем считанную строку в таблицу.
                    model.addRow(new Object[] {
                        racer,
                        team,
                        points
                    });
                }
            }

            // Закрываем поток чтения.
            reader.close();

            JOptionPane.showMessageDialog(
                parent,
                "Данные успешно загружены"
            );

        } catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(
                parent,
                "Файл не найден"
            );

            ex.printStackTrace();

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                parent,
                "Ошибка при загрузке файла"
            );

            ex.printStackTrace();
        }
    }
}

