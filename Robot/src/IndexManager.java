package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * @author thomas
 *
 */

public class IndexManager {
    private static IndexManager indexManager;
    private static String content = " ";
    
    private static String INDEX_DIR = "E:\\ProIndex";
    private static String DATA_DIR = "E:\\ProData";
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;
    
    /**
     * ��������������
     * @return ������������������
     */
    
    public IndexManager getManager() {
        if (indexManager == null){
            this.indexManager = new IndexManager();
        }
        return indexManager;
    }
    
    /**
     * ������ǰ�ļ�Ŀ¼������
     * @param path ��ǰ�ļ�Ŀ¼
     * @return �Ƿ�ɹ�
     */
    
    public static boolean createIndex(String path) {
        Date date1 = new Date();
        List<File> fileList = getFileList(path);
        for (File file : fileList) {
            content = " ";
            //��ȡ�ļ���׺
            String type = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if ("txt".equalsIgnoreCase(type)){
                
                content += OpenFile.txt2String(file);
            
            } else if("doc".equalsIgnoreCase(type)) {
            
                content += OpenFile.doc2String(file);
            
            } else if("xls".equalsIgnoreCase(type)) {
                
                content += OpenFile.xls2String(file);
                
            }
            
            System.out.println("name :" + file.getName());
            System.out.println("path :" + file.getPath());
            System.out.println();
                     
            try {
                analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
                directory = FSDirectory.open(new File(INDEX_DIR));
    
                File indexFile = new File(INDEX_DIR);
                if (!indexFile.exists()) {
                    indexFile.mkdirs();
                }
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                indexWriter = new IndexWriter(directory, config);
                
                Document document = new Document();
                document.add(new TextField("filename", file.getName(), Store.YES));
                document.add(new TextField("content", content, Store.YES));
                document.add(new TextField("path", file.getPath(), Store.YES));
                indexWriter.addDocument(document);
                indexWriter.commit();
                closeWriter();
    
                
            } catch(Exception e) {
                e.printStackTrace();
            }
            content = " ";
        }
        Date date2 = new Date();
        System.out.println("����������ʱ��" + (date2.getTime() - date1.getTime()) + "ms\n");
        return true;
    }
    
    /**
     * �������������ط����������ļ�
     * @param text ���ҵ��ַ���
     * @return �����������ļ�List
     */
    
    public static String searchIndex(String text) {
        String temp = "����ػش������˹��ͷ�";

        try {
            directory = FSDirectory.open(new File(INDEX_DIR));
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
    
            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
            Query query = parser.parse(text);
            
            ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        
            double ans = 0.0;
               
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                
                //System.out.println("____________________________");
                //System.out.println(hitDoc.get("filename"));
                //System.out.println(hitDoc.get("content"));
                //System.out.println(hitDoc.get("content"));
                //System.out.println(hitDoc.get("path"));
                //System.out.println("____________________________");
                
                if (StrSimilar.SimilarDegree(text, hitDoc.get("content")) > ans) {
                	ans = StrSimilar.SimilarDegree(text, hitDoc.get("content"));
                	temp = hitDoc.get("content");
                }
       
            }         	
            
            ireader.close();
            directory.close();

            return temp;
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    /**
     * ����Ŀ¼�µ��ļ�
     * @param dirPath ��Ҫ��ȡ�ļ���Ŀ¼
     * @return �����ļ�list
     */
    
    public static List<File> getFileList(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (isTxtFile(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    
    /**
     * �ж��Ƿ�ΪĿ���ļ���Ŀǰ֧��txt xls doc��ʽ
     * @param fileName �ļ�����
     * @return ������ļ����������������������true�����򷵻�false
     */
    
    public static boolean isTxtFile(String fileName) {
        if (fileName.lastIndexOf(".txt") > 0) {
            return true;
        } else if (fileName.lastIndexOf(".xls") > 0) {
            return true;
        } else if (fileName.lastIndexOf(".doc") > 0) {
            return true;
        }
        return false;
    }
    
    public static void closeWriter() throws Exception {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    /**
     * ɾ���ļ�Ŀ¼�µ������ļ�
     * @param file Ҫɾ�����ļ�Ŀ¼
     * @return ����ɹ�������true.
     */
    
    public static boolean deleteDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }
    
    public static String ask(String str) {
        File fileIndex = new File(INDEX_DIR);
        
        if (deleteDir(fileIndex)) {
            fileIndex.mkdir();
        } else {
            fileIndex.mkdir();
        }
        
        createIndex(DATA_DIR);
        
        return searchIndex(str);
    }
}
