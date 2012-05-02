/*
 * Copyright 2012 Project APT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apt.aptserver.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apt.aptserver.dto.ProductBacklogItem;

/**
 *
 * @author Ravi Buddharaju
 */

//TODO later link this class to Daos to get to the DB.
public class PBIManager {
    
    private static Map<String, ProductBacklogItem> pbMap = createMap();

    private static Map<String, ProductBacklogItem> createMap() {
        Map<String, ProductBacklogItem> pbMap = new HashMap<>();
        ProductBacklogItem item1 = new ProductBacklogItem("name1", "desc1");
        pbMap.put(item1.getBacklogId(), item1);
        ProductBacklogItem item2 = new ProductBacklogItem("name2", "desc2");
        pbMap.put(item2.getBacklogId(), item2);
        return pbMap;
    }

    public ProductBacklogItem addPBItem(ProductBacklogItem item) {
        pbMap.put(item.getBacklogId(), item);
        return item;
    }

    public List<ProductBacklogItem> getPBItems() {
        List<ProductBacklogItem> pbiList = new ArrayList<>();
        pbiList.addAll(pbMap.values());
        return pbiList;
    }
    
    public ProductBacklogItem getPBItem(String id) {
        return pbMap.get(id);
    }

    public boolean hasPBIItem(ProductBacklogItem item) {
        return pbMap.containsKey(item.getBacklogId());
    }

    public void updateItem(ProductBacklogItem item) {
        if ( !hasPBIItem(item)) throw new IllegalStateException("Object not available for update");
        pbMap.put(item.getBacklogId(), item);
    }

    public boolean deleteItem(String backlogId) {
        final ProductBacklogItem removedItem = pbMap.remove(backlogId);
        return removedItem != null;
    }
}
