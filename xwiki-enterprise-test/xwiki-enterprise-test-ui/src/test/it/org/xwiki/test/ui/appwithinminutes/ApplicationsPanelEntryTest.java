/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.test.ui.appwithinminutes;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.xwiki.appwithinminutes.test.po.ApplicationHomeEditPage;
import org.xwiki.panels.test.po.ApplicationsPanel;
import org.xwiki.test.ui.AbstractAdminAuthenticatedTest;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Tests the applications panel entry. This test needs its own class because it needs to be in a separated space in the
 * wiki. In the other test classes we create one application per method, in the same space, which leads to duplicate
 * entries in the panel.
 * 
 * @version $Id$
 * @since 4.3RC1
 */
public class ApplicationsPanelEntryTest extends AbstractAdminAuthenticatedTest
{
    /**
     * The page being tested.
     */
    private ApplicationHomeEditPage editPage;

    /**
     * The query string parameters passed to the edit action.
     */
    private final Map<String, String> editQueryStringParameters = new HashMap<String, String>();

    @Before
    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        getUtil().deletePage(getTestClassName(), getTestMethodName());
        editQueryStringParameters.put("editor", "inline");
        editQueryStringParameters.put("template", "AppWithinMinutes.LiveTableTemplate");
        editQueryStringParameters.put("AppWithinMinutes.LiveTableClass_0_class", "XWiki.XWikiUsers");
        getUtil().gotoPage(getTestClassName(), getTestMethodName(), "edit", editQueryStringParameters);
        editPage = new ApplicationHomeEditPage().waitUntilPageIsLoaded();
    }

    @Test
    public void testApplicationPanelEntry()
    {
        // Test the icon remains the same between edits
        editPage.setIcon("icon:bell");
        editPage.clickSaveAndView();
        getUtil().gotoPage(getTestClassName(), getTestMethodName(), "edit");
        Assert.assertEquals("icon:bell", editPage.getIcon());

        ApplicationsPanel panel = ApplicationsPanel.gotoPage();
        ViewPage page = panel.clickApplication(getTestClassName());
        // Verify we're on the right page!
        Assert.assertEquals(getTestClassName(), page.getMetaDataValue("space"));
        Assert.assertEquals(getTestMethodName(), page.getMetaDataValue("page"));
    }
}
