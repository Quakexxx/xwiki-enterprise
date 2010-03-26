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
package com.xpn.xwiki.it.selenium;

import com.xpn.xwiki.it.selenium.framework.AbstractWysiwygTestCase;

public class StandardFeaturesTest extends AbstractWysiwygTestCase
{
    public void testEmptyWysiwyg()
    {
        switchToSource();
        // NOTE: The new line character is there because the content of a new XWiki document is "\n" instead of "".
        // See XWIKI-4640: New XWiki 2.0 pages contain a new line character
        assertSourceText("\n");
    }

    public void testTypingAndDeletion()
    {
        String text = "foobar";
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText(text);
        assertContent(text);
        typeBackspace(text.length());
        testEmptyWysiwyg();
    }

    public void testBold()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickBoldButton();
        assertContent("<h5><strong>foobar</strong></h5>");
    }

    public void testItalics()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickItalicsButton();
        assertContent("<h5><em>foobar</em></h5>");
    }

    public void testUnderline()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickUnderlineButton();
        assertContent("<h5><ins>foobar</ins></h5>");
    }

    public void testStrikethrough()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickStrikethroughButton();
        assertContent("<h5><del>foobar</del></h5>");
    }

    public void testSubscript()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickSubscriptButton();
        assertContent("<h5><sub>foobar</sub></h5>");
    }

    public void testSuperscript()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        applyStyleTitle5();
        selectElement("h5", 1);
        clickSuperscriptButton();
        assertContent("<h5><sup>foobar</sup></h5>");
    }

    public void testUnorderedList()
    {
        // Create a list with 3 items
        typeTextThenEnter("a");
        typeTextThenEnter("b");
        // We press Enter here to be sure there's no bogus BR after the typed text.
        typeTextThenEnter("c");
        // Delete the empty line which was created only to avoid bogus BRs. See XWIKI-2732.
        typeBackspace();
        selectAllContent();
        clickUnorderedListButton();
        assertContent("<ul><li>a</li><li>b</li><li>c</li></ul>");

        // Undo
        clickUnorderedListButton();
        assertContent("a<br>b<br>c");

        // Create a list with 1 item and delete it
        resetContent();
        typeText("a");
        selectAllContent();
        clickUnorderedListButton();
        typeBackspace(2);
        testEmptyWysiwyg();
    }

    public void testOrderedList()
    {
        // Create a list with 3 items
        typeTextThenEnter("a");
        typeTextThenEnter("b");
        // We press Enter here to be sure there's no bogus BR after the typed text.
        typeTextThenEnter("c");
        // Delete the empty line which was created only to avoid bogus BRs. See XWIKI-2732.
        typeBackspace();
        selectAllContent();
        clickOrderedListButton();
        assertContent("<ol><li>a</li><li>b</li><li>c</li></ol>");

        // Undo
        clickOrderedListButton();
        assertContent("a<br>b<br>c");

        // Create a list with 1 item and delete it
        resetContent();
        typeText("a");
        selectAllContent();
        clickOrderedListButton();
        typeBackspace(2);
        testEmptyWysiwyg();
    }

    public void testStyle()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("foobar");
        selectAllContent();

        applyStyleTitle1();
        assertContent("<h1>foobar</h1>");

        applyStyleTitle2();
        assertContent("<h2>foobar</h2>");

        applyStyleTitle3();
        assertContent("<h3>foobar</h3>");

        applyStyleTitle4();
        assertContent("<h4>foobar</h4>");

        applyStyleTitle5();
        assertContent("<h5>foobar</h5>");

        applyStyleTitle6();
        assertContent("<h6>foobar</h6>");

        applyStylePlainText();
        assertContent("<p>foobar</p>");
    }

    /**
     * @see XWIKI-2949: A separator (HR) inserted at the beginning of a document is badly displayed and difficult to
     *      remove
     */
    public void testHR()
    {
        clickHRButton();
        // Create a heading and then delete it just to remove the bogus BR at the end.
        applyStyleTitle1();
        typeBackspace();
        // We don't switch to source because we want to see if the Backspace works.
        assertContent("<hr>");

        // Strange but we need to type Backspace twice although there's nothing else besides the horizontal ruler.
        typeBackspace(2);
        testEmptyWysiwyg();
        switchToWysiwyg();

        typeText("foobar");
        applyStyleTitle1();
        // Type Enter then Backspace to remove the bogus BR at the end.
        typeEnter();
        typeBackspace();
        // Since the left arrow key doesn't move the caret we have to use the Range API instead.
        moveCaret("XWE.body.firstChild.firstChild", 3);
        clickHRButton();
        assertContent("<h1>foo</h1><hr><h1>bar</h1>");
    }

    /**
     * @see XWIKI-3012: Exception when opening a WYSIWYG dialog in FF2.0
     * @see XWIKI-2992: Place the caret after the inserted symbol
     * @see XWIKI-3682: Trademark symbol is not displayed correctly.
     */
    public void testInsertSymbol()
    {
        clickSymbolButton();
        getSelenium().click("//div[@title='copyright sign']");
        clickSymbolButton();
        closeDialog();
        clickSymbolButton();
        getSelenium().click("//div[@title='registered sign']");
        clickSymbolButton();
        getSelenium().click("//div[@title='trade mark sign']");
        switchToSource();
        assertSourceText("\u00A9\u00AE\u2122");
    }

    /**
     * The rich text area should remain focused and the text shouldn't be changed.
     * 
     * @see XWIKI-3043: Prevent tab from moving focus from the new WYSIWYG editor
     */
    public void testTabDefault()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("a");
        typeTab();
        typeText("b");
        typeShiftTab();
        typeText("c");
        assertContent("a&nbsp;&nbsp;&nbsp; bc");
    }

    /**
     * The list item should be indented or outdented depending on the Shift key.
     * 
     * @see XWIKI-3043: Prevent tab from moving focus from the new WYSIWYG editor
     */
    public void testTabInListItem()
    {
        // Select all content to overwrite the bogus BR.
        selectAllContent();
        typeText("x");
        typeShiftEnter();
        // "y" (lower case only) is misinterpreted.
        // See http://jira.openqa.org/browse/SIDE-309
        // See http://jira.openqa.org/browse/SRC-385
        typeText("Y");
        selectAllContent();
        clickUnorderedListButton();
        // Since the left arrow key doesn't move the caret we have to use the Range API instead.
        moveCaret("XWE.body.firstChild.childNodes[1].firstChild", 0);
        typeTab();
        assertContent("<ul><li>x<ul><li>Y</li></ul></li></ul>");
        typeShiftTab();
        assertContent("<ul><li>x</li><li>Y</li></ul>");
    }

    /**
     * @see XWIKI-2735: Clicking on the space between two lines hides the cursor
     */
    public void testEmptyLinesAreEditable()
    {
        switchToSource();
        setSourceText("a\n\n\n\nb");
        switchToWysiwyg();
        // TODO: Since neither the down arrow key nor the click doesn't seem to move the caret we have to find another
        // way of placing the caret on the empty lines, without using the Range API.
        // For the moment let's just test if the empty lines contain a BR.
        assertContent("<p>a</p><p><br></p><p><br></p><p>b</p>");
    }

    /**
     * @see XWIKI-3039: Changes are lost if an exception is thrown during saving
     */
    public void testRecoverAfterConversionException()
    {
        // We removed the startwikilink comment to force a parsing failure.
        String html = "<span class=\"wikiexternallink\"><a href=\"mailto:x@y.z\">xyz</a></span><!--stopwikilink-->";
        setContent(html);
        // Test to see if the HTML was accepted by the rich text area.
        assertContent(html);
        // Let's see what happens when we save an continue.
        clickEditSaveAndContinue();
        // The user shouldn't loose his changes.
        assertContent(html);
    }

    /**
     * @see XWIKI-2732: Unwanted BR tags
     */
    public void testUnwantedBRsAreRemoved()
    {
        typeText("a");
        typeShiftEnter();
        typeText("b");
        typeShiftEnter();
        switchToSource();
        assertSourceText("a\nb\\\\");
    }

    /**
     * @see XWIKI-3138: WYSIWYG 2.0 Preview Error
     */
    public void testPreview()
    {
        typeText("x");
        selectAllContent();
        clickBoldButton();
        clickEditPreview();
        clickBackToEdit();
        switchToSource();
        assertSourceText("**x**");
    }

    /**
     * @see XWIKI-2993: Insert horizontal line on a selection of unordered list.
     */
    public void testInsertHRInPlaceOfASelectedList()
    {
        typeTextThenEnter("foo");
        typeText("bar");
        selectAllContent();
        clickUnorderedListButton();
        clickHRButton();
        switchToSource();
        assertSourceText("----");
    }

    /**
     * @see XWIKI-3053: When a HR is inserted at the beginning of a paragraph an extra empty paragraph is generated
     *      before that HR
     */
    public void testInsertHRInsideParagraph()
    {
        // "y" (lower case only) is misinterpreted.
        // See http://jira.openqa.org/browse/SIDE-309
        // See http://jira.openqa.org/browse/SRC-385
        typeText("xY");
        applyStyleTitle1();
        applyStylePlainText();

        // Insert HR at the end of the paragraph.
        clickHRButton();

        // Move the caret between x and Y.
        moveCaret("XWE.body.firstChild.firstChild", 1);

        // Insert HR in the middle of the paragraph.
        clickHRButton();

        // Move the caret before x.
        moveCaret("XWE.body.firstChild.firstChild", 0);

        // Insert HR at the beginning of the paragraph.
        clickHRButton();

        // We have to assert the HTML content because the arrow keys don't move the caret so we can't test if the user
        // can edit the generated empty paragraphs. The fact that they contain a BR proves this.
        assertContent("<p><br></p><hr><p>x</p><hr><p>Y</p><hr><p><br></p>");
    }

    /**
     * @see XWIKI-3191: New lines at the end of list items are not preserved by the wysiwyg
     */
    public void testNewLinesAtTheEndOfListItemsArePreserved()
    {
        String sourceText = "* \\\\\n** \\\\\n*** test1";
        switchToSource();
        setSourceText(sourceText);
        switchToWysiwyg();
        switchToSource();
        assertSourceText(sourceText);
    }

    /**
     * @see XWIKI-3194: Cannot remove just one text style when using the style attribute instead of formatting tags
     */
    public void testRemoveBoldStyleWhenTheStyleAttributeIsUsed()
    {
        switchToSource();
        setSourceText("hello (% style=\"font-weight: bold; font-family: monospace;\" %)vincent(%%) world");
        switchToWysiwyg();

        // Select the word in bold.
        selectNodeContents("XWE.body.firstChild.childNodes[1]");
        waitForBoldDetected(true);

        // Remove the bold style.
        clickBoldButton();
        waitForBoldDetected(false);

        // Check the XWiki syntax.
        switchToSource();
        assertSourceText("hello (% style=\"font-weight: normal; font-family: monospace;\" %)vincent(%%) world");
    }

    /**
     * @see XWIKI-2997: Cannot un-bold a text with style Title 1
     */
    public void testRemoveBoldStyleWithinHeading()
    {
        // Insert a heading and make sure it has bold style.
        switchToSource();
        setSourceText("(% style=\"font-weight: bold;\" %)\n= Title 1 =");
        switchToWysiwyg();

        // Select a part of the heading.
        select("XWE.body.firstChild.firstChild.firstChild", 3, "XWE.body.firstChild.firstChild.firstChild", 5);
        waitForBoldDetected(true);

        // Remove the bold style.
        clickBoldButton();
        waitForBoldDetected(false);

        // Check the XWiki syntax.
        switchToSource();
        assertSourceText("(% style=\"font-weight: bold;\" %)\n= Tit(% style=\"font-weight: normal;\" %)le(%%) 1 =");
    }

    /**
     * @see XWIKI-3111: A link to an email address can be removed by removing the underline style
     */
    public void testRemoveUnderlineStyleFromALink()
    {
        // Insert a link to an email address.
        switchToSource();
        setSourceText("[[foo>>mailto:x@y.z||title=\"bar\"]]");
        switchToWysiwyg();

        // Select the text of the link.
        selectNode("XWE.body.getElementsByTagName('a')[0]");
        waitForUnderlineDetected(true);

        // Try to remove the underline style.
        clickUnderlineButton();
        // The underline style is still present although we changed the value of the text-decoration property. I don't
        // think we can do something about this.
        waitForUnderlineDetected(true);

        // Check the XWiki syntax.
        switchToSource();
        assertSourceText("[[foo>>mailto:x@y.z||style=\"text-decoration: none;\" title=\"bar\"]]");
    }

    /**
     * Tests if the state of the tool bar buttons is updated immediately after the editor finished loading.
     */
    public void testToolBarIsUpdatedOnLoad()
    {
        clickLinkWithText("Wiki");
        setFieldValue("content", "**__abc__**");
        clickLinkWithText("WYSIWYG");
        waitForEditorToLoad();
        waitForBoldDetected(true);
        waitForUnderlineDetected(true);

        switchToSource();
        setSourceText("**abc**");
        switchToWysiwyg();
        waitForBoldDetected(true);
        waitForUnderlineDetected(false);
    }

    /**
     * @see XWIKI-2669: New WYSIWYG editor doesn't work when special characters are entered by the user.
     */
    public void testHTMLSpecialChars()
    {
        typeText("<\"'&#'\">");
        switchToSource();
        assertSourceText("<\"'&#'\">");

        // Change the source to force a conversion.
        setSourceText("<'\"&#\"'>");
        switchToWysiwyg();
        assertContent("<p>&lt;'\"&amp;#\"'&gt;</p>");

        applyStyleTitle1();
        switchToSource();
        assertSourceText("= <'\"&#\"'> =");
    }

    /**
     * @see XWIKI-4033: When saving after section edit entire page is overwritten.
     */
    public void testSectionEditing()
    {
        // Save the current location to be able to get back to it later.
        String location = getSelenium().getLocation();

        // Create two sections.
        switchToSource();
        // Set the content directly.
        setFieldValue("content", "= s1 =\n\nabc\n\n= s2 =\n\nxyz");
        clickEditSaveAndView();

        // Edit the second section.
        open(location + (location.indexOf('?') < 0 ? "?" : "") + "&section=2");
        waitForEditorToLoad();
        typeDelete(2);
        typeText("Section 2");
        switchToSource();
        assertSourceText("= Section 2 =\n\nxyz");
        clickEditSaveAndView();

        // Check the content of the page.
        open(location);
        waitForEditorToLoad();
        switchToSource();
        assertSourceText("= s1 =\n\nabc\n\n= Section 2 =\n\nxyz");
    }

    /**
     * @see XWIKI-4335: Typing ">" + text in wysiwyg returns a quote
     */
    public void testQuoteSyntaxIsEscaped()
    {
        typeText("> 1");
        switchToSource();
        switchToWysiwyg();
        assertEquals("> 1", getEval("window.XWE.body.textContent"));
    }

    /**
     * @see XWIKI: Problems removing italics from a definition.
     */
    public void testRemoveItalicsFromDefinition()
    {
        switchToSource();
        // Make sure the definitions are displayed with italics to avoid depending on the current skin.
        setSourceText("(% style=\"font-style: italic;\" %)\n(((\n; term1\n: definition1\n:; term2\n:: definition2\n)))");
        switchToWysiwyg();
        selectNodeContents("XWE.document.getElementsByTagName('dd')[0].firstChild");
        clickItalicsButton();
        switchToSource();
        assertSourceText("(% style=\"font-style: italic;\" %)\n(((\n; term1\n"
            + ": (% style=\"font-style: normal;\" %)definition1(%%)\n:; term2\n:: definition2\n)))");
    }

    /**
     * @see XWIKI-4364: Verbatim blocks suffer corruption when previewed using the GWT editor
     */
    public void testMultiLineVerbatimBlock()
    {
        switchToSource();
        String multiLineVerbatimBlock = "{{{a verbatim block\nwhich is multiline}}}";
        setSourceText(multiLineVerbatimBlock);
        switchToWysiwyg();
        switchToSource();
        assertSourceText(multiLineVerbatimBlock);
    }

    /**
     * @see XWIKI-4399: ((( ))) looses class or style definitions when you edit in WYSIWYG.
     */
    public void testGroupStyleIsPreserved()
    {
        switchToSource();
        String styledGroup = "(% class=\"abc\" style=\"color: red;\" %)\n(((\ntext\n)))";
        setSourceText(styledGroup);
        switchToWysiwyg();
        switchToSource();
        assertSourceText(styledGroup);
    }

    /**
     * @see XWIKI-4529: XWiki velocity variables are undefined when the edited content is rendered.
     */
    public void testXWikiVarsAreDefined()
    {
        switchToSource();
        setSourceText("{{velocity}}#if($hasEdit)1#{else}2#end{{/velocity}}");
        switchToWysiwyg();
        assertEquals("velocity1", getEval("window.XWE.body.textContent"));
    }

    /**
     * @see XWIKI-4665: Pressing Meta+G (Jump to page) in the WYSIWYG editor displays the popup inside the rich text
     *      area.
     */
    public void testJavaScriptExtensionsAreNotIncludedInEditMode()
    {
        // Type some text to be sure the conversion is triggered when switching to source.
        typeText("x");
        // Type Meta+G to open the "Jump to page" dialog.
        getSelenium().metaKeyDown();
        typeText("g");
        getSelenium().metaKeyUp();
        // Switch to source and check the result.
        switchToSource();
        assertSourceText("x");
        // Now check that the "Jump to page" feature works indeed.
        assertElementNotPresent("//div[@class = 'xdialog-modal-container']");
        getSelenium().metaKeyDown();
        getSelenium().typeKeys(WYSIWYG_LOCATOR_FOR_SOURCE_TEXTAREA, "g");
        getSelenium().metaKeyUp();
        assertElementPresent("//div[@class = 'xdialog-modal-container']");
    }

    /**
     * @see XWIKI-4346: Cannot use the WYSIWYG editor in small screen after you have deleted all text in full screen
     */
    public void testEditInFullScreen()
    {
        typeText("123");
        clickEditInFullScreen();
        // The caret is placed at start (which means the selection is lost).
        typeDelete(3);
        clickExitFullScreen();
        typeText("x");
        switchToSource();
        assertSourceText("x");
    }

    /**
     * @see XWIKI-5036: WYSIWYG editor doesn't display when creating a document named #"&§-_\
     */
    public void testEditPageWithSpecialSymbolsInName()
    {
        clickEditCancelEdition();
        clickLinkWithLocator("tmCreatePage");
        waitPage();
        setFieldValue("page", "#\"&\u00A7-_\\");
        getSelenium().click("//input[@value = 'Create']");
        waitPage();
        waitForEditorToLoad();
        typeText("This is a test");
        clickEditPreview();
        assertTextPresent("This is a test");
    }
}
