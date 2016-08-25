package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class OpenFile {
	/**
     * ��ȡtxt�ļ�������
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    
    public static String txt2String(File file) {
        String result = " ";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
            String s = null;
            while ((s = br.readLine())!= null) {//ʹ��readLine������һ�ζ�һ��
                result = result + "\n" + s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * ��ȡdoc�ļ�����
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    
    public static String doc2String(File file) {
        String result = " ";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            Range rang = doc.getRange();
            result += rang.text();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * ��ȡxls�ļ�����
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    
    public static String xls2String(File file) {
        String result = " ";
        try {
            FileInputStream fis = new FileInputStream(file);   
            StringBuilder sb = new StringBuilder();   
            jxl.Workbook rwb = Workbook.getWorkbook(fis);   
            Sheet[] sheet = rwb.getSheets();   
            for (int i = 0; i < sheet.length; i++) {   
                Sheet rs = rwb.getSheet(i);   
                for (int j = 0; j < rs.getRows(); j++) {   
                   Cell[] cells = rs.getRow(j);   
                   for (int k = 0; k < cells.length; k++)   
                	   sb.append(cells[k].getContents());   
                }   
            }   
            fis.close();   
            result += sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
