
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class FileUtilities {

    private FileUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String uploadFile(String path) {
        StringBuilder str = new StringBuilder();
        File file = new File(path);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                str.append(scanner.nextLine()).append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Failed to load file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return str.toString().trim();
    }
    
}