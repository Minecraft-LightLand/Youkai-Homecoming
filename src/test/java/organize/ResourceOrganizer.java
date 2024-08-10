package organize;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ResourceOrganizer {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static final Map<String, ResourceOrganizer> MAP = new LinkedHashMap<>();
    public static String MODID;
    public final Type type;
    public final String folder;
    public final String target;

    public ResourceOrganizer(Type type, String folder, String target) {
        this.type = type;
        this.folder = folder;
        this.target = target;
        MAP.put(folder, this);
    }

    public static void main(String[] args) throws Exception {
        new LangFileOrganizer();
        //new ItemFileOrganizer();
        //new BlockFileOrganizer();
        //new ArmorFileOrganizer();
        //new RecipeFileOrganizer();
        //new AssetMisc();
        //new DataMisc();
        //new ConfigFileOrganizer();
        //new GeckoMisc();
        File f = new File("./src/test/resources");
        for (File fi : f.listFiles()) {
            MODID = fi.getName();
            //delete(new File("./src/main/resources/assets/l2artifacts/"));
            if (!fi.isDirectory())
                continue;
            for (ResourceOrganizer obj : MAP.values()) {
                File fo = new File(fi.getPath() + "/" + obj.folder);
                if (!fo.exists())
                    continue;
                obj.organize(fo);
            }
        }
    }

    public static void delete(File f) throws Exception {
        if (f.exists()) {
            if (f.isDirectory())
                for (File fi : f.listFiles())
                    delete(fi);
            f.delete();
        }
    }

    public static void check(File f) throws Exception {
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        f.createNewFile();
    }

    public abstract void organize(File f) throws Exception;

    public final String getTargetFolder() {
        return getResourceFolder(true) + type + "/" + MODID + "/" + target;
    }

    public final String getResourceFolder(boolean main) {
        return (main ? "./src/main/resources/" : "./src/test/resources/");
    }

    protected String readFile(String path) {
        List<String> list = null;
        try {
            list = Files.readLines(new File(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String str = "";
        for (String s : list)
            str += s + "\n";
        return str.replaceAll("\\^m", MODID);
    }

    protected void write(String name, String cont) throws Exception {
        File f = new File(name);
        check(f);
        PrintStream ps = new PrintStream(f, StandardCharsets.UTF_8);
        ps.println(cont);
        ps.close();
    }

    public enum Type {
        ASSETS("assets"), DATA("data");

        public final String side;

        Type(String side) {
            this.side = side;
        }

        public String toString() {
            return side;
        }
    }

}
