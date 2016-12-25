import junit.framework.TestCase;
import java.awt.*;
import java.util.*;

/**
 * @author Horatiu Lazu
 * @version 1.0
 */
public class ContentPanelTest extends TestCase {
    /** CONTENT_PANEL ContentPanel This is a three variable content panel */
    private final ContentPanel CONTENT_PANEL = new ContentPanel(3);
    /** CONTENT_PANEL_EXTENDED ContentPanel This is a four variable content panel */
    private final ContentPanel CONTENT_PANEL_EXTENDED = new ContentPanel(4);

    /** Tests the getVar() access method (getter)
     * @throws Exception
     */
    public void testGetVar() throws Exception {
        assertEquals(CONTENT_PANEL.getVar(), 3);
    }

    /**
     * Verifies if expression simplification simplifies correctly and appears on JFrame.
     * This requires X11 compatibility to run on Travis CI, hence only run on OSs with GUI.
     * @see: Build #64 / UnitTests:
     * No X11 DISPLAY variable was set, but this program performed an operation which requires it.
     * @throws Exception
     */
    public void testGetSimplifiedExpression() throws Exception {
        try {
            // Test A (no grouping)
            new Frame(false);
            CONTENT_PANEL.getSimplifiedExpression(new LinkedList<Grouping>());
            assertEquals(Frame.ans[CONTENT_PANEL.getVar() - 2].getText(), "False");
            LinkedList<Grouping> groupingList = new LinkedList<Grouping>();

            // Test B (one large grouping)
            groupingList.add(new Grouping(0, 0, 1, 1));
            CONTENT_PANEL.getSimplifiedExpression(groupingList);
            assertEquals(Frame.ans[CONTENT_PANEL.getVar() - 2].getText(), "A'");

            // Test C (one horizontal grouping)
            groupingList = new LinkedList<Grouping>();
            groupingList.add(new Grouping(0, 1, 1, 1));
            CONTENT_PANEL.getSimplifiedExpression(groupingList);
            assertEquals(Frame.ans[CONTENT_PANEL.getVar() - 2].getText(), "A'B");

            // Test D (one vertical grouping)
            groupingList = new LinkedList<Grouping>();
            groupingList.add(new Grouping(1, 0, 1, 1));
            CONTENT_PANEL.getSimplifiedExpression(groupingList);
            assertEquals(Frame.ans[CONTENT_PANEL.getVar() - 2].getText(), "A'C");

            // Test E (complete group)
            groupingList = new LinkedList<Grouping>();
            groupingList.add(new Grouping(0, 0, 1, 3));
            CONTENT_PANEL.getSimplifiedExpression(groupingList);
            assertEquals(Frame.ans[CONTENT_PANEL.getVar() - 2].getText(), "True");


            /* Four Variable Tests */

            FourVariableTest fVT = new FourVariableTest();
            HashMap<LinkedList<CompoundedGroupings>, String> fourVariableTests = fVT.getTests();

            // Execute tests from fourVariableTests
            for(LinkedList<CompoundedGroupings> compoundedGroups : fourVariableTests.keySet()){
                CONTENT_PANEL_EXTENDED.getSimplifiedExpression(compoundedGroups.get(0).getGroups());
                assertEquals(Frame.ans[CONTENT_PANEL_EXTENDED.getVar() - 2].getText(), fourVariableTests.get(compoundedGroups));
            }

        }
        catch(HeadlessException e){
            assert(true);
        }
    }
}