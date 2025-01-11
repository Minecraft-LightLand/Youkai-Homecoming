package organize;

import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGenerator {

    public static void main(String[] args) throws Exception {
        new GUIGenerator("twilightdelight").gen();
    }

    private class Comp {

        private final String name;
        private final Item it;
        private final int x, y, rx, ry;

        private Comp(String str, JsonObject e) {
            name = str;
            it = ITEM_MAP.get(e.get("sprite").getAsString());
            x = e.get("x").getAsInt();
            y = e.get("y").getAsInt();
            rx = getInt(e, "rx", 1);
            ry = getInt(e, "ry", 1);
        }

        @Override
        public String toString() {
            return name;
        }

        private void draw(Graphics g, int cx, int cy) throws IOException {
            for (int i = 0; i < rx; i++)
                for (int j = 0; j < ry; j++)
                    g.drawImage(it.getImg(), cx + i * it.w, cy + j * it.h, null);
        }

        private int gety0() {
            return y - it.h / 2;
        }

        private int gety1() {
            return gety0() + ry * it.h;
        }

    }

    private class Item {

        private final String name, app;
        private final int w, h, dx, dy;

        private BufferedImage bimg;

        private Item(String str, String appe, JsonObject e) {
            ITEM_MAP.put(appe == null ? str : str + appe, this);
            name = str;
            app = appe;
            w = e.get("w").getAsInt();
            h = e.get("h").getAsInt();
            dx = getInt(e, "dx", 0);
            dy = getInt(e, "dy", 0);
        }

        @Override
        public String toString() {
            return app == null ? name : name + app;
        }

        private BufferedImage getImg() throws IOException {
            if (bimg != null)
                return bimg;
            String path = GUI + "-templates/sprites/" + name;
            if (app != null)
                path += "/" + app;
            path += ".png";
            return bimg = ImageIO.read(new File(path));
        }

    }

    private final String GUI, DST, CONT, CDST;

    GUIGenerator(String modid) {
        GUI = "./src/test/resources/" + modid + "/gui/";
        DST = "./src/test/resources/" + modid + "/assets/textures/gui/";
        CDST = "./src/test/resources/" + modid + "/data/" + modid + "/gui/";
        CONT = GUI + "-templates/container/" + modid + "/";
    }

    private final Map<String, Item> ITEM_MAP = new HashMap<>();

    void gen() throws IOException {
        readSprites();
        File f = new File(CONT);
        Item top = ITEM_MAP.get("top");
        Item middle = ITEM_MAP.get("middle");
        for (File fi : f.listFiles()) {
            JsonObject e = readJsonFile(fi.getPath()).getAsJsonObject();
            JsonObject out = new JsonObject();
            List<Item> side = new ArrayList<>();
            List<Comp> comp = new ArrayList<>();
            int height = 0;
            if (e.has("height")) {
                height = e.get("height").getAsInt();
            }
            Item bottom = ITEM_MAP.get(e.get("isContainer").getAsBoolean() ? "bottom" : "bottom_screen");
            e.get("side").getAsJsonArray().forEach(s -> side.add(ITEM_MAP.get(s.getAsString())));
            for (Map.Entry<String, JsonElement> ent : e.get("comp").getAsJsonObject().entrySet())
                comp.add(new Comp(ent.getKey(), ent.getValue().getAsJsonObject()));
            int y0 = 0, y1 = 0;
            for (Comp c : comp) {
                y0 = Math.min(y0, c.gety0());
                y1 = Math.max(y1, c.gety1());
            }
            if (top.h + y1 - y0 + bottom.h < height) {
                y1 = height - bottom.h - top.h + y0;
            }
            out.addProperty("height", top.h + y1 - y0 + bottom.h);
            BufferedImage bimg = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bimg.getGraphics();
            g.drawImage(top.getImg(), 0, 0, null);
            for (int i = 0; i < y1 - y0; i++)
                g.drawImage(middle.getImg(), 0, top.h + i, null);
            g.drawImage(bottom.getImg(), 0, top.h + y1 - y0, null);
            JsonObject jarr = new JsonObject();
            for (Comp c : comp) {
                int cx = c.x - c.it.w / 2;
                int cy = c.y - c.it.h / 2 - y0 + top.h;
                c.draw(g, cx, cy);
                JsonObject co = new JsonObject();
                co.addProperty("x", cx + c.it.dx);
                co.addProperty("y", cy + c.it.dy);
                co.addProperty("w", c.it.w);
                co.addProperty("h", c.it.h);
                co.addProperty("rx", c.rx);
                co.addProperty("ry", c.ry);
                jarr.add(c.name, co);
            }
            out.add("comp", jarr);
            int dx = 0, dy = 0;
            Item pre = null;
            JsonObject jside = new JsonObject();
            for (Item s : side) {
                JsonObject so = new JsonObject();
                if (pre != null) {
                    if (pre.h == s.h && top.w + dx + pre.w + s.w < 256) {
                        dx += pre.w;
                    } else {
                        dx = 0;
                        dy += pre.h;
                    }
                }
                so.addProperty("x", top.w + dx);
                so.addProperty("y", dy);
                so.addProperty("w", s.w);
                so.addProperty("h", s.h);
                jside.add(s.toString(), so);
                g.drawImage(s.getImg(), top.w + dx, dy, null);
                pre = s;
            }
            out.add("side", jside);
            g.dispose();
            File fx = new File(DST + "container/" + fi.getName().split("\\.")[0] + ".png");
            check(fx);
            ImageIO.write(bimg, "PNG", fx);
            write(DST + "coords/" + fi.getName(), out);
            write(CDST + "coords/" + fi.getName(), out);

        }

    }

    private int getInt(JsonObject e, String key, int def) {
        return e.has(key) ? e.get(key).getAsInt() : def;
    }

    private void readSprites() throws IOException {
        JsonElement e = readJsonFile(GUI + "-templates/info.json");
        e.getAsJsonObject().entrySet().forEach(ent -> {
            String name = ent.getKey();
            JsonObject o = ent.getValue().getAsJsonObject();
            if (o.has("ids"))
                o.get("ids").getAsJsonArray().forEach(ele -> new Item(name, ele.getAsString(), o));
            else
                new Item(name, null, o);
        });
    }

    private void write(String path, JsonObject obj) throws IOException {
        File fy = new File(path);
        check(fy);
        JsonWriter jw = new JsonWriter(Files.newWriter(fy, Charset.defaultCharset()));
        jw.setLenient(true);
        jw.setIndent("\t");
        Streams.write(obj, jw);
        jw.close();
    }

    public static void check(File f) throws IOException {
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();
    }

    private static void delete(File f) {
        if (!f.exists())
            return;
        if (f.isDirectory())
            for (File fi : f.listFiles())
                delete(fi);
        f.delete();
    }

    private static Map<String, List<String>> readJson(String path) throws IOException {
        JsonElement e = readJsonFile(path);
        Map<String, List<String>> ans = new HashMap<>();
        e.getAsJsonObject().entrySet().forEach(ent0 -> ent0.getValue().getAsJsonObject().entrySet().forEach(ent1 -> {
            String key = ent1.getKey();
            List<String> list;
            if (ans.containsKey(key))
                list = ans.get(key);
            else
                ans.put(key, list = new ArrayList<>());
            ent1.getValue().getAsJsonObject().entrySet().forEach(ent2 -> {
                String group = ent2.getKey();
                ent2.getValue().getAsJsonArray().forEach(ent3 -> {
                    String name = ent3.isJsonObject() ? ent3.toString() : ent3.getAsString();
                    if (name.startsWith("_") || name.startsWith("^"))
                        list.add(group + name);
                    else if (name.endsWith("_"))
                        list.add(name + group);
                    else
                        list.add(name);
                });
            });
        }));
        return ans;
    }

    private static JsonElement readJsonFile(String path) throws IOException {
        File f = new File(path);
        JsonReader r = new JsonReader(Files.newReader(f, Charset.defaultCharset()));
        JsonElement e = new JsonParser().parse(r);
        r.close();
        return e;
    }


}
