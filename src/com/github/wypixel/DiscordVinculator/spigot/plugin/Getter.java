package com.github.wypixel.DiscordVinculator.spigot.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Getter {

	public static List<Class<?>> getClassesForPackageByFile(File file, String pkgname) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		try {
			String relPath = pkgname.replace('.', '/');		
			try (JarFile jarFile = new JarFile(file)) {
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (entryName.endsWith(".class") 
							&& entryName.startsWith(relPath)
							&& entryName.length() > (relPath.length() + "/".length())) {
						String className = entryName.replace('/', '.').replace('\\', '.');
						if (className.endsWith(".class"))
							className = className.substring(0, className.length()-6);
						Class<?> c = loadClass(className);
						if (c != null)
							classes.add(c);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Unexpected IOException reading JAR File '" + file.getAbsolutePath() + "'", e);
		}

		return classes;
	}
	
	
	public static ArrayList<Class<?>> getClassesForPackage(Object instance, String packageName) {
		CodeSource source = instance.getClass().getProtectionDomain().getCodeSource();
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		ArrayList<Class<?>> processedClasses = new ArrayList<Class<?>>();
		ArrayList<String> names = new ArrayList<String>();
		
		if (source != null)
			processJarfile(source.getLocation(), packageName, classes);
		
		for (Class<?> simpleClass : classes) {
			names.add(simpleClass.getSimpleName());
			processedClasses.add(simpleClass);
		}
		
		classes.clear();
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		
		for (String name : names) {
			for (Class<?> simpleClass : processedClasses) {
				if (simpleClass.getSimpleName().equals(name)) {
					classes.add(simpleClass);
					break;
				}
			}
		}
		return classes;
	}
	
	private static Class<?> loadClass(String className) {	
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
		}
	}

	private static void processJarfile(URL resource, String packageName, ArrayList<Class<?>> classes) {
		JarFile jarFile;
		String relPath = packageName.replace('.', '/'), resPath = resource.getPath().replace("%20", " "), jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
		
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
		}
		
		Enumeration<JarEntry> entries = jarFile.entries();
		
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName(), className = null;
			
			if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length()))
				className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");

			if (className != null)
				classes.add(loadClass(className));
		}
		
		try {
			jarFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}