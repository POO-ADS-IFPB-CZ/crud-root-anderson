import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDao<T> {
    private final String filename;

    public GenericDao(String filename) {
        this.filename = filename;
    }

    public void salvar(List<T> lista) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(lista);
        }
    }

    public List<T> listar() throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) in.readObject();
        }
    }
}
