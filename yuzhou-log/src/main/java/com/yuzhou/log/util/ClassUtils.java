package com.yuzhou.log.util;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类对象工具
 *
 * @author xiongcheng.lxch
 */
public class ClassUtils {
    /**
     * 当前包下所有类，支持本地文件和jar文件
     *
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        // 用'/'代替'.'路径
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            if (isJarFile(directory)) {
                classes.addAll(findClasses(directory, classLoader));
            } else {
                classes.addAll(findClasses(directory, packageName));
            }
        }
        return classes;
    }

    private static boolean isJarFile(File file) {
        String directoryPath = file.getPath();
        if (directoryPath.contains("jar!")) {
            return true;
        }
        return false;
    }

    private static List<Class> findClasses(File directory, ClassLoader classLoader)
            throws ClassNotFoundException, IOException {
        List<Class> classes = new ArrayList<Class>();
        String jarPackagePath = directory.getPath();
        String jar = jarPackagePath.substring(jarPackagePath.indexOf(":") + 1, jarPackagePath.indexOf("!"));
        String pakage = jarPackagePath.substring(jarPackagePath.indexOf("!") + 2);

        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.startsWith(pakage) && name.endsWith(".class")) {
                String replace = name.replace("/", ".");
                String names = replace.substring(0, name.length() - 6);
                Class<?> aClass = classLoader.loadClass(names);
                classes.add(aClass);
            }
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,
                        packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                // 去掉'.class'
                classes.add(Class.forName(packageName
                        + '.'
                        + file.getName().substring(0,
                        file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
