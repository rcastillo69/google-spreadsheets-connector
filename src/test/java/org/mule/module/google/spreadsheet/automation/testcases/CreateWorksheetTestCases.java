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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.module.google.spreadsheet.automation.RegressionTests;
import org.mule.module.google.spreadsheet.automation.SmokeTests;
import org.mule.module.google.spreadsheet.model.Worksheet;
import org.mule.modules.tests.ConnectorTestUtils;


public class CreateWorksheetTestCases extends GoogleSpreadsheetsTestParent {

    private String spreadsheetTitle;
    private String worksheetTitle;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("createWorksheet");

        spreadsheetTitle = getTestRunMessageValue("spreadsheet");
        createSpreadsheet(spreadsheetTitle);
    }

    @Test
    @Category({SmokeTests.class, RegressionTests.class})
    public void testCreateWorksheet() {
        try {
            worksheetTitle = getTestRunMessageValue("worksheet");

            Worksheet worksheet = runFlowAndGetPayload("create-worksheet");
            assertNotNull(worksheet);
            assertEquals(worksheet.getName(), worksheetTitle);
            assertEquals(worksheet.getRowCount(), getTestRunMessageValue("rowCount"));
            assertEquals(worksheet.getColCount(), getTestRunMessageValue("colCount"));
        } catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        deleteWorksheet(spreadsheetTitle, worksheetTitle);
        deleteSpreadsheet(spreadsheetTitle);
    }

}
