/**
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.google.spreadsheet.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.module.google.spreadsheet.automation.RegressionTests;
import org.mule.module.google.spreadsheet.model.Worksheet;
import org.mule.modules.tests.ConnectorTestUtils;

public class UpdateWorksheetMetadataTestCases extends GoogleSpreadsheetsTestParent {

    private String spreadsheetTitle;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("updateWorksheetMetadata");

        spreadsheetTitle = getTestRunMessageValue("spreadsheet");
        createSpreadsheet(spreadsheetTitle);

        createWorksheet();
    }

    @Category({RegressionTests.class})
    @Test
    public void testUpdateWorksheetMetadata() {
        try {
            String updatedTitle = getTestRunMessageValue("updatedTitle");
            int updatedColCount = (Integer)getTestRunMessageValue("updatedColCount");
            int updatedRowCount = (Integer)getTestRunMessageValue("updatedRowCount");

            upsertOnTestRunMessage("title", updatedTitle);
            upsertOnTestRunMessage("rowCount", updatedRowCount);
            upsertOnTestRunMessage("colCount", updatedColCount);

            runFlowAndGetPayload("update-worksheet-metadata");

            List<Worksheet> retrievedWorksheets = getWorksheetByTitle(spreadsheetTitle, updatedTitle);

            // There should only be one worksheet with this name
            Worksheet updatedWorksheet = retrievedWorksheets.get(0);

            assertEquals(updatedColCount, updatedWorksheet.getColCount());
            assertEquals(updatedRowCount, updatedWorksheet.getRowCount());
            assertEquals(updatedTitle, updatedWorksheet.getTitle());
        } catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        runFlowAndGetPayload("delete-worksheet");
        deleteSpreadsheet(spreadsheetTitle);
    }

}
