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

package org.apt.aptserver;

import java.net.URI;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apt.aptserver.dto.ProductBacklogItem;
import org.apt.aptserver.manager.PBIManager;

/**
 *
 * @author Ravi Buddharaju
 */
// The Java class will be hosted at the URI path "/backlog"
@Path("/backlog")
public class ProductBacklogResource {

    //to be used to build the URIs especially for POST
    @Context
    UriInfo uriInfo;
    private PBIManager pbiManager = new PBIManager();

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{id}")
    public Response getProductBacklogItem(@PathParam("id") String id) {
        return Response.ok(new GenericEntity<ProductBacklogItem>(pbiManager.getPBItem(id)) {
        }).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ProductBacklogItem> getProductBacklogItems() {
        return pbiManager.getPBItems();
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postProductBacklogItem(ProductBacklogItem item) {
        return addItem(item);
    }
    

    private Response addItem(ProductBacklogItem item) throws UriBuilderException, IllegalArgumentException {
        item = pbiManager.addPBItem(item);
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI userUri = ub.path(item.getBacklogId()).build();
        return Response.created(userUri).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putProductBacklogItem(ProductBacklogItem item) {

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();

        Response response;
        if (pbiManager.hasPBIItem(item)) {
            pbiManager.updateItem(item);
            response = Response.noContent().build();
        } else {
            response = addItem(item);
        }
        return response;
    }

    @Path("/{backlogId}")
    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteProductBacklogItem(@PathParam("backlogId") String backlogId) {
        boolean status = pbiManager.deleteItem(backlogId);
        if (status) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            //PRECONDITION_FAILED - 412
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
}