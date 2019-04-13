# Deobfuscator

An util for reverse engineers which allows you to deobfuscate an app with your database

How to use:
1. <a href="https://github.com/F0x1d/Deobfuscator/releases/tag/0.1">Download .jar file</a>, download or create replacements.json (your database)
2. Open cmd and navigate to folder with this files (.jar and .json)
3. Write `java -jar <jar file name>.jar --deobf <path to your decompiled apk>`


WARNING!
Note, that method's deobfuscation is before class's deobfuscation!


Example of database:<br>
`[{"type":"class", "target":"Lcom/vk/auth/g", "replace":"Lcom/vk/auth/VKAccount"},
{"type":"method", "method":"B(Z)V", "path":"com\\vk\\auth\\g.smali", "new_method":"flex(Z)V"}]`
