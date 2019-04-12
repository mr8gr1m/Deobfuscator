package com.f0x1d;

import com.f0x1d.model.ClassFile;
import com.f0x1d.utils.Replacer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Main {

    public static void main(String... args) {
        try {
            if (args[0].equals("--p")) {
                String path = "C:\\Users\\admin\\Desktop\\ANDROID\\BatchApkTool\\_INPUT_APK";

                if (args.length > 1)
                    path = args[1];

                if (path.equals("C:\\Users\\admin\\Desktop\\ANDROID\\BatchApkTool\\_INPUT_APK")){
                    System.out.println("Path for app pls");
                    return;
                }

                Files.walkFileTree(new File(path).toPath(), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                        File file = path.toFile();
                        if (file.getName().contains("F0x1dSettings") || file.getName().contains("OTA")) {
                            Map<String, String> map = new HashMap<>();
                            map.put("setOnPreferenceClickListener(Landroid/support/v7/preference/Preference$OnPreferenceClickListener;)V",
                                    "a(Landroid/support/v7/preference/Preference$c;)V");
                            map.put("findPreference",
                                    "a");
                            map.put(".implements Landroid/support/v7/preference/Preference$OnPreferenceClickListener;",
                                    ".implements Landroid/support/v7/preference/Preference$c;");
                            map.put(".method public onPreferenceClick(Landroid/support/v7/preference/Preference;)Z",
                                    ".method public a(Landroid/support/v7/preference/Preference;)Z");
                            map.put("setSummary",
                                    "b");

                            Replacer.replace(new ClassFile(file.getAbsolutePath()), map);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else if (args[0].equals("--deobf")) {
                System.out.println("~~~~~ BEST REPLACER EVER STARTED ~~~~~\n~~~~~ GOOD LUCK ~~~~~");

                String path = "C:\\Users\\admin\\Desktop\\ANDROID\\BatchApkTool\\_INPUT_APK";
                ClassFile hooks = new ClassFile("replacements.json");

                if (args.length > 1)
                    path = args[1];

                if (path.equals("C:\\Users\\admin\\Desktop\\ANDROID\\BatchApkTool\\_INPUT_APK")){
                    System.out.println("Path for app pls");
                    return;
                }

                String jsonText = "";

                for (String s : hooks.read()) {
                    jsonText = jsonText + s + "\n";
                }

                JSONArray array = new JSONArray(jsonText);

                Files.walkFileTree(new File(path).toPath(), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                        File file = path.toFile();
                        if (file.getName().contains(".smali")) {
                            replaceMethodWithJSON(file, array);
                            replaceClassWithJSON(file, array);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        if (exc != null)
                            System.err.println(exc.getLocalizedMessage());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });

                System.out.println("~~~~~ ONE MORE TIME TO CHECK ~~~~~");

                Files.walkFileTree(new File(path).toPath(), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                        File file = path.toFile();
                        if (file.getName().contains(".smali") && file.length() < 1) {
                            file.delete();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        if (exc != null)
                            System.err.println(exc.getLocalizedMessage());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                System.out.println("Нет такой команды");
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private static void replaceClassWithJSON(File file, JSONArray array) {
        if (file.isDirectory())
            return;

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                if (object.getString("type").equals("class")){
                    Map<String, String> map = new HashMap<>();
                    map.put(object.getString("target"),
                            object.getString("replace"));

                    Replacer.replace(new ClassFile(file.getAbsolutePath()), map);

                    if (file.getAbsolutePath().contains(object.getString("old_name"))) {
                        System.out.println("file found at: " + file.getAbsolutePath());
                        File newFile = new File(file.getAbsolutePath().replace(object.getString("old_name"), object.getString("new_name")));
                        file.renameTo(newFile);
                    }

                }
            }
        } catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }
    }

    private static void replaceMethodWithJSON(File file, JSONArray array){
        if (file.isDirectory())
            return;

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                if (object.getString("type").equals("method")){
                    Map<String, String> map = new HashMap<>();
                    map.put(object.getString("path2") + ";->" + object.getString("method"),
                            object.getString("path2") + ";->" + object.getString("new_method"));

                    Replacer.replace(new ClassFile(file.getAbsolutePath()), map);

                    if (file.getAbsolutePath().contains(object.getString("path"))) {
                        System.out.println("Replaced original method in: " + file.getAbsolutePath());

                        ClassFile fileWithMethod = new ClassFile(file.getAbsolutePath());

                        String fileWithMethodText = "";

                        for (String s : fileWithMethod.read()) {
                            fileWithMethodText = fileWithMethodText + s + "\n";
                        }

                        fileWithMethod.write(fileWithMethodText.replace(object.getString("method"), object.getString("new_method")), false);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

}
