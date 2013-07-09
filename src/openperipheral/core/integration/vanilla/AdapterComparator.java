package openperipheral.core.integration.vanilla;

import net.minecraft.tileentity.TileEntityComparator;
import openperipheral.api.IPeripheralAdapter;
import openperipheral.api.LuaMethod;
import dan200.computer.api.IComputerAccess;

public class AdapterComparator implements IPeripheralAdapter {

	@Override
	public Class getTargetClass() {
		return TileEntityComparator.class;
	}
	
	@LuaMethod
	public int getOutputSignal(IComputerAccess computer, TileEntityComparator comparator) {
		return comparator.getOutputSignal();
	}

}
