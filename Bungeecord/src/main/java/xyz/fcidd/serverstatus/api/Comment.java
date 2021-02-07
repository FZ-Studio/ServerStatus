package xyz.fcidd.serverstatus.api;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

@Deprecated
public class Comment {

    private LinkedList<String> headComment;
    private LinkedList<String> feetComment;
    private HashMap<String, LinkedList<String>> keyComments;

    /**
     * 构造函数，初始化变量
     */
    public Comment() {
        headComment = new LinkedList<>();
        feetComment = new LinkedList<>();
        keyComments = new HashMap<>();
    }

    public void putHeadComment(String comment) {
        comment = comment.replace("\n", "");
        this.headComment.add(comment);
    }

    /**
     * 添加足注释
     * 
     * @param comment 注释，删除换行
     */
    public void putFeetComment(String comment) {
        comment = comment.replace("\n", "");
        this.feetComment.add(comment);
    }

    /**
     * @param key     需要添加注释的键名
     * 
     * @param comment 注释，删除换行
     */
    public void putKeyComment(String key, String comment) {
        comment = comment.replace("\n", "");
        if (this.keyComments.containsKey(key)) {
            this.keyComments.get(key).add(comment);
        } else {
            LinkedList<String> value = new LinkedList<>();
            value.add(comment);
            this.keyComments.put(key, value);
        }
    }

    /**
     * @param inF 要写入的文件
     */
    public void writeComment(File inF) throws Exception {
        if (keyComments.isEmpty() && headComment.isEmpty() && feetComment.isEmpty()) {
            throw new NullPointerException("comment is empty");
        }
        if (!inF.exists()) {
            inF.getParentFile().mkdirs();
            if (!inF.createNewFile()) {
                System.err.println("create file \"" + inF.getAbsolutePath() + "\"" + " failed");
            }
        } else {
            if (!inF.isFile()) {
                throw new IllegalArgumentException("Argument is not a normal file");
            }
        }
        File outF = File.createTempFile("config.yml", ".tmp", inF.getParentFile());
        try (FileInputStream fis = new FileInputStream(inF);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                FileOutputStream fos = new FileOutputStream(outF);
                OutputStreamWriter out = new OutputStreamWriter(fos, "StandardCharsets.UTF_8");) {
            String oldLine;
            String oldKey;
            int innerIndex;
            if (!headComment.isEmpty()) {
                headComment.forEach(comment -> {
                    try {
                        out.write("#" + comment + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            while ((oldLine = in.readLine()) != null) {
                if (oldLine.equals("")) {
                    out.write("\n");
                } else {
                    innerIndex = oldLine.indexOf(":");
                    if (innerIndex != -1) {
                        oldKey = oldLine.substring(0, innerIndex).replaceAll("\\s", "");
                        if (keyComments.containsKey(oldKey)) {
                            keyComments.get(oldKey).forEach(comment -> {
                                try {
                                    out.write("#" + comment + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            keyComments.remove(oldKey);
                        }
                    }
                    out.write(oldLine + "\n");
                }
            }
            if (!keyComments.isEmpty()) {
                System.out.print("keys ");
                keyComments.keySet().forEach(key -> System.out.print("\"" + key + "\" "));
                System.out.println("not found, skip automatically");
            }
            if (!feetComment.isEmpty()) {
                feetComment.forEach(comment -> {
                    try {
                        out.write("#" + comment + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (inF.delete()) {
            if (!outF.renameTo(inF)) {
                System.err.println("rename file \"" + outF.getAbsolutePath() + "\" failed");
            }
        } else {
            System.err.println("delete file \"" + inF.getAbsolutePath() + "\" failed");
        }
        if (outF.exists()) {
            if (!outF.delete()) {
                System.err.println("delete file \"" + outF.getAbsolutePath() + "\" failed");
            }
        }
    }
}
