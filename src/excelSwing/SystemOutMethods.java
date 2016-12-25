package excelSwing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class SystemOutMethods {
	//////////////// system out methods here
	static void systemOutYuzOkumaLinkedList(LinkedList<YuzOkuma> faceReadsLinkedList2) {
		for (int i = 0; i < faceReadsLinkedList2.size(); i++) {
			System.out.println(faceReadsLinkedList2.get(i));
		}
	}

	@SuppressWarnings("deprecation")
	static void systemOutExcelRowIterator(Iterator<Row> iterator) {
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.print(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.print(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					System.out.print(cell.getNumericCellValue());
					break;
				}
				System.out.print(" - ");
			}
			System.out.println();
		}
	}

	static void systemOutLinkedList(LinkedList<String[]> yuzOkumalar) {
		ListIterator<String[]> listIterator = yuzOkumalar.listIterator();
		while (listIterator.hasNext()) {
			systemOutStringArray(listIterator.next());
		}
	}

	static void systemOutStringArray(String[] titles) {
		int a = 0;
		while (a < titles.length) {
			System.out.print(titles[a]);
			System.out.print(", ");
			a++;
		}
		System.out.println();
	}
}
