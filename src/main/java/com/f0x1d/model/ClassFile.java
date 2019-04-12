package com.f0x1d.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassFile extends File {

    public ClassFile(String pathname) {
        super(pathname);
    }

    public ClassFile(String parent, String child) {
        super(parent, child);
    }

    public void write(String text, boolean append){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this, append));
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ClassFile[] listFiles() {
        ClassFile[] filesNew = new ClassFile[super.listFiles().length];

        File[] files = super.listFiles();
        for (int i = 0; i < files.length; i++){
            filesNew[i] = new ClassFile(files[i].getAbsolutePath());
        }
        return filesNew;
    }

    public List<String> read(){
        try {
            List<String> strings = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(this));
            String line;
            while ((line = reader.readLine()) != null){
                strings.add(line);
            }
            reader.close();
            return strings;

        } catch (Exception e){}
        return new ArrayList<>();
    }
}
