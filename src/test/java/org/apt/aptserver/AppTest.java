package org.apt.aptserver;

import com.sun.jersey.api.client.*;
import com.sun.jersey.test.framework.JerseyTest;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apt.aptserver.dto.ProductBacklogItem;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AppTest extends JerseyTest {

    public AppTest() throws Exception {
        super("org.apt.aptserver");
    }

    private WebResource webResource;
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        webResource = resource();
    }

    /**
     * Test checks that the application.wadl is reachable.
     */
    @Test
    public void testApplicationWadl() {
        String applicationWadl = webResource.path("application.wadl").get(String.class);
        assertTrue("Something wrong. Returned wadl length is not > 0",
                applicationWadl.length() > 0);
    }

    @Test
    public void testProductBacklogItem() {
        ProductBacklogItem e1 = getPBItem("S1");
        assertNotNull(e1.getName());
        assertNotNull(e1.getDescription());
    }

    @Test
    public void testProductBacklogItemListUsingPOST() {
        List<ProductBacklogItem> e1 = getProductList();

        int size = e1.size();
        print(e1);

        ProductBacklogItem pbi = new ProductBacklogItem("name3", "desc3");
        ClientResponse response = webResource.path("backlog").type(MediaType.APPLICATION_XML).post(ClientResponse.class, pbi);

        //location will have the new id added by the post
//        System.out.println("####################-" + response.toString() + ":" + response.getLocation());
        e1 = getProductList();

        print(e1);
        assertEquals(size + 1, e1.size());
        assertEquals(201, response.getStatus());
    }
    
    
    @Test
    public void testProductBacklogItemListUsingPUTCreateCase() {
        List<ProductBacklogItem> e1 = getProductList();
        int size = e1.size();
        print(e1);

        ProductBacklogItem pbi = new ProductBacklogItem("name-put", "desc-put");
        ClientResponse response = webResource.path("backlog").type(MediaType.APPLICATION_XML).put(ClientResponse.class, pbi);
        
        e1 = getProductList();

        print(e1);
        //PUT should increase the size by 1
        assertEquals(size + 1, e1.size());
        assertEquals(201, response.getStatus());
    }
    
    @Test
    public void testProductBacklogItemListUsingPUTUpdateCase() {
        ProductBacklogItem pbi = new ProductBacklogItem("name-put2", "desc-put2");
        ClientResponse response = webResource.path("backlog").type(MediaType.APPLICATION_XML).put(ClientResponse.class, pbi);
        assertEquals(201, response.getStatus());

        List<ProductBacklogItem> e1 = getProductList();
        int size = e1.size();
        String backlogId = pbi.getBacklogId();
        final String modifiedDesc = "desc-put2-modified";
        //desc modified, new record should not be created.
        pbi.setDescription(modifiedDesc);
        response = webResource.path("backlog").type(MediaType.APPLICATION_XML).put(ClientResponse.class, pbi);
        
        e1 = getProductList();
        print(e1);
        //PUT should increase the size by 1 as it should just update the same record
        ProductBacklogItem pbItem = getPBItem(backlogId);
        assertEquals(modifiedDesc, pbItem.getDescription());
        assertEquals(size, e1.size());
        assertEquals(204, response.getStatus());
    }
    
    @Test
    public void testProductBacklogItemListUsingDelete() {
        List<ProductBacklogItem> e1 = getProductList();
        int size = e1.size();
        print(e1);

        ProductBacklogItem pbi = new ProductBacklogItem("name-delete", "desc-delete");
        ClientResponse response = webResource.path("backlog").type(MediaType.APPLICATION_XML).put(ClientResponse.class, pbi);
        
        e1 = getProductList();

        print(e1);
        //PUT should increase the size by 1
        assertEquals(size + 1, e1.size());
        assertEquals(201, response.getStatus());
        
        response = webResource.path("backlog/" + pbi.getBacklogId()).type(MediaType.APPLICATION_XML)
                .delete(ClientResponse.class);
        assertEquals(204, response.getStatus());
        
        e1 = getProductList();

        print(e1);
        //PUT should increase the size by 1
        assertEquals(size, e1.size());
    }

    private List<ProductBacklogItem> getProductList() throws UniformInterfaceException, ClientHandlerException {
        GenericType<List<ProductBacklogItem>> genericType =
                new GenericType<List<ProductBacklogItem>>() {
                };
        List<ProductBacklogItem> e1 = webResource.path("backlog").get(genericType);
        return e1;
    }


    private void print(List<ProductBacklogItem> e1) {
        System.out.println("List size = " + e1.size());
        for (ProductBacklogItem pbi : e1) {
            System.out.println("e1 = " + pbi.getName() + ":" + pbi.getDescription());
        }
    }
    
    
    private ProductBacklogItem getPBItem(String id) throws UniformInterfaceException, ClientHandlerException {
        return webResource.path("backlog/" + id).
                get(ProductBacklogItem.class);
    }
}
