/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.common.discovery;

import cpw.mods.fml.common.ModContainer;

import java.util.List;
import java.util.regex.Pattern;

public interface ITypeDiscoverer {
    Pattern classFile = Pattern.compile("([^\\s$]+).class$");

    List<ModContainer> discover(ModCandidate candidate, ASMDataTable table);
}