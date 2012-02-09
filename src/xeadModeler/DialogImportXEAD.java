package xeadModeler;

/*
 * Copyright (c) 2012 WATANABE kozo <qyf05466@nifty.com>,
 * All rights reserved.
 *
 * This file is part of XEAD Modeler.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the XEAD Project nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class DialogImportXEAD extends JDialog {
	private static final long serialVersionUID = 1L;
	static ResourceBundle res = ResourceBundle.getBundle("xeadModeler.Res");
	//
	JPanel panelMain = new JPanel();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JPanel jPanel1 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JRadioButton jRadioButtonTablesAndFunctions = new JRadioButton();
	JRadioButton jRadioButtonTables = new JRadioButton();
	JRadioButton jRadioButtonFunctions = new JRadioButton();
	JRadioButton jRadioButtonTasks = new JRadioButton();
	ButtonGroup buttonGroup1 = new ButtonGroup();
	JLabel jLabel3 = new JLabel();
	JProgressBar jProgressBar = new JProgressBar();
	JButton jButtonStart = new JButton();
	JButton jButtonCancel = new JButton();
	SortableXeadNodeComboBoxModel comboBoxModelBlockFrom = new SortableXeadNodeComboBoxModel();
	SortableXeadNodeComboBoxModel comboBoxModelBlockInto = new SortableXeadNodeComboBoxModel();
	JComboBox jComboBoxBlockFrom = new JComboBox(comboBoxModelBlockFrom);
	JComboBox jComboBoxBlockInto = new JComboBox(comboBoxModelBlockInto);
	JTextArea jTextArea1 = new JTextArea();
	JLabel jLabel4 = new JLabel();
	JTextField jTextFieldImportFileName = new JTextField();
	JLabel jLabel5 = new JLabel();
	JTextField jTextFieldImportSystemName = new JTextField();
	//
	String importResult = "";
	Modeler frame_;
	org.w3c.dom.Document domDocumentFrom;
	org.w3c.dom.Element systemElement;
	org.w3c.dom.Element blockElementFrom, blockElementInto;
	String blockIDFrom, blockIDInto;
	FileWriter fileWriter = null;
	BufferedWriter bufferedWriter = null;
	//
	int updateTableCounter, createTableCounter, cancelTableCounter;
	int updateFunctionCounter, createFunctionCounter, cancelFunctionCounter;
	int updateTaskCounter, createTaskCounter, cancelTaskCounter;
	int missingTableCounter, missingTableKeyCounter, missingFunctionCounter, missingFunctionIOCounter;

	public DialogImportXEAD(Modeler frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			frame_ = frame;
			jbInit();
			pack();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public DialogImportXEAD(Modeler frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		this.setResizable(false);
		this.setTitle(res.getString("DialogImportXEAD01"));
		//
		jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel4.setText(res.getString("DialogImportXEAD02"));
		jLabel4.setBounds(new Rectangle(10, 9, 144, 15));
		jTextFieldImportFileName.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldImportFileName.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldImportFileName.setBounds(new Rectangle(164, 9, 197, 21));
		jTextFieldImportFileName.setEditable(false);
		//
		jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel5.setText(res.getString("DialogImportXEAD03"));
		jLabel5.setBounds(new Rectangle(10, 33, 144, 15));
		jTextFieldImportSystemName.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldImportSystemName.setBounds(new Rectangle(164, 33, 197, 21));
		jTextFieldImportSystemName.setEditable(false);
		//
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		//jPanel1.setBounds(new Rectangle(164, 83, 197, 60));
		jPanel1.setBounds(new Rectangle(164, 57, 197, 87));
		jPanel1.setLayout(gridLayout1);
		gridLayout1.setColumns(1);
		gridLayout1.setRows(4);
		jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel3.setText(res.getString("DialogImportXEAD04"));
		jLabel3.setBounds(new Rectangle(12, 57, 141, 15));
		jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
		jRadioButtonTablesAndFunctions.setFont(new java.awt.Font("Dialog", 0, 12));
		jRadioButtonTablesAndFunctions.setText(res.getString("DialogImportXEAD05"));
		jRadioButtonTablesAndFunctions.addChangeListener(new DialogImportXEAD_jRadioButton_changeAdapter(this));
		jRadioButtonTables.setFont(new java.awt.Font("Dialog", 0, 12));
		jRadioButtonTables.setText(res.getString("DialogImportXEAD46"));
		jRadioButtonTables.addChangeListener(new DialogImportXEAD_jRadioButton_changeAdapter(this));
		jRadioButtonFunctions.setFont(new java.awt.Font("Dialog", 0, 12));
		jRadioButtonFunctions.setText(res.getString("DialogImportXEAD44"));
		jRadioButtonFunctions.addChangeListener(new DialogImportXEAD_jRadioButton_changeAdapter(this));
		jRadioButtonTasks.setFont(new java.awt.Font("Dialog", 0, 12));
		jRadioButtonTasks.setText(res.getString("DialogImportXEAD06"));
		jRadioButtonTasks.addChangeListener(new DialogImportXEAD_jRadioButton_changeAdapter(this));
		jPanel1.add(jRadioButtonFunctions, null);
		jPanel1.add(jRadioButtonTables, null);
		jPanel1.add(jRadioButtonTablesAndFunctions, null);
		jPanel1.add(jRadioButtonTasks, null);
		buttonGroup1.add(jRadioButtonFunctions);
		buttonGroup1.add(jRadioButtonTables);
		buttonGroup1.add(jRadioButtonTablesAndFunctions);
		buttonGroup1.add(jRadioButtonTasks);
		//
		jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setBounds(new Rectangle(10, 148, 144, 15));
		jComboBoxBlockFrom.setBounds(new Rectangle(164, 148, 197, 21));
		jComboBoxBlockFrom.setFont(new java.awt.Font("Dialog", 0, 12));
		jComboBoxBlockFrom.addActionListener(new DialogImportXEAD_jComboBox_actionAdapter(this));
		//
		jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel2.setBounds(new Rectangle(10, 172, 144, 15));
		jComboBoxBlockInto.setBounds(new Rectangle(164, 172, 197, 21));
		jComboBoxBlockInto.setFont(new java.awt.Font("Dialog", 0, 12));
		jComboBoxBlockInto.addActionListener(new DialogImportXEAD_jComboBox_actionAdapter(this));
		//
		jTextArea1.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextArea1.setForeground(Color.BLUE);
		jTextArea1.setEditable(false);
		jTextArea1.setBounds(new Rectangle(9, 199, 351, 159));
		jTextArea1.setLineWrap(true);
		jTextArea1.setBackground(SystemColor.control);
		jTextArea1.setBorder(BorderFactory.createLoweredBevelBorder());
		//
		jProgressBar.setBounds(new Rectangle(9, 367, 104, 25));
		//
		jButtonStart.setBounds(new Rectangle(132, 367, 104, 25));
		jButtonStart.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonStart.setText(res.getString("DialogImportXEAD07"));
		jButtonStart.addActionListener(new DialogImportXEAD_jButtonStart_actionAdapter(this));
		jButtonStart.setEnabled(false);
		//
		jButtonCancel.setBounds(new Rectangle(255, 367, 104, 25));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText(res.getString("DialogImportXEAD08"));
		jButtonCancel.addActionListener(new DialogImportXEAD_jButtonCancel_actionAdapter(this));
		//
		panelMain.setLayout(null);
		panelMain.setPreferredSize(new Dimension(370, 404));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jLabel1, null);
		panelMain.add(jLabel2, null);
		panelMain.add(jLabel3, null);
		panelMain.add(jLabel4, null);
		panelMain.add(jLabel5, null);
		panelMain.add(jPanel1, null);
		panelMain.add(jTextFieldImportFileName, null);
		panelMain.add(jTextFieldImportSystemName, null);
		panelMain.add(jComboBoxBlockFrom, null);
		panelMain.add(jComboBoxBlockInto, null);
		panelMain.add(jTextArea1, null);
		panelMain.add(jProgressBar, null);
		panelMain.add(jButtonStart, null);
		panelMain.add(jButtonCancel, null);
		//
		this.getContentPane().add(panelMain, BorderLayout.SOUTH);
		//
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public String request(String fileName) {
		NodeList elementList;
		org.w3c.dom.Element element;
		importResult = "";
		//
		domDocumentFrom = null;
		jRadioButtonFunctions.setSelected(true);
		//
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new FileInputStream(fileName)));
			domDocumentFrom = parser.getDocument();
		}
		catch (IOException ex) {
		}
		catch (SAXException ex) {
		}
		//
		jTextFieldImportFileName.setText(fileName);
		elementList = domDocumentFrom.getElementsByTagName("System");
		element = (org.w3c.dom.Element)elementList.item(0);
		jTextFieldImportSystemName.setText(element.getAttribute("Name"));
		jRadioButton_stateChanged();
		jProgressBar.setValue(0);
		//
		super.setVisible(true);
		//
		return importResult;
	}

	void jRadioButton_stateChanged() {
		XeadNode node;
		NodeList elementList;
		//
		if (domDocumentFrom != null) {
			//
			if (jRadioButtonTables.isSelected() || jRadioButtonFunctions.isSelected() || jRadioButtonTablesAndFunctions.isSelected()) {
				//
				jLabel1.setText(res.getString("DialogImportXEAD09"));
				jLabel2.setText(res.getString("DialogImportXEAD10"));
				if (jRadioButtonTables.isSelected()) {
					jTextArea1.setText(res.getString("DialogImportXEAD47"));
				}
				if (jRadioButtonFunctions.isSelected()) {
					jTextArea1.setText(res.getString("DialogImportXEAD45"));
				}
				if (jRadioButtonTablesAndFunctions.isSelected()) {
					jTextArea1.setText(res.getString("DialogImportXEAD11"));
				}
				///////////////////
				//Setup ComboBox1//
				///////////////////
				comboBoxModelBlockFrom.removeAllElements();
				elementList = domDocumentFrom.getElementsByTagName("Subsystem");
				for (int i = 0; i < elementList.getLength(); i++) {
					node = new XeadNode("Subsystem",(org.w3c.dom.Element)elementList.item(i));
					comboBoxModelBlockFrom.addElement((Object)node);
				}
				comboBoxModelBlockFrom.sortElements();
				comboBoxModelBlockFrom.insertElementAt(res.getString("DialogImportXEAD12"), 0);
				comboBoxModelBlockFrom.setSelectedItem(comboBoxModelBlockFrom.getElementAt(0));
				///////////////////
				//Setup ComboBox2//
				///////////////////
				comboBoxModelBlockInto.removeAllElements();
				elementList = frame_.domDocument.getElementsByTagName("Subsystem");
				for (int i = 0; i < elementList.getLength(); i++) {
					node = new XeadNode("Subsystem",(org.w3c.dom.Element)elementList.item(i));
					comboBoxModelBlockInto.addElement((Object)node);
				}
				comboBoxModelBlockInto.sortElements();
				comboBoxModelBlockInto.insertElementAt(res.getString("DialogImportXEAD12"), 0);
				comboBoxModelBlockInto.setSelectedItem(comboBoxModelBlockInto.getElementAt(0));
			}
			//
			if (jRadioButtonTasks.isSelected()) {
				//
				jLabel1.setText(res.getString("DialogImportXEAD13"));
				jLabel2.setText(res.getString("DialogImportXEAD14"));
				jTextArea1.setText(res.getString("DialogImportXEAD15"));
				///////////////////
				//Setup ComboBox1//
				///////////////////
				comboBoxModelBlockFrom.removeAllElements();
				elementList = domDocumentFrom.getElementsByTagName("Role");
				for (int i = 0; i < elementList.getLength(); i++) {
					node = new XeadNode("Role",(org.w3c.dom.Element)elementList.item(i));
					comboBoxModelBlockFrom.addElement((Object)node);
				}
				comboBoxModelBlockFrom.sortElements();
				comboBoxModelBlockFrom.insertElementAt(res.getString("DialogImportXEAD12"), 0);
				comboBoxModelBlockFrom.setSelectedItem(comboBoxModelBlockFrom.getElementAt(0));
				///////////////////
				//Setup ComboBox2//
				///////////////////
				comboBoxModelBlockInto.removeAllElements();
				elementList = frame_.domDocument.getElementsByTagName("Role");
				for (int i = 0; i < elementList.getLength(); i++) {
					node = new XeadNode("Role",(org.w3c.dom.Element)elementList.item(i));
					comboBoxModelBlockInto.addElement((Object)node);
				}
				comboBoxModelBlockInto.sortElements();
				comboBoxModelBlockInto.insertElementAt(res.getString("DialogImportXEAD12"), 0);
				comboBoxModelBlockInto.setSelectedItem(comboBoxModelBlockInto.getElementAt(0));
			}
		}
	}
	
	void jComboBox_actionPerformed(ActionEvent e) {
		if (jComboBoxBlockFrom.getSelectedIndex() != 0 && jComboBoxBlockInto.getSelectedIndex() != 0) {
			jButtonStart.setEnabled(true);
		} else {
			jButtonStart.setEnabled(false);
		}
	}

	void jButtonStart_actionPerformed(ActionEvent e) {
		XeadNode workNode;
		NodeList workElementList;
		String logFileName = "";
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			String dateTime = getStringValueOfDateAndTime();
			logFileName = "XeadImportLog" + dateTime + ".txt";
			fileWriter = new FileWriter(logFileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(res.getString("DialogImportXEAD01") + "(" + dateTime + ")" + "\n");
			bufferedWriter.write("\n");
			bufferedWriter.write(res.getString("DialogImportXEAD02") + ":" + jTextFieldImportFileName.getText() + "\n");
			bufferedWriter.write(res.getString("DialogImportXEAD31") + ":" + frame_.currentFileName + res.getString("DialogImportXEAD41") + "\n");
			bufferedWriter.write("\n");
			if (jRadioButtonTables.isSelected()) {
				bufferedWriter.write(res.getString("DialogImportXEAD04") + ":" + res.getString("DialogImportXEAD46") + "\n");
			}
			if (jRadioButtonFunctions.isSelected()) {
				bufferedWriter.write(res.getString("DialogImportXEAD04") + ":" + res.getString("DialogImportXEAD44") + "\n");
			}
			if (jRadioButtonTablesAndFunctions.isSelected()) {
				bufferedWriter.write(res.getString("DialogImportXEAD04") + ":" + res.getString("DialogImportXEAD05") + "\n");
			}
			if (jRadioButtonTasks.isSelected()) {
				bufferedWriter.write(res.getString("DialogImportXEAD04") + ":" + res.getString("DialogImportXEAD06") + "\n");
			}
			bufferedWriter.write(res.getString("DialogImportXEAD03") + ":" + jTextFieldImportSystemName.getText() + "\n");
			bufferedWriter.write(res.getString("DialogImportXEAD32") + ":" + frame_.systemName + "\n");
			bufferedWriter.write("\n");
			//
			updateTableCounter = 0;
			createTableCounter = 0;
			cancelTableCounter = 0;
			updateFunctionCounter = 0;
			createFunctionCounter = 0;
			cancelFunctionCounter = 0;
			updateTaskCounter = 0;
			createTaskCounter = 0;
			cancelTaskCounter = 0;
			missingTableCounter = 0;
			missingFunctionCounter = 0;
			missingFunctionIOCounter = 0;
			//
			workNode = (XeadNode)comboBoxModelBlockFrom.getSelectedItem();
			blockElementFrom = workNode.getElement();
			blockIDFrom = blockElementFrom.getAttribute("ID");
			bufferedWriter.write(jLabel1.getText() + ":" + blockElementFrom.getAttribute("Name") + "(" + blockElementFrom.getAttribute("SortKey") + ")" + "\n");
			//
			workNode = (XeadNode)comboBoxModelBlockInto.getSelectedItem();
			blockElementInto = workNode.getElement();
			blockIDInto = blockElementInto.getAttribute("ID");
			bufferedWriter.write(jLabel2.getText() + ":" + blockElementInto.getAttribute("Name") + "(" + blockElementInto.getAttribute("SortKey") + ")" + "\n");
			//
			workElementList = frame_.domDocument.getElementsByTagName("System");
			systemElement = (org.w3c.dom.Element)workElementList.item(0);
			//
			if (jRadioButtonTables.isSelected()) {
				//
				importSubsystem();
				//
				importTables();
				//
				importSubsystemAttributesOfTable();
				//
				importResult = res.getString("DialogImportXEAD16") +
				updateTableCounter + res.getString("DialogImportXEAD17") +
				createTableCounter + res.getString("DialogImportXEAD18") +
				cancelTableCounter + res.getString("DialogImportXEAD50") + "\n" +
				res.getString("DialogImportXEAD27") + missingTableKeyCounter + res.getString("DialogImportXEAD23") + "\n" +
				res.getString("DialogImportXEAD29") + logFileName + res.getString("DialogImportXEAD30") + "\n" + "\n";
			}
			//
			if (jRadioButtonFunctions.isSelected()) {
				//
				importSubsystem();
				//
				importFunctions();
				//
				importResult = res.getString("DialogImportXEAD19") +
				updateFunctionCounter + res.getString("DialogImportXEAD17") +
				createFunctionCounter + res.getString("DialogImportXEAD18") +
				cancelFunctionCounter + res.getString("DialogImportXEAD50") + "\n" +
				res.getString("DialogImportXEAD20") + missingTableCounter + res.getString("DialogImportXEAD21") + "\n" + res.getString("DialogImportXEAD22") + missingFunctionCounter + res.getString("DialogImportXEAD23") + "\n" +
				res.getString("DialogImportXEAD29") + logFileName + res.getString("DialogImportXEAD30") + "\n" + "\n";
			}
			//
			if (jRadioButtonTablesAndFunctions.isSelected()) {
				//
				importSubsystem();
				//
				importTables();
				//
				importSubsystemAttributesOfTable();
				//
				importFunctions();
				//
				importResult = res.getString("DialogImportXEAD16") +
				updateTableCounter + res.getString("DialogImportXEAD17") +
				createTableCounter + res.getString("DialogImportXEAD18") +
				cancelTableCounter + res.getString("DialogImportXEAD50") + "\n" +
				res.getString("DialogImportXEAD27") +
				missingTableKeyCounter + res.getString("DialogImportXEAD23") + "\n" +
				res.getString("DialogImportXEAD19") +
				updateFunctionCounter + res.getString("DialogImportXEAD17") +
				createFunctionCounter + res.getString("DialogImportXEAD18") +
				cancelFunctionCounter + res.getString("DialogImportXEAD50") + "\n" +
				res.getString("DialogImportXEAD20") +
				missingTableCounter + res.getString("DialogImportXEAD21") + "\n" +
				res.getString("DialogImportXEAD22") +
				missingFunctionCounter + res.getString("DialogImportXEAD23") + "\n" +
				res.getString("DialogImportXEAD29") + logFileName + res.getString("DialogImportXEAD30") + "\n" + "\n";
			}
			//
			if (jRadioButtonTasks.isSelected()) {
				//
				importRole();
				//
				importTasks();
				//
				importResult = res.getString("DialogImportXEAD24") +
				updateTaskCounter + res.getString("DialogImportXEAD17") +
				createTaskCounter + res.getString("DialogImportXEAD18") +
				cancelTaskCounter + res.getString("DialogImportXEAD50") + "\n" +
				res.getString("DialogImportXEAD25") +
				missingFunctionIOCounter + res.getString("DialogImportXEAD26") + "\n" +
				res.getString("DialogImportXEAD29") + logFileName + res.getString("DialogImportXEAD30") + "\n" + "\n";
			}
			//
		}
		catch (Exception ex1) {
			try {
				bufferedWriter.close();
			}
			catch (Exception ex2) {
			}
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			this.setVisible(false);
		}
		//
		try {
			bufferedWriter.flush();
			bufferedWriter.close();
		}
		catch (Exception ex3) {
			try {
				bufferedWriter.close();
			}
			catch (Exception ex4) {
			}
		}
	}

	void importTables() {
		org.w3c.dom.Element workElement;
		NodeList workElementList, targetElementList;
		org.w3c.dom.Element[] elementListFrom = new org.w3c.dom.Element[500];
		int itemsOfElementListFrom = 0;
		int checkCounter = 0;
		String sortKeyOfDefinition;
		//
		//////////////////////////
		//Import Table/Data Type//
		//////////////////////////
		importTypeDefinitionWithTagName("TableType");
		importTypeDefinitionWithTagName("DataType");
		//
		///////////////////////////////////////
		//Setup List of Tables to be imported//
		///////////////////////////////////////
		workElementList = domDocumentFrom.getElementsByTagName("Table");
		for (int i = 0; i < workElementList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)workElementList.item(i);
			if (!workElement.getAttribute("SortKey").equals("") && workElement.getAttribute("SubsystemID").equals(blockIDFrom)) {
				elementListFrom[itemsOfElementListFrom] = workElement;
				itemsOfElementListFrom++;
			}
		}
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(itemsOfElementListFrom);
		//
		try {
			bufferedWriter.write("\n");
		}
		catch (IOException ex) {
		}
		//
		/////////////////////////////////////////////////
		//Import Table definitions into target document//
		/////////////////////////////////////////////////
		targetElementList = frame_.domDocument.getElementsByTagName("Table");
		for (int i = 0; i < itemsOfElementListFrom; i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			checkCounter = 0;
			sortKeyOfDefinition = elementListFrom[i].getAttribute("SortKey");
			for (int n = 0; n < targetElementList.getLength(); n++) {
				workElement = (org.w3c.dom.Element)targetElementList.item(n);
				if (workElement.getAttribute("SortKey").equals(sortKeyOfDefinition)) {
					checkCounter++;
					if (workElement.getAttribute("SubsystemID").equals(blockIDInto)) {
						try {
							bufferedWriter.write(elementListFrom[i].getAttribute("Name") +
									"(" +
									elementListFrom[i].getAttribute("SortKey") +
									"):" +
									res.getString("DialogImportXEAD33") +
							"\n");
						}
						catch (IOException ex1) {
						}
						updateTableDefinition(elementListFrom[i], workElement);
						updateTableCounter++;
					} else {
						try {
							bufferedWriter.write(elementListFrom[i].getAttribute("Name") +
									"(" +
									elementListFrom[i].getAttribute("SortKey") +
									"):" +
									res.getString("DialogImportXEAD48") +
							"\n");
						}
						catch (IOException ex1) {
						}
						cancelTableCounter++;
					}
					break;
				}
			}
			//
			if (checkCounter == 0) {
				createTableDefinition(elementListFrom[i], blockIDInto);
				createTableCounter++;
				try {
					bufferedWriter.write(elementListFrom[i].getAttribute("Name") + "(" +
							elementListFrom[i].getAttribute("SortKey") +
							"):" +
							res.getString("DialogImportXEAD34") +
					"\n");
				}
				catch (IOException ex2) {
				}
			}
		}
		//
		try {
			bufferedWriter.write("\n");
		}
		catch (IOException ex3) {
		}
		//////////////////////////////////////////////////
		//Import Table Relationship into target document//
		//////////////////////////////////////////////////
		String table1ID, table2ID, tableID;
		//
		workElementList = domDocumentFrom.getElementsByTagName("Relationship");
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(workElementList.getLength());
		//
		for (int i = 0; i < workElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			workElement = (org.w3c.dom.Element)workElementList.item(i);
			table1ID = workElement.getAttribute("Table1ID");
			table2ID = workElement.getAttribute("Table2ID");
			checkCounter = 0;
			for (int m = 0; m < itemsOfElementListFrom; m++) {
				tableID = elementListFrom[m].getAttribute("ID");
				if (tableID.equals(table1ID)) {
					checkCounter++;
				}
				if (tableID.equals(table2ID)) {
					checkCounter++;
				}
			}
			if (checkCounter >= 1) {
				adjustTableRelationship(workElement);
			}
		}
	}

	void importFunctions() {
		org.w3c.dom.Element workElement;
		NodeList workElementList, targetElementList;
		org.w3c.dom.Element[] elementListFrom = new org.w3c.dom.Element[500];
		int itemsOfElementListFrom = 0;
		int checkCounter = 0;
		String sortKeyOfDefinition;
		//
		////////////////////////
		//Import Function Type//
		////////////////////////
		importTypeDefinitionWithTagName("FunctionType");
		//
		//////////////////////////////////////////
		//Setup List of Functions to be imported//
		/////////////////////////////////////////
		workElementList = domDocumentFrom.getElementsByTagName("Function");
		for (int i = 0; i < workElementList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)workElementList.item(i);
			if (!workElement.getAttribute("SortKey").equals("") && workElement.getAttribute("SubsystemID").equals(blockIDFrom)) {
				elementListFrom[itemsOfElementListFrom] = workElement;
				itemsOfElementListFrom++;
			}
		}
		//
		try {
			bufferedWriter.write("\n");
		}
		catch (IOException ex) {
		}
		//
		////////////////////////////////////////////////////
		//Import Function definitions into target document//
		////////////////////////////////////////////////////
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(itemsOfElementListFrom);
		//
		targetElementList = frame_.domDocument.getElementsByTagName("Function");
		for (int i = 0; i < itemsOfElementListFrom; i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			checkCounter = 0;
			sortKeyOfDefinition = elementListFrom[i].getAttribute("SortKey");
			//
			for (int n = 0; n < targetElementList.getLength(); n++) {
				workElement = (org.w3c.dom.Element)targetElementList.item(n);
				if (workElement.getAttribute("SortKey").equals(sortKeyOfDefinition)) {
					checkCounter++;
					if (workElement.getAttribute("SubsystemID").equals(blockIDInto)) {
						try {
							bufferedWriter.write(elementListFrom[i].getAttribute("Name") +
									"(" +
									elementListFrom[i].getAttribute("SortKey") +
									"):" +
									res.getString("DialogImportXEAD33") +
							"\n");
						}
						catch (IOException ex1) {
						}
						updateFunctionDefinition(elementListFrom[i], workElement);
						updateFunctionCounter++;
					} else {
						try {
							bufferedWriter.write(elementListFrom[i].getAttribute("Name") +
									"(" +
									elementListFrom[i].getAttribute("SortKey") +
									"):" +
									res.getString("DialogImportXEAD48") +
							"\n");
						}
						catch (IOException ex1) {
						}
						cancelFunctionCounter++;
					}
					break;
				}
			}
			//
			if (checkCounter == 0) {
				try {
					bufferedWriter.write(elementListFrom[i].getAttribute("Name") + "(" +
							elementListFrom[i].getAttribute("SortKey") +
							"):" +
							res.getString("DialogImportXEAD34") +
					"\n");
				}
				catch (IOException ex2) {
				}
				createFunctionDefinition(elementListFrom[i], blockIDInto);
				createFunctionCounter++;
			}
		}
		try {
			bufferedWriter.write("\n");
		}
		catch (IOException ex3) {
		}
		//
		////////////////////////////////////////////////
		//Import Functions-called into target document//
		////////////////////////////////////////////////
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(itemsOfElementListFrom);
		//
		targetElementList = frame_.domDocument.getElementsByTagName("Function");
		for (int i = 0; i < itemsOfElementListFrom; i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			sortKeyOfDefinition = elementListFrom[i].getAttribute("SortKey");
			for (int n = 0; n < targetElementList.getLength(); n++) {
				workElement = (org.w3c.dom.Element)targetElementList.item(n);
				if (workElement.getAttribute("SortKey").equals(sortKeyOfDefinition)) {
					if (workElement.getAttribute("SubsystemID").equals(blockIDInto)) {
						adjustFunctionsCalled(elementListFrom[i], workElement);
						break;
					}
				}
			}
		}
	}

	void importRole() {
		//
		//////////////////////
		//Import Departments//
		//////////////////////
		importTypeDefinitionWithTagName("Department");
		//
		////////////////////////////////////
		//Update attributes of target Role//
		////////////////////////////////////
		blockElementInto.setAttribute("Name", blockElementFrom.getAttribute("Name"));
		blockElementInto.setAttribute("Descriptions", blockElementFrom.getAttribute("Descriptions"));
		blockElementInto.setAttribute("Department", convertInternalIDOfTheTypeTag(blockElementFrom.getAttribute("DepartmentID"), "Department"));
		//
	}

	void importTasks() {
		org.w3c.dom.Element workElement;
		NodeList workElementList, targetElementList;
		org.w3c.dom.Element[] elementListFrom = new org.w3c.dom.Element[500];
		int itemsOfElementListFrom = 0;
		int checkCounter = 0;
		String sortKeyOfDefinition;
		//
		//////////////////////////////////////
		//Setup List of Tasks to be imported//
		//////////////////////////////////////
		workElementList = domDocumentFrom.getElementsByTagName("Task");
		for (int i = 0; i < workElementList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)workElementList.item(i);
			if (!workElement.getAttribute("SortKey").equals("") && workElement.getAttribute("RoleID").equals(blockIDFrom)) {
				elementListFrom[itemsOfElementListFrom] = workElement;
				itemsOfElementListFrom++;
			}
		}
		//
		try {
			bufferedWriter.write("\n");
		}
		catch (IOException ex) {
		}
		//
		////////////////////////////////////////////////
		//Import Task definitions into target document//
		////////////////////////////////////////////////
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(itemsOfElementListFrom);
		//
		targetElementList = frame_.domDocument.getElementsByTagName("Task");
		for (int i = 0; i < itemsOfElementListFrom; i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			try {
				bufferedWriter.write(elementListFrom[i].getAttribute("Name") + "(" +
						elementListFrom[i].getAttribute("SortKey") + "):");
			}
			catch (IOException ex1) {
			}
			//
			checkCounter = 0;
			sortKeyOfDefinition = elementListFrom[i].getAttribute("SortKey");
			for (int n = 0; n < targetElementList.getLength(); n++) {
				workElement = (org.w3c.dom.Element)targetElementList.item(n);
				if (workElement.getAttribute("SortKey").equals(sortKeyOfDefinition)) {
					checkCounter++;
					if (workElement.getAttribute("RoleID").equals(blockIDInto)) {
						try {
							bufferedWriter.write(res.getString(
									"DialogImportXEAD33") + "\n");
						}
						catch (IOException ex2) {
						}
						updateTaskDefinition(elementListFrom[i], workElement);
						updateTaskCounter++;
					} else {
						try {
							bufferedWriter.write(res.getString(
									"DialogImportXEAD49") + "\n");
						}
						catch (IOException ex2) {
						}
						cancelTaskCounter++;
					}
					break;
				}
			}
			//
			if (checkCounter == 0) {
				try {
					bufferedWriter.write(res.getString(
							"DialogImportXEAD34") + "\n");
				}
				catch (IOException ex3) {
				}
				createTaskDefinition(elementListFrom[i], blockIDInto);
				createTaskCounter++;
			}
		}
	}

	String getStringValueOfDateAndTime() {
		String returnValue = "";
		GregorianCalendar calendar = new GregorianCalendar();
		//
		int year = calendar.get(Calendar.YEAR);
		//
		int month = calendar.get(Calendar.MONTH) + 1;
		String monthStr = "";
		if (month < 10) {
			monthStr = "0" + Integer.toString(month);
		} else {
			monthStr = Integer.toString(month);
		}
		//
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String dayStr = "";
		if (day < 10) {
			dayStr = "0" + Integer.toString(day);
		} else {
			dayStr = Integer.toString(day);
		}
		//
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String hourStr = "";
		if (hour < 10) {
			hourStr = "0" + Integer.toString(hour);
		} else {
			hourStr = Integer.toString(hour);
		}
		//
		int minute = calendar.get(Calendar.MINUTE);
		String minStr = "";
		if (minute < 10) {
			minStr = "0" + Integer.toString(minute);
		} else {
			minStr = Integer.toString(minute);
		}
		//
		int second = calendar.get(Calendar.SECOND);
		String secStr = "";
		if (second < 10) {
			secStr = "0" + Integer.toString(second);
		} else {
			secStr = Integer.toString(second);
		}
		//
		returnValue = Integer.toString(year) + monthStr + dayStr + hourStr + minStr + secStr;
		return returnValue;
	}

	void updateFunctionDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		String internalID;
		//
		elementInto.setAttribute("Name", elementFrom.getAttribute("Name"));
		elementInto.setAttribute("Summary", elementFrom.getAttribute("Summary"));
		elementInto.setAttribute("Parameters", elementFrom.getAttribute("Parameters"));
		elementInto.setAttribute("Return", elementFrom.getAttribute("Return"));
		elementInto.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		internalID = convertInternalIDOfTheTypeTag(elementFrom.getAttribute("FunctionTypeID"), "FunctionType");
		elementInto.setAttribute("FunctionTypeID", internalID);
		//
		adjustIOPanelDefinition(elementFrom, elementInto);
		adjustIOWebPageDefinition(elementFrom, elementInto);
		adjustIOSpoolDefinition(elementFrom, elementInto);
		adjustIOTableDefinition(elementFrom, elementInto);
	}

	void adjustIOPanelDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2;
		org.w3c.dom.Element elementToBeUpdated = null;
		NodeList elementList1, elementList2, elementList3;
		int checkCounter = 0;
		//
		elementList1 = elementFrom.getElementsByTagName("IOPanel");
		elementList2 = elementInto.getElementsByTagName("IOPanel");
		//
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			//
			checkCounter = 0;
			for (int j = 0; j < elementList2.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(j);
				if (workElement1.getAttribute("SortKey").equals(workElement2.getAttribute("SortKey"))) {
					elementToBeUpdated = workElement2;
					checkCounter++;
				}
			}
			//////////////////
			//Create IOPanel//
			//////////////////
			if (checkCounter == 0) {
				org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOPanel");
				//
				childElement.setAttribute("ID", Integer.toString(getNextIDOfFunctionIOs()));
				childElement.setAttribute("Name", workElement1.getAttribute("Name"));
				childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				childElement.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
				childElement.setAttribute("Background", workElement1.getAttribute("Background"));
				childElement.setAttribute("Size", workElement1.getAttribute("Size"));
				childElement.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
				//
				elementList3 = workElement1.getElementsByTagName("IOPanelStyle");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelStyle");
					grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
					childElement.appendChild(grandChildElement);
				}
				//
				elementList3 = workElement1.getElementsByTagName("IOPanelField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					childElement.appendChild(grandChildElement);
				}
				//
				elementInto.appendChild(childElement);
			}
			//////////////////
			//Update IOPanel//
			//////////////////
			if (checkCounter == 1) {
				elementToBeUpdated.setAttribute("Name", workElement1.getAttribute("Name"));
				elementToBeUpdated.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				elementToBeUpdated.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				elementToBeUpdated.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
				elementToBeUpdated.setAttribute("Background", workElement1.getAttribute("Background"));
				elementToBeUpdated.setAttribute("Size", workElement1.getAttribute("Size"));
				elementToBeUpdated.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
				///////////////////////////////////////
				//Purge IOPanelStyle and IOPanelField//
				///////////////////////////////////////
				purgeChildElements(elementToBeUpdated, "IOPanelStyle");
				purgeChildElements(elementToBeUpdated, "IOPanelField");
				///////////////////////
				//Insert IOPanelStyle//
				///////////////////////
				elementList3 = workElement1.getElementsByTagName("IOPanelStyle");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelStyle");
					grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
					elementToBeUpdated.appendChild(grandChildElement);
				}
				///////////////////////
				//Insert IOPanelField//
				///////////////////////
				elementList3 = workElement1.getElementsByTagName("IOPanelField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					elementToBeUpdated.appendChild(grandChildElement);
				}
			}
		}
	}

	void adjustIOWebPageDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2;
		org.w3c.dom.Element elementToBeUpdated = null;
		NodeList elementList1, elementList2, elementList3;
		int checkCounter = 0;
		//
		elementList1 = elementFrom.getElementsByTagName("IOWebPage");
		elementList2 = elementInto.getElementsByTagName("IOWebPage");
		//
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			//
			checkCounter = 0;
			for (int j = 0; j < elementList2.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(j);
				if (workElement1.getAttribute("SortKey").equals(workElement2.getAttribute("SortKey"))) {
					elementToBeUpdated = workElement2;
					checkCounter++;
				}
			}
			////////////////////
			//Create IOWebPage//
			////////////////////
			if (checkCounter == 0) {
				org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOWebPage");
				//
				childElement.setAttribute("ID", Integer.toString(getNextIDOfFunctionIOs()));
				childElement.setAttribute("Name", workElement1.getAttribute("Name"));
				childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				childElement.setAttribute("FileName", workElement1.getAttribute("FileName"));
				//
				elementList3 = workElement1.getElementsByTagName("IOWebPageField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOWebPageField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					childElement.appendChild(grandChildElement);
				}
				//
				elementInto.appendChild(childElement);
			}
			////////////////////
			//Update IOWebPage//
			////////////////////
			if (checkCounter == 1) {
				//org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOWebPage");
				//
				elementToBeUpdated.setAttribute("Name", workElement1.getAttribute("Name"));
				elementToBeUpdated.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				elementToBeUpdated.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				elementToBeUpdated.setAttribute("FileName", workElement1.getAttribute("FileName"));
				////////////////////////
				//Purge IOWebPageField//
				////////////////////////
				purgeChildElements(elementToBeUpdated, "IOWebPageField");
				//
				/////////////////////////
				//Insert IOWebPageField//
				/////////////////////////
				elementList3 = workElement1.getElementsByTagName("IOWebPageField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOWebPageField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					elementToBeUpdated.appendChild(grandChildElement);
				}
			}
		}
	}

	void adjustIOSpoolDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2;
		org.w3c.dom.Element elementToBeUpdated = null;
		NodeList elementList1, elementList2, elementList3;
		int checkCounter = 0;
		//
		elementList1 = elementFrom.getElementsByTagName("IOSpool");
		elementList2 = elementInto.getElementsByTagName("IOSpool");
		//
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			//
			checkCounter = 0;
			for (int j = 0; j < elementList2.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(j);
				if (workElement1.getAttribute("SortKey").equals(workElement2.getAttribute("SortKey"))) {
					elementToBeUpdated = workElement2;
					checkCounter++;
				}
			}
			//////////////////
			//Create IOSpool//
			//////////////////
			if (checkCounter == 0) {
				org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOSpool");
				//
				childElement.setAttribute("ID", Integer.toString(getNextIDOfFunctionIOs()));
				childElement.setAttribute("Name", workElement1.getAttribute("Name"));
				childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				childElement.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
				childElement.setAttribute("Background", workElement1.getAttribute("Background"));
				childElement.setAttribute("Size", workElement1.getAttribute("Size"));
				childElement.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
				//
				elementList3 = workElement1.getElementsByTagName("IOSpoolStyle");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolStyle");
					grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
					childElement.appendChild(grandChildElement);
				}
				//
				elementList3 = workElement1.getElementsByTagName("IOSpoolField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					childElement.appendChild(grandChildElement);
				}
				//
				elementInto.appendChild(childElement);
			}
			//////////////////
			//Update IOSpool//
			//////////////////
			if (checkCounter == 1) {
				elementToBeUpdated.setAttribute("Name", workElement1.getAttribute("Name"));
				elementToBeUpdated.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				elementToBeUpdated.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				elementToBeUpdated.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
				elementToBeUpdated.setAttribute("Background", workElement1.getAttribute("Background"));
				elementToBeUpdated.setAttribute("Size", workElement1.getAttribute("Size"));
				elementToBeUpdated.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
				///////////////////////////////////////
				//Purge IOSpoolStyle and IOSpoolField//
				///////////////////////////////////////
				purgeChildElements(elementToBeUpdated, "IOSpoolStyle");
				purgeChildElements(elementToBeUpdated, "IOSpoolField");
				//
				///////////////////////
				//Insert IOPanelStyle//
				///////////////////////
				elementList3 = workElement1.getElementsByTagName("IOSpoolStyle");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolStyle");
					grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
					elementToBeUpdated.appendChild(grandChildElement);
				}
				///////////////////////
				//Insert IOPanelField//
				///////////////////////
				elementList3 = workElement1.getElementsByTagName("IOSpoolField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolField");
					grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
					grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
					grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					elementToBeUpdated.appendChild(grandChildElement);
				}
			}
		}
	}

	void adjustIOTableDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element ioTableElementFrom, workElement1, workElement2;
		org.w3c.dom.Element ioTableElementInto = null;
		NodeList elementListFrom, elementListInto, workElementList1, workElementList2;
		org.w3c.dom.Element tableElementFrom, tableElementInto;
		String internalTableIDFrom = "";
		String internalTableIDInto = "";
		String internalFieldIDFrom = "";
		String internalFieldIDInto = "";
		String tableName = "";
		String tableSortKey = "";
		int checkCounter = 0;
		//
		NodeList tableListFrom = domDocumentFrom.getElementsByTagName("Table");
		NodeList tableListInto = frame_.domDocument.getElementsByTagName("Table");
		//
		elementListFrom = elementFrom.getElementsByTagName("IOTable");
		elementListInto = elementInto.getElementsByTagName("IOTable");
		//
		for (int i = 0; i < elementListFrom.getLength(); i++) {
			ioTableElementFrom = (org.w3c.dom.Element)elementListFrom.item(i);
			internalTableIDFrom = ioTableElementFrom.getAttribute("TableID");
			internalTableIDInto = convertInternalIDOfTheTag(internalTableIDFrom, "Table");
			//
			tableElementInto = null;
			if (!internalTableIDInto.equals("")) {
				for (int j = 0; j < tableListInto.getLength(); j++) {
					workElement1 = (org.w3c.dom.Element)tableListInto.item(j);
					if (internalTableIDInto.equals(workElement1.getAttribute("ID"))) {
						tableElementInto = workElement1;
					}
				}
			}
			//
			checkCounter = 0;
			if (tableElementInto != null) {
				for (int j = 0; j < elementListInto.getLength(); j++) {
					workElement1 = (org.w3c.dom.Element)elementListInto.item(j);
					if (workElement1.getAttribute("SortKey").equals(ioTableElementFrom.getAttribute("SortKey"))) {
						if (workElement1.getAttribute("TableID").equals(internalTableIDInto)) {
							ioTableElementInto = workElement1;
							checkCounter++;
						}
					}
				}
			}
			//////////////////
			//Create IOTable//
			//////////////////
			if (checkCounter == 0) {
				if (tableElementInto == null) {
					missingTableCounter++;
					//
					for (int m = 0; m < tableListFrom.getLength(); m++) {
						tableElementFrom = (org.w3c.dom.Element)tableListFrom.item(m);
						if (internalTableIDFrom.equals(tableElementFrom.getAttribute("ID"))) {
							tableName = tableElementFrom.getAttribute("Name");
							tableSortKey = tableElementFrom.getAttribute("SortKey");
						}
					}
					try {
						bufferedWriter.write("  " + tableName + "(" + tableSortKey + ")" + res.getString("DialogImportXEAD37") + "\n");
					}
					catch (IOException ex) {
					}
				} else {
					org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOTable");
					//
					childElement.setAttribute("ID", Integer.toString(getNextIDOfFunctionIOs()));
					childElement.setAttribute("NameExtension", ioTableElementFrom.getAttribute("NameExtension"));
					childElement.setAttribute("SortKey", ioTableElementFrom.getAttribute("SortKey"));
					childElement.setAttribute("Descriptions", ioTableElementFrom.getAttribute("Descriptions"));
					childElement.setAttribute("TableID", internalTableIDInto);
					childElement.setAttribute("OpC", ioTableElementFrom.getAttribute("OpC"));
					childElement.setAttribute("OpR", ioTableElementFrom.getAttribute("OpR"));
					childElement.setAttribute("OpU", ioTableElementFrom.getAttribute("OpU"));
					childElement.setAttribute("OpD", ioTableElementFrom.getAttribute("OpD"));
					//
					workElementList1 = ioTableElementFrom.getElementsByTagName("IOTableField");
					workElementList2 = tableElementInto.getElementsByTagName("TableField");
					for (int m = 0; m < workElementList2.getLength(); m++) {
						workElement2 = (org.w3c.dom.Element)workElementList2.item(m);
						//
						org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOTableField");
						grandChildElement.setAttribute("FieldID", workElement2.getAttribute("ID"));
						//
						//Set IO descriptions//
						grandChildElement.setAttribute("Descriptions", "");
						internalFieldIDFrom = convertInternalIDOfTableField(internalTableIDInto, workElement2.getAttribute("ID"), frame_.domDocument, domDocumentFrom);
						if (!internalFieldIDFrom.equals("")) {
							for (int p = 0; p < workElementList1.getLength(); p++) {
								workElement1 = (org.w3c.dom.Element)workElementList1.item(p);
								if (internalFieldIDFrom.equals(workElement1.getAttribute("FieldID"))) {
									grandChildElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
								}
							}
						}
						childElement.appendChild(grandChildElement);
					}
					elementInto.appendChild(childElement);
				}
			}
			//////////////////
			//Update IOTable//
			//////////////////
			if (checkCounter == 1) {
				ioTableElementInto.setAttribute("NameExtension", ioTableElementFrom.getAttribute("NameExtension"));
				ioTableElementInto.setAttribute("SortKey", ioTableElementFrom.getAttribute("SortKey"));
				ioTableElementInto.setAttribute("Descriptions", ioTableElementFrom.getAttribute("Descriptions"));
				ioTableElementInto.setAttribute("OpC", ioTableElementFrom.getAttribute("OpC"));
				ioTableElementInto.setAttribute("OpR", ioTableElementFrom.getAttribute("OpR"));
				ioTableElementInto.setAttribute("OpU", ioTableElementFrom.getAttribute("OpU"));
				ioTableElementInto.setAttribute("OpD", ioTableElementFrom.getAttribute("OpD"));
				///////////////////////
				//Update IOTableField//
				///////////////////////
				workElementList1 = ioTableElementFrom.getElementsByTagName("IOTableField");
				workElementList2 = ioTableElementInto.getElementsByTagName("IOTableField");
				for (int m = 0; m < workElementList1.getLength(); m++) {
					workElement1 = (org.w3c.dom.Element)workElementList1.item(m);
					internalFieldIDInto = convertInternalIDOfTableField(internalTableIDFrom, workElement1.getAttribute("FieldID"), domDocumentFrom, frame_.domDocument);
					if (!internalFieldIDInto.equals("")) {
						for (int p = 0; p < workElementList2.getLength(); p++) {
							workElement2 = (org.w3c.dom.Element)workElementList2.item(p);
							if (internalFieldIDInto.equals(workElement2.getAttribute("FieldID"))) {
								workElement2.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
							}
						}
					}
				}
			}
			setupSubsystemAttributeOfTable(internalTableIDInto, blockElementInto);
		}
	}

	void setupSubsystemAttributeOfTable(String internalTableIDInto, org.w3c.dom.Element subsystemElementInto) {
		org.w3c.dom.Element workElement1, workElement2, workElement3;
		NodeList relationshipListInto, subsystemRelationshipListInto;
		//
		int checkCounter = 0;
		NodeList subsystemTableListInto = subsystemElementInto.getElementsByTagName("SubsystemTable");
		for (int i = 0; i < subsystemTableListInto.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)subsystemTableListInto.item(i);
			if (workElement1.getAttribute("TableID").equals(internalTableIDInto)) {
				checkCounter++;
			}
		}
		if (checkCounter == 0) {
			//
			/////////////////////////////////////////
			//add subsystem attributes of the table//
			/////////////////////////////////////////
			org.w3c.dom.Element newElement = frame_.domDocument.createElement("SubsystemTable");
			newElement.setAttribute("TableID", internalTableIDInto);
			newElement.setAttribute("BoxPosition", "50,50");
			newElement.setAttribute("ExtDivLoc", "600");
			newElement.setAttribute("IntDivLoc", "300");
			newElement.setAttribute("ShowOnModel", "false");
			newElement.setAttribute("ShowInstance", "false");
			newElement.setAttribute("Instance", "");
			subsystemElementInto.appendChild(newElement);
			//
			subsystemTableListInto = subsystemElementInto.getElementsByTagName("SubsystemTable");
			relationshipListInto = frame_.domDocument.getElementsByTagName("Relationship");
			subsystemRelationshipListInto = subsystemElementInto.getElementsByTagName("SubsystemRelationship");
			//
			//////////////////////////////
			//add subsystem relationship//
			//////////////////////////////
			for (int k = 0; k < relationshipListInto.getLength(); k++) {
				workElement2 = (org.w3c.dom.Element)relationshipListInto.item(k);
				checkCounter = 0;
				if (workElement2.getAttribute("Table1ID").equals(internalTableIDInto)) {
					for (int j = 0; j < subsystemTableListInto.getLength(); j++) {
						workElement3 = (org.w3c.dom.Element)subsystemTableListInto.item(j);
						if (workElement3.getAttribute("TableID").equals(workElement2.getAttribute("Table2ID"))) {
							checkCounter++;
						}
					}
				}
				if (workElement2.getAttribute("Table2ID").equals(internalTableIDInto)) {
					for (int j = 0; j < subsystemTableListInto.getLength(); j++) {
						workElement3 = (org.w3c.dom.Element)subsystemTableListInto.item(j);
						if (workElement3.getAttribute("TableID").equals(workElement2.getAttribute("Table1ID"))) {
							checkCounter++;
						}
					}
				}
				if (checkCounter > 0) {
					checkCounter = 0;
					for (int j = 0; j < subsystemRelationshipListInto.getLength(); j++) {
						workElement3 = (org.w3c.dom.Element)subsystemRelationshipListInto.item(j);
						if (workElement3.getAttribute("RelationshipID").equals(workElement2.getAttribute("ID"))) {
							checkCounter++;
						}
					}
					if (checkCounter == 0) {
						org.w3c.dom.Element newRelElement = frame_.domDocument.createElement("SubsystemRelationship");
						newRelElement.setAttribute("RelationshipID", workElement2.getAttribute("ID"));
						newRelElement.setAttribute("TerminalIndex1", "-1");
						newRelElement.setAttribute("TerminalIndex2", "-1");
						subsystemElementInto.appendChild(newRelElement);
					}
				}
			}
		}
	}

	void updateTaskDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		//////////////////////////
		//Update Task Definition//
		//////////////////////////
		elementInto.setAttribute("Name", elementFrom.getAttribute("Name"));
		elementInto.setAttribute("Event", elementFrom.getAttribute("Event"));
		elementInto.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		//
		/////////////////////////////////
		//Purge and create Task Actions//
		/////////////////////////////////
		purgeChildElements(elementInto, "TaskAction");
		createTaskActions(elementFrom, elementInto);
	}

	void purgeChildElements(org.w3c.dom.Element parentElement, String childTagNameToBePurged) {
		org.w3c.dom.Element workElement;
		NodeList workElementList;
		org.w3c.dom.Element[] elementArray1 = new org.w3c.dom.Element[1000];
		int arrayIndex1 = -1;
		org.w3c.dom.Node parentDomNode;
		//
		workElementList = parentElement.getElementsByTagName(childTagNameToBePurged);
		for (int i = 0; i < workElementList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)workElementList.item(i);
			arrayIndex1++;
			elementArray1[arrayIndex1] = workElement;
		}
		//
		for (int i = 0; i <= arrayIndex1; i++) {
			parentDomNode = elementArray1[i].getParentNode();
			parentDomNode.removeChild(elementArray1[i]);
		}
	}

	void updateTableDefinition(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2, workElement3, lastElement;
		NodeList elementList;
		int lastID = 0;
		int checkCounter = 0;
		String workStr = "";
		org.w3c.dom.Element[] ioTableList = new org.w3c.dom.Element[300];
		int countOfIOTableList = 0;
		String fieldID = "";
		//
		String tableIDFrom = elementFrom.getAttribute("ID");
		String tableIDInto = elementInto.getAttribute("ID");
		NodeList fieldListFrom = elementFrom.getElementsByTagName("TableField");
		NodeList fieldListInto = elementInto.getElementsByTagName("TableField");
		NodeList keyListFrom = elementFrom.getElementsByTagName("TableKey");
		NodeList keyListInto = elementInto.getElementsByTagName("TableKey");
		NodeList relationshipListInto = frame_.domDocument.getElementsByTagName("Relationship");
		//
		elementList = frame_.domDocument.getElementsByTagName("IOTable");
		for (int i = 0; i < elementList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList.item(i);
			if (tableIDInto.equals(workElement1.getAttribute("TableID"))) {
				ioTableList[countOfIOTableList] = workElement1;
				countOfIOTableList++;
			}
		}
		//
		///////////////////////////
		//Update Table Attributes//
		///////////////////////////
		elementInto.setAttribute("Name", elementFrom.getAttribute("Name"));
		elementInto.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		elementInto.setAttribute("TableTypeID", convertInternalIDOfTheTypeTag(elementFrom.getAttribute("TableTypeID"), "TableType"));
		//
		/////////////////////
		//Adjust TableField//
		/////////////////////
		for (int i = 0; i < fieldListFrom.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)fieldListFrom.item(i);
			workStr = convertInternalIDOfTableField(tableIDFrom, workElement1.getAttribute("ID"), domDocumentFrom, frame_.domDocument);
			//
			checkCounter = 0;
			for (int j = 0; j < fieldListInto.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)fieldListInto.item(j);
				if (workStr.equals(workElement2.getAttribute("ID"))) {
					workElement2.setAttribute("Name", workElement1.getAttribute("Name"));
					workElement2.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
					workElement2.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
					workElement2.setAttribute("Alias", workElement1.getAttribute("Alias"));
					workElement2.setAttribute("AttributeType", workElement1.getAttribute("AttributeType"));
					workElement2.setAttribute("DataTypeID", convertInternalIDOfTheTypeTag(workElement1.getAttribute("DataTypeID"), "DataType"));
					workElement2.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
					workElement2.setAttribute("NotNull", workElement1.getAttribute("NotNull"));
					workElement2.setAttribute("Default", workElement1.getAttribute("Default"));
					checkCounter++;
					break;
				}
			}
			if (checkCounter == 0) {
				org.w3c.dom.Element newElement = frame_.domDocument.createElement("TableField");
				lastElement = getLastDomElementOfTheType("TableField");
				if (lastElement == null) {
					lastID = 0;
				} else {
					lastID = Integer.parseInt(lastElement.getAttribute("ID"));
				}
				fieldID = Integer.toString(lastID + 1);
				newElement.setAttribute("ID", fieldID);
				newElement.setAttribute("Name", workElement1.getAttribute("Name"));
				newElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				newElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				newElement.setAttribute("Alias", workElement1.getAttribute("Alias"));
				newElement.setAttribute("AttributeType", workElement1.getAttribute("AttributeType"));
				newElement.setAttribute("DataTypeID", convertInternalIDOfTheTypeTag(workElement1.getAttribute("DataTypeID"), "DataType"));
				newElement.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
				newElement.setAttribute("NotNull", workElement1.getAttribute("NotNull"));
				newElement.setAttribute("Default", workElement1.getAttribute("Default"));
				elementInto.appendChild(newElement);
				//
				for (int j = 0; j < countOfIOTableList; j++) {
					org.w3c.dom.Element ioTableFieldElement = frame_.domDocument.createElement("IOTableField");
					ioTableFieldElement.setAttribute("FieldID", fieldID);
					ioTableFieldElement.setAttribute("Descriptions", "");
					ioTableList[j].appendChild(ioTableFieldElement);
				}
			}
		}
		///////////////////
		//Adjust TableKey//
		///////////////////
		for (int i = 0; i < keyListFrom.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)keyListFrom.item(i);
			checkCounter = 0;
			lastElement = null;
			for (int j = 0; j < keyListInto.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)keyListInto.item(j);
				if (theseAreIdenticalKeyDefinitions(workElement1, workElement2)) {
					workElement2.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
					checkCounter++;
				} else {
					if (workElement1.getAttribute("Type").equals("PK") && workElement2.getAttribute("Type").equals("PK")) {
						checkCounter++;
						boolean validRelationshipExisting = false;
						for (int p = 0; p < relationshipListInto.getLength(); p++) {
							workElement3 = (org.w3c.dom.Element)relationshipListInto.item(p);
							if (workElement3.getAttribute("Table1ID").equals(tableIDInto) && workElement3.getAttribute("TableKey1ID").equals(workElement2.getAttribute("ID"))) {
								validRelationshipExisting = true;
								break;
							}
							if (workElement3.getAttribute("Table2ID").equals(tableIDInto) && workElement3.getAttribute("TableKey2ID").equals(workElement2.getAttribute("ID"))) {
								validRelationshipExisting = true;
								break;
							}
						}
						if (validRelationshipExisting) {
							try {
								bufferedWriter.write("  " + res.getString("DialogImportXEAD43") + "\n");
							}
							catch (IOException ex) {
							}
						} else {
							purgeChildElements(workElement2, "TableKeyField");
							elementList = workElement1.getElementsByTagName("TableKeyField");
							for (int m = 0; m < elementList.getLength(); m++) {
								workElement3 = (org.w3c.dom.Element)elementList.item(m);
								org.w3c.dom.Element childElement = frame_.domDocument.createElement("TableKeyField");
								childElement.setAttribute("FieldID", convertInternalIDOfTableField(elementFrom.getAttribute("ID"), workElement3.getAttribute("FieldID"), domDocumentFrom, frame_.domDocument));
								childElement.setAttribute("SortKey", workElement3.getAttribute("SortKey"));
								workElement2.appendChild(childElement);
							}
						}
						break;
					}
				}
				if (lastElement == null) {
					lastElement = workElement2;
				} else {
					if (Integer.parseInt(workElement2.getAttribute("ID")) > Integer.parseInt(lastElement.getAttribute("ID"))) {
						lastElement = workElement2;
					}
				}
			}
			if (checkCounter == 0 && lastElement != null) {
				org.w3c.dom.Element childElement = frame_.domDocument.createElement("TableKey");
				lastID = Integer.parseInt(lastElement.getAttribute("ID"));
				childElement.setAttribute("ID", Integer.toString(lastID + 1));
				childElement.setAttribute("Type", workElement1.getAttribute("Type"));
				childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				elementList = workElement1.getElementsByTagName("TableKeyField");
				for (int m = 0; m < elementList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList.item(m);
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("TableKeyField");
					grandChildElement.setAttribute("FieldID", convertInternalIDOfTableField(elementFrom.getAttribute("ID"), workElement2.getAttribute("FieldID"), domDocumentFrom, frame_.domDocument));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					childElement.appendChild(grandChildElement);
				}
				elementInto.appendChild(childElement);
			}
		}
	}

	void createTableDefinition(org.w3c.dom.Element elementFrom, String subsystemID) {
		org.w3c.dom.Element workElement1, workElement2;
		NodeList elementList1, elementList2;
		int lastID = 0;
		///////////////////////////
		//Create Table Definition//
		///////////////////////////
		org.w3c.dom.Element newElement = frame_.domDocument.createElement("Table");
		//
		org.w3c.dom.Element lastElement = getLastDomElementOfTheType("Table");
		if (lastElement == null) {
			lastID = 0;
		} else {
			lastID = Integer.parseInt(lastElement.getAttribute("ID"));
		}
		newElement.setAttribute("ID", Integer.toString(lastID + 1));
		newElement.setAttribute("Name", elementFrom.getAttribute("Name"));
		newElement.setAttribute("SortKey", elementFrom.getAttribute("SortKey"));
		newElement.setAttribute("SubsystemID", subsystemID);
		newElement.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		newElement.setAttribute("TableTypeID", convertInternalIDOfTheTypeTag(elementFrom.getAttribute("TableTypeID"), "TableType"));
		//
		systemElement.appendChild(newElement);
		//
		/////////////////////
		//Create TableField//
		/////////////////////
		elementList1 = elementFrom.getElementsByTagName("TableField");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("TableField");
			//
			lastElement = getLastDomElementOfTheType("TableField");
			if (lastElement == null) {
				lastID = 0;
			} else {
				lastID = Integer.parseInt(lastElement.getAttribute("ID"));
			}
			childElement.setAttribute("ID", Integer.toString(lastID + 1));
			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("Alias", workElement1.getAttribute("Alias"));
			childElement.setAttribute("AttributeType", workElement1.getAttribute("AttributeType"));
			childElement.setAttribute("DataTypeID", convertInternalIDOfTheTypeTag(workElement1.getAttribute("DataTypeID"), "DataType"));
			childElement.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
			childElement.setAttribute("NotNull", workElement1.getAttribute("NotNull"));
			childElement.setAttribute("Default", workElement1.getAttribute("Default"));
			//
			newElement.appendChild(childElement);
		}
		///////////////////
		//Create TableKey//
		///////////////////
		elementList1 = elementFrom.getElementsByTagName("TableKey");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("TableKey");
			//
			childElement.setAttribute("ID", workElement1.getAttribute("ID"));
			childElement.setAttribute("Type", workElement1.getAttribute("Type"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			//
			elementList2 = workElement1.getElementsByTagName("TableKeyField");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("TableKeyField");
				grandChildElement.setAttribute("FieldID", convertInternalIDOfTableField(elementFrom.getAttribute("ID"), workElement2.getAttribute("FieldID"), domDocumentFrom, frame_.domDocument));
				grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
				childElement.appendChild(grandChildElement);
			}
			//
			newElement.appendChild(childElement);
		}
	}

	void adjustTableRelationship(org.w3c.dom.Element elementFrom) {
		org.w3c.dom.Element workElement;
		int lastID = 0;
		//
		String tableID1From = elementFrom.getAttribute("Table1ID");
		String tableID2From = elementFrom.getAttribute("Table2ID");
		String tableKey1From = elementFrom.getAttribute("TableKey1ID");
		String tableKey2From = elementFrom.getAttribute("TableKey2ID");
		String relationshipType = elementFrom.getAttribute("Type");
		NodeList tableListFrom = domDocumentFrom.getElementsByTagName("Table");
		NodeList relationshipListInto = frame_.domDocument.getElementsByTagName("Relationship");
		//
		String tableName1From = "";
		String tableName2From = "";
		String tableSortKey1From = "";
		String tableSortKey2From = "";
		for (int i = 0; i < tableListFrom.getLength(); i++) {
			workElement = (org.w3c.dom.Element)tableListFrom.item(i);
			if (workElement.getAttribute("ID").equals(tableID1From)) {
				tableName1From = workElement.getAttribute("Name");
				tableSortKey1From = workElement.getAttribute("SortKey");
			}
			if (workElement.getAttribute("ID").equals(tableID2From)) {
				tableName2From = workElement.getAttribute("Name");
				tableSortKey2From = workElement.getAttribute("SortKey");
			}
		}
		//
		String relationshipIDInto = convertInternalIDOfTableRelationship(elementFrom.getAttribute("ID"));
		if (relationshipIDInto.equals("")) {
			String tableID1Into = convertInternalIDOfTheTag(tableID1From, "Table");
			String tableID2Into = convertInternalIDOfTheTag(tableID2From, "Table");
			String convertedKey1ID = convertTableKeyID(tableID1From, tableKey1From, tableID1Into);
			String convertedKey2ID = convertTableKeyID(tableID2From, tableKey2From, tableID2Into);
			if (convertedKey1ID.equals("") || convertedKey2ID.equals("")) {
				missingTableKeyCounter++;
				//
				try {
					bufferedWriter.write(tableName1From + "(" + tableSortKey1From + ")," + tableName2From + "(" + tableSortKey2From + ")" + res.getString("DialogImportXEAD35") + "\n");
				}
				catch (IOException ex) {
				}
			} else {
				org.w3c.dom.Element newElement = frame_.domDocument.createElement("Relationship");
				//
				org.w3c.dom.Element lastElement = getLastDomElementOfTheType("Relationship");
				if (lastElement == null) {
					lastID = 0;
				} else {
					lastID = Integer.parseInt(lastElement.getAttribute("ID"));
				}
				newElement.setAttribute("ID", Integer.toString(lastID + 1));
				//
				newElement.setAttribute("Table1ID", tableID1Into);
				newElement.setAttribute("Table2ID", tableID2Into);
				newElement.setAttribute("TableKey1ID", convertedKey1ID);
				newElement.setAttribute("TableKey2ID", convertedKey2ID);
				newElement.setAttribute("Type", relationshipType);
				newElement.setAttribute("ExistWhen1", elementFrom.getAttribute("ExistWhen1"));
				newElement.setAttribute("ExistWhen2", elementFrom.getAttribute("ExistWhen2"));
				systemElement.appendChild(newElement);
				//
				try {
					bufferedWriter.write(tableName1From + "(" + tableSortKey1From + ")," + tableName2From + "(" + tableSortKey2From + ")" + res.getString("DialogImportXEAD36") + "\n");
				}
				catch (IOException ex) {
				}
			}
		} else {
			for (int i = 0; i < relationshipListInto.getLength(); i++) {
				workElement = (org.w3c.dom.Element)relationshipListInto.item(i);
				if (workElement.getAttribute("ID").equals(relationshipIDInto)) {
					workElement.setAttribute("ExistWhen1", elementFrom.getAttribute("ExistWhen1"));
					workElement.setAttribute("ExistWhen2", elementFrom.getAttribute("ExistWhen2"));
					break;
				}
			}
		}
	}
	
	void importSubsystemAttributesOfTable() {
		org.w3c.dom.Element workElement1, workElement2;
		NodeList elementListFrom, elementListInto, tableListFrom;
		String tableID1 = "";
		String tableID2 = "";
		String relationshipIDFrom = "";
		String relationshipIDInto = "";
		String relationshipIDWork = "";
		int checkCounter = 0;
		//
		/////////////////////////
		//Adjust SubsystemTable//
		/////////////////////////
		tableListFrom = domDocumentFrom.getElementsByTagName("Table");
		elementListFrom = blockElementFrom.getElementsByTagName("SubsystemTable");
		elementListInto = blockElementInto.getElementsByTagName("SubsystemTable");
		//
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(elementListFrom.getLength());
		//
		for (int i = 0; i < elementListFrom.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			workElement1 = (org.w3c.dom.Element)elementListFrom.item(i);
			tableID1 = convertInternalIDOfTheTag(workElement1.getAttribute("TableID"), "Table");
			if (tableID1.equals("")) {
				try {
					for (int j = 0; j < tableListFrom.getLength(); j++) {
						workElement2 = (org.w3c.dom.Element)tableListFrom.item(j);
						if (workElement2.getAttribute("ID").equals(workElement1.getAttribute("TableID"))) {
							bufferedWriter.write(workElement2.getAttribute("Name") + "(" + workElement2.getAttribute("SortKey") + ")" + res.getString("DialogImportXEAD42") + "\n");
						}
					}
				}
				catch (IOException ex) {
				}
			} else {
				checkCounter = 0;
				for (int j = 0; j < elementListInto.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)elementListInto.item(j);
					tableID2 = workElement2.getAttribute("TableID");
					if (tableID1.equals(tableID2)) {
						workElement2.setAttribute("BoxPosition", workElement1.getAttribute("BoxPosition"));
						workElement2.setAttribute("ExtDivLoc", workElement1.getAttribute("ExtDivLoc"));
						workElement2.setAttribute("IntDivLoc", workElement1.getAttribute("IntDivLoc"));
						workElement2.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
						workElement2.setAttribute("ShowInstance", workElement1.getAttribute("ShowInstance"));
						workElement2.setAttribute("Instance", workElement1.getAttribute("Instance"));
						checkCounter++;
						break;
					}
				}
				if (checkCounter == 0) {
					org.w3c.dom.Element newElement = frame_.domDocument.createElement("SubsystemTable");
					newElement.setAttribute("TableID", tableID1);
					newElement.setAttribute("BoxPosition", workElement1.getAttribute("BoxPosition"));
					newElement.setAttribute("ExtDivLoc", workElement1.getAttribute("ExtDivLoc"));
					newElement.setAttribute("IntDivLoc", workElement1.getAttribute("IntDivLoc"));
					newElement.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
					newElement.setAttribute("ShowInstance", workElement1.getAttribute("ShowInstance"));
					newElement.setAttribute("Instance", workElement1.getAttribute("Instance"));
					blockElementInto.appendChild(newElement);
				}
			}
		}
		////////////////////////////////
		//Adjust SubsystemRelationship//
		////////////////////////////////
		elementListFrom = blockElementFrom.getElementsByTagName("SubsystemRelationship");
		elementListInto = blockElementInto.getElementsByTagName("SubsystemRelationship");
		//
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(elementListFrom.getLength());
		//
		for (int i = 0; i < elementListFrom.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			workElement1 = (org.w3c.dom.Element)elementListFrom.item(i);
			relationshipIDFrom = workElement1.getAttribute("RelationshipID");
			relationshipIDInto = convertInternalIDOfTableRelationship(relationshipIDFrom);
			if (!relationshipIDInto.equals("")) {
				checkCounter = 0;
				for (int j = 0; j < elementListInto.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)elementListInto.item(j);
					relationshipIDWork = workElement2.getAttribute("RelationshipID");
					if (relationshipIDWork.equals(relationshipIDInto)) {
						workElement2.setAttribute("TerminalIndex1", workElement1.getAttribute("TerminalIndex1"));
						workElement2.setAttribute("TerminalIndex2", workElement1.getAttribute("TerminalIndex2"));
						workElement2.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
						checkCounter++;
						break;
					}
				}
				if (checkCounter == 0) {
					org.w3c.dom.Element newElement = frame_.domDocument.createElement("SubsystemRelationship");
					newElement.setAttribute("RelationshipID", relationshipIDInto);
					newElement.setAttribute("TerminalIndex1", workElement1.getAttribute("TerminalIndex1"));
					newElement.setAttribute("TerminalIndex2", workElement1.getAttribute("TerminalIndex2"));
					newElement.setAttribute("ShowOnModel", workElement1.getAttribute("ShowOnModel"));
					blockElementInto.appendChild(newElement);
				}
			}
		}
	}

	void importSubsystem() {
		/////////////////////////////////////////
		//Update attributes of target subsystem//
		/////////////////////////////////////////
		blockElementInto.setAttribute("Name", blockElementFrom.getAttribute("Name"));
		blockElementInto.setAttribute("Descriptions", blockElementFrom.getAttribute("Descriptions"));
	}

	String convertInternalIDOfTableRelationship(String relationshipIDFrom) {
		org.w3c.dom.Element workElement1, workElement2, workElement3;
		NodeList tableList, tableKeyList, relationshipList;
		org.w3c.dom.Element tableKey1ElementFrom = null;
		org.w3c.dom.Element tableKey2ElementFrom = null;
		org.w3c.dom.Element tableKey1ElementInto = null;
		org.w3c.dom.Element tableKey2ElementInto = null;
		String table1IDFrom = "";
		String tableKey1IDFrom = "";
		String table2IDFrom = "";
		String tableKey2IDFrom = "";
		String relationshipTypeFrom = "";
		String table1IDInto = "";
		String tableKey1IDInto = "";
		String table2IDInto = "";
		String tableKey2IDInto = "";
		String relationshipTypeInto = "";
		String relationshipIDInto = "";
		String relationshipIDWork = "";
		//
		relationshipList = domDocumentFrom.getElementsByTagName("Relationship");
		for (int i = 0; i < relationshipList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)relationshipList.item(i);
			if (workElement1.getAttribute("ID").equals(relationshipIDFrom)) {
				table1IDFrom = workElement1.getAttribute("Table1ID");
				tableKey1IDFrom = workElement1.getAttribute("TableKey1ID");
				table2IDFrom = workElement1.getAttribute("Table2ID");
				tableKey2IDFrom = workElement1.getAttribute("TableKey2ID");
				relationshipTypeFrom = workElement1.getAttribute("Type");
			}
		}
		//
		tableList = domDocumentFrom.getElementsByTagName("Table");
		for (int i = 0; i < tableList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)tableList.item(i);
			if (workElement1.getAttribute("ID").equals(table1IDFrom)) {
				tableKeyList = workElement1.getElementsByTagName("TableKey");
				for (int j = 0; j < tableKeyList.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)tableKeyList.item(j);
					if (workElement2.getAttribute("ID").equals(tableKey1IDFrom)) {
						tableKey1ElementFrom = workElement2;
					}
				}
			}
			if (workElement1.getAttribute("ID").equals(table2IDFrom)) {
				tableKeyList = workElement1.getElementsByTagName("TableKey");
				for (int j = 0; j < tableKeyList.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)tableKeyList.item(j);
					if (workElement2.getAttribute("ID").equals(tableKey2IDFrom)) {
						tableKey2ElementFrom = workElement2;
					}
				}
			}
		}
		//
		relationshipList = frame_.domDocument.getElementsByTagName("Relationship");
		tableList = frame_.domDocument.getElementsByTagName("Table");
		//
		for (int i = 0; i < relationshipList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)relationshipList.item(i);
			relationshipIDWork = workElement1.getAttribute("ID");
			table1IDInto = workElement1.getAttribute("Table1ID");
			tableKey1IDInto = workElement1.getAttribute("TableKey1ID");
			table2IDInto = workElement1.getAttribute("Table2ID");
			tableKey2IDInto = workElement1.getAttribute("TableKey2ID");
			relationshipTypeInto = workElement1.getAttribute("Type");
			//
			if (relationshipTypeFrom.equals(relationshipTypeInto)) {
				for (int k = 0; k < tableList.getLength(); k++) {
					workElement2 = (org.w3c.dom.Element)tableList.item(k);
					if (workElement2.getAttribute("ID").equals(table1IDInto)) {
						tableKeyList = workElement2.getElementsByTagName("TableKey");
						for (int j = 0; j < tableKeyList.getLength(); j++) {
							workElement3 = (org.w3c.dom.Element)tableKeyList.item(j);
							if (workElement3.getAttribute("ID").equals(tableKey1IDInto)) {
								tableKey1ElementInto = workElement3;
							}
						}
					}
					if (workElement2.getAttribute("ID").equals(table2IDInto)) {
						tableKeyList = workElement2.getElementsByTagName("TableKey");
						for (int j = 0; j < tableKeyList.getLength(); j++) {
							workElement3 = (org.w3c.dom.Element)tableKeyList.item(j);
							if (workElement3.getAttribute("ID").equals(tableKey2IDInto)) {
								tableKey2ElementInto = workElement3;
							}
						}
					}
				}
				//
				if (theseAreIdenticalKeyDefinitions(tableKey1ElementFrom, tableKey1ElementInto) &&
						theseAreIdenticalKeyDefinitions(tableKey2ElementFrom, tableKey2ElementInto)) {
					relationshipIDInto = relationshipIDWork;
					break;
				}
			}
		}
		return relationshipIDInto;
	}

	void createFunctionDefinition(org.w3c.dom.Element elementFrom, String subsystemID) {
		org.w3c.dom.Element workElement1, workElement2;
		NodeList elementList1, elementList2, elementList3;
		String internalTableIDInto = "";
		String internalFieldIDInto = "";
		int lastID = 0;
		org.w3c.dom.Element tableElement;
		String tableName = "";
		String tableSortKey = "";
		NodeList tableListFrom = domDocumentFrom.getElementsByTagName("Table");
		//////////////////////////////
		//Create Function Definition//
		//////////////////////////////
		org.w3c.dom.Element newElement = frame_.domDocument.createElement("Function");
		//
		org.w3c.dom.Element lastElement = getLastDomElementOfTheType("Function");
		if (lastElement == null) {
			lastID = 0;
		} else {
			lastID = Integer.parseInt(lastElement.getAttribute("ID"));
		}
		newElement.setAttribute("ID", Integer.toString(lastID + 1));
		newElement.setAttribute("Name", elementFrom.getAttribute("Name"));
		newElement.setAttribute("SortKey", elementFrom.getAttribute("SortKey"));
		newElement.setAttribute("SubsystemID", subsystemID);
		newElement.setAttribute("Summary", elementFrom.getAttribute("Summary"));
		newElement.setAttribute("Parameters", elementFrom.getAttribute("Parameters"));
		newElement.setAttribute("Return", elementFrom.getAttribute("Return"));
		newElement.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		newElement.setAttribute("FunctionTypeID", convertInternalIDOfTheTypeTag(elementFrom.getAttribute("FunctionTypeID"), "FunctionType"));
		//////////////////
		//Create IOPanel//
		//////////////////
		elementList1 = elementFrom.getElementsByTagName("IOPanel");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOPanel");
			//
			childElement.setAttribute("ID", workElement1.getAttribute("ID"));
			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
			childElement.setAttribute("Background", workElement1.getAttribute("Background"));
			childElement.setAttribute("Size", workElement1.getAttribute("Size"));
			childElement.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
			//
			elementList2 = workElement1.getElementsByTagName("IOPanelStyle");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelStyle");
				grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
				childElement.appendChild(grandChildElement);
			}
			//
			elementList2 = workElement1.getElementsByTagName("IOPanelField");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOPanelField");
				grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
				grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
				grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
				grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
				grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
				childElement.appendChild(grandChildElement);
			}
			//
			newElement.appendChild(childElement);
		}
		////////////////////
		//Create IOWebPage//
		////////////////////
		elementList1 = elementFrom.getElementsByTagName("IOWebPage");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOWebPage");
			//
			childElement.setAttribute("ID", workElement1.getAttribute("ID"));
			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("FileName", workElement1.getAttribute("FileName"));
			//
			elementList2 = workElement1.getElementsByTagName("IOWebPageField");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOWebPageField");
				grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
				grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
				grandChildElement.setAttribute("IOType", workElement2.getAttribute("IOType"));
				grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
				grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
				childElement.appendChild(grandChildElement);
			}
			//
			newElement.appendChild(childElement);
		}
		//////////////////
		//Create IOSpool//
		//////////////////
		elementList1 = elementFrom.getElementsByTagName("IOSpool");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOSpool");
			//
			childElement.setAttribute("ID", workElement1.getAttribute("ID"));
			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("ImageText", workElement1.getAttribute("ImageText"));
			childElement.setAttribute("Background", workElement1.getAttribute("Background"));
			childElement.setAttribute("Size", workElement1.getAttribute("Size"));
			childElement.setAttribute("FontSize", workElement1.getAttribute("FontSize"));
			//
			elementList2 = workElement1.getElementsByTagName("IOSpoolStyle");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolStyle");
				grandChildElement.setAttribute("Value", workElement2.getAttribute("Value"));
				childElement.appendChild(grandChildElement);
			}
			//
			elementList2 = workElement1.getElementsByTagName("IOSpoolField");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOSpoolField");
				grandChildElement.setAttribute("Name", workElement2.getAttribute("Name"));
				grandChildElement.setAttribute("Label", workElement2.getAttribute("Label"));
				grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
				grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
				childElement.appendChild(grandChildElement);
			}
			//
			newElement.appendChild(childElement);
		}
		//////////////////
		//Create IOTable//
		//////////////////
		elementList1 = elementFrom.getElementsByTagName("IOTable");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			//
			internalTableIDInto = convertInternalIDOfTheTag(workElement1.getAttribute("TableID"), "Table");
			if (internalTableIDInto.equals("")) {
				//
				missingTableCounter++;
				//
				for (int m = 0; m < tableListFrom.getLength(); m++) {
					tableElement = (org.w3c.dom.Element)tableListFrom.item(m);
					if (workElement1.getAttribute("TableID").equals(tableElement.getAttribute("ID"))) {
						tableName = tableElement.getAttribute("Name");
						tableSortKey = tableElement.getAttribute("SortKey");
					}
				}
				try {
					bufferedWriter.write("  " + tableName + "(" + tableSortKey + ")" + res.getString("DialogImportXEAD37") + "\n");
				}
				catch (IOException ex) {
				}
				//
			} else {
				//
				org.w3c.dom.Element childElement = frame_.domDocument.createElement("IOTable");
				//
				childElement.setAttribute("ID", workElement1.getAttribute("ID"));
				childElement.setAttribute("NameExtension", workElement1.getAttribute("NameExtension"));
				childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
				childElement.setAttribute("TableID", internalTableIDInto);
				childElement.setAttribute("OpC", workElement1.getAttribute("OpC"));
				childElement.setAttribute("OpR", workElement1.getAttribute("OpR"));
				childElement.setAttribute("OpU", workElement1.getAttribute("OpU"));
				childElement.setAttribute("OpD", workElement1.getAttribute("OpD"));
				//
				elementList3 = workElement1.getElementsByTagName("IOTableField");
				for (int m = 0; m < elementList3.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)elementList3.item(m);
					internalFieldIDInto = convertInternalIDOfTableField(workElement1.getAttribute("TableID"), workElement2.getAttribute("FieldID"), domDocumentFrom, frame_.domDocument);
					if (!internalFieldIDInto.equals("")) {
						org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("IOTableField");
						grandChildElement.setAttribute("FieldID", internalFieldIDInto);
						grandChildElement.setAttribute("Descriptions", workElement2.getAttribute("Descriptions"));
						childElement.appendChild(grandChildElement);
					}
				}
				//
				newElement.appendChild(childElement);
				//
				setupSubsystemAttributeOfTable(internalTableIDInto, blockElementInto);
			}
		}
		//
		systemElement.appendChild(newElement);
	}

	void createTaskDefinition(org.w3c.dom.Element elementFrom, String roleID) {
		int lastID = 0;
		//////////////////////////
		//Create Task Definition//
		//////////////////////////
		org.w3c.dom.Element newElement = frame_.domDocument.createElement("Task");
		//
		org.w3c.dom.Element lastElement = getLastDomElementOfTheType("Task");
		if (lastElement == null) {
			lastID = 0;
		} else {
			lastID = Integer.parseInt(lastElement.getAttribute("ID"));
		}
		newElement.setAttribute("ID", Integer.toString(lastID + 1));
		newElement.setAttribute("Name", elementFrom.getAttribute("Name"));
		newElement.setAttribute("Event", elementFrom.getAttribute("Event"));
		newElement.setAttribute("SortKey", elementFrom.getAttribute("SortKey"));
		newElement.setAttribute("RoleID", roleID);
		newElement.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
		//////////////////////
		//Create TaskActions//
		//////////////////////
		createTaskActions(elementFrom, newElement);
		//
		systemElement.appendChild(newElement);
	}

	void createTaskActions(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2, workElement3,workElement4;
		NodeList elementList1, elementList2, elementList3;
		String functionSortKey = "";
		String functionName = "";
		String ioSortKey = "";
		String ioName = "";
		String targetFunctionID = "";
		String targetFunctionIOID = "";
		//
		NodeList functionListFrom = domDocumentFrom.getElementsByTagName("Function");
		NodeList functionListInto = frame_.domDocument.getElementsByTagName("Function");
		//////////////////////
		//Create TaskActions//
		//////////////////////
		elementList1 = elementFrom.getElementsByTagName("TaskAction");
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			org.w3c.dom.Element childElement = frame_.domDocument.createElement("TaskAction");
			//
			childElement.setAttribute("ExecuteIf", workElement1.getAttribute("ExecuteIf"));
			childElement.setAttribute("Label", workElement1.getAttribute("Label"));
			childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
			childElement.setAttribute("Descriptions", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("IndentLevel", workElement1.getAttribute("IndentLevel"));
			//
			elementList2 = workElement1.getElementsByTagName("TaskFunctionIO");
			for (int m = 0; m < elementList2.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)elementList2.item(m);
				//
				ioSortKey = "";
				ioName = "";
				targetFunctionID = "";
				targetFunctionIOID = "";
				//
				for (int j = 0; j < functionListFrom.getLength(); j++) {
					workElement3 = (org.w3c.dom.Element)functionListFrom.item(j);
					if (workElement3.getAttribute("ID").equals(workElement2.getAttribute("FunctionID"))) {
						functionName = workElement3.getAttribute("Name");
						functionSortKey = workElement3.getAttribute("SortKey");
						ioSortKey = "";
						ioName = "";
						//
						elementList3 = workElement3.getElementsByTagName("IOPanel");
						for (int n = 0; n < elementList3.getLength(); n++) {
							workElement4 = (org.w3c.dom.Element)elementList3.item(n);
							if (workElement4.getAttribute("ID").equals(workElement2.getAttribute("IOID"))) {
								ioSortKey = workElement4.getAttribute("SortKey");
								ioName = workElement4.getAttribute("Name");
								break;
							}
						}
						if (ioSortKey.equals("") && ioName.equals("")) {
							elementList3 = workElement3.getElementsByTagName("IOSpool");
							for (int n = 0; n < elementList3.getLength(); n++) {
								workElement4 = (org.w3c.dom.Element)elementList3.item(n);
								if (workElement4.getAttribute("ID").equals(workElement2.getAttribute("IOID"))) {
									ioSortKey = workElement4.getAttribute("SortKey");
									ioName = workElement4.getAttribute("Name");
									break;
								}
							}
						}
						if (ioSortKey.equals("") && ioName.equals("")) {
							elementList3 = workElement3.getElementsByTagName("IOWebPage");
							for (int n = 0; n < elementList3.getLength(); n++) {
								workElement4 = (org.w3c.dom.Element)elementList3.item(n);
								if (workElement4.getAttribute("ID").equals(workElement2.getAttribute("IOID"))) {
									ioSortKey = workElement4.getAttribute("SortKey");
									ioName = workElement4.getAttribute("Name");
									break;
								}
							}
						}
						break;
					}
				}
				//
				for (int j = 0; j < functionListInto.getLength(); j++) {
					workElement3 = (org.w3c.dom.Element)functionListInto.item(j);
					if (workElement3.getAttribute("SortKey").equals(functionSortKey)) {
						targetFunctionID = workElement3.getAttribute("ID");
						//
						elementList3 = workElement3.getElementsByTagName("IOPanel");
						for (int n = 0; n < elementList3.getLength(); n++) {
							workElement4 = (org.w3c.dom.Element)elementList3.item(n);
							//if (workElement4.getAttribute("SortKey").equals(ioSortKey) && workElement4.getAttribute("Name").equals(ioName)) {
							if (workElement4.getAttribute("SortKey").equals(ioSortKey)) {
								targetFunctionIOID = workElement4.getAttribute("ID");
								break;
							}
						}
						if (targetFunctionIOID.equals("")) {
							elementList3 = workElement3.getElementsByTagName("IOSpool");
							for (int n = 0; n < elementList3.getLength(); n++) {
								workElement4 = (org.w3c.dom.Element)elementList3.item(n);
								//if (workElement4.getAttribute("SortKey").equals(ioSortKey) && workElement4.getAttribute("Name").equals(ioName)) {
								if (workElement4.getAttribute("SortKey").equals(ioSortKey)) {
									targetFunctionIOID = workElement4.getAttribute("ID");
									break;
								}
							}
						}
						if (targetFunctionIOID.equals("")) {
							elementList3 = workElement3.getElementsByTagName("IOWebPage");
							for (int n = 0; n < elementList3.getLength(); n++) {
								workElement4 = (org.w3c.dom.Element)elementList3.item(n);
								if (workElement4.getAttribute("SortKey").equals(ioSortKey)) {
									targetFunctionIOID = workElement4.getAttribute("ID");
									break;
								}
							}
						}
						break;
					}
				}
				//
				if (targetFunctionID.equals("") || targetFunctionIOID.equals("")) {
					missingFunctionIOCounter++;
					//
					try {
						bufferedWriter.write("  " + functionName + "(" + functionSortKey + ")/" + ioName + "(" + ioSortKey + ")" + res.getString("DialogImportXEAD40") + "\n");
					}
					catch (IOException ex) {
					}
					//
				} else {
					org.w3c.dom.Element grandChildElement = frame_.domDocument.createElement("TaskFunctionIO");
					grandChildElement.setAttribute("FunctionID", targetFunctionID);
					grandChildElement.setAttribute("IOID", targetFunctionIOID);
					grandChildElement.setAttribute("Operations", workElement2.getAttribute("Operations"));
					grandChildElement.setAttribute("SortKey", workElement2.getAttribute("SortKey"));
					childElement.appendChild(grandChildElement);
				}
			}
			//
			elementInto.appendChild(childElement);
		}
	}

	void adjustFunctionsCalled(org.w3c.dom.Element elementFrom, org.w3c.dom.Element elementInto) {
		org.w3c.dom.Element workElement1, workElement2;
		org.w3c.dom.Element elementToBeUpdated = null;
		NodeList elementList1, elementList2;
		String internalID1, internalID2;
		int countEquals = 0;
		org.w3c.dom.Element functionElement;
		String functionName = "";
		String functionSortKey = "";
		String functionNameCalling = "";
		String functionSortKeyCalling = "";
		//
		NodeList functionList = domDocumentFrom.getElementsByTagName("Function");
		elementList1 = elementFrom.getElementsByTagName("FunctionUsedByThis");
		elementList2 = elementInto.getElementsByTagName("FunctionUsedByThis");
		//
		for (int i = 0; i < elementList1.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)elementList1.item(i);
			internalID1 = workElement1.getAttribute("FunctionID");
			internalID2 = convertInternalIDOfTheTag(internalID1, "Function");
			if (internalID2.equals("")) {
				//
				missingFunctionCounter++;
				//
				for (int m = 0; m < functionList.getLength(); m++) {
					functionElement = (org.w3c.dom.Element)functionList.item(m);
					if (internalID1.equals(functionElement.getAttribute("ID"))) {
						functionName = functionElement.getAttribute("Name");
						functionSortKey = functionElement.getAttribute("SortKey");
					}
					if (elementFrom.getAttribute("ID").equals(functionElement.getAttribute("ID"))) {
						functionNameCalling = functionElement.getAttribute("Name");
						functionSortKeyCalling = functionElement.getAttribute("SortKey");
					}
				}
				try {
					bufferedWriter.write(functionName + "(" + functionSortKey + ")" + res.getString("DialogImportXEAD38") + functionNameCalling + "(" + functionSortKeyCalling + ")" + res.getString("DialogImportXEAD39") +"\n");
				}
				catch (IOException ex) {
				}
				//
			} else {
				//
				countEquals = 0;
				for (int j = 0; j < elementList2.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)elementList2.item(j);
					if (internalID2.equals(workElement2.getAttribute("FunctionID"))) {
						elementToBeUpdated = workElement2;
						countEquals++;
						break;
					}
				}
				/////////////////////////
				//Create FunctionCalled//
				/////////////////////////
				if (countEquals == 0) {
					org.w3c.dom.Element childElement = frame_.domDocument.createElement("FunctionUsedByThis");
					//
					childElement.setAttribute("FunctionID", internalID2);
					childElement.setAttribute("LaunchEvent", workElement1.getAttribute("LaunchEvent"));
					childElement.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
					//
					elementInto.appendChild(childElement);
				}
				/////////////////////////
				//Update FunctionCalled//
				/////////////////////////
				if (countEquals == 1) {
					elementToBeUpdated.setAttribute("LaunchEvent", workElement1.getAttribute("LaunchEvent"));
					elementToBeUpdated.setAttribute("SortKey", workElement1.getAttribute("SortKey"));
				}
			}
		}
	}

	void importTypeDefinitionWithTagName(String tagName) {
		int checkCount = 0;
		int lastID = 0;
		org.w3c.dom.Element elementFrom, elementInto;
		//
		NodeList typeListFrom = domDocumentFrom.getElementsByTagName(tagName);
		NodeList typeListInto = frame_.domDocument.getElementsByTagName(tagName);
		//
		jProgressBar.setValue(0);
		jProgressBar.setMaximum(typeListFrom.getLength());
		//
		for (int i = 0; i < typeListFrom.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			//
			elementFrom = (org.w3c.dom.Element)typeListFrom.item(i);
			checkCount = 0;
			for (int j = 0; j < typeListInto.getLength(); j++) {
				elementInto = (org.w3c.dom.Element)typeListInto.item(j);
				if (elementInto.getAttribute("SortKey").equals(elementFrom.getAttribute("SortKey"))) {
					checkCount++;
					if (tagName.equals("Department") || tagName.equals("TableType") || tagName.equals("FunctionType")) {
						elementInto.setAttribute("Name", elementFrom.getAttribute("Name"));
						elementInto.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
					}
					if (tagName.equals("DataType")) {
						elementInto.setAttribute("Name", elementFrom.getAttribute("Name"));
						elementInto.setAttribute("BasicType", elementFrom.getAttribute("BasicType"));
						elementInto.setAttribute("Length", elementFrom.getAttribute("Length"));
						elementInto.setAttribute("Decimal", elementFrom.getAttribute("Decimal"));
						elementInto.setAttribute("SQLExpression", elementFrom.getAttribute("SQLExpression"));
					}
					break;
				}
			}
			if (checkCount == 0) {
				org.w3c.dom.Element newElement = frame_.domDocument.createElement(tagName);
				//
				org.w3c.dom.Element lastElement = getLastDomElementOfTheType(tagName);
				if (lastElement == null) {
					lastID = 0;
				} else {
					lastID = Integer.parseInt(lastElement.getAttribute("ID"));
				}
				newElement.setAttribute("ID", Integer.toString(lastID + 1));
				newElement.setAttribute("SortKey", elementFrom.getAttribute("SortKey"));
				//
				if (tagName.equals("Department") || tagName.equals("TableType") || tagName.equals("FunctionType")) {
					newElement.setAttribute("Name", elementFrom.getAttribute("Name"));
					newElement.setAttribute("Descriptions", elementFrom.getAttribute("Descriptions"));
				}
				if (tagName.equals("DataType")) {
					newElement.setAttribute("Name", elementFrom.getAttribute("Name"));
					newElement.setAttribute("BasicType", elementFrom.getAttribute("BasicType"));
					newElement.setAttribute("Length", elementFrom.getAttribute("Length"));
					newElement.setAttribute("Decimal", elementFrom.getAttribute("Decimal"));
					newElement.setAttribute("SQLExpression", elementFrom.getAttribute("SQLExpression"));
				}
				//
				systemElement.appendChild(newElement);
			}
		}
	}

	String convertInternalIDOfTheTypeTag(String originalInternalID, String tagName) {
		String convertedInternalID = "";
		String defaultID = "";
		String defaultSortKey = "";
		String sortKey = "";
		org.w3c.dom.Element element = null;
		//
		NodeList nodeList = domDocumentFrom.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(originalInternalID)) {
				sortKey = element.getAttribute("SortKey");
				break;
			}
		}
		//
		if (!sortKey.equals("")) {
			nodeList = frame_.domDocument.getElementsByTagName(tagName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (org.w3c.dom.Element)nodeList.item(i);
				//
				if (defaultID.equals("")) {
					defaultID = element.getAttribute("ID");
					defaultSortKey = element.getAttribute("SortKey");
				} else {
					if (defaultSortKey.compareTo(element.getAttribute("SortKey")) > 0) {
						defaultID = element.getAttribute("ID");
						defaultSortKey = element.getAttribute("SortKey");
					}
				}
				//
				if (element.getAttribute("SortKey").equals(sortKey)) {
					convertedInternalID = element.getAttribute("ID");
				}
			}
		}
		//
		if (convertedInternalID.equals("")) {
			convertedInternalID = defaultID;
		}
		//
		return convertedInternalID;
	}

	String convertInternalIDOfTheTag(String originalInternalID, String tagName) {
		String convertedInternalID = "";
		String sortKey = "";
		String name = "";
		org.w3c.dom.Element element = null;
		//
		NodeList nodeList = domDocumentFrom.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(originalInternalID)) {
				sortKey = element.getAttribute("SortKey");
				name = element.getAttribute("Name");
				break;
			}
		}
		//
		if (!sortKey.equals("")) {
			nodeList = frame_.domDocument.getElementsByTagName(tagName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (org.w3c.dom.Element)nodeList.item(i);
				//
				if (element.getAttribute("SortKey").equals(sortKey) && element.getAttribute("Name").equals(name)) {
					convertedInternalID = element.getAttribute("ID");
					break;
				}
			}
		}
		//
		return convertedInternalID;
	}

	String convertInternalIDOfTableField(String originalInternalTableID, String originalInternalFieldID, org.w3c.dom.Document documentFrom, org.w3c.dom.Document documentInto) {
		String convertedInternalFieldID = "";
		String externalIDOfTable = "";
		String externalIDOfField = "";
		String nameOfField = "";
		org.w3c.dom.Element tableElement = null;
		org.w3c.dom.Element element = null;
		//
		NodeList nodeList = documentFrom.getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(originalInternalTableID)) {
				externalIDOfTable = element.getAttribute("SortKey");
				tableElement = element;
				break;
			}
		}
		//
		if (tableElement != null) {
			nodeList = tableElement.getElementsByTagName("TableField");
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (org.w3c.dom.Element)nodeList.item(i);
				if (element.getAttribute("ID").equals(originalInternalFieldID)) {
					externalIDOfField = element.getAttribute("Alias");
					nameOfField = element.getAttribute("Name");
					break;
				}
			}
			//
			if (!externalIDOfTable.equals("")  && !nameOfField.equals("")) {
				tableElement = null;
				nodeList = documentInto.getElementsByTagName("Table");
				for (int i = 0; i < nodeList.getLength(); i++) {
					element = (org.w3c.dom.Element)nodeList.item(i);
					if (element.getAttribute("SortKey").equals(externalIDOfTable)) {
						tableElement = element;
						break;
					}
				}
				//
				if (tableElement != null) {
					nodeList = tableElement.getElementsByTagName("TableField");
					for (int i = 0; i < nodeList.getLength(); i++) {
						element = (org.w3c.dom.Element)nodeList.item(i);
						if (element.getAttribute("Alias").equals(externalIDOfField) && element.getAttribute("Name").equals(nameOfField)) {
							convertedInternalFieldID = element.getAttribute("ID");
							break;
						}
						if (element.getAttribute("Alias").equals(externalIDOfField) && !element.getAttribute("Alias").equals("")) {
							convertedInternalFieldID = element.getAttribute("ID");
							break;
						}
					}
				}
			}
		}
		//
		return convertedInternalFieldID;
	}

	String convertTableKeyID(String originalInternalTableID, String originalInternalKeyID, String targetTableID) {
		String convertedInternalKeyID = "";
		String keyType = "";
		org.w3c.dom.Element element1, element2, element3, element4;
		NodeList nodeList1, nodeList2, nodeList3, nodeList4;
		//
		nodeList1 = domDocumentFrom.getElementsByTagName("Table");
		for (int i = 0; i < nodeList1.getLength(); i++) {
			element1 = (org.w3c.dom.Element)nodeList1.item(i);
			if (element1.getAttribute("ID").equals(originalInternalTableID)) {
				nodeList2 = element1.getElementsByTagName("TableKey");
				for (int j = 0; j < nodeList2.getLength(); j++) {
					element2 = (org.w3c.dom.Element)nodeList2.item(j);
					if (element2.getAttribute("ID").equals(originalInternalKeyID)) {
						keyType = element2.getAttribute("Type");
						//
						nodeList3 = frame_.domDocument.getElementsByTagName("Table");
						for (int m = 0; m < nodeList3.getLength(); m++) {
							element3 = (org.w3c.dom.Element)nodeList3.item(m);
							if (element3.getAttribute("ID").equals(targetTableID)) {
								nodeList4 = element3.getElementsByTagName("TableKey");
								for (int n = 0; n < nodeList4.getLength(); n++) {
									element4 = (org.w3c.dom.Element)nodeList4.item(n);
									if (element4.getAttribute("Type").equals(keyType)) {
										if (theseAreIdenticalKeyDefinitions(element2, element4)) {
											convertedInternalKeyID = element4.getAttribute("ID");
										}
									}
								}
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
		return convertedInternalKeyID;
	}

	boolean theseAreIdenticalKeyDefinitions(org.w3c.dom.Element keyElementFrom, org.w3c.dom.Element keyElementInto) {
		String workStr;
		org.w3c.dom.Element element1;
		NodeList nodeList1;
		String[] keyFieldIDFrom = new String[30];
		int countOfKeyFieldFrom = 0;
		String[] keyFieldIDInto = new String[30];
		int countOfKeyFieldInto = 0;
		int checkCount = 0;
		boolean result = false;
		//
		String keyTypeFrom = keyElementFrom.getAttribute("Type");
		org.w3c.dom.Element tableElementFrom = (org.w3c.dom.Element)keyElementFrom.getParentNode();
		String tableIDFrom = tableElementFrom.getAttribute("ID");
		String tableSortKeyFrom = tableElementFrom.getAttribute("SortKey");
		//
		String keyTypeInto = keyElementInto.getAttribute("Type");
		org.w3c.dom.Element tableElementInto = (org.w3c.dom.Element)keyElementInto.getParentNode();
		String tableSortKeyInto = tableElementInto.getAttribute("SortKey");
		//
		if (tableSortKeyFrom.equals(tableSortKeyInto) && keyTypeFrom.equals(keyTypeInto)) {
			//
			nodeList1 = keyElementFrom.getElementsByTagName("TableKeyField");
			for (int i = 0; i < nodeList1.getLength(); i++) {
				element1 = (org.w3c.dom.Element)nodeList1.item(i);
				keyFieldIDFrom[countOfKeyFieldFrom] = element1.getAttribute("FieldID");
				countOfKeyFieldFrom++;
			}
			//
			nodeList1 = keyElementInto.getElementsByTagName("TableKeyField");
			for (int i = 0; i < nodeList1.getLength(); i++) {
				element1 = (org.w3c.dom.Element)nodeList1.item(i);
				keyFieldIDInto[countOfKeyFieldInto] = element1.getAttribute("FieldID");
				countOfKeyFieldInto++;
			}
			//
			if (countOfKeyFieldInto == countOfKeyFieldFrom) {
				checkCount = 0;
				for (int i = 0; i < countOfKeyFieldFrom; i++) {
					workStr = convertInternalIDOfTableField(tableIDFrom, keyFieldIDFrom[i], domDocumentFrom, frame_.domDocument);
					for (int j = 0; j < countOfKeyFieldInto; j++) {
						if (workStr.equals(keyFieldIDInto[j])) {
							keyFieldIDInto[j] = "";
							checkCount++;
						}
					}
				}
				if (checkCount == countOfKeyFieldInto) {
					result = true;
				}
			}
		}
		//
		return result;
	}

	org.w3c.dom.Element getLastDomElementOfTheType(String tagName) {
		org.w3c.dom.Element element = null;
		org.w3c.dom.Element lastDomElement = null;
		int lastDomElementID = 0;
		int elementID = 0;
		NodeList nodeList = frame_.domDocument.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (lastDomElement == null) {
				lastDomElement = element;
			} else {
				elementID = Integer.parseInt(element.getAttribute("ID"));
				lastDomElementID = Integer.parseInt(lastDomElement.getAttribute("ID"));
				if (elementID > lastDomElementID) {
					lastDomElement = element;
				}
			}
		}
		return lastDomElement;
	}

	int getNextIDOfFunctionIOs() {
		org.w3c.dom.Element element = null;
		int elementID = 0;
		int lastID = 0;
		//
		NodeList nodeList = frame_.domDocument.getElementsByTagName("IOPanel");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			elementID = Integer.parseInt(element.getAttribute("ID"));
			if (lastID == 0) {
				lastID = elementID;
			} else {
				if (elementID > lastID) {
					lastID = elementID;
				}
			}
		}
		//
		nodeList = frame_.domDocument.getElementsByTagName("IOWebPage");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			elementID = Integer.parseInt(element.getAttribute("ID"));
			if (lastID == 0) {
				lastID = elementID;
			} else {
				if (elementID > lastID) {
					lastID = elementID;
				}
			}
		}
		//
		nodeList = frame_.domDocument.getElementsByTagName("IOSpool");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			elementID = Integer.parseInt(element.getAttribute("ID"));
			if (lastID == 0) {
				lastID = elementID;
			} else {
				if (elementID > lastID) {
					lastID = elementID;
				}
			}
		}
		//
		nodeList = frame_.domDocument.getElementsByTagName("IOTable");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			elementID = Integer.parseInt(element.getAttribute("ID"));
			if (lastID == 0) {
				lastID = elementID;
			} else {
				if (elementID > lastID) {
					lastID = elementID;
				}
			}
		}
		//
		lastID++;
		return lastID;
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	class XeadNode {
		private String nodeType_;
		private org.w3c.dom.Element domNode_;
		//
		//Constructor//
		public XeadNode(String type, org.w3c.dom.Element node) {
			super();
			nodeType_ = type;
			domNode_ = node;
		}
		public String toString() {
			String str = "";
			str = this.getName();
			return str;
		}
		public String getName() {
			String str = "";
			//
			if (nodeType_.equals("Role")) {
				str = domNode_.getAttribute("SortKey") + " " + domNode_.getAttribute("Name");
			}
			if (nodeType_.equals("Subsystem")) {
				str = domNode_.getAttribute("SortKey") + " " + domNode_.getAttribute("Name");
			}
			//
			return str;
		}
		public org.w3c.dom.Element getElement() {
			return domNode_;
		}
	}

	class SortableXeadNodeComboBoxModel extends DefaultComboBoxModel {
		private static final long serialVersionUID = 1L;
		public void sortElements() {
			TreeSet<XeadNode> treeSet = new TreeSet<XeadNode>(new NodeComparator());
			int elementCount = this.getSize();
			XeadNode node;
			for (int i = 0; i < elementCount; i++) {
				node = (XeadNode)this.getElementAt(i);
				treeSet.add(node);
			}
			this.removeAllElements();
			Iterator<XeadNode> it = treeSet.iterator();
			while( it.hasNext() ){
				node = (XeadNode)it.next();
				this.addElement(node);
			}
		}
	}

	class NodeComparator implements java.util.Comparator<XeadNode> {
		public int compare(XeadNode node1, XeadNode node2) {
			String value1, value2;
			value1 = node1.getElement().getAttribute("SortKey");
			value2 = node2.getElement().getAttribute("SortKey");
			int compareResult = value1.compareTo(value2);
			if (compareResult == 0) {
				value1 = node1.getElement().getAttribute("ID");
				value2 = node2.getElement().getAttribute("ID");
				compareResult = value1.compareTo(value2);
				if (compareResult == 0) {
					compareResult = 1;
				}
			}
			return(compareResult);
		}
	}
}

class DialogImportXEAD_jComboBox_actionAdapter implements java.awt.event.ActionListener {
	DialogImportXEAD adaptee;

	DialogImportXEAD_jComboBox_actionAdapter(DialogImportXEAD adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBox_actionPerformed(e);
	}
}

class DialogImportXEAD_jRadioButton_changeAdapter implements ChangeListener {
	DialogImportXEAD adaptee;

	DialogImportXEAD_jRadioButton_changeAdapter(DialogImportXEAD adaptee) {
		this.adaptee = adaptee;
	}
	public void stateChanged(ChangeEvent e) {
		adaptee.jRadioButton_stateChanged();
	}
}

class DialogImportXEAD_jButtonStart_actionAdapter implements java.awt.event.ActionListener {
	DialogImportXEAD adaptee;

	DialogImportXEAD_jButtonStart_actionAdapter(DialogImportXEAD adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStart_actionPerformed(e);
	}
}

class DialogImportXEAD_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogImportXEAD adaptee;

	DialogImportXEAD_jButtonCancel_actionAdapter(DialogImportXEAD adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}