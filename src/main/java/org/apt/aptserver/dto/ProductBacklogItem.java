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
package org.apt.aptserver.dto;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ravi Buddharaju
 */

@XmlRootElement
public class ProductBacklogItem {

    private static long index = 1;
    public enum State {Backlog, Defined, Progress, Complete, Accepted};
    
    private String backlogId;
    private String name;

    public ProductBacklogItem() {}
        
    public ProductBacklogItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.state = State.Backlog;
        this.createdDate = new Date();
        //TODO change later :-) some incremental value
        this.backlogId = "S" + String.valueOf(index++);
    }
    
    //TODO later change to clob
    private String description;
    private int rank;
    private String estimatedPoint;
    //private User assignedTo;
    //private User createdBy;
    //private User modifiedBy;
    private State state; 
    private Date createdDate;
    private Date modifiedDate;

    public String getBacklogId() {
        return backlogId;
    }

    public void setBacklogId(String backlogId) {
        this.backlogId = backlogId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimatedPoint() {
        return estimatedPoint;
    }

    public void setEstimatedPoint(String estimatedPoint) {
        this.estimatedPoint = estimatedPoint;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
