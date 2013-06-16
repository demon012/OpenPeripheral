package openperipheral.common.config;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.Property;
import openperipheral.OpenPeripheral;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.FMLRelauncher;

public class ConfigSettings {

	public static final String NETWORK_CHANNEL = "OpenPeripheral";
	public static int CACHE_REFRESH_INTERVAL = 7;
	public static String RESOURCE_PATH = "/mods/openperipheral";
	public static String LANGUAGE_PATH = String.format("%s/languages", RESOURCE_PATH);
	
	private static String externalBase = "https://raw.github.com/mikeemoo/OpenPeripheral/master/";
	
	public static String EXTERNAL_LUA_LISTING = String.format("%s%s", externalBase, "mods/openperipheral/scripts.txt");
	public static String EXTERNAL_LUA_FOLDER = String.format("%s%s", externalBase, "mods/openperipheral/lua/");
	public static String LOCAL_LUA_LISTING;
	public static String LOCAL_LUA_FOLDER;
	
	public static boolean FRESH_INSTALL = false;
	
	public static boolean analyticsEnabled = true;

	public static String CACHE_FILE = "OpenPeripheral_methods.json";
	public static String CACHE_PATH = "";
	public static String DATA_URL = String.format("%s%s", externalBase, "methods_new.json");
	public static String previousVersion;

	// blocks and items
	public static int glassesId = 1055;
	public static int glassesBridgeId = 580;
	public static int proxyBlockId = 581;
	

	public static void loadAndSaveConfig(File suggestedConfigFile) {

		Configuration configFile = new Configuration(suggestedConfigFile);

		ModContainer container = FMLCommonHandler.instance().findContainerFor(OpenPeripheral.instance);
		String version = container.getVersion();

		Property prop = configFile.get("general", "enableAnalytics", true);
		prop.comment = "Do you want analytics enabled?";
		analyticsEnabled = prop.getBoolean(true);

		prop = configFile.get("general", "currentVersion", 0);
		prop.comment = "Do not edit this file. OpenPeripheral adds no blocks or items! this value should not be changed";
		previousVersion = prop.getString();

		if (version != previousVersion) {
			FRESH_INSTALL = true;
		}

		prop.set(version);

		prop = configFile.get("general", "dataUrl", DATA_URL);
		prop.comment = "The URL of the data file";
		DATA_URL = prop.getString();

		prop = configFile.get("general", "cacheFile", CACHE_FILE);
		prop.comment = "The path to the cache file";
		CACHE_FILE = prop.getString();

		prop = configFile.get("general", "cacheInterval", CACHE_REFRESH_INTERVAL);
		prop.comment = "How often the cache file gets updated (in days)";
		CACHE_REFRESH_INTERVAL = prop.getInt();

		prop = configFile.get("items", "glassesId", glassesId);
		prop.comment = "The id of the glasses";
		glassesId = prop.getInt();

		prop = configFile.get("blocks", "bridgeId", glassesBridgeId);
		prop.comment = "The id of the glasses bridge";
		glassesBridgeId = prop.getInt();

		if (FRESH_INSTALL && analyticsEnabled) {
			analytics(container);
		}

		File baseDirectory = null;

		if (FMLRelauncher.side() == "CLIENT") {
			baseDirectory = Minecraft.getMinecraftDir();
		} else {
			baseDirectory = new File(".");
		}
		
		File configDirectory = new File(baseDirectory, "config/");
		File cacheFile = new File(configDirectory, CACHE_FILE);
		File openPeripheralFolder = new File(baseDirectory, "openperipheral/");
		File luaFolder = new File(openPeripheralFolder, "lua/");
		
		LOCAL_LUA_LISTING = openPeripheralFolder.getAbsolutePath() + "/scripts.txt";
		LOCAL_LUA_FOLDER = luaFolder.getAbsolutePath();
		
		configFile.save();
		CACHE_PATH = cacheFile.getAbsolutePath();
	}

	private static void analytics(ModContainer container) {
		String charset = "UTF-8";
		String url;
		try {
			url = String.format(
				"http://www.openccsensors.info/op_analytics?version=%s&side=%s&forge=%s",
				URLEncoder.encode(container.getVersion(), charset),
				URLEncoder.encode(FMLRelauncher.side(), charset),
				URLEncoder.encode(ForgeVersion.getVersion(), charset)
			);
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(4000);
			connection.setRequestProperty("Accept-Charset", charset);
			InputStream response = connection.getInputStream();
		} catch (Exception e) {
		}
	}
}
