package mapwriter.forge;

import java.io.File;

import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.overlay.*;
import mapwriter.region.MwChunk;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;

public class ClientProxy extends CommonProxy {

	private MwConfig config;

	public void preInit(File configFile) {
		this.config = new MwConfig(configFile);
	}

	public void load() {
		Mw mw = new Mw(this.config);
		Object eventHandler = new EventHandler(mw);
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(eventHandler);

		Object eventKeyHandler = new MwKeyHandler();
		FMLCommonHandler.instance().bus().register(eventKeyHandler);
		MinecraftForge.EVENT_BUS.register(eventKeyHandler);

		// temporary workaround for user defined key bindings not being loaded
		// at game start. see https://github.com/MinecraftForge/FML/issues/378
		// for more info.
		Minecraft.getMinecraft().gameSettings.loadOptions();
	}

	public void postInit() {
		if (Loader.isModLoaded("CarpentersBlocks")) {
			MwChunk.carpenterdata();
		}
		if (Loader.isModLoaded("ForgeMultipart")) {
			MwChunk.FMPdata();
		}
		MwAPI.registerDataProvider("Slime", new OverlaySlime());
		MwAPI.registerDataProvider("ChunkGrid", new OverlayGrid());
		MwAPI.registerDataProvider("OreVeinGrid", new OverlayVeinsGrid());
		MwAPI.registerDataProvider("FluidsGrid", new OverlayFluidsGrid());
		MwAPI.registerDataProvider("RegionGrid", new OverlayRegionsGrid());
		MwAPI.registerDataProvider("ImpactVeinGrid", new OverlayIVeinsGrid());		
		// MwAPI.registerDataProvider("Checker", new OverlayChecker());
		// MwAPI.setCurrentDataProvider("Slime");
	}
}
