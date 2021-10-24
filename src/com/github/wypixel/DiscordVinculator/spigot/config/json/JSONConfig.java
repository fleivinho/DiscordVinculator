package com.github.wypixel.DiscordVinculator.spigot.config.json;

import com.github.wypixel.DiscordVinculator.spigot.plugin.WPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.intellij.lang.annotations.Language;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("all")
public class JSONConfig implements Serializable {
    
    // The parsed objects
	private WPlugin plugin;
	private String name;
	private File file;
	private File oldFile;
    private Map<String, Object> parsedObjects = new HashMap<>();
    
    File f;

     public JSONConfig(File f) {
         this.f = f;
         this.name = f.getName();
         if(!f.exists()) {
             try {
                 f.createNewFile();
             } catch(IOException ex) {
                 ex.printStackTrace();
             }
         } else {
             try {
                 loadJSONConfig(f);
             } catch(Exception ex) {

             }
         }
     }

    public JSONConfig loadJSONConfig(File f) throws FileNotJSONException {
        if(!f.exists()) {
            throw new NullPointerException("Could not find file " + f.getName() + " in " + f.getPath());
        } else if(!f.getName().endsWith(".json")) {
           throw new FileNotJSONException("Unsupported file extension \"" + f.getName().split("\\.")[1] + "\".");
        } else {
            JSONParser parser = new JSONParser();
            try {
                // Parses the JSON
                JSONObject parsed = (JSONObject) parser.parse(new FileReader(f));
                Set<Object> keys = parsed.keySet();
                for(Object key : keys) {
                    parsedObjects.put((String) key, parsed.get(key));
                }
            } catch (ParseException | IOException ex) {

            }
        }

        return this;
    }

    public <T> T get(String order, String order2, String name)  {
    	JSONObject o = get(order);
    	JSONObject a = (JSONObject) o.get(order2);
    	try {
    	return (T) a.get(name);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public <T> T get(String order, String name)  {
    	JSONObject o = get(order);
    	try {
    	return (T) o.get(name);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
   public <T> T get(String name)  {
	   try {
        return (T) parsedObjects.get(name);
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return null;
   }

   public void set(String key, Object obj) {
       parsedObjects.put(key, obj);
   }
     
   public void save() {
       JSONObject JSON_OBJECT = new JSONObject();
       
       for(Entry<String, Object> entry : parsedObjects.entrySet()) {
           String key = entry.getKey();
           Object obj = entry.getValue();
           
           if(obj instanceof List || obj.getClass().isAssignableFrom(List.class)) {
               // Infer generic types cause sw333333333333333333333333333g amirite?
               List<?> list = (List<?>) obj;
               JSONArray array = new JSONArray();
               
               for(Object objs : list) {
                  // Y0L0?
                 if(objs instanceof String) {
                     array.add((String)objs);
                 } 
                 if(objs instanceof Integer) {
                     array.add((Integer)objs);
                 }
                 if(objs instanceof Double) {
                     array.add((Double)objs);
                 }
                 if(objs instanceof Float) {
                     array.add((Float) objs);
                 }
                 if(objs instanceof Short) {
                     array.add((Short) objs);
                 }
                 if(objs instanceof ItemStack) {
                     // TODO: Serialize ItemStack
                     continue;
                 }
                 if(objs instanceof Entity) {
                     // TODO: Serialize Entity
                     continue;
                 }
                 
               }
               JSON_OBJECT.put(key,array);
                  
           }
           if(obj instanceof Integer) {
               JSON_OBJECT.put(key,(Integer) obj);
           }
           
           if(obj instanceof Double) {
               JSON_OBJECT.put(key, (Double) obj);
           }
           
           if(obj instanceof String) {
               JSON_OBJECT.put(key, (String) obj);
           }
       }
     try {
      //FileWriter writer = new FileWriter(f);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
      JSON_OBJECT.writeJSONString(writer);
      
      writer.flush();
      
      writer.close();
      
     } catch(Exception ex) {
         ex.printStackTrace();
     }
   }


    public String getName() {
        return name.replace(".json", "");
    }

    public File getOldFile() {
        return oldFile;
    }

    public void setOldFile(File oldFile) {
        this.oldFile = oldFile;
    }
}

