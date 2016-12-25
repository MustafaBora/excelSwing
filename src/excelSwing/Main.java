package excelSwing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Main extends JPanel implements ActionListener {
	private static final long serialVersionUID = -248216545345812100L;

	static private final String newline = "\n";
	private boolean DEBUG = true;
	DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;
	private LinkedList<YuzOkuma> faceReadsLinkedList = new LinkedList<YuzOkuma>();
	private LinkedList<Personel> personnelLinkedList = new LinkedList<Personel>();
	private Panel leftPanel;
	private JComboBox<ComboItem> monthCombo;
	private String selectedMonth;

	public Main() {
		super(new BorderLayout());
		// Create the log first, because the action listeners
		// need to refer to it.
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create a file chooser
		fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// Create the open button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		openButton = new JButton("Excel Oku, Ekle"
		// , createImageIcon("/images/Open.gif")
		);
		openButton.addActionListener(this);

		// Create the save button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		saveButton = new JButton("Excele Yaz"
		// ,createImageIcon("/images/Save.gif")
		);
		saveButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);

		leftPanel = new Panel();
		FlowLayout fl_leftPanel = (FlowLayout) leftPanel.getLayout();
		fl_leftPanel.setHgap(100);
		logScrollPane.setRowHeaderView(leftPanel);

		monthCombo = new JComboBox<ComboItem>();
		monthCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedMonth = ((ComboItem)monthCombo.getSelectedItem()).getKey();
				log.append("Opening: " + ((ComboItem)monthCombo.getSelectedItem()).getKey() + " secildi." + newline);
			}
		});
		monthCombo.setToolTipText("Ay Seciniz");
		monthCombo.setMaximumRowCount(12);

		monthCombo.addItem(new ComboItem("Ocak", "Ocak"));
		monthCombo.addItem(new ComboItem("Subat", "Subat"));
		monthCombo.addItem(new ComboItem("Mart", "Mart"));
		monthCombo.addItem(new ComboItem("Nisan", "Nisan"));
		monthCombo.addItem(new ComboItem("Mayis", "Mayis"));
		monthCombo.addItem(new ComboItem("Haziran", "Haziran"));
		monthCombo.addItem(new ComboItem("Temmuz", "Temmuz"));
		monthCombo.addItem(new ComboItem("Agustos", "Agustos"));
		monthCombo.addItem(new ComboItem("Eylul", "Eylul"));
		monthCombo.addItem(new ComboItem("Ekim", "Ekim"));
		monthCombo.addItem(new ComboItem("Kasim", "Kasim"));
		monthCombo.addItem(new ComboItem("Aralik", "Aralik"));
		leftPanel.add(monthCombo);
		

	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(Main.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would open the file.
				log.append("Opening: " + file.getName() + "." + newline);

				String[] titles = new String[6];
				LinkedList<String[]> yuzOkumalarStrList = new LinkedList<String[]>();	
				readExcelFile(file, titles, yuzOkumalarStrList);
				/*
				 *	2- O personel icin o gune ait min ve max degerleri bularak fazladan kayitlari sil.
					3- O gun icin tek okuma olsa bile yine gir.
				*/
				//systemOutYuzOkumalar(faceReadsLinkedList);

			} else {
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());

			// Handle save button action.
		} else if (e.getSource() == saveButton) {
			int returnVal = fc.showSaveDialog(Main.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would save the file.
				log.append("Saving: " + file.getName() + "." + newline);
				saveExcel(file, faceReadsLinkedList);
			} else {
				log.append("Save command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}
	
	
	
	///////////////////bussiness logic starts here///////////////////////

	private void saveExcel(File file, LinkedList<?> linkedList) {
		if (linkedList.isEmpty())
			return;
		XSSFWorkbook workbook = new XSSFWorkbook();

		if (linkedList.get(0) instanceof YuzOkuma) {
			XSSFSheet sheet = workbook.createSheet("Yuz Okuma");
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("CIHAZ");
			cell = row.createCell(1);
			cell.setCellValue("ADI - SOYADI");
			cell = row.createCell(2);
			cell.setCellValue("KİMLİK NUMARASI");
			cell = row.createCell(3);
			cell.setCellValue("OKUMA TARİHİ");
			cell = row.createCell(4);
			cell.setCellValue("SAAT");
			cell = row.createCell(5);
			cell.setCellValue("YÜZ OKUMA NUMARASI");
			for (int i = 0; i < linkedList.size(); i++) {
				row = sheet.createRow(i + 1);
				cell = row.createCell(0);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getCihaz());
				cell = row.createCell(1);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getAdiSoyadi());
				cell = row.createCell(2);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getKimlikNumarasi());
				cell = row.createCell(3);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getOkumaTarihi());
				cell = row.createCell(4);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getSaat());
				cell = row.createCell(5);
				cell.setCellValue(((YuzOkuma) linkedList.get(i)).getYuzOkumaNumarasi());
			}
		} else if (linkedList.get(0) instanceof Personel) {
			XSSFSheet sheet = workbook.createSheet("Personeller");
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("ADI - SOYADI");
			cell = row.createCell(1);
			cell.setCellValue("KİMLİK NUMARASI");
			cell = row.createCell(2);
			cell.setCellValue("YÜZ OKUMA NUMARASI");
			// TODO personeller burada gosterilecek
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			try {
				workbook.write(fos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void systemOutYuzOkumalar(LinkedList<YuzOkuma> yuzOkumalarList) {
		for (YuzOkuma kayit : yuzOkumalarList) {
			System.out.println(kayit);
		}
	}

	private void readExcelFile(File file, String[] titles, LinkedList<String[]> faceReadsStrList) {
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
			if(DEBUG) {
				System.out.println(getfirstRow(iterator, titles));
			}
			if(checkTitles(titles) ) {
				System.err.println("Secilen excel dosyasi veya sutunlari yanlis!");
				log.append("Secilen excel dosyasi veya sutunlari yanlis!");
				workbook.close();
				fis.close();
				return;
			}
			if(DEBUG) {
				System.out.println(getColumns(iterator, faceReadsStrList));
				//SystemOutMethods.systemOutIterator(iterator);
				SystemOutMethods.systemOutStringArray(titles);
				//SystemOutMethods.systemOutLinkedList(faceReadsStrList);
			}
			createFaceReadsLinkedList(faceReadsStrList, faceReadsLinkedList);
			createPersonelLinkedList(faceReadsLinkedList);
			if(DEBUG) {
				SystemOutMethods.systemOutYuzOkumaLinkedList(faceReadsLinkedList);
			}
			workbook.close();
			fis.close();
		} catch (FileNotFoundException e) {
			System.err.println("caught (FileNotFoundException e)");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("caught (IOException e)");
			e.printStackTrace();
		}
	}

	private void createPersonelLinkedList(LinkedList<YuzOkuma> faceReadsLinkedList2) {
		// TODO
		/*
		Tum yuzOkumaListesinde gez
		1- personel personelLinkedListte var mi
			1.1-	Yoksa kaydet
		*/
	}

	private void createFaceReadsLinkedList(LinkedList<String[]> faceReadsStrList, LinkedList<YuzOkuma> faceReadsLinkedList2) {
		//now the input linkedList of str[} contains ordered Strings
		for(int i=0; i<faceReadsStrList.size(); i++) {
			String[] temp = faceReadsStrList.get(i);
			//prepare date
			//String dateStr = "12/15/2016"
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			Date date;
			try {
				date = format.parse(temp[3]);
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			//TODO: Burada 0 dedigim kisim o gun icin kacinci okuma oldugu min ve maxi alirim diye dusundum
			YuzOkuma newYO = new YuzOkuma(temp[0],temp[1], temp[2], date, temp[4], temp[5]
			//		, 0
					);
			faceReadsLinkedList.add(newYO);
		}
	}

	private boolean checkTitles(String[] titles) {
		if(!titles[0].equals("CİHAZ")) return false;
		if(!titles[1].equals("ADI - SOYADI")) return false;
		if(!titles[2].equals("KİMLİK NUMARASI")) return false;
		if(!titles[3].equals("OKUMA TARİHİ")) return false;
		if(!titles[4].equals("YÜZ OKUMA NUMARASI")) return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	private String getColumns(Iterator<Row> iterator, LinkedList<String[]> data) {
		int count;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			String[] row = new String[6];
			count = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						row[count] = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						return "booleanType";
					case Cell.CELL_TYPE_NUMERIC:
						//return "numericType";
//						if (DateUtil.isCellDateFormatted(cell)) {
//							System.out.println(cell.getDateCellValue());
//						}
						row[count] = formatter.formatCellValue(cell);
						break;
				}
				count++;
			}
			data.add(row);
		}
		return "success";
	}

	@SuppressWarnings("deprecation")
	private String getfirstRow(Iterator<Row> iterator, String[] firstRow) {
		if(iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			int count=0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						firstRow[count] = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						return "booleanType";
					case Cell.CELL_TYPE_NUMERIC:
						//return "numericType";
//						if (DateUtil.isCellDateFormatted(cell)) {
//							System.out.println(cell.getDateCellValue());
//						}
						firstRow[count] = formatter.formatCellValue(cell);
						break;
				}
				count++;
			}
			return "success";
		}
		else {
			return "emptyExcel";
		}
	}

	
	/////////////////////////////bussiness logic ends here///////////

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("HelloWorld");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.getContentPane().add(new Main());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
	
	class ComboItem {
		private String key;
		private String value;

		public ComboItem(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
}