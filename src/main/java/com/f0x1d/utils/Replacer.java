package com.f0x1d.utils;

import com.f0x1d.model.ClassFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Replacer {

    public static void replace(ClassFile classFile, Map<String, String> map){
        List<String> newText = new ArrayList<>();

        for (String text : classFile.read()) {
            for (Map.Entry<String, String> set : map.entrySet()) {
                text = text.replace(set.getKey(), set.getValue());
            }

            newText.add(text);
        }

        StringBuilder builder = new StringBuilder();
        for (String s : newText) {
            builder.append("\n" + s);
        }

        String itog = builder.toString();
        itog = itog.substring(itog.indexOf('\n')+1);

        classFile.write(itog, false);
    }
}
