import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import junit.framework.TestCase;


class ButtonTest extends TestCase {

	@Test
	public void testButton()
    {
    	try
    	{
       buttonClass test  = new buttonClass();
       test.setButton(true);
      
       assertEquals(true,test.getButton());
    	
    	
    	}
    	catch (Exception e) {

            assertTrue(false);
		}
    }

}