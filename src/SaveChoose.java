import javax.swing.*;
import java.awt.*;

public class SaveChoose extends JFrame {
    JFileChooser fileChooser;

    public SaveChoose() {
        super("Сохранить файл");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        UIManager.put(
                "FileChooser.saveButtonText", "Сохранить");
        UIManager.put(
                "FileChooser.cancelButtonText", "Отмена");
        UIManager.put(
                "FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put(
                "FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put(
                "FileChooser.lookInLabelText", "Директория");
        UIManager.put(
                "FileChooser.saveInLabelText", "Сохранить в директории");
        UIManager.put(
                "FileChooser.folderNameLabelText", "Путь директории");
    }
}
