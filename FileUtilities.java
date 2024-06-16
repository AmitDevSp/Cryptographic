
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class FileUtilities {

    private FileUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String uploadFile(String path) {
        String str = "";
        File file = new File(path+ ".txt"); 

        Scanner scanner;
        try {
            scanner = new Scanner(file);
            str = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.print("Failed to load file");
            System.out.print(e);
        }	
        return str;
    }
    
}