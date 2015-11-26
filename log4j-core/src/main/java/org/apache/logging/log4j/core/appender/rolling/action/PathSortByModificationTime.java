/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * {@link PathSorter} that sorts path by their LastModified attribute.
 */
@Plugin(name = "SortByModificationTime", category = "Core", printObject = true)
public class PathSortByModificationTime implements PathSorter {

    private final boolean recentFirst;
    private final int multiplier;

    /**
     * Constructs a new SortByModificationTime sorter.
     * 
     * @param recentFirst if true, most recently modified paths should come first
     */
    public PathSortByModificationTime(final boolean recentFirst) {
        this.recentFirst = recentFirst;
        this.multiplier = recentFirst ? 1 : -1;
    }

    /**
     * Create a PathSorter that sorts by lastModified time.
     * 
     * @param recentFirst if true, most recently modified paths should come first.
     * @return A PathSorter.
     */
    @PluginFactory
    public static PathSorter createSorter( //
            @PluginAttribute(value = "recentFirst", defaultBoolean = true) final boolean recentFirst) {
        return new PathSortByModificationTime(recentFirst);
    }

    /**
     * Returns whether this sorter sorts recent files first.
     * @return whether this sorter sorts recent files first
     */
    public boolean isRecentFirst() {
        return recentFirst;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final PathWithAttributes path1, final PathWithAttributes path2) {
        final long lastModified1 = path1.getAttributes().lastModifiedTime().toMillis();
        final long lastModified2 = path2.getAttributes().lastModifiedTime().toMillis();
        return multiplier * Long.signum(lastModified2 - lastModified1);
    }
}
