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

package cpw.mods.fml.common.toposort;

import java.util.Set;

public class ModSortingException extends RuntimeException {
    private final SortingExceptionData sortingExceptionData;

    public <T> ModSortingException(String string, T node, Set<T> visitedNodes) {
        super(string);
        this.sortingExceptionData = new SortingExceptionData(node, visitedNodes);
    }

    public <T> SortingExceptionData<T> getExceptionData() {
        return sortingExceptionData;
    }

    public class SortingExceptionData<T> {
        private final T firstBadNode;
        private final Set<T> visitedNodes;
        public SortingExceptionData(T node, Set<T> visitedNodes) {
            this.firstBadNode = node;
            this.visitedNodes = visitedNodes;
        }

        public T getFirstBadNode() {
            return firstBadNode;
        }

        public Set<T> getVisitedNodes() {
            return visitedNodes;
        }
    }

}
